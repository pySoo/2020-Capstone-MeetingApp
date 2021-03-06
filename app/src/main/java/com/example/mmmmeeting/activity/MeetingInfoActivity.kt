package com.example.mmmmeeting.activity

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.MeetingInfoActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class MeetingInfoActivity constructor() : AppCompatActivity(), View.OnClickListener {
    private var name: TextView? = null
    private var description: TextView? = null
    private var code: TextView? = null
    private var user: TextView? = null
    private var leadertv: TextView? = null
    private var invite: Button? = null
    private var delete: Button? = null
    private var changeLeader: Button? = null
    private var meetingname: String? = null
    private var num: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting_info)
        val intent: Intent = getIntent()
        meetingname = intent.getExtras()!!.getString("Name")
        val meetingdescription: String? = intent.getExtras()!!.getString("Description")
        val meetingCode: String? = intent.getExtras()!!.getString("Code")
        val isLeader: Boolean = intent.getExtras()!!.getBoolean("isLeader")
        name = findViewById(R.id.meetingName)
        description = findViewById(R.id.meetingDescription)
        code = findViewById(R.id.meetingCode)
        user = findViewById(R.id.meetingUsers)
        invite = findViewById(R.id.inviteBtn)
        delete = findViewById(R.id.deleteBtn)
        changeLeader = findViewById(R.id.newLeader)
        leadertv = findViewById(R.id.meetingLeader)
        if (isLeader) {
            changeLeader.setVisibility(View.VISIBLE)
        }
        invite.setOnClickListener(this)
        delete.setOnClickListener(this)
        changeLeader.setOnClickListener(this)
        name.setText(meetingname)
        description.setText(meetingdescription)
        code.setText(meetingCode)
        readmeeting(meetingCode)

        // ????????? ???????????? TextView ????????? -> ?????? ??????????????? ???????????????
        code.setOnTouchListener(object : OnTouchListener {
            //?????? ????????? ????????? ??????(?????????)
            public override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //????????? ??? ??????
                    //???????????? ?????? ??????
                    val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData: ClipData = ClipData.newPlainText("Code", code.getText().toString()) //??????????????? ID?????? ???????????? id ?????? ???????????? ??????
                    Toast.makeText(this@MeetingInfoActivity, "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                    clipboardManager.setPrimaryClip(clipData)
                }
                return true
            }
        })
    }

    // ???????????? ?????? ?????? ?????? ?????? ????????? ???????????? ?????? ?????? ??????
    private fun readmeeting(meetingCode: String?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val docRef: DocumentReference = db.collection("meetings").document((meetingCode)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // ?????? ????????? ???????????? ??????
                        // ?????? ????????? ????????? ??????
                        val list: List<*>? = document.getData()!!.get("userID") as List<*>?
                        num = list!!.size
                        userFind(document.getData()!!.get("userID"))

                        // ????????? ??????
                        if (document.getData()!!.get("leader").toString().length == 0) {
                            newLeader(document.getData()!!.get("userID"), document.getId())
                        } else {
                            leaderFind(document.getData()!!.get("leader").toString())
                        }
                        Log.d("Attend", "Data is : " + document.getId())
                    } else {
                        // ???????????? ?????? ??????
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
    }

    private fun leaderFind(leader: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val docRef: DocumentReference = db.collection("users").document(leader)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        leadertv!!.setText(document.get("name").toString())
                    } else {
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
    }

    private fun newLeader(userID: Any?, code: String) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        // ?????? ?????? ????????? ?????? -> ????????? ?????? ????????? ?????? uid ????????? ?????? ?????? ????????? ?????? ?????????
        db.collection("users").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //?????? document ??????
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                if (userID.toString().contains(document.getId())) {
                                    db.collection("meetings").document(code).update("leader", document.getId())
                                    leaderFind(document.getId())
                                    Log.d("Document Read", document.getId() + " => " + document.getData())
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

    private fun userFind(userID: Any?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        // ?????? ?????? ????????? ?????? -> ????????? ?????? ????????? ?????? uid ????????? ?????? ????????? ????????? ??????
        db.collection("users").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //?????? document ??????
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                if (userID.toString().contains(document.getId())) {
                                    val username: String = document.get("name").toString() + "  "

                                    // ?????? ?????? ????????? ??????????????? ??????
                                    user!!.append(username)
                                    Log.d("Document Read", document.getId() + " => " + document.getData())
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

    private fun myStartActivity(c: Class<*>, code: TextView?) {
        val intent: Intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("Code", code!!.getText().toString())
        startActivity(intent)
    }

    private fun myStartActivity(c: Class<*>, meetingname: String?, code: TextView?) {
        val intent: Intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("Name", meetingname)
        intent.putExtra("Code", code!!.getText().toString())
        startActivity(intent)
    }

    private fun myStartActivity(c: Class<*>) {
        val intent: Intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("Code", code!!.getText().toString())
        startActivity(intent)
    }

    public override fun onClick(v: View) {
        when (v.getId()) {
            (R.id.inviteBtn) -> myStartActivity(inviteActivity::class.java, meetingname, code)
            (R.id.newLeader) -> if (num == 1) {
                Toast.makeText(this, "????????? ????????? ????????????.", Toast.LENGTH_SHORT).show()
            } else {
                myStartActivity(newLeaderActivity::class.java, code)
                finish()
            }
            (R.id.deleteBtn) -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("?????? ??????") // ??????
                        .setMessage("[ " + meetingname + " ]" + " ????????? ????????? ??????????????????????") // ?????????
                        // .setCancelable(false)        // ?????? ?????? ????????? ?????? ?????? ??????
                        .setPositiveButton("??????", object : DialogInterface.OnClickListener {
                            // ?????? ?????? ????????? ??????, ????????? ???????????????.
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //?????? ????????? ?????? //db??? ?????? ?????? ?????????
                                myStartActivity(MeetingDeleteActivity::class.java, code)
                            }
                        })
                        .setNegativeButton("??????", object : DialogInterface.OnClickListener {
                            // ?????? ?????? ?????????
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //?????? ?????????...
                            }
                        })
                val dialog: AlertDialog = builder.create() // ????????? ?????? ??????
                dialog.show() // ????????? ?????????
            }
        }
    }

    public override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}