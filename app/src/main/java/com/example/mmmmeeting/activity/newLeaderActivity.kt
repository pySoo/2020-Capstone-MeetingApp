package com.example.mmmmeeting.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.newLeaderActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class newLeaderActivity constructor() : AppCompatActivity() {
    var changeLeader: Button? = null
    var newLeaderName: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_leader)
        changeLeader = findViewById(R.id.changeBtn)
        newLeaderName = findViewById(R.id.newLeaderName)
        changeLeader.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val intent: Intent = getIntent()
                val username: String = newLeaderName.getText().toString()
                val meetingCode: String? = intent.getExtras()!!.getString("Code")
                newLeader(username, meetingCode)
                finish()
            }
        })
    }

    private fun newLeader(user: String, code: String?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        // 모임 사용자 중 같은 이름이 존재
        db.collection("meetings").document((code)!!).get()
                .addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                    public override fun onComplete(task: Task<DocumentSnapshot>) {
                        if (task.isSuccessful()) {
                            val document: DocumentSnapshot = task.getResult()
                            if (document.exists()) {
                                // 같은 이름 있나 확인
                                userNameCheck(user, document.get("userID"), code)
                            } else {
                            }
                        } else {
                            Log.d("Attend", "Task Fail : " + task.getException())
                        }
                    }
                })
    }

    private fun userNameCheck(userName: String, userID: Any?, code: String?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        // 모든 유저 정보를 확인 -> 모임에 속한 유저와 같은 uid 발견시 해당 유저의 이름을 출력
        db.collection("users").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                if (userID.toString().contains(document.getId())) {
                                    if ((document.get("name").toString() == userName)) {
                                        db.collection("meetings").document((code)!!).update("leader", document.getId())
                                        Toast.makeText(this@newLeaderActivity, "모임장이 변경되었습니다", Toast.LENGTH_SHORT).show()
                                        Log.d("Reader Read", document.getId() + " => " + document.getData())
                                        return
                                    }
                                    Log.d("Document Read", document.getId() + " => " + document.getData())
                                }
                            }
                            Toast.makeText(this@newLeaderActivity, "이름을 다시 확인하세요.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("Document Read", "Error getting documents: ", task.getException())
                        }
                    }
                })
    }
}