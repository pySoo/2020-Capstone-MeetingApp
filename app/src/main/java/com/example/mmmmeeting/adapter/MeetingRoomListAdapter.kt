package com.example.mmmmeeting.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.mmmmeeting.Info.MeetingRoomItems
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.MeetingActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class MeetingRoomListAdapter constructor() : BaseAdapter() {
    var items: ArrayList<MeetingRoomItems> = ArrayList()
    var context // activity 정보 저장
            : Context? = null
    var code: String? = null

    // 아이템 추가
    fun addItem(item: MeetingRoomItems) {
        items.add(item)
    }

    // 아이템 크기
    public override fun getCount(): Int {
        return items.size
    }

    // 위치의 아이템
    public override fun getItem(position: Int): Any {
        return items.get(position)
    }

    // 위치 (아이템ID)
    public override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 뷰 설정
    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView: View = convertView
        context = parent.getContext() // activity 정보 읽기
        val listItem: MeetingRoomItems = items.get(position) //position 해당하는 listItem

        //list_item inflate => convertView 참조
        if (convertView == null) {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.meeting_room_item, parent, false)
        }

        // 텍스트뷰 설정
        val nameText: TextView = convertView.findViewById(R.id.roomName)
        val descriptText: TextView = convertView.findViewById(R.id.roomDescription)

        // 텍스트뷰에 글자 지정
        descriptText.setText(listItem.getDescription())
        nameText.setText(listItem.getName())
        val gridItem: RelativeLayout = convertView.findViewById<View>(R.id.meetingRoomItem) as RelativeLayout

        // 다음 액티비티 넘어가게
        gridItem.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                //Toast.makeText(context, "동작 확인 : " + listItem.getRoad(), Toast.LENGTH_SHORT).show();
                // MemberInitActivity 주소 정보 전달
                val intent: Intent = Intent(context, MeetingActivity::class.java)
                intent.putExtra("Name", listItem.getName())
                intent.putExtra("Description", listItem.getDescription())
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                db.collection("meetings").get()
                        .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                            public override fun onComplete(task: Task<QuerySnapshot>) {
                                if (task.isSuccessful()) {
                                    //모든 document 출력 (dou id + data arr { : , ... ,  })
                                    for (document: QueryDocumentSnapshot in task.getResult()) {
                                        // 모임 이름이 같은 경우 해당 모임의 코드 출력
                                        if ((document.get("name").toString() == listItem.getName()) && (document.get("description").toString() == listItem.getDescription())) {
                                            intent.putExtra("Code", document.getId())
                                            Log.d("Grid send", document.getId())
                                            context.startActivity(intent)
                                            return
                                        } else {
                                            Log.d("Document Snapshot", "No Document")
                                        }
                                    }
                                } else {
                                    Log.d("Document Read", "Error getting documents: ", task.getException())
                                }
                            }
                        })
            }
        })
        return convertView
    }
}