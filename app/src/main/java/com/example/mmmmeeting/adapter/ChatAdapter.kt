package com.example.mmmmeeting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mmmmeeting.Info.ChatItem
import com.example.mmmmeeting.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ChatAdapter(var chatItems: ArrayList<ChatItem>, var layoutInflater: LayoutInflater) : BaseAdapter() {
    val user = FirebaseAuth.getInstance().currentUser
    var db = FirebaseFirestore.getInstance()
    var profilePath: String? = null
    override fun getCount(): Int {
        return chatItems.size
    }

    override fun getItem(position: Int): Any {
        return chatItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View {
        val item = chatItems[position]
        var itemView: View? = null

        // 날짜만 받아왔을 때
        if (item.id == "date") {
            itemView = layoutInflater.inflate(R.layout.list_date, viewGroup, false)
            val date = itemView.findViewById<TextView>(R.id.date)
            println("date is: " + item.time)
            date.text = item.time
            itemView.visibility = View.VISIBLE
            return itemView
        }


        //메세지가 내 메세지인지 UID로 확인
        if (item.id == user!!.uid) {
            itemView = layoutInflater.inflate(R.layout.list_mychatbox, viewGroup, false)
            itemView.visibility = View.INVISIBLE
            //만들어진 itemView에 값들 설정
            val tvName = itemView.findViewById<TextView>(R.id.tv_name)
            val tvMsg = itemView.findViewById<TextView>(R.id.tv_msg)
            val tvTime = itemView.findViewById<TextView>(R.id.tv_time)
            tvName.text = item.name
            tvMsg.text = item.message
            tvTime.text = item.time
            itemView.visibility = View.VISIBLE
        } else {
            itemView = layoutInflater.inflate(R.layout.list_otherchatbox, viewGroup, false)
            val finalItemView = itemView
            itemView.visibility = View.INVISIBLE
            db.collection("users").document(item.id).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val iv: CircleImageView = finalItemView!!.findViewById(R.id.iv)
                        val tvName = finalItemView.findViewById<TextView>(R.id.tv_name)
                        val tvMsg = finalItemView.findViewById<TextView>(R.id.tv_msg)
                        val tvTime = finalItemView.findViewById<TextView>(R.id.tv_time)
                        tvName.text = item.name
                        tvMsg.text = item.message
                        tvTime.text = item.time
                        if (document["profilePath"] != null) {
                            profilePath = document["profilePath"].toString()
                            Glide.with(finalItemView).load(profilePath).centerCrop().override(500).into(iv)
                        }
                        finalItemView.visibility = View.VISIBLE
                    }
                }
            }
        }
        return itemView
    }
}