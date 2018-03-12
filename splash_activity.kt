package com.crypticducs000webhost.myplaylist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.widget.Toast

class splash_activity : AppCompatActivity() {
    var permissionString = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.RECORD_AUDIO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_activity)

        if(!hasPermissions(this@splash_activity,*permissionString)){
            //we have to ask for permmission
            ActivityCompat.requestPermissions(this@splash_activity,permissionString,131)
        }else {
            Handler().postDelayed({
                var startAct = Intent(this@splash_activity, MainActivity::class.java)
                startActivity(startAct)
                this.finish()
            },1000)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            131->{
                if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED
                        && grantResults[1]== PackageManager.PERMISSION_GRANTED
                        && grantResults[2]== PackageManager.PERMISSION_GRANTED
                        && grantResults[3]== PackageManager.PERMISSION_GRANTED
                        && grantResults[4]== PackageManager.PERMISSION_GRANTED){
                    Handler().postDelayed({
                        var startAct = Intent(this@splash_activity, MainActivity::class.java)
                        startActivity(startAct)
                        this.finish()
                    },1000)
                }else{
                    Toast.makeText(this@splash_activity,"Please grant all the permissions to continue", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                return
            }
            else->{
                Toast.makeText(this@splash_activity,"Something went wrong", Toast.LENGTH_SHORT).show()
                this.finish()
                return
            }
        }
    }

    fun hasPermissions(context: Context, vararg permissions: String):Boolean{
        var hasAllPermissions = true
        for(permission in permissions){
            var res = context.checkCallingOrSelfPermission(permission)
            if(res!= PackageManager.PERMISSION_GRANTED){
                hasAllPermissions = false
            }
        }
        return hasAllPermissions
    }
}
