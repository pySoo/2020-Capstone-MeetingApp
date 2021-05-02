// 지각자 체크 Activity - 1시간 이상 지각한 경우 출석체크 불가
package com.example.mmmmeeting.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mmmmeeting.Info.ScheduleInfo
import com.example.mmmmeeting.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CheckLateActivity constructor() : AppCompatActivity(), View.OnClickListener {
    private var postInfo: ScheduleInfo? = null
    private var hour: Int = 0
    private var minute: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var calDate: Date? = null
    private var scID: String? = null
    var user: FirebaseUser? = null
    var db: FirebaseFirestore? = null
    var calendar: Calendar? = null
    var tempCal: Calendar? = null
    var attendanceBtn: Button? = null
    var meetingDate: Date? = null
    var place: String? = null
    var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_check_late)
        attendanceBtn = findViewById(R.id.checkAttendBtn)
        attendanceBtn.setOnClickListener(this)
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().getCurrentUser()
        calendar = Calendar.getInstance()
        calDate = Date()
        tempCal = Calendar.getInstance()

        // 해당 스케쥴 ID를 통해 약속 날짜를 찾아냄
        postInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo?
        scID = postInfo.getId()
        val docRef: DocumentReference = db!!.collection("schedule").document((scID)!!)

        // Firebase DB와의 연동 - 약속 날짜, 약속 장소를 찾음
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        meetingDate = document.getDate("meetingDate")
                        val placeMap: Map<String, String>? = document.getData()!!.get("meetingPlace") as Map<String, String>?
                        if (placeMap != null) {
                            place = placeMap.get("name")
                        }
                    } else {
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })

        // Handler 사용 이유: 출석체크 버튼 클릭 후 DB에서 약속 시간을 가져오는데 시간이 걸리기 때문
        // DB 시간과 현재 시간 비교가 끝난 후 Handler 메시지 값을 통해 지각 여부를 결정한다.
        handler = object : Handler() {
            public override fun handleMessage(msg: Message) {
                val bd: Bundle = msg.getData()
                val str: String? = bd.getString("arg")
                when (str) {
                    "Late" -> {
                        Toast.makeText(getApplicationContext(), "약속시간이 지나서 출석체크 할 수 없습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    "TimeCheck" -> myStartActivity(CurrentMapActivity::class.java, postInfo, hour, minute)
                }
            }
        }
    }

    // 지각 여부를 체크하는 메서드
    private fun timeCheck() {
        // 출석 체크 버튼을 누르는 사이에 약속 시간이 변경 되었을 수 있어서 시간만 가져옴
        val docRef: DocumentReference = db!!.collection("schedule").document((scID)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        meetingDate = document.getDate("meetingDate")

                        // 약속 시간
                        calendar!!.setTime(meetingDate)
                        hour = calendar!!.get(Calendar.HOUR_OF_DAY)
                        minute = calendar!!.get(Calendar.MINUTE)
                        month = calendar!!.get(Calendar.MONTH)
                        day = calendar!!.get(Calendar.DAY_OF_MONTH)
                        calDate = calendar!!.getTime()

                        // 현재 시간
                        val now: Date = Date()
                        val temcal: Calendar = Calendar.getInstance()
                        temcal.setTime(now)
                        val nowHour: Int = temcal.get(Calendar.HOUR_OF_DAY)
                        val nowMinute: Int = temcal.get(Calendar.MINUTE)
                        val nowMonth: Int = temcal.get(Calendar.MONTH)
                        val nowDay: Int = temcal.get(Calendar.DAY_OF_MONTH)

                        // 당일인 경우만 시간 체크
                        if (nowMonth == month && nowDay == day) {
                            // 1시간 이상 지각이면 출석 체크 불가
                            if ((nowHour - hour >= 1 && nowMinute - minute > 0) || nowHour >= hour + 2) {
                                // Handler에 메시지 전달
                                val bd: Bundle = Bundle()
                                bd.putString("arg", "Late")
                                sendMessage(bd)
                                // 제 시간에 옴
                            } else {
                                val bd: Bundle = Bundle()
                                bd.putString("arg", "TimeCheck")
                                sendMessage(bd)
                            }
                        } else {
                            val bd: Bundle = Bundle()
                            bd.putString("arg", "Late")
                            sendMessage(bd)
                        }
                    } else {
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
    }

    private fun sendMessage(bd: Bundle) {
        val msg: Message = handler!!.obtainMessage()
        msg.setData(bd)
        handler!!.sendMessage(msg)
    }

    public override fun onClick(v: View) {
        when (v.getId()) {
            R.id.checkAttendBtn -> {
                // 버튼 누르면 thread 실행해서 시간 받아오고 바로 종료되게 함
                // -> thread를 계속 실행하니까 flag값이 계속 바뀜
                if (meetingDate == null || place == null) {
                    Toast.makeText(getApplicationContext(), "약속 시간 또는 장소가 정해지지 않았습니다.", Toast.LENGTH_SHORT).show()
                    return
                }
                // 약속 날짜 & 장소가 모두 정해졌으면 약속 시간 체크
                timeCheck()
            }
        }
    }

    private fun myStartActivity(c: Class<*>, postInfo: ScheduleInfo?, hour: Int, minute: Int) {
        val intent: Intent = Intent(this, c)
        intent.putExtra("scheduleInfo", postInfo)
        intent.putExtra("hour", hour)
        intent.putExtra("minute", minute)
        startActivityForResult(intent, 0)
    }
}