package com.crypticducs000webhost.myplaylist

import android.app.FragmentTransaction
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
object Statified {
    var notificationManager: NotificationManager? = null
}
    var trackNotificationBuilder:Notification?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val intent = Intent (this@MainActivity,MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this@MainActivity,System.currentTimeMillis().toInt(),
                intent,0)
        trackNotificationBuilder = Notification.Builder(this)
                .setContentTitle("A track is playing in backgroud")
                .setSmallIcon(R.drawable.music)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setAutoCancel(true)
                .build()
        Statified.notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val mainScreenFragment = MainScreenFragment()
        this.supportFragmentManager
                .beginTransaction()
                .add(R.id.details_fragment, mainScreenFragment, "MainScreenFragment")
                .commit()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_redirect -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
                val fragment = MainScreenFragment()
                this.supportFragmentManager
                        .beginTransaction()
                        .add(R.id.details_fragment, fragment, "MainScreenFragment")
                        .commit()
            }
            R.id.nav_nowPlaying ->{
                val fragment =  SongPlayingFragment()
                this.supportFragmentManager
                        .beginTransaction()
                        .add(R.id.details_fragment,fragment,"MainScreenFragment")
                        .commit()
            }
          R.id.nav_gallery -> {
              val fragment = FavouriteFragment()
              this.supportFragmentManager
                      .beginTransaction()
                      .add(R.id.details_fragment, fragment, "MainScreenFragment")
                      .commit()
           }
            R.id.nav_slideshow -> {
                val fragment = SettingsFragment()
                this.supportFragmentManager
                        .beginTransaction()
                        .add(R.id.details_fragment, fragment, "MainScreenFragment")
                        .commit()

            }
            R.id.nav_manage -> {
                val fragment = AboutFragment()
                this.supportFragmentManager
                        .beginTransaction()
                        .add(R.id.details_fragment, fragment, "MainScreenFragment")
                        .commit()

            }
            R.id.nav_share -> {
                Toast.makeText(this@MainActivity, "Coming Soon.", Toast.LENGTH_SHORT).show()

            }
            R.id.nav_send -> {
                Toast.makeText(this@MainActivity, "Coming Soon.", Toast.LENGTH_SHORT).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onStart() {
        super.onStart()
        try {
            Statified.notificationManager?.cancel(1978)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            if(SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean){
                Statified.notificationManager?.notify(1978,trackNotificationBuilder)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
           Statified.notificationManager?.cancel(1978)

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}
