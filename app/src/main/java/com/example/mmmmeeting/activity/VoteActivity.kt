package com.example.mmmmeeting.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.mmmmeeting.Info.ScheduleInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.VoteActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import java.io.IOException
import java.util.*

class VoteActivity constructor() : ToolbarActivity() {
    var schedule: ScheduleInfo? = null
    var id: String? = null //vote id
    var scheduleId: String? = null
    var fl_place_list: LinearLayout? = null
    var place_list_view: LinearLayout? = null
    var start_Btn: Button? = null
    var end_Btn: Button? = null
    var com_Btn: Button? = null
    var votePlace: HashMap<String, Any> = HashMap() // 투표할 장소
    var selected_count //투표수
            : Int = 0
    var list: List<HashMap<String, Any>>? = ArrayList() // 투표할 장소정보 리스트
    var voterList: ArrayList<String?>? = ArrayList() //투표한 사람 리스트
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
    var userId: String? = null // 현재 user id
    var leaderId: String? = null // 모임의 방장 id
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)
        setToolbarTitle("장소 투표")
        place_list_view = findViewById(R.id.place_list_view)
        start_Btn = findViewById(R.id.start_Btn)
        end_Btn = findViewById(R.id.end_Btn)
        com_Btn = findViewById(R.id.com_Btn)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val intent: Intent = getIntent()
        schedule = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo?
        scheduleId = schedule.getId()
        userId = user!!.getUid() // 현재 user id
        val docRef: DocumentReference = db.collection("meetings").document(schedule.getMeetingID())
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        leaderId = document.getData()!!.get("leader").toString()
                    } else {
                        // 존재하지 않는 문서
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
        db.collection("vote").whereEqualTo("scheduleID", scheduleId).get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                id = document.getId() // document 이름(id)
                                checkVote()
                            }
                            if (id == null) { //만들어진 투표 리스트가 없으면
                                Toast.makeText(this@VoteActivity, "생성한 투표 목록이 없습니다.", Toast.LENGTH_SHORT).show()
                                com_Btn.setVisibility(View.INVISIBLE)
                                finish()
                            }
                        } else {
                            Log.d("Document Read", "Error getting documents: ", task.getException())
                        }
                    }
                })
        start_Btn.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) { // 시작 버튼 클릭
                db.collection("vote").document((id)!!).update("state", "invalid") //투표상태를 변경
                Toast.makeText(this@VoteActivity, "투표를 시작합니다." + "\n" + "이제 투표 목록을 변경할 수 없습니다.", Toast.LENGTH_SHORT).show()
                start_Btn.setVisibility(View.INVISIBLE) //시작 버튼을 지움
                com_Btn.setVisibility(View.VISIBLE) // 투표 완료 버튼을 보여줌
                end_Btn.setVisibility(View.VISIBLE) // 투표 종료 버튼을 보여줌
            }
        })
        end_Btn.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) { // 종료 버튼 클릭
                //일단 최다 득표
                val meetingPlace: HashMap<String, Any?> = HashMap() //최종 장소
                var count: Int // 장소별 투표수
                val max: Int // 최대값
                var check: Int = 0 // 최다 득표가 여러개인지 체크
                val vote_count: ArrayList<Int> = ArrayList() // 장소별 투표수 리스트
                val maxList: MutableList<HashMap<String, Any>> = ArrayList() //최다 득표 장소 리스트
                for (i in list!!.indices) { // vote_count에 투표수를 넣음
                    votePlace = list!!.get(i)
                    count = votePlace.get("vote").toString().toInt()
                    vote_count.add(count)
                }
                max = Collections.max(vote_count) // 최다 득표수 찾기
                for (i in vote_count.indices) { // 최다 득표 장소들을 리스트에 넣음
                    if (vote_count.get(i) == max) {
                        check++ // 여러개임을 표시
                        maxList.add(list!!.get(i))
                    }
                }
                if (check == 1) { //최다 득표 장수가 하나면 바로 등록
                    votePlace = maxList.get(0)
                    val place: GeoPoint? = votePlace.get("latlng") as GeoPoint? //위도, 경도
                    val name: String? = votePlace.get("name") as String?
                    meetingPlace.put("name", name)
                    meetingPlace.put("latlng", place)
                    Toast.makeText(this@VoteActivity, "가장 많은 투표를 받은 장소는 " + name + "입니다.", Toast.LENGTH_SHORT).show()
                    db.collection("schedule").document((scheduleId)!!).update("meetingPlace", meetingPlace) // db에 최종장소 올리기
                    Toast.makeText(this@VoteActivity, name + "(으)로 약속 장소가 설정되었습니다.", Toast.LENGTH_SHORT).show()

                    // db에서 투표 삭제
                    db.collection("vote").document((id)!!).delete()
                } else {
                    Toast.makeText(this@VoteActivity, "가장 많은 투표를 받은 장소가 " + check + "곳입니다." + "\n" + "최종 선택이 필요합니다.", Toast.LENGTH_SHORT).show()
                    start_Btn.setVisibility(View.INVISIBLE)
                    place_list_view.removeAllViews() // view 지우기
                    createList(maxList) // 최다 득표로 목록 다시 생성
                }
                db.collection("vote").document((id)!!).update("state", "done") //투표 종료 상태로 변경
            }
        })
    }

    fun checkVote() {
        val docRef: DocumentReference = db.collection("vote").document((id)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        list = document.get("place") as List<HashMap<String, Any>>?
                        if ((document.get("state") == "done")) { //종료된 투표
                            com_Btn!!.setVisibility(View.INVISIBLE)
                            Toast.makeText(this@VoteActivity, "이미 종료된 투표입니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            createList(list) //투표목록 생성
                            if ((document.get("state") == "valid")) { //시작안한 상태
                                if ((userId == leaderId)) { // 방장이면 시작, 종료 버튼을 보여줌
                                    com_Btn!!.setVisibility(View.INVISIBLE)
                                    start_Btn!!.setVisibility(View.VISIBLE) //시작 버튼 보여주기
                                } else { // 시작 안했는데 방장이 아니면
                                    com_Btn!!.setVisibility(View.INVISIBLE) // 투표 완료 버튼을 안보여줌
                                    Toast.makeText(this@VoteActivity, "아직 투표가 시작되지 않았습니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else { //투표가 시작했으면
                                if ((userId == leaderId)) { // 방장이면
                                    end_Btn!!.setVisibility(View.VISIBLE) // 종료 버튼 보여주기
                                }
                            }
                        }
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

    fun createList(voteList: List<HashMap<String, Any>>?) {
        for (i in voteList!!.indices) {
            votePlace = voteList.get(i)
            val placeName: String? = votePlace.get("name") as String? //주소 이름
            val g: GeoPoint? = votePlace.get("latlng") as GeoPoint? //위도, 경도
            val latLng: LatLng = LatLng(g!!.getLatitude(), g.getLongitude()) //latlng 변수로 전환
            val placeAddress: String = getCurrentAddress(latLng) //주소로 변환
            selected_count = votePlace.get("vote").toString().toInt() //투표수
            voterList = votePlace.get("voter") as ArrayList<String?>? //이 장소에 투표한 유저들
            val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            val fl_param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            fl_place_list = LinearLayout(this@VoteActivity)
            fl_place_list!!.setOrientation(LinearLayout.VERTICAL)
            fl_place_list!!.setLayoutParams(fl_param)
            fl_place_list!!.setBackgroundColor(Color.WHITE)
            fl_place_list!!.setPadding(0, 10, 0, 30)
            val rl_param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            val pl_name: RelativeLayout = RelativeLayout(this@VoteActivity)
            pl_name.setLayoutParams(rl_param)

            //장소 이름, 주소 출력부분
            val pInfo: TextView = TextView(this@VoteActivity)
            val s: SpannableString = SpannableString(placeName + "\n\n" + placeAddress)
            s.setSpan(RelativeSizeSpan(1.8f), 0, placeName!!.length, 0)
            s.setSpan(ForegroundColorSpan(Color.parseColor("#F28379")), 0, placeName.length, 0)
            pInfo.setText(s)
            pInfo.setLayoutParams(rl_param)
            pl_name.addView(pInfo)

            //좋아요버튼
            val favorite: Button = Button(this@VoteActivity)
            val btn_param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(90, 90)
            btn_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
            favorite.setLayoutParams(btn_param)
            favorite.setPadding(0, 20, 5, 0)
            favorite.setId(i)
            favorite.setBackground(ContextCompat.getDrawable(this@VoteActivity, R.drawable.heart))
            if (voterList!!.contains(userId)) {
                favorite.setSelected(!favorite.isSelected()) //선택여부 반전
            }
            pl_name.addView(favorite)

            //좋아요 count
            val favorite_count: TextView = TextView(this@VoteActivity)
            favorite_count.setId(i)
            val fc_param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            fc_param.addRule(RelativeLayout.LEFT_OF, favorite.getId())
            fc_param.addRule(RelativeLayout.BELOW, favorite.getId())
            favorite_count.setLayoutParams(fc_param)
            pl_name.addView(favorite_count)
            fl_place_list!!.addView(pl_name)

            //LinearLayout 생성
            val ly: LinearLayout = LinearLayout(this@VoteActivity)
            ly.setLayoutParams(param)
            ly.setOrientation(LinearLayout.HORIZONTAL)
            fl_place_list!!.addView(ly)
            place_list_view!!.addView(fl_place_list)
            favorite.setOnClickListener(object : View.OnClickListener {
                @SuppressLint("ResourceType")
                public override fun onClick(v: View) {
                    val textView: TextView
                    v.setSelected(!v.isSelected()) //선택여부 반전
                    when (v.getId()) {
                        (0) -> {
                            votePlace = voteList.get(0)
                            selected_count = votePlace.get("vote").toString().toInt()
                            voterList = votePlace.get("voter") as ArrayList<String?>? //이 장소에 투표한 유저들
                            textView = findViewById<View>(0) as TextView
                            if (v.isSelected()) { //현재 좋아요 누른 상태
                                selected_count++
                                textView.setText(selected_count.toString())
                                updateDB_plus(selected_count, votePlace, voterList)
                            } else {
                                if (selected_count > 0) selected_count--
                                textView.setText(selected_count.toString())
                                updateDB_minus(selected_count, votePlace, voterList)
                            }
                        }
                        (1) -> {
                            votePlace = voteList.get(1)
                            selected_count = votePlace.get("vote").toString().toInt()
                            voterList = votePlace.get("voter") as ArrayList<String?>? //이 장소에 투표한 유저들
                            textView = findViewById<View>(1) as TextView
                            if (v.isSelected()) { //현재 좋아요 누른 상태
                                selected_count++
                                textView.setText(selected_count.toString())
                                updateDB_plus(selected_count, votePlace, voterList)
                            } else {
                                if (selected_count > 0) selected_count--
                                textView.setText(selected_count.toString())
                                updateDB_minus(selected_count, votePlace, voterList)
                            }
                        }
                        (2) -> {
                            votePlace = voteList.get(2)
                            selected_count = votePlace.get("vote").toString().toInt()
                            voterList = votePlace.get("voter") as ArrayList<String?>? //이 장소에 투표한 유저들
                            textView = findViewById<View>(2) as TextView
                            if (v.isSelected()) { //현재 좋아요 누른 상태
                                selected_count++
                                textView.setText(selected_count.toString())
                                updateDB_plus(selected_count, votePlace, voterList)
                            } else {
                                if (selected_count > 0) selected_count--
                                textView.setText(selected_count.toString())
                                updateDB_minus(selected_count, votePlace, voterList)
                            }
                        }
                        (3) -> {
                            votePlace = voteList.get(3)
                            selected_count = votePlace.get("vote").toString().toInt()
                            voterList = votePlace.get("voter") as ArrayList<String?>? //이 장소에 투표한 유저들
                            textView = findViewById<View>(3) as TextView
                            if (v.isSelected()) { //현재 좋아요 누른 상태
                                selected_count++
                                textView.setText(selected_count.toString())
                                updateDB_plus(selected_count, votePlace, voterList)
                            } else {
                                if (selected_count > 0) selected_count--
                                textView.setText(selected_count.toString())
                                updateDB_minus(selected_count, votePlace, voterList)
                            }
                        }
                        (4) -> {
                            votePlace = voteList.get(4)
                            selected_count = votePlace.get("vote").toString().toInt()
                            voterList = votePlace.get("voter") as ArrayList<String?>? //이 장소에 투표한 유저들
                            textView = findViewById<View>(4) as TextView
                            if (v.isSelected()) { //현재 좋아요 누른 상태
                                selected_count++
                                textView.setText(selected_count.toString())
                                updateDB_plus(selected_count, votePlace, voterList)
                            } else {
                                if (selected_count > 0) selected_count--
                                textView.setText(selected_count.toString())
                                updateDB_minus(selected_count, votePlace, voterList)
                            }
                        }
                    }
                }
            })
        }
        com_Btn!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) { // 완료 버튼 클릭
                finish()
            }
        })
    }

    fun updateDB_plus(selected_count: Int, votePlace: HashMap<String, Any>, voterList: ArrayList<String?>?) {
        db.collection("vote").document((id)!!).update("place", FieldValue.arrayRemove(votePlace))
        votePlace.put("vote", selected_count)
        voterList!!.add(userId)
        db.collection("vote").document((id)!!).update("place", FieldValue.arrayUnion(votePlace))
    }

    fun updateDB_minus(selected_count: Int, votePlace: HashMap<String, Any>, voterList: ArrayList<String?>?) {
        db.collection("vote").document((id)!!).update("place", FieldValue.arrayRemove(votePlace))
        votePlace.put("vote", selected_count)
        voterList!!.remove(userId)
        db.collection("vote").document((id)!!).update("place", FieldValue.arrayUnion(votePlace))
    }

    fun getCurrentAddress(latlng: LatLng): String {
        //지오코더... GPS를 주소로 변환
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1)
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        } else {
            val address: Address = addresses.get(0)
            return address.getAddressLine(0).toString()
        }
    }
}