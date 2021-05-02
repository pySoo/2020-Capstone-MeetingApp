package com.example.mmmmeeting.Info

import java.io.Serializable
import java.util.*

class ScheduleInfo : Serializable {
    var title: String
    var meetingID: String
        private set
    var contents: ArrayList<String?>
    var lateComer: ArrayList<String>? = null
    var meetingPlace: String? = null
    var meetingDate: Date? = null
    var createdAt: Date
    var id: String? = null
    var type: String

    constructor(title: String, meetingID: String, contents: ArrayList<String?>, createdAt: Date, id: String?, type: String) {
        this.title = title
        this.meetingID = meetingID
        this.contents = contents
        this.createdAt = createdAt
        this.id = id
        this.type = type
    }

    constructor(title: String, meetingID: String, contents: ArrayList<String?>, lateComer: ArrayList<String>?, meetingPlace: String?, meetingDate: Date?, createdAt: Date, type: String) {
        this.title = title
        this.meetingID = meetingID
        this.contents = contents
        this.lateComer = lateComer
        this.meetingPlace = meetingPlace
        this.meetingDate = meetingDate
        this.createdAt = createdAt
        this.type = type
    }

    val scheduleInfo: Map<String, Any?>
        get() {
            val docData: MutableMap<String, Any?> = HashMap()
            docData["title"] = title
            docData["meetingID"] = meetingID
            docData["contents"] = contents
            docData["lateComer"] = lateComer
            docData["meetingPlace"] = meetingPlace
            docData["meetingDate"] = meetingDate
            docData["createdAt"] = createdAt
            docData["type"] = type
            return docData
        }

    fun setMeetingID() {
        meetingID = meetingID
    }
}