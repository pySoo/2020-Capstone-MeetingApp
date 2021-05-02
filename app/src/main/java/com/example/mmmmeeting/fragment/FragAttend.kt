package com.example.mmmmeeting.fragmentimport

import android.content.DialogInterface
import android.graphics.Color
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import java.text.SimpleDateFormat
import java.util.*

com.google.firebase.firestore.GeoPointimport android.text.TextWatcherimport android.text.Editableimport android.widget.LinearLayoutimport android.view.ViewGroupimport android.view.LayoutInflaterimport com.example.mmmmeeting.Rimport com.bumptech.glide.Glideimport com.google.android.exoplayer2.SimpleExoPlayerimport com.example.mmmmeeting.Info.PostInfoimport android.widget.TextViewimport com.example.mmmmeeting.Info.ScheduleInfoimport android.util.TypedValueimport android.view.Gravityimport com.example.mmmmeeting.Info.ChatItemimport android.widget.BaseAdapterimport com.google.firebase.auth.FirebaseUserimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.firebase.firestore.DocumentSnapshotimport android.app.Activityimport androidx.recyclerview.widget.RecyclerViewimport com.example.mmmmeeting.BoardDeleterimport androidx.cardview.widget.CardViewimport com.example.mmmmeeting.OnPostListenerimport android.content.Intentimport com.example.mmmmeeting.activity.ContentBoardActivityimport com.example.mmmmeeting.view.ReadContentsViewimport com.example.mmmmeeting.activity.MakePostActivityimport android.view.MenuInflaterimport com.example.mmmmeeting.Info.AddressItemsimport com.example.mmmmeeting.activity.MemberInitActivityimport com.example.mmmmeeting.Info.CalUserItemsimport android.widget.EditTextimport com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolderimport com.example.mmmmeeting.ScheduleDeleterimport com.example.mmmmeeting.OnScheduleListenerimport com.example.mmmmeeting.activity.CalendarActivityimport com.example.mmmmeeting.activity.ContentScheduleActivityimport android.view.View.OnLongClickListenerimport com.example.mmmmeeting.activity.AlarmActivityimport android.widget.Toastimport android.widget.FrameLayoutimport com.example.mmmmeeting.view.ReadScheduleViewimport com.example.mmmmeeting.activity.EditScheduleActivityimport com.example.mmmmeeting.Info.MeetingRoomItemsimport android.widget.RelativeLayoutimport com.example.mmmmeeting.activity.MeetingActivityimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.QueryDocumentSnapshotimport android.location.LocationManagerimport androidx.core.content.ContextCompatimport android.content.pm.PackageManagerimport com.example.mmmmeeting.activity.GpsTrackerimport android.os.Bundleimport android.os.IBinderimport com.example.mmmmeeting.activity.GrahamScanimport com.example.mmmmeeting.activity.ToolbarActivityimport com.example.mmmmeeting.adapter.MeetingRoomListAdapterimport com.google.android.gms.tasks.OnSuccessListenerimport com.google.android.gms.tasks.OnFailureListenerimport com.google.firebase.firestore.FieldValueimport com.example.mmmmeeting.activity.MakeMeetingActivityimport com.example.mmmmeeting.activity.MeetingAttendActivityimport com.google.firebase.firestore.CollectionReferenceimport com.google.firebase.database.FirebaseDatabaseimport com.google.firebase.database.DatabaseReferenceimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.common.SignInButtonimport com.example.mmmmeeting.activity.MainActivityimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.example.mmmmeeting.activity.SignActivityimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.android.gms.common.api.ApiExceptionimport com.google.firebase.auth.AuthCredentialimport com.google.firebase.auth.GoogleAuthProviderimport com.google.firebase.auth.AuthResultimport android.view.WindowManagerimport android.text.SpannableStringimport android.text.style.RelativeSizeSpanimport android.text.style.ForegroundColorSpanimport android.location.Geocoderimport androidx.appcompat.app.AppCompatActivityimport android.content.SharedPreferencesimport android.content.ComponentNameimport com.example.mmmmeeting.DeviceBootReceiverimport com.example.mmmmeeting.AlarmReceiverimport android.app.PendingIntentimport android.app.AlarmManagerimport android.os.Buildimport androidx.core.app.ActivityCompatimport android.widget.TimePickerimport androidx.recyclerview.widget.GridLayoutManagerimport com.example.mmmmeeting.adapter.GalleryAdapterimport android.provider.MediaStoreimport com.google.android.material.bottomnavigation.BottomNavigationViewimport com.example.mmmmeeting.fragment.FragCalendarimport com.example.mmmmeeting.fragment.FragChatimport com.example.mmmmeeting.fragment.FragAttendimport com.example.mmmmeeting.fragment.FragAccountimport com.example.mmmmeeting.fragment.FragHomeimport com.example.mmmmeeting.fragment.FragBoardimport com.example.mmmmeeting.activity.MeetingInfoActivityimport android.content.pm.ActivityInfoimport androidx.annotation .LayoutResimport com.prolificinteractive.materialcalendarview.MaterialCalendarViewimport com.prolificinteractive.materialcalendarview.CalendarModeimport com.example.mmmmeeting.decorators.SundayDecoratorimport com.example.mmmmeeting.decorators.SaturdayDecoratorimport com.example.mmmmeeting.decorators.OneDayDecoratorimport android.text.method.ScrollingMovementMethodimport com.example.mmmmeeting.activity.CalendarActivity.ApiSimulatorimport com.prolificinteractive.materialcalendarview.OnDateSelectedListenerimport com.prolificinteractive.materialcalendarview.CalendarDayimport com.example.mmmmeeting.activity.NoticeActivityimport android.os.AsyncTaskimport com.example.mmmmeeting.decorators.EventDecoratorimport com.google.firebase.storage.StorageReferenceimport android.view.View.OnFocusChangeListenerimport com.google.firebase.storage.FirebaseStorageimport com.example.mmmmeeting.view.ContentsItemViewimport com.example.mmmmeeting.activity.GalleryActivityimport com.google.firebase.storage.StorageMetadataimport com.google.firebase.storage.UploadTaskimport com.example.mmmmeeting.activity.CurrentMapActivityimport com.google.android.gms.maps.GoogleMapimport android.widget.ArrayAdapterimport android.widget.SeekBarimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport com.google.android.gms.maps.model.CircleOptionsimport com.google.android.gms.maps.CameraUpdateFactoryimport android.widget.SeekBar.OnSeekBarChangeListenerimport com.google.android.gms.maps.model.MarkerOptionsimport com.google.android.gms.maps.model.BitmapDescriptorFactoryimport com.google.android.gms.maps.SupportMapFragmentimport com.example.mmmmeeting.Info.VoteInfoimport org.json.JSONArrayimport org.json.JSONObjectimport android.widget.RatingBarimport org.json.JSONExceptionimport androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallbackimport com.google.android.gms.location.FusedLocationProviderClientimport com.google.android.gms.location.LocationRequestimport com.google.android.gms.location.LocationSettingsRequestimport com.google.android.gms.location.LocationServicesimport com.google.android.material.snackbar.Snackbarimport com.google.android.gms.location.LocationCallbackimport com.google.android.gms.location.LocationResultimport com.google.maps.android.SphericalUtilimport android.os.Looperimport android.widget.RatingBar.OnRatingBarChangeListenerimport com.example.mmmmeeting.activity.SearchAddressActivityimport kotlin.Throwsimport com.example.mmmmeeting.Info.MeetingInfoimport android.view.View.OnTouchListenerimport android.view.MotionEventimport android.content.ClipDataimport com.example.mmmmeeting.activity.inviteActivityimport com.example.mmmmeeting.activity.newLeaderActivityimport com.example.mmmmeeting.activity.MeetingDeleteActivityimport noman.googleplaces.PlacesListenerimport androidx.appcompat.app.AppCompatDialogimport android.graphics.drawable.ColorDrawableimport com.example.mmmmeeting.activity.MiddlePlaceActivity.BackgroundTaskimport android.graphics.drawable.BitmapDrawableimport android.graphics.Bitmapimport android.graphics.Typefaceimport com.example.mmmmeeting.activity.PlaceListActivityimport noman.googleplaces.NRPlacesimport noman.googleplaces.PlaceTypeimport noman.googleplaces.PlacesExceptionimport com.example.mmmmeeting.activity.MiddlePlaceActivityimport com.example.mmmmeeting.activity.SearchPlaceActivityimport com.example.mmmmeeting.activity.VoteActivityimport com.google.android.libraries.places.api.Placesimport com.google.android.libraries.places.widget.AutocompleteSupportFragmentimport com.google.android.libraries.places.widget.listener.PlaceSelectionListenerimport com.example.mmmmeeting.activity.MakeScheduleActivityimport com.example.mmmmeeting.adapter.AddressAdapterimport org.xmlpull.v1.XmlPullParserFactoryimport org.xmlpull.v1.XmlPullParserimport com.example.mmmmeeting.view.ReadScheduleView_newimport com.example.mmmmeeting.adapter.ChatAdapterimport com.google.firebase.database.ChildEventListenerimport com.google.firebase.database.DataSnapshotimport com.google.firebase.database.DatabaseErrorimport com.example.mmmmeeting.adapter.ScheduleAdapterimport androidx.swiperefreshlayout.widget.SwipeRefreshLayoutimport androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListenerimport androidx.recyclerview.widget.LinearLayoutManagerimport com.example.mmmmeeting.adapter.BoardAdapterimport com.example.mmmmeeting.adapter.CalUserResultAdapterimport com.example.mmmmeeting.adapter.CalUserAdapterimport com.example.mmmmeeting.fragment.FragAccount_Resultimport android.util.Patternsimport com.prolificinteractive.materialcalendarview.DayViewDecoratorimport android.graphics.drawable.Drawableimport com.prolificinteractive.materialcalendarview.DayViewFacadeimport com.prolificinteractive.materialcalendarview.spans.DotSpanimport android.text.style.StyleSpanimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.app.NotificationChannel
class FragAttend constructor() : Fragment() {
    private var view: View? = null
    private var db: FirebaseFirestore? = null
    private var meetingCode: String? = null
    private var list_entries: List<Map.Entry<String, Double?>>? = null
    private val attendMap: HashMap<String, Double?> = HashMap()
    private var userList: ArrayList<String> = ArrayList()
    private val userRank: HashMap<String, Int> = HashMap()
    var listView: ListView? = null
    var adapter: CalUserResultAdapter? = null
    var attend_show: LinearLayout? = null
    var best_title: TextView? = null
    var resetDatetv: TextView? = null
    var nullText: TextView? = null
    var reset: Button? = null
    var resetDate: Int = 0
    var resetTime: Int = 0
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
    var time: SimpleDateFormat = SimpleDateFormat("HHmm")
    var Tag: String = "attend test"
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.frag_attend, container, false)
        //attend_show = view.findViewById(R.id.attend_show);
        best_title = view.findViewById<TextView>(R.id.best_title)
        resetDatetv = view.findViewById<TextView>(R.id.resetDatetv)
        nullText = view.findViewById<TextView>(R.id.nullText)
        listView = view!!.findViewById(R.id.attend_show)
        adapter = CalUserResultAdapter()
        db = FirebaseFirestore.getInstance()
        reset = view!!.findViewById(R.id.resetBtn)
        val today: String = sdf.format(Date())
        val thisTime: String = time.format(Date())
        var bundle: Bundle? = getArguments()
        if (bundle != null) {
            bundle = getArguments()
            meetingCode = bundle.getString("Code")
        }
        val docRef: DocumentReference = db.collection("meetings").document(meetingCode)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        Log.d(Tag, "meeting find : " + document.get("name"))
                        val getDate: String = document.get("resetDate").toString()
                        val getTime: String = document.get("resetTime").toString()
                        userList = document.get("userID")
                        println("userList: " + userList)
                        resetDate = Integer.valueOf(getDate)
                        resetTime = Integer.valueOf(getTime)
                        val year: String = getDate.substring(0, 4)
                        val month: String = getDate.substring(4, 6)
                        val date: String = getDate.substring(6, 8)
                        resetDatetv.setText(year + "년 " + month + "월 " + date + "일 ~ ")
                        Log.d(Tag, "get reset Date : " + resetDate)
                        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
                        if ((document.get("leader").toString() == mAuth.getUid())) {
                            reset.setVisibility(View.VISIBLE)
                        }
                        setResetBtn(today, thisTime)
                        setAttend(meetingCode)
                        return
                    } else {
                        // 존재하지 않는 문서
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
        return view
    }

    private fun setResetBtn(today: String, thisTime: String) {
        reset!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view!!.getContext())
                //제목
                alertDialogBuilder.setTitle("출석점수 초기화")

                //AlertDialog 세팅
                val s: SpannableString = SpannableString("출석점수를 초기화하시겠습니까?\n" +
                        "초기화시 지금까지의 점수가 사라지고 되돌릴 수 없습니다.")
                s.setSpan(RelativeSizeSpan(0.5f), 22, 22, 0)
                s.setSpan(ForegroundColorSpan(Color.parseColor("#62ABD9")), 22, 22, 0)
                alertDialogBuilder.setMessage(s)
                        .setCancelable(false)
                        .setPositiveButton("아니오", object : DialogInterface.OnClickListener {
                            public override fun onClick(dialogInterface: DialogInterface?, i: Int) {}
                        }).setNegativeButton("네", object : DialogInterface.OnClickListener {
                            public override fun onClick(dialogInterface: DialogInterface?, i: Int) {
                                db.collection("meetings").document(meetingCode).update("resetDate", today)
                                db.collection("meetings").document(meetingCode).update("resetTime", thisTime)
                                db.collection("meetings").document(meetingCode).update("best", HashMap<String, Int>())
                                adapter = CalUserResultAdapter()
                                listView!!.setAdapter(adapter)
                                Toast.makeText(view!!.getContext(), "출석점수를 초기화했습니다.", Toast.LENGTH_SHORT).show()
                                val year: String = today.substring(0, 4)
                                val month: String = today.substring(4, 6)
                                val date: String = today.substring(6, 8)
                                resetDatetv.setText(year + "년 " + month + "월 " + date + "일 ~ ")
                                nullText.setVisibility(View.VISIBLE)
                            }
                        })

                //다이얼로그 생성
                val alertDialog: AlertDialog = alertDialogBuilder.create()

                //다이얼로그 보여주기
                alertDialog.show()
            }
        })
    }

    private fun setAttend(getId: String?) {
        db.collection("schedule").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            attendMap.clear()
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                val meetingIDCheck: Boolean = (getId == document.get("meetingID").toString())
                                val meetingDate: Date = document.getDate("meetingDate")
                                // 날짜 지난 것만 지각 포인트 체크 -> 리셋 날 기준으로 날짜 확인
                                val meetingTimeCheck: Boolean = checkTime(meetingDate)

//                                Date now = new Date();// 날짜 지난 것만 지각 포인트 체크
//                                Date meetingDate = document.getDate("meetingDate");
//                                boolean meetingMonthCheck = now.compareTo(meetingDate) == 1 ? true : false;
                                Log.d(Tag, "check val : " + meetingIDCheck + "/" + meetingTimeCheck)
                                if (meetingIDCheck && meetingTimeCheck) {
                                    countAttend(document.get("timePoint"), attendMap)
                                }
                            }
                            mapSort(attendMap)
                            Handler().postDelayed(object : Runnable {
                                public override fun run() {
                                    if (attendMap.isEmpty()) {
                                        nullText.setVisibility(View.VISIBLE)
                                    }
                                }
                            }, 300)
                        }
                    }
                })
    }

    // 날짜가 지났는지 확인
    private fun checkTime(date: Date?): Boolean {
        var scheduleDate: Int = 0
        var scheduleTime: Int = 0
        if (date != null) {
            scheduleDate = sdf.format(date).toInt()
            scheduleTime = time.format(date).toInt()
        }
        if (scheduleDate == resetDate && scheduleTime >= resetTime) {
            Log.d(Tag, "scheduleTime : " + scheduleTime + "/ resetTime " + resetTime)
            return true
        } else if (scheduleDate > resetDate) {
            Log.d(Tag, "scheduleDate : " + scheduleDate + "/ resetDate" + resetDate)
            return true
        }
        return false
    }

    // 누가 출석 했는지 확인, 이름과 점수로 array 생성, 출석 안 한 사람은 -12점
    private fun countAttend(attender: HashMap<String, Double>?, attendMap: HashMap<String, Double?>) {
        if (attender == null) {
            return
        }
        val attenderSet: Set<*> = attender.keys
        val tempMap: HashMap<String, Double> = HashMap()

        // 출첵 점수 저장
        for (i in userList.indices) {
            val userName: String = userList.get(i)
            val iter: Iterator<*> = attenderSet.iterator()
            while (iter.hasNext()) {
                val checkUser: String = iter.next() as String
                if ((userName == checkUser)) {
                    val point: Double = attender.get(checkUser).toString().toDouble()
                    tempMap.put(userName, point)
                    break
                } else {
                    tempMap.put(userName, -12.0)
                }
            }
        }
        println(tempMap)

        // 기존 점수와 합침
        val tempSet: Set<*> = tempMap.keys
        val iter: Iterator<*> = tempSet.iterator()
        while (iter.hasNext()) {
            val user: String = iter.next() as String
            var now: Double? = tempMap.get(user)!!.toDouble()
            if (attendMap.get(user) != null) {
                val old: Double = attendMap.get(user)!!.toDouble()
                now!! += old
            }
            attendMap.put(user, now)
        }
        println("attendMap:" + attendMap)
    }

    private fun mapSort(attendMap: HashMap<String, Double?>) {
        Log.d(Tag, "attender map is " + attendMap.toString())
        if (attendMap.isEmpty()) {
            return
        }
        list_entries = ArrayList<Map.Entry<String, Double?>>(attendMap.entries)
        // 비교함수 Comparator를 사용하여 내림 차순으로 정렬
        Collections.sort(list_entries, object : Comparator<Map.Entry<String?, Double?>?> {
            public override fun compare(obj1: Map.Entry<String?, Double>, obj2: Map.Entry<String?, Double?>): Int {
                // 오름 차순으로 정렬
                return obj1.value.compareTo((obj2.value)!!)
            }
        })
        Log.d(Tag, "list_entries is " + list_entries.toString())
        var same: Int = 1
        var rank: Int = 1
        val size: Int = if (list_entries.size < 5) list_entries.size else 5
        for (i in 0 until size) {
            if (i != 0) {
                if ((list_entries.get(i - 1).value == list_entries.get(i).value)) {
                    same++
                } else {
                    rank += same
                    same = 1
                }
            }
            setLayout(rank, list_entries.get(i).key)
        }
        db.collection("meetings").document(meetingCode).update("best", attendMap)
    }

    fun setLayout(num: Int, user_id: String) {
        Log.d(Tag, "Show layout")
        //db에서 모임원들 이름 가져오기
        db.collection("users").get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            nullText.setVisibility(View.GONE)
                            var s: SpannableString
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                if ((user_id == document.getId())) {
                                    val user_name: String = document.get("name").toString()
                                    s = SpannableString(num.toString() + "등 (" + attendMap.get(user_id)!!.toDouble() + "점)")
                                    s.setSpan(RelativeSizeSpan(1.2f), 0, s.length - 8, 0)
                                    s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length - 8, 0)
                                    val item: CalUserItems = CalUserItems(user_name, user_id)
                                    item.setMoney(s.toString())
                                    adapter.addItem(item)
                                }
                            }
                            listView!!.setAdapter(adapter)
                        }
                    }
                })
    }
}