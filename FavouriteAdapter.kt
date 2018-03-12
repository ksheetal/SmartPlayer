package com.crypticducs000webhost.myplaylist

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * Created by Hp on 12/22/2017.
 */
class FavouriteAdapter(_songDetails:ArrayList<Songs>,_context:Context) : RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>(){

    var songDetails : ArrayList<Songs>?=null
    var mContext :Context? =null

    init {
        this.songDetails=_songDetails
        this.mContext=_context
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val songObject = songDetails?.get(position)
        holder?.trackTitle?.text = songObject?.SongsTitle
        holder?.trackArtist?.text = songObject?.artist
        holder?.contentHolder?.setOnClickListener({
            val songPlayingFragment = SongPlayingFragment()
            var args = Bundle()
            args.putString("songArtist",songObject?.artist)
            args.putString("path",songObject?.songData)
            args.putString("songTitle",songObject?.SongsTitle)
            args.putInt("SongId",songObject?.songID?.toInt() as Int)
            args.putInt("SongPosition",position)
            args.putParcelableArrayList("songData",songDetails)
            songPlayingFragment.arguments=args
            (mContext as FragmentActivity).supportFragmentManager
                    .beginTransaction()
                    .add(R.id.details_fragment,songPlayingFragment)
                    .addToBackStack("SongPlayingFragment")
                    .commit()
        })

    }
    override fun getItemCount(): Int {
        if(songDetails == null){
            return 0
        }else{
            return (songDetails as ArrayList<Songs>).size
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.row_custom_mainscreen_adapter,parent,false)


        return MyViewHolder(itemView)
    }
    class MyViewHolder(view:View): RecyclerView.ViewHolder(view) {
        var trackTitle :TextView?=null
        var trackArtist: TextView?=null
        var contentHolder:RelativeLayout?=null
        init {
            trackTitle = view.findViewById<TextView>(R.id.trackTitle)
            trackArtist= view.findViewById<TextView>(R.id.trackArtist)
            contentHolder = view.findViewById<RelativeLayout>(R.id.contentRow)
        }

    }
}