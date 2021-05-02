package com.example.mmmmeeting.Info

import com.google.firebase.firestore.GeoPoint
import java.util.*

class VoteInfo(var scheduleID: String?) {
    val place = ArrayList<HashMap<String, Any>>()
    var state = "valid"
    fun setPlace(location: GeoPoint, vote: Int) {
        val place = HashMap<String, Any>()
        place["latlng"] = location
        place["vote"] = vote
        this.place.add(place)
    }
}