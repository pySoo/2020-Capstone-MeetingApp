// 공유 캘린더 Activity - 모임원 모두 생성, 수정, 삭제, 확인 가능
package com.example.mmmmeeting.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.mmmmeeting.Info.ScheduleInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.CalendarActivity
import com.example.mmmmeeting.decorators.EventDecorator
import com.example.mmmmeeting.decorators.OneDayDecorator
import com.example.mmmmeeting.decorators.SaturdayDecorator
import com.example.mmmmeeting.decorators.SundayDecorator
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class CalendarActivity constructor() : ToolbarActivity() {
    var str: String? = null
    var cha_Btn: Button? = null
    var del_Btn: Button? = null
    var save_Btn: Button? = null
    var sel_Btn: Button? = null
    var diaryTextView: TextView? = null
    var memotext: TextView? = null
    var contextEditText: EditText? = null
    private var scInfo: ScheduleInfo? = null
    var scID: String? = null
    var mdate // 정해진 약속 날짜
            : Date? = null
    private var db: FirebaseFirestore? = null
    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
    var userId: String? = null // 현재 user id
    var leaderId: String? = null // 모임의 방장 id
    var calendarMap: MutableMap<String, String>? = HashMap() // 날짜별 메모 저장
    var materialCalendarView: MaterialCalendarView? = null
    var result: ArrayList<String> = ArrayList() //점 표시할 날짜들
    var selectedDay: ArrayList<String> = ArrayList() //확정된 날짜
    var transString: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setToolbarTitle("캘린더")
        materialCalendarView = findViewById<View>(R.id.calendarView) as MaterialCalendarView?
        materialCalendarView!!.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
        materialCalendarView!!.addDecorators(
                SundayDecorator(),  // 일요일 색칠
                SaturdayDecorator(),  // 토요일 색칠
                OneDayDecorator(this@CalendarActivity)) // 오늘 날짜 색칠
        diaryTextView = findViewById(R.id.diaryTextView)
        save_Btn = findViewById(R.id.save_Btn)
        del_Btn = findViewById(R.id.del_Btn)
        cha_Btn = findViewById(R.id.cha_Btn)
        sel_Btn = findViewById(R.id.sel_Btn)
        memotext = findViewById(R.id.memotext)
        contextEditText = findViewById(R.id.contextEditText)
        memotext.setMovementMethod(ScrollingMovementMethod())
        db = FirebaseFirestore.getInstance()
        scInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo?
        scID = scInfo.getId() // schedule ID 가져오기
        userId = user!!.getUid() // 현재 user id
        db!!.collection("meetings").document(scInfo.getMeetingID()).get()
                .addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                    public override fun onComplete(task: Task<DocumentSnapshot>) {
                        if (task.isSuccessful()) {
                            val document: DocumentSnapshot = task.getResult()
                            if (document.exists()) {
                                // 해당 문서가 존재하는 경우
                                leaderId = document.getData()!!.get("leader").toString()
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
        val docRef: DocumentReference = db!!.collection("schedule").document((scID)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            //schedule에 저장된 calendarText 받아오기
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) { // 해당 문서가 존재하는 경우
                        if (document.getData()!!.get("calendarText") != null) { // 저장된 메모가 있을 때
                            calendarMap = document.getData()!!.get("calendarText") as MutableMap<String, String>?
                            val set: Set<*> = calendarMap!!.keys
                            val iterator: Iterator<*> = set.iterator()
                            while (iterator.hasNext()) { // 점 표시할 리스트에 메모가 있는 날짜를 넣어준다
                                val key: String = iterator.next() as String
                                result.add(key)
                            }
                            ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor()) // 데코 표시
                        }
                        if (document.getData()!!.get("meetingDate") != null) { // 확정된 날짜가 있으면
                            val day: Date = document.getTimestamp("meetingDate")!!.toDate() // 확정 날짜 받아오기
                            val sel_day: String = transString.format(day) // String으로 변환
                            selectedDay.add(sel_day)
                            selectDayDeco()
                        }
                        Log.d("Attend", "Data is : " + document.getId())
                    } else { // 존재하지 않는 문서
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
        materialCalendarView!!.setOnDateChangedListener(object : OnDateSelectedListener {
            public override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) { //달력 날짜가 선택되면
                diaryTextView.setVisibility(View.VISIBLE) // 해당 날짜가 뜨는 textView Visible
                save_Btn.setVisibility(View.VISIBLE) // 저장 버튼 Visivble
                contextEditText.setVisibility(View.VISIBLE) // EditText가 Visible
                memotext.setVisibility(View.INVISIBLE) // 저장된 내용이 Visible
                cha_Btn.setVisibility(View.INVISIBLE) // 수정 버튼 Invisible
                del_Btn.setVisibility(View.INVISIBLE) // 삭제 버튼 Invisible
                sel_Btn.setVisibility(View.INVISIBLE) // 선택 버튼 Invisible
                val Year: Int = date.getYear()
                val Month: Int = date.getMonth() + 1
                val Day: Int = date.getDay()
                Log.i("Year test", Year.toString() + "")
                Log.i("Month test", Month.toString() + "")
                Log.i("Day test", Day.toString() + "")
                val shot_Day: String = Year.toString() + "-" + Month + "-" + Day
                Log.i("shot_Day test", shot_Day + "")
                materialCalendarView!!.clearSelection()
                diaryTextView.setText(String.format("%d / %d / %d", Year, Month, Day)) // 날짜를 보여주는 텍스트에 해당 날짜를 넣음
                contextEditText.setText("") // EditTecx에 공백 넣음
                checkDay(shot_Day) // checkDay 호출
                sel_Btn.setOnClickListener(object : View.OnClickListener {
                    //선택 버튼 누르면
                    public override fun onClick(view: View) {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this@CalendarActivity)
                        builder.setTitle("날짜 확정") // 제목
                                .setMessage(shot_Day + "로 설정하시겠습니까?") // 메세지
                                // .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                                .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                                    // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                    public override fun onClick(dialog: DialogInterface, whichButton: Int) { //약속 날짜를 확정 //db로 해당 날짜 올리기
                                        val day: IntArray = IntArray(2) // 약속 달과 일
                                        day.get(0) = Month - 1
                                        day.get(1) = Day
                                        val intent: Intent = Intent(this@CalendarActivity, NoticeActivity::class.java)
                                        intent.putExtra("meetingdate", day)
                                        intent.putExtra("scInfo", scInfo.getTitle())
                                        startActivityForResult(intent, 0) // 시간 받아오기
                                        selectedDay.clear()
                                        selectedDay.add(shot_Day)
                                        removeDeco()
                                        // selectDay(selectedDay); //확정 날짜 이벤트 표시
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
                })
                save_Btn.setOnClickListener(object : View.OnClickListener {
                    public override fun onClick(view: View) { //저장 버튼 클릭
                        str = contextEditText.getText().toString() // EditText 내용을 str에 저장
                        calendarMap!!.put(shot_Day, str!!)
                        db!!.collection("schedule").document((scID)!!).update("calendarText", calendarMap) // calendarText에 새메모 업데이트
                        memotext.setText(str) // TextView에 str 출력
                        save_Btn.setVisibility(View.INVISIBLE) // 저장 버튼 Invisible
                        cha_Btn.setVisibility(View.VISIBLE) // 수정 버튼 Visible
                        del_Btn.setVisibility(View.VISIBLE) // 삭제 버튼 Visible
                        if ((userId == leaderId)) {
                            sel_Btn.setVisibility(View.VISIBLE) // 선택 버튼 Visible
                        }
                        contextEditText.setVisibility(View.INVISIBLE)
                        memotext.setVisibility(View.VISIBLE)
                        result.add(shot_Day) // result에 메모가 저장된 날짜 추가
                        ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor()) // 데코 표시
                    }
                })
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                mdate = data!!.getSerializableExtra("fulldate") as Date?
                scInfo.setMeetingDate(mdate)
                db!!.collection("schedule").document((scID)!!).update("meetingDate", mdate) // 선택한 날짜로 db에 저장
                //  Toast.makeText(getApplicationContext(), mdate+"로 약속날짜가 설정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private inner class ApiSimulator internal constructor(var Time_Result: ArrayList<String>) : AsyncTask<Void?, Void?, List<CalendarDay>>() {
        protected override fun doInBackground(vararg voids: Void): List<CalendarDay> {
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val calendar: Calendar = Calendar.getInstance()
            val dates: ArrayList<CalendarDay> = ArrayList()
            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for (i in Time_Result.indices) {
                val time: Array<String> = Time_Result.get(i).split("-").toTypedArray()
                val year: Int = time.get(0).toInt()
                val month: Int = time.get(1).toInt()
                val dayy: Int = time.get(2).toInt()
                calendar.set(year, month - 1, dayy)
                val day: CalendarDay = CalendarDay.from(calendar)
                dates.add(day)
            }
            return dates
        }

        override fun onPostExecute(calendarDays: List<CalendarDay>) {
            super.onPostExecute(calendarDays)
            if (isFinishing()) {
                return
            }
            materialCalendarView!!.addDecorator(EventDecorator(Color.RED, calendarDays, this@CalendarActivity, 1))
        }
    }

    fun checkDay(shot_Day: String) {
        try {
            str = String(calendarMap!!.get(shot_Day)) // shot_Day로 저장된 메모를 str에 받음
            contextEditText!!.setVisibility(View.INVISIBLE)
            memotext!!.setVisibility(View.VISIBLE)
            memotext!!.setText(str)
            save_Btn!!.setVisibility(View.INVISIBLE)
            cha_Btn!!.setVisibility(View.VISIBLE)
            del_Btn!!.setVisibility(View.VISIBLE)
            if ((userId == leaderId)) {
                sel_Btn!!.setVisibility(View.VISIBLE) // 선택 버튼 Visible
            }
            cha_Btn!!.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(view: View) { // 수정 버튼 클릭
                    contextEditText!!.setVisibility(View.VISIBLE)
                    memotext!!.setVisibility(View.INVISIBLE)
                    contextEditText!!.setText(str) // editText에 textView에 저장된 내용 출력
                    save_Btn!!.setVisibility(View.VISIBLE)
                    cha_Btn!!.setVisibility(View.INVISIBLE)
                    del_Btn!!.setVisibility(View.INVISIBLE)
                    sel_Btn!!.setVisibility(View.INVISIBLE)
                    memotext!!.setText(contextEditText!!.getText())
                }
            })
            del_Btn!!.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(view: View) { // 삭제 버튼 클릭
                    memotext!!.setVisibility(View.INVISIBLE)
                    contextEditText!!.setText("")
                    contextEditText!!.setVisibility(View.VISIBLE)
                    save_Btn!!.setVisibility(View.VISIBLE)
                    cha_Btn!!.setVisibility(View.INVISIBLE)
                    del_Btn!!.setVisibility(View.INVISIBLE)
                    sel_Btn!!.setVisibility(View.INVISIBLE)
                    calendarMap!!.remove(shot_Day)
                    db!!.collection("schedule").document((scID)!!).update("calendarText", calendarMap)
                    result.remove(shot_Day) // 이벤트 표시에서 메모 삭제되는 날 지우기
                    if (selectedDay.contains(shot_Day)) { //삭제되는 날이 확정된 날이면
                        selectedDay.clear()
                        db!!.collection("schedule").document((scID)!!).update("meetingDate", null) // db에 확정된 날짜를 지움
                    }
                    removeDeco()
                }
            })
            if (memotext!!.getText() == null) {
                memotext!!.setVisibility(View.INVISIBLE)
                diaryTextView!!.setVisibility(View.VISIBLE)
                save_Btn!!.setVisibility(View.VISIBLE)
                cha_Btn!!.setVisibility(View.INVISIBLE)
                del_Btn!!.setVisibility(View.INVISIBLE)
                sel_Btn!!.setVisibility(View.INVISIBLE)
                contextEditText!!.setVisibility(View.VISIBLE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun selectDayDeco() { //확정 날짜 표시
        val calendar: Calendar = Calendar.getInstance()
        val dates: ArrayList<CalendarDay> = ArrayList()
        for (i in selectedDay.indices) {
            val time: Array<String> = selectedDay.get(i).split("-").toTypedArray()
            val year: Int = time.get(0).toInt()
            val month: Int = time.get(1).toInt()
            val dayy: Int = time.get(2).toInt()
            calendar.set(year, month - 1, dayy)
            val day: CalendarDay = CalendarDay.from(calendar)
            dates.add(day)
        }
        materialCalendarView!!.addDecorator(EventDecorator(Color.RED, dates, this@CalendarActivity, 0))
    }

    fun removeDeco() {
        materialCalendarView!!.removeDecorators() // 모든 데코 지우기
        materialCalendarView!!.addDecorators( //모든 데코 표시
                SundayDecorator(),
                SaturdayDecorator(),
                OneDayDecorator(this@CalendarActivity))
        selectDayDeco() //확정 날짜 이벤트 표시
        ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor()) // 데코 표시
    }
}