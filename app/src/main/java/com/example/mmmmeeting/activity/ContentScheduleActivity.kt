// 약속 내용을 보여주며 날짜, 모임 장소 정하는 화면
package com.example.mmmmeeting.activityimport

import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import java.util.*

com.google.firebase.firestore.GeoPointimport android.text.TextWatcherimport android.text.Editableimport android.widget.LinearLayoutimport android.view.ViewGroupimport android.view.LayoutInflaterimport com.example.mmmmeeting.Rimport com.bumptech.glide.Glideimport com.google.android.exoplayer2.SimpleExoPlayerimport com.example.mmmmeeting.Info.PostInfoimport android.widget.TextViewimport com.example.mmmmeeting.Info.ScheduleInfoimport android.util.TypedValueimport android.view.Gravityimport com.example.mmmmeeting.Info.ChatItemimport android.widget.BaseAdapterimport com.google.firebase.auth.FirebaseUserimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.firebase.firestore.DocumentSnapshotimport android.app.Activityimport androidx.recyclerview.widget.RecyclerViewimport com.example.mmmmeeting.BoardDeleterimport androidx.cardview.widget.CardViewimport com.example.mmmmeeting.OnPostListenerimport android.content.Intentimport com.example.mmmmeeting.activity.ContentBoardActivityimport com.example.mmmmeeting.view.ReadContentsViewimport com.example.mmmmeeting.activity.MakePostActivityimport android.view.MenuInflaterimport com.example.mmmmeeting.Info.AddressItemsimport com.example.mmmmeeting.activity.MemberInitActivityimport com.example.mmmmeeting.Info.CalUserItemsimport android.widget.EditTextimport com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolderimport com.example.mmmmeeting.ScheduleDeleterimport com.example.mmmmeeting.OnScheduleListenerimport com.example.mmmmeeting.activity.CalendarActivityimport com.example.mmmmeeting.activity.ContentScheduleActivityimport android.view.View.OnLongClickListenerimport com.example.mmmmeeting.activity.AlarmActivityimport android.widget.Toastimport android.widget.FrameLayoutimport com.example.mmmmeeting.view.ReadScheduleViewimport com.example.mmmmeeting.activity.EditScheduleActivityimport com.example.mmmmeeting.Info.MeetingRoomItemsimport android.widget.RelativeLayoutimport com.example.mmmmeeting.activity.MeetingActivityimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.QueryDocumentSnapshotimport android.location.LocationManagerimport androidx.core.content.ContextCompatimport android.content.pm.PackageManagerimport com.example.mmmmeeting.activity.GpsTrackerimport android.os.Bundleimport android.os.IBinderimport com.example.mmmmeeting.activity.GrahamScanimport com.example.mmmmeeting.activity.ToolbarActivityimport com.example.mmmmeeting.adapter.MeetingRoomListAdapterimport com.google.android.gms.tasks.OnSuccessListenerimport com.google.android.gms.tasks.OnFailureListenerimport com.google.firebase.firestore.FieldValueimport com.example.mmmmeeting.activity.MakeMeetingActivityimport com.example.mmmmeeting.activity.MeetingAttendActivityimport com.google.firebase.firestore.CollectionReferenceimport com.google.firebase.database.FirebaseDatabaseimport com.google.firebase.database.DatabaseReferenceimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.common.SignInButtonimport com.example.mmmmeeting.activity.MainActivityimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.example.mmmmeeting.activity.SignActivityimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.android.gms.common.api.ApiExceptionimport com.google.firebase.auth.AuthCredentialimport com.google.firebase.auth.GoogleAuthProviderimport com.google.firebase.auth.AuthResultimport android.view.WindowManagerimport android.text.SpannableStringimport android.text.style.RelativeSizeSpanimport android.text.style.ForegroundColorSpanimport android.location.Geocoderimport androidx.appcompat.app.AppCompatActivityimport android.content.SharedPreferencesimport android.content.ComponentNameimport com.example.mmmmeeting.DeviceBootReceiverimport com.example.mmmmeeting.AlarmReceiverimport android.app.PendingIntentimport android.app.AlarmManagerimport android.os.Buildimport androidx.core.app.ActivityCompatimport android.widget.TimePickerimport androidx.recyclerview.widget.GridLayoutManagerimport com.example.mmmmeeting.adapter.GalleryAdapterimport android.provider.MediaStoreimport com.google.android.material.bottomnavigation.BottomNavigationViewimport com.example.mmmmeeting.fragment.FragCalendarimport com.example.mmmmeeting.fragment.FragChatimport com.example.mmmmeeting.fragment.FragAttendimport com.example.mmmmeeting.fragment.FragAccountimport com.example.mmmmeeting.fragment.FragHomeimport com.example.mmmmeeting.fragment.FragBoardimport com.example.mmmmeeting.activity.MeetingInfoActivityimport android.content.pm.ActivityInfoimport androidx.annotation .LayoutResimport com.prolificinteractive.materialcalendarview.MaterialCalendarViewimport com.prolificinteractive.materialcalendarview.CalendarModeimport com.example.mmmmeeting.decorators.SundayDecoratorimport com.example.mmmmeeting.decorators.SaturdayDecoratorimport com.example.mmmmeeting.decorators.OneDayDecoratorimport android.text.method.ScrollingMovementMethodimport com.example.mmmmeeting.activity.CalendarActivity.ApiSimulatorimport com.prolificinteractive.materialcalendarview.OnDateSelectedListenerimport com.prolificinteractive.materialcalendarview.CalendarDayimport com.example.mmmmeeting.activity.NoticeActivityimport android.os.AsyncTaskimport com.example.mmmmeeting.decorators.EventDecoratorimport com.google.firebase.storage.StorageReferenceimport android.view.View.OnFocusChangeListenerimport com.google.firebase.storage.FirebaseStorageimport com.example.mmmmeeting.view.ContentsItemViewimport com.example.mmmmeeting.activity.GalleryActivityimport com.google.firebase.storage.StorageMetadataimport com.google.firebase.storage.UploadTaskimport com.example.mmmmeeting.activity.CurrentMapActivityimport com.google.android.gms.maps.GoogleMapimport android.widget.ArrayAdapterimport android.widget.SeekBarimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport com.google.android.gms.maps.model.CircleOptionsimport com.google.android.gms.maps.CameraUpdateFactoryimport android.widget.SeekBar.OnSeekBarChangeListenerimport com.google.android.gms.maps.model.MarkerOptionsimport com.google.android.gms.maps.model.BitmapDescriptorFactoryimport com.google.android.gms.maps.SupportMapFragmentimport com.example.mmmmeeting.Info.VoteInfoimport org.json.JSONArrayimport org.json.JSONObjectimport android.widget.RatingBarimport org.json.JSONExceptionimport androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallbackimport com.google.android.gms.location.FusedLocationProviderClientimport com.google.android.gms.location.LocationRequestimport com.google.android.gms.location.LocationSettingsRequestimport com.google.android.gms.location.LocationServicesimport com.google.android.material.snackbar.Snackbarimport com.google.android.gms.location.LocationCallbackimport com.google.android.gms.location.LocationResultimport com.google.maps.android.SphericalUtilimport android.os.Looperimport android.widget.RatingBar.OnRatingBarChangeListenerimport com.example.mmmmeeting.activity.SearchAddressActivityimport kotlin.Throwsimport com.example.mmmmeeting.Info.MeetingInfoimport android.view.View.OnTouchListenerimport android.view.MotionEventimport android.content.ClipDataimport com.example.mmmmeeting.activity.inviteActivityimport com.example.mmmmeeting.activity.newLeaderActivityimport com.example.mmmmeeting.activity.MeetingDeleteActivityimport noman.googleplaces.PlacesListenerimport androidx.appcompat.app.AppCompatDialogimport android.graphics.drawable.ColorDrawableimport com.example.mmmmeeting.activity.MiddlePlaceActivity.BackgroundTaskimport android.graphics.drawable.BitmapDrawableimport android.graphics.Bitmapimport android.graphics.Typefaceimport com.example.mmmmeeting.activity.PlaceListActivityimport noman.googleplaces.NRPlacesimport noman.googleplaces.PlaceTypeimport noman.googleplaces.PlacesExceptionimport com.example.mmmmeeting.activity.MiddlePlaceActivityimport com.example.mmmmeeting.activity.SearchPlaceActivityimport com.example.mmmmeeting.activity.VoteActivityimport com.google.android.libraries.places.api.Placesimport com.google.android.libraries.places.widget.AutocompleteSupportFragmentimport com.google.android.libraries.places.widget.listener.PlaceSelectionListenerimport com.example.mmmmeeting.activity.MakeScheduleActivityimport com.example.mmmmeeting.adapter.AddressAdapterimport org.xmlpull.v1.XmlPullParserFactoryimport org.xmlpull.v1.XmlPullParserimport com.example.mmmmeeting.view.ReadScheduleView_newimport com.example.mmmmeeting.adapter.ChatAdapterimport com.google.firebase.database.ChildEventListenerimport com.google.firebase.database.DataSnapshotimport com.google.firebase.database.DatabaseErrorimport com.example.mmmmeeting.adapter.ScheduleAdapterimport androidx.swiperefreshlayout.widget.SwipeRefreshLayoutimport androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListenerimport androidx.recyclerview.widget.LinearLayoutManagerimport com.example.mmmmeeting.adapter.BoardAdapterimport com.example.mmmmeeting.adapter.CalUserResultAdapterimport com.example.mmmmeeting.adapter.CalUserAdapterimport com.example.mmmmeeting.fragment.FragAccount_Resultimport android.util.Patternsimport com.prolificinteractive.materialcalendarview.DayViewDecoratorimport android.graphics.drawable.Drawableimport com.prolificinteractive.materialcalendarview.DayViewFacadeimport com.prolificinteractive.materialcalendarview.spans.DotSpanimport android.text.style.StyleSpanimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.app.NotificationChannel
class ContentScheduleActivity constructor() : ToolbarActivity(), View.OnClickListener {
    private var scheduleInfo: ScheduleInfo? = null
    private var scheduleDeleter: ScheduleDeleter? = null
    private var readContentsVIew: ReadScheduleView_new? = null
    private var contentsLayout: LinearLayout? = null
    var btn_calendar: Button? = null
    var btn_place: Button? = null
    var btn_attendance: Button? = null
    var btn_middle: Button? = null
    var btn_search: Button? = null
    var btn_vote: Button? = null
    private var content_schedule: View? = null
    var code: String? = null
    private var hour: Int = 0
    private var minute: Int = 0
    var handler: Handler? = null
    var cal: Calendar? = null
    var tempCal: Calendar? = null
    var meetingDate: Date? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_schedule)
        content_schedule = findViewById<View>(R.id.content_schedule)
        scheduleInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo?
        code = scheduleInfo.getMeetingID()
        contentsLayout = findViewById<LinearLayout>(R.id.contentsLayout)
        readContentsVIew = findViewById<ReadScheduleView_new>(R.id.readScheduleView_new)
        btn_calendar = findViewById<Button>(R.id.calendarBtn)
        btn_attendance = findViewById<Button>(R.id.attendanceBtn)
        btn_middle = findViewById<Button>(R.id.middleBtn)
        btn_search = findViewById<Button>(R.id.searchBtn)
        btn_vote = findViewById<Button>(R.id.voteBtn)
        btn_middle!!.setOnClickListener(this)
        btn_search!!.setOnClickListener(this)
        btn_vote!!.setOnClickListener(this)
        btn_calendar!!.setOnClickListener(this)
        btn_attendance!!.setOnClickListener(this)
        scheduleDeleter = ScheduleDeleter(this)
        scheduleDeleter.setOnPostListener(onPostListener)
        cal = Calendar.getInstance()
        tempCal = Calendar.getInstance()
        uiUpdate()
        handler = object : Handler() {
            public override fun handleMessage(msg: Message) {
                val bd: Bundle = msg.getData()
                val str: String = bd.getString("arg")
                when (str) {
                    "Late" -> Toast.makeText(getApplicationContext(), "약속시간이 지났습니다.", Toast.LENGTH_SHORT).show()
                    "TimeCheck" -> myStartActivity(CurrentMapActivity::class.java, scheduleInfo, hour, minute)
                }
            }
        }
    }

    public override fun onClick(v: View) {
        when (v.getId()) {
            R.id.calendarBtn -> myStartActivity(CalendarActivity::class.java, scheduleInfo)
            (R.id.middleBtn) -> {
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                db.collection("meetings").document(code)
                        .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
                            public override fun onComplete(task: Task<DocumentSnapshot>) {
                                if (task.isSuccessful()) {
                                    val document: DocumentSnapshot = task.getResult()
                                    if (document.exists()) {
                                        // 해당 문서가 존재하는 경우
                                        // document에서 이름이 userID인 필드의 데이터 얻어옴
                                        if (document.getData().get("userID").size != 1) {
                                            myStartActivity(MiddlePlaceActivity::class.java, scheduleInfo)
                                        } else {
                                            val snackbar: Snackbar = Snackbar.make(content_schedule, "모임원이 1명일 때 중간지점을 찾을 수 없어요", Snackbar.LENGTH_INDEFINITE)
                                                    .setActionTextColor(getColor(R.color.colorPrimary))
                                            snackbar.setAction("확인", object : View.OnClickListener {
                                                public override fun onClick(v: View) {
                                                    snackbar.dismiss()
                                                }
                                            })
                                            snackbar.show()
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
            (R.id.searchBtn) -> myStartActivity(SearchPlaceActivity::class.java, scheduleInfo)
            (R.id.voteBtn) -> myStartActivity(VoteActivity::class.java, scheduleInfo)
            R.id.attendanceBtn -> if (scheduleInfo.getMeetingDate() == null || scheduleInfo.getMeetingPlace() == null) {
                Toast.makeText(getApplicationContext(), "약속 시간 또는 장소가 정해지지 않았습니다.", Toast.LENGTH_SHORT).show()
                return
            } else {
                timeCheck()
            }
        }
    }

    private fun timeCheck() {
        // 약속 변경 후 일수도 있어서 다시 검사
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val docRef: DocumentReference = db.collection("schedule").document(scheduleInfo.getId())
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        meetingDate = document.getDate("meetingDate")
                        if (meetingDate == null || document.getData().get("meetingPlace") == null) {
                            Toast.makeText(getApplicationContext(), "약속 시간 또는 장소가 정해지지 않았습니다.", Toast.LENGTH_SHORT).show()
                            return
                        }
                        cal!!.setTime(meetingDate)
                        hour = cal!!.get(Calendar.HOUR_OF_DAY)
                        minute = cal!!.get(Calendar.MINUTE)
                        val month: Int = cal!!.get(Calendar.MONTH)
                        val day: Int = cal!!.get(Calendar.DAY_OF_MONTH)
                        val now: Date = Date()
                        val temcal: Calendar = Calendar.getInstance()
                        temcal.setTime(now)
                        // 현재 시간 받아오기
                        val nowHour: Int = temcal.get(Calendar.HOUR_OF_DAY)
                        val nowMinute: Int = temcal.get(Calendar.MINUTE)
                        val nowMonth: Int = temcal.get(Calendar.MONTH)
                        val nowDay: Int = temcal.get(Calendar.DAY_OF_MONTH)

                        // 당일의 경우 시간 체크
                        if (nowMonth == month && nowDay == day) {
                            if (nowHour >= hour + 1 && nowMinute >= minute || nowHour >= hour + 2) {
                                // flag를 없애고 bundle로 값을 전달해줌
                                val bd: Bundle = Bundle()
                                bd.putString("arg", "Late")
                                sendMessage(bd)
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

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            scheduleInfo = data.getSerializableExtra("scheduleInfo") as ScheduleInfo
            println("I'mback")
            contentsLayout.removeAllViews()
            uiUpdate()
        }
    }

    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.delete -> {
                scheduleDeleter.storageDelete(scheduleInfo)
                return true
            }
            R.id.modify -> {
                myStartActivity(EditScheduleActivity::class.java, scheduleInfo)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    var onPostListener: OnScheduleListener = object : OnScheduleListener {
        public override fun onDelete(postInfo: ScheduleInfo) {
            Log.e("로그 ", "삭제 성공")
        }

        public override fun onModify() {
            Log.e("로그 ", "수정 성공")
        }
    }

    private fun uiUpdate() {
        readContentsVIew.setScheduleInfo(scheduleInfo)
    }

    private fun myStartActivity(c: Class<*>, schInfo: ScheduleInfo) {
        val intent: Intent = Intent(this, c)
        intent.putExtra("scheduleInfo", schInfo)
        intent.putExtra("Code", schInfo.getMeetingID())
        startActivityForResult(intent, 0)
    }

    private fun myStartActivity(c: Class<*>, postInfo: ScheduleInfo?, hour: Int, minute: Int) {
        val intent: Intent = Intent(this, c)
        intent.putExtra("scheduleInfo", postInfo)
        intent.putExtra("hour", hour)
        intent.putExtra("minute", minute)
        startActivityForResult(intent, 0)
    }
}