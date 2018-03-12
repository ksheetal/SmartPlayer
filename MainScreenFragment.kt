package com.crypticducs000webhost.myplaylist


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class MainScreenFragment : Fragment() {

    var nowPlayingBottmBar: RelativeLayout? = null
    var playPauseButton: ImageButton? = null
    var songTitle: TextView? = null
    var visibleLayout: RelativeLayout? = null
    var noSongs: RelativeLayout? = null
    var recyclerView: RecyclerView? = null
    var myAcitivity: Activity? = null
    var _mainScreenAdapter: MainScreenAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        var view = inflater!!.inflate(R.layout.fragment_main_screen, container, false)
        setHasOptionsMenu(true)
        visibleLayout = view?.findViewById<RelativeLayout>(R.id.visibleLayout)
        noSongs = view?.findViewById<RelativeLayout>(R.id.noSongs)
        nowPlayingBottmBar = view?.findViewById<RelativeLayout>(R.id.hiddenBarMainScreen)
        songTitle = view?.findViewById<TextView>(R.id.songTitleMainScreen)
        playPauseButton = view?.findViewById<ImageButton>(R.id.playPauseButton)
        recyclerView = view?.findViewById<RecyclerView>(R.id.contentMain)
        activity.title = "All songs"
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val getsongList = getSongsFromPhone()
        val prefs =activity.getSharedPreferences("action_sort",Context.MODE_PRIVATE)
        val action_sort_ascending = prefs.getString("action_sort_asceding","true")
        val action_sort_recent = prefs.getString("action_sort_recent","false")
        if(getsongList == null){
            visibleLayout?.visibility =View.INVISIBLE
            noSongs?.visibility = View.VISIBLE
        }else{
            _mainScreenAdapter = MainScreenAdapter(getsongList as ArrayList<Songs>, myAcitivity as Context)
            val mLayoutManager = LinearLayoutManager(myAcitivity)
            recyclerView?.layoutManager = mLayoutManager
            recyclerView?.itemAnimator = DefaultItemAnimator()
            recyclerView?.adapter = _mainScreenAdapter

        }
        if(getsongList!=null){
            if(action_sort_ascending!!.equals("true",ignoreCase = true)){
                Collections.sort(getsongList,Songs.Statified.nameComparator)
                _mainScreenAdapter?.notifyDataSetChanged()
            }else if (action_sort_recent!!.equals("ture",ignoreCase = true)){
                Collections.sort(getsongList,Songs.Statified.dateComparator)
                _mainScreenAdapter?.notifyDataSetChanged()
            }
        }
       // bottomBar_setup()
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.main,menu)
        return
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val switcher = item?.itemId
        val getsongList = getSongsFromPhone()
        if(switcher ==R.id.action_sort_acending){
            val editor = myAcitivity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)?.edit()
            editor?.putString("action_sort_ascending","true")
            editor?.putString("action_sort_recent","false")
            editor?.apply()
            if (getsongList!=null){
                Collections.sort(getsongList,Songs.Statified.nameComparator)
            }
            _mainScreenAdapter?.notifyDataSetChanged()
            return false

        }else if (switcher == R.id.action_sort_recent){
            val editorTwo = myAcitivity?.getSharedPreferences("action_sort",Context.MODE_PRIVATE)?.edit()
            val getsongList = getSongsFromPhone()
            editorTwo?.putString("action_sort_recent","true")
            editorTwo?.putString("action_sort_ascending","false")
            editorTwo?.apply()
            if (getsongList!=null){
                Collections.sort(getsongList,Songs.Statified.dateComparator)
            }
            _mainScreenAdapter?.notifyDataSetChanged()
            return false
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        myAcitivity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myAcitivity = activity
    }

    fun getSongsFromPhone(): ArrayList<Songs> {
        var arrayList = ArrayList<Songs>()
        var contentResolver = myAcitivity?.contentResolver
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
}// Required empty public constructor
