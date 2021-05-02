package com.example.mmmmeeting.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mmmmeeting.Info.MeetingInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.MakeMeetingActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class MakeMeetingActivity constructor() : ToolbarActivity() {
    var makeMeeting: Button? = null
    var meetingName: EditText? = null
    var meetingDesc: EditText? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_meeting)
        makeMeeting = findViewById(R.id.makeMeetingBtn)
        meetingName = findViewById(R.id.makeMeetingText)
        meetingDesc = findViewById(R.id.meetingDesc)
        makeMeeting.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val name: String = meetingName.getText().toString()
                val description: String = meetingDesc.getText().toString()
                if (!nullCheck(name, description)) {
                    // 모임 이름이 이미 존재하는지 확인
                    checkMeetingName(name, description)
                }
            }
        })
    }

    private fun nullCheck(name: String, description: String): Boolean {
        if (name.length == 0 || description.length == 0) {
            Toast.makeText(this, "이름과 설명을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
            return true
        } else return false
    }

    private fun checkMeetingName(name: String, description: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("meetings").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                if ((document.getData().get("name").toString() == name) && (document.getData().get("description").toString() == description)) {
                                    //  이름과 설명이 모두 같으면 실패
                                    Toast.makeText(this@MakeMeetingActivity, "이름과 설명이 모두 같은 모임이 이미 존재합니다.", Toast.LENGTH_SHORT).show()
                                    Log.d("Document Read", name)
                                    return
                                }
                            }
                            //문서 확인 결과 중복이름이 없으면
                            meetingUpdate()
                        } else {
                            Log.d("Document Read", "Error getting documents: ", task.getException())
                        }
                    }
                })
    }

    private fun meetingUpdate() {
        val name: String = (findViewById<View>(R.id.makeMeetingText) as EditText).getText().toString()
        val description: String = (findViewById<View>(R.id.meetingDesc) as EditText).getText().toString()
        val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        // db에 저장할 모임 정보 객체 생성
        val info: MeetingInfo = MeetingInfo(name, description)
        info.setUserID(user!!.getUid())
        info.setLeader(user.getUid())

        // meeting table에 미팅 정보 저장
        db.collection("meetings").document().set(info)
                .addOnSuccessListener(object : OnSuccessListener<Void?> {
                    public override fun onSuccess(aVoid: Void?) {
                        // 미팅 정보 저장 = 메시지 출력 후 메인으로 복귀
                        startToast("미팅 생성에 성공하였습니다.")
                        myStartActivity(MainActivity::class.java)
                        finish()
                    }
                })
                .addOnFailureListener(object : OnFailureListener {
                    public override fun onFailure(e: Exception) {
                        startToast("미팅 생성에 실패하였습니다.")
                    }
                })
    }

    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun myStartActivity(c: Class<*>) {
        val intent: Intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}