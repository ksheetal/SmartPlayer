package com.crypticducs000webhost.myplaylist

import android.app.Activity
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.FragmentActivity
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_favorite.*

/**
 * Created by Hp on 12/22/2017.
 */
class FavouriteFragment : Fragment() {
    var myActivity: Activity? = null
    //    var getSongsList:ArrayList<Songs>?=null
    var noFavourites: TextView? = null
    var nowPlayingBottomBar: RelativeLayout? = null
    var playpauseButton: ImageButton? = null
    var songTitle: TextView? = null
    var recyclerView: RecyclerView? = null
    var trackPosition: Int = 0
    var refreshList: ArrayList<Songs>? = null
    var getListFromDataBase: ArrayList<Songs>? = null
    //  val songPlayingFragment:SongPlayingFragment?=null
    var favoriteCOntent: MyPlayListDatabase? = null

    object Statified {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.fragment_favorite, container, false)

        noFavourites = view?.findViewById(R.id.noFavorites)
        nowPlayingBottomBar = view?.findViewById(R.id.hiddenBarFavScreen)
        songTitle = view?.findViewById(R.id.songTitleFavScreen)
        playpauseButton = view?.findViewById(R.id.playPauseButton)
        recyclerView = view?.findViewById(R.id.favoriteRecycler)
        activity.title = "Favourites"
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        favoriteCOntent = MyPlayListDatabase(myActivity)
        var songPlayingFragment = SongPlayingFragment()
        //    getSongsList =getSongsFromPhone()
        desplay_favorites_by_searching()
        booleanArrayOf()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
    }

    fun getSongsFromPhone(): ArrayList<Songs> {
        var arrayList = ArrayList<Songs>()
        var contentResolver = myActivity?.contentResolver
        var songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var songCursor = contentResolver?.query(songUri, null, null, null, null)
        if (songCursor != null && songCursor.moveToFirst()) {
            val songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val songData = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val songIndex = songCursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            while (songCursor.moveToNext()) {
                var currentId = songCursor.getLong(songId)
                var currentTitle = songCursor.getString(songTitle)
                var currentArtist = songCursor.getString(songArtist)
                var currentData = songCursor.getString(songData)
                var currentIndex = songCursor.getLong(songIndex)
                arrayList.add(Songs(currentId, currentTitle, currentArtist, currentData, currentIndex))
            }
        }
        return arrayList
    }

    fun bottomBarSetup() {
        try {
           bottomBarCLickHandler()
            songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            SongPlayingFragment.Statified.mediaplayer?.setOnCompletionListener {
                songTitle?.setText(SongPlayingFragment.Statified.currentSongHelper?.songTitle)
                SongPlayingFragment.Staticaed.onSongComplete()

            }
            if (SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean) {
                nowPlayingBottomBar?.visibility = View.VISIBLE
            } else {
                nowPlayingBottomBar?.visibility = View.INVISIBLE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   fun bottomBarCLickHandler() {
        nowPlayingBottomBar?.setOnClickListener({
            Statified.mediaPlayer = SongPlayingFragment.Statified.mediaplayer
            val songPlayingFragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist", SongPlayingFragment.Statified.currentSongHelper?.songArtist)
            args.putString("path", SongPlayingFragment.Statified.currentSongHelper?.songPath)
            args.putString("songTitle", SongPlayingFragment.Statified.currentSongHelper?.songTitle)
            args.putInt("SongId", SongPlayingFragment.Statified.currentSongHelper?.songId?.toInt() as Int)
            args.putInt("SongPosition", SongPlayingFragment.Statified.currentSongHelper?.currentPostion?.toInt() as Int)
            args.putParcelableArrayList("songData", SongPlayingFragment.Statified.fetchSongs)
            args.putString("FavBottomBar", "success")
            songPlayingFragment.arguments = args
            fragmentManager.beginTransaction().replace(R.id.details_fragment, songPlayingFragment)
                    .addToBackStack("SongPlayingFragment")
                    .commit()
        })

        playpauseButton?.setOnClickListener(
                {
                    if (SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean) {
                        SongPlayingFragment.Statified.mediaplayer?.pause()
                        trackPosition = SongPlayingFragment.Statified.mediaplayer?.getCurrentPosition() as Int
                        playpauseButton?.setBackgroundResource(R.drawable.play_icon)
                    } else {
                        SongPlayingFragment.Statified.mediaplayer?.seekTo(trackPosition)
                        SongPlayingFragment.Statified.mediaplayer?.start()
                        playpauseButton?.setBackgroundResource(R.drawable.pause_icon)
                    }
                })
    }

    fun desplay_favorites_by_searching() {
        if (favoriteCOntent?.checkSize() as Int > 0) {
            refreshList = ArrayList<Songs>()
            getListFromDataBase = favoriteCOntent?.queryDBList()
            var fetchListFromDevice = getSongsFromPhone()
            if (fetchListFromDevice != null) {
                for (i in 0..fetchListFromDevice?.size - 1) {
                    for (j in 0..getListFromDataBase?.size as Int - 1) {
                        if ((getListFromDataBase?.get(j)?.songID) === (fetchListFromDevice?.get(i)?.songID)) {
                            refreshList?.add((getListFromDataBase as ArrayList<Songs>)[j])
                        }
                    }
                }
            } else {

            }
            if (refreshList == null) {
                recyclerView?.visibility = View.INVISIBLE
                noFavourites?.visibility = View.VISIBLE
            } else {
                var favoriteAdapter = FavouriteAdapter(refreshList as ArrayList<Songs>, myActivity as Context)
                var mLayoutManager = LinearLayoutManager(activity)
                recyclerView?.layoutManager = mLayoutManager
                recyclerView?.itemAnimator = DefaultItemAnimator()
                recyclerView?.adapter = favoriteAdapter
                recyclerView?.setHasFixedSize(true)
            }
        } else {
            recyclerView?.visibility = View.INVISIBLE
            noFavourites?.visibility = View.VISIBLE
        }
    }
}