package com.example.mmmmeeting.Info

import java.io.Serializable
import java.util.*

class PostInfo : Serializable {
    var title: String
    var description: String
    var contents: ArrayList<String?>?
    var publisher: String
    var createdAt: Date
    var id: String? = null
    var meetingID: String?
        private set

    constructor(title: String, description: String, meetingID: String?, contents: ArrayList<String?>?, publisher: String, createdAt: Date, id: String?) {
        this.title = title
        this.description = description
        this.meetingID = meetingID
        this.contents = contents
        this.publisher = publisher
        this.createdAt = createdAt
        this.id = id
    }

    constructor(title: String, description: String, meetingID: String?, contents: ArrayList<String?>?, formats: ArrayList<String?>?, publisher: String, createdAt: Date) {
        this.title = title
        this.description = description
        this.meetingID = meetingID
        this.contents = contents
        this.publisher = publisher
        this.createdAt = createdAt
    }

    constructor(title: String, description: String, contentsList: ArrayList<String?>?, uid: String, date: Date, meetingcode: String?) {
        this.title = title
        this.description = description
        contents = contentsList
        publisher = uid
        createdAt = date
        meetingID = meetingcode
    }

    val postInfo: Map<String, Any?>
        get() {
            val docData: MutableMap<String, Any?> = HashMap()
            docData["title"] = title
            docData["description"] = description
            docData["meetingID"] = meetingID
            docData["contents"] = contents
            docData["publisher"] = publisher
            docData["createdAt"] = createdAt
            return docData
        }

    fun setMeetingID() {
        meetingID = meetingID
    }
}