package com.crypticducs000webhost.myplaylist

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Hp on 12/21/2017.
 */
class Songs(var songID: Long, var SongsTitle: String, var artist: String, var songData: String, var dateAdded:Long):Parcelable{
    override fun writeToParcel(p0: Parcel?, p1: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }
object Statified{
    var nameComparator : Comparator<Songs> = Comparator<Songs> { song1, song2 ->
        val songOne = song1.SongsTitle.toUpperCase()
        val sonTwo = song2.SongsTitle.toUpperCase()
        songOne.compareTo(sonTwo)
    }

    var dateComparator: Comparator<Songs> = Comparator<Songs>{song1,song2 ->
        val songOne = song1.dateAdded.toDouble()
        val songTwo = song2.dateAdded.toDouble()
        songTwo.compareTo(songOne)
    }
}
}