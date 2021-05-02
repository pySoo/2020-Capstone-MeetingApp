package com.example.mmmmeeting.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.mmmmeeting.Info.MeetingRoomItems
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.MainActivity
import com.example.mmmmeeting.adapter.MeetingRoomListAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*

class MainActivity constructor() : ToolbarActivity(), View.OnClickListener {
    private var mAuth: FirebaseAuth? = null
    private var db: FirebaseFirestore? = null
    var addMeeting: Button? = null
    var attendMeeting: Button? = null
    var text: TextView? = null
    var check: Int = 0
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setToolbarTitle("우리 지금 만나")
        addMeeting = findViewById(R.id.meetingAdd)
        attendMeeting = findViewById(R.id.meetingAttend)
        text = findViewById(R.id.text)
        addMeeting.setOnClickListener(this)
        attendMeeting.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        showMeetings()
    }

    // 모임 목록을 보여줌
    private fun showMeetings() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
        val gridView: ListView = findViewById(R.id.meetingRoomList)
        val adapter: MeetingRoomListAdapter = MeetingRoomListAdapter()
        mAuth = FirebaseAuth.getInstance()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        // 현재 user의 uid 확인 -> 모든 모임 정보를 확인
        // 모임 정보에 현재 유저의 uid가 존재하면 화면에 출력
        // 모임 정보에 현재 유저 uid 없으면 출력 X
        db.collection("meetings")
                .get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                if (document.getData().get("userID").toString().contains(user!!.getUid())) {
                                    // 현재 유저가 존재하는 모임을 찾은 경우
                                    // adapter에 모임에 대한 정보를 갖는 객체 생성해 저장
                                    adapter.addItem(MeetingRoomItems(document.getData().get("name").toString(), document.getData().get("description").toString()))
                                    check = 1
                                    Log.d("Document Read", document.getId() + " => " + document.getData())
                                } else {
                                    Log.d("Document Snapshot", "No Document")
                                }
                            }
                            if (check == 0) {
                                text!!.setText("등록된 모임이 없습니다." + "\n" + "모임을 생성하거나 참여해보세요!")
                            } else {
                                // 모든 문서 확인이 끝나면
                                // gridView에 adapter 할당 -> adapter에 있는 아이템 그리드 뷰로 화면에 출력
                                text!!.setText("")
                                gridView.setAdapter(adapter)
                            }
                        } else {
                            Log.d("Document Read", "Error getting documents: ", task.getException())
                        }
                    }
                })
    }

    //메뉴바 코드
    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.setting, menu)
        return true
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.revoke -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("회원 탈퇴") // 제목
                        .setMessage("우리 지금 만나 앱을 정말로 탈퇴하시겠습니까?") // 메세지
                        // .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) {
                                revokeAccess()
                                finish()
                            }
                        })
                        .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                            // 취소 버튼 클릭시
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //취소 이벤트...
                            }
                        })
                val dialog: AlertDialog = builder.create() // 알림창 객체 생성
                dialog.show() // 알림창 띄우기
                return true
            }
            R.id.logout -> {
                signOut()
                finish()
                return true
            }
            R.id.memberInfo -> {
                myStartActivity(MemberInitActivity::class.java)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 로그아웃 함수
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    // 회원 탈퇴 함수
    private fun revokeAccess() {
        db = FirebaseFirestore.getInstance()

        // 인증제거
        mAuth!!.getCurrentUser()!!.delete()

        // user 테이블에서 현재 user uid로 저장된 문서 삭제
        db!!.collection("users").document((mAuth!!.getUid())!!)
                .delete()
                .addOnSuccessListener(object : OnSuccessListener<Void?> {
                    public override fun onSuccess(aVoid: Void?) {
                        Log.d("revoke User", "DocumentSnapshot successfully deleted!")
                    }
                })
                .addOnFailureListener(object : OnFailureListener {
                    public override fun onFailure(e: Exception) {
                        Log.w("revoke User", "Error deleting document", e)
                    }
                })

//      현재 user가 속한 모임의 모임원 정보에서 uid 제거
        db!!.collection("meetings").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                // 현재 uid가 모임 정보에 존재하는 경우
                                if (document.getData().get("userID").toString().contains((mAuth!!.getUid())!!)) {
                                    // db에서 현재 유저 uid 삭제
                                    val userdel: DocumentReference = db!!.collection("meetings").document(document.getId())
                                    if ((document.get("leader") == mAuth!!.getUid())) {
                                        userdel.update("leader", "")
                                    }
                                    userdel.update("userID", FieldValue.arrayRemove(mAuth!!.getUid()))
                                    Log.d("Delete", document.getId() + " => " + document.getData())
                                    meetingMemberCheck(document.getId())
                                    finish()
                                    return
                                } else {
                                    Log.d("Delete", "No Document")
                                    finish()
                                }
                            }
                        } else {
                            Log.d("Delete", "Error getting documents: ", task.getException())
                        }
                    }
                })
        Toast.makeText(this@MainActivity, "회원탈퇴를 완료했습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun meetingMemberCheck(code: String) {
        val meetingdel: DocumentReference = db!!.collection("meetings").document(code)
        meetingdel.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    Log.d("Delete3", document.get("userID").toString())
                    // 모임원 없는 모임이 된 경우 모임 삭제
                    Log.d("Delete3 Test", "users:" + (document.get("userID").toString().length))
                    // 모임원 없음 = 2
                    // 모임원 한 명 = 30
                    // 이 후 모임원 한 명 증가시마다 length +30 씩 증가
                    if (document.get("userID").toString().length == 2) {
                        // 모임에 있는 약속, 게시판 내용도 전부 삭제해야..
                        scheduleDelete(code)
                        boardDelete(code)
                        chatDelete(code)
                        meetingdel.delete()
                    }
                } else {
                    Log.d("Delete", "Task Fail : " + task.getException())
                }
            }
        })
    }

    private fun myStartActivity(c: Class<*>) {
        val intent: Intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    public override fun onClick(v: View) {
        when (v.getId()) {
            (R.id.meetingAdd) -> myStartActivity(MakeMeetingActivity::class.java)
            (R.id.meetingAttend) -> myStartActivity(MeetingAttendActivity::class.java)
        }
    }

    private fun scheduleDelete(code: String) {
        val scheduleDel: CollectionReference = db!!.collection("schedule")
        scheduleDel.get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                // 문서가 참조하는 이름이 삭제 해야하는 모임 이름과 같으면 일정 삭제
                                if ((document.getData().get("meetingID").toString() == code)) {
                                    Log.d("일정 삭제", document.getData().get("title").toString())
                                    voteDelete(document.getId())
                                    scheduleDel.document(document.getId()).delete()
                                    return
                                }
                            }
                        } else {
                            Log.d("Document Read", "Error getting documents: ", task.getException())
                        }
                    }
                })
    }

    private fun boardDelete(code: String) {
        val boardDel: CollectionReference = db!!.collection("posts")
        boardDel.get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                // 문서가 참조하는 이름이 삭제 해야하는 모임 이름과 같으면 게시글 삭제
                                if ((document.getData().get("meetingID").toString() == code)) {
                                    Log.d("일정 삭제", document.getData().get("title").toString())
                                    boardDel.document(document.getId()).delete()
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

    private fun chatDelete(code: String) {
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val chatRef: DatabaseReference = firebaseDatabase.getReference("chat").child(code)
        chatRef.removeValue()
    }

    private fun voteDelete(code: String) {
        val voteDel: CollectionReference = db!!.collection("vote")
        voteDel.get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            //모든 document 확인
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                // 문서가 참조하는 이름이 삭제 해야하는 약속 이름과 같으면 투표 삭제
                                if ((document.getData().get("scheduleID").toString() == code)) {
                                    voteDel.document(document.getId()).delete()
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
}