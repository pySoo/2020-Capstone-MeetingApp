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

        // 코드가 출력되는 TextView 클릭시 -> 코드 클립보드에 복사되도록
        code.setOnTouchListener(object : OnTouchListener {
            //터치 이벤트 리스너 등록(누를때)
            public override fun onTouch(v: View, event: MotionEvent): Boolean {
                if (event.getAction() == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    //클립보드 사용 코드
                    val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData: ClipData = ClipData.newPlainText("Code", code.getText().toString()) //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                    Toast.makeText(this@MeetingInfoActivity, "모임코드가 복사되었습니다.", Toast.LENGTH_SHORT).show()
                    clipboardManager.setPrimaryClip(clipData)
                }
                return true
            }
        })
    }

    // 모임코드 출력 위해 현재 모임 이름에 해당하는 모임 코드 찾기
    private fun readmeeting(meetingCode: String?) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val docRef: DocumentReference = db.collection("meetings").document((meetingCode)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        // 찾은 모임의 사용자 확인
                        val list: List<*>? = document.getData()!!.get("userID") as List<*>?
                        num = list!!.size
                        userFind(document.getData()!!.get("userID"))

                        // 모임장 확인
                        if (document.getData()!!.get("leader").toString().length == 0) {
                            newLeader(document.getData()!!.get("userID"), document.getId())
                        } else {
                            leaderFind(document.getData()!!.get("leader").toString())
                        }
                        Log.d("Attend", "Data is : " + document.getId())
                    } else {
                        // 존재하지 않는 문서
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

        // 모든 유저 정보를 확인 -> 모임에 속한 유저와 같은 uid 발견시 가장 먼저 발견한 유저 리더로
        db.collection("users").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
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

        // 모든 유저 정보를 확인 -> 모임에 속한 유저와 같은 uid 발견시 해당 유저의 이름을 출력
        db.collection("users").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                if (userID.toString().contains(document.getId())) {
                                    val username: String = document.get("name").toString() + "  "

                                    // 찾은 유저 이름을 텍스트뷰에 설정
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
                Toast.makeText(this, "양도할 사람이 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                myStartActivity(newLeaderActivity::class.java, code)
                finish()
            }
            (R.id.deleteBtn) -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("모임 탈퇴") // 제목
                        .setMessage("[ " + meetingname + " ]" + " 모임을 정말로 나가시겠습니까?") // 메세지
                        // .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //약속 날짜를 확정 //db로 해당 날짜 올리기
                                myStartActivity(MeetingDeleteActivity::class.java, code)
                            }
                        })
                        .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                            // 취소 버튼 클릭시
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //취소 이벤트...
                            }
                        })
                val dialog: AlertDialog = builder.create() // 알림창 객체 생성
                dialog.show() // 알림창 띄우기
            }
        }
    }

    public override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}