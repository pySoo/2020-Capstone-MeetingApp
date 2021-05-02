package com.example.mmmmeeting.activityimport

import android.util.Log
import android.view.View
import android.widget.Button
import com.example.mmmmeeting.Util
import com.google.firebase.firestore.DocumentReference
import java.util.*

com.google.firebase.firestore.GeoPointimport android.text.TextWatcherimport android.text.Editableimport android.widget.LinearLayoutimport android.view.ViewGroupimport android.view.LayoutInflaterimport com.example.mmmmeeting.Rimport com.bumptech.glide.Glideimport com.google.android.exoplayer2.SimpleExoPlayerimport com.example.mmmmeeting.Info.PostInfoimport android.widget.TextViewimport com.example.mmmmeeting.Info.ScheduleInfoimport android.util.TypedValueimport android.view.Gravityimport com.example.mmmmeeting.Info.ChatItemimport android.widget.BaseAdapterimport com.google.firebase.auth.FirebaseUserimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.firebase.firestore.DocumentSnapshotimport android.app.Activityimport androidx.recyclerview.widget.RecyclerViewimport com.example.mmmmeeting.BoardDeleterimport androidx.cardview.widget.CardViewimport com.example.mmmmeeting.OnPostListenerimport android.content.Intentimport com.example.mmmmeeting.activity.ContentBoardActivityimport com.example.mmmmeeting.view.ReadContentsViewimport com.example.mmmmeeting.activity.MakePostActivityimport android.view.MenuInflaterimport com.example.mmmmeeting.Info.AddressItemsimport com.example.mmmmeeting.activity.MemberInitActivityimport com.example.mmmmeeting.Info.CalUserItemsimport android.widget.EditTextimport com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolderimport com.example.mmmmeeting.ScheduleDeleterimport com.example.mmmmeeting.OnScheduleListenerimport com.example.mmmmeeting.activity.CalendarActivityimport com.example.mmmmeeting.activity.ContentScheduleActivityimport android.view.View.OnLongClickListenerimport com.example.mmmmeeting.activity.AlarmActivityimport android.widget.Toastimport android.widget.FrameLayoutimport com.example.mmmmeeting.view.ReadScheduleViewimport com.example.mmmmeeting.activity.EditScheduleActivityimport com.example.mmmmeeting.Info.MeetingRoomItemsimport android.widget.RelativeLayoutimport com.example.mmmmeeting.activity.MeetingActivityimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.QueryDocumentSnapshotimport android.location.LocationManagerimport androidx.core.content.ContextCompatimport android.content.pm.PackageManagerimport com.example.mmmmeeting.activity.GpsTrackerimport android.os.Bundleimport android.os.IBinderimport com.example.mmmmeeting.activity.GrahamScanimport com.example.mmmmeeting.activity.ToolbarActivityimport com.example.mmmmeeting.adapter.MeetingRoomListAdapterimport com.google.android.gms.tasks.OnSuccessListenerimport com.google.android.gms.tasks.OnFailureListenerimport com.google.firebase.firestore.FieldValueimport com.example.mmmmeeting.activity.MakeMeetingActivityimport com.example.mmmmeeting.activity.MeetingAttendActivityimport com.google.firebase.firestore.CollectionReferenceimport com.google.firebase.database.FirebaseDatabaseimport com.google.firebase.database.DatabaseReferenceimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.common.SignInButtonimport com.example.mmmmeeting.activity.MainActivityimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.example.mmmmeeting.activity.SignActivityimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.android.gms.common.api.ApiExceptionimport com.google.firebase.auth.AuthCredentialimport com.google.firebase.auth.GoogleAuthProviderimport com.google.firebase.auth.AuthResultimport android.view.WindowManagerimport android.text.SpannableStringimport android.text.style.RelativeSizeSpanimport android.text.style.ForegroundColorSpanimport android.location.Geocoderimport androidx.appcompat.app.AppCompatActivityimport android.content.SharedPreferencesimport android.content.ComponentNameimport com.example.mmmmeeting.DeviceBootReceiverimport com.example.mmmmeeting.AlarmReceiverimport android.app.PendingIntentimport android.app.AlarmManagerimport android.os.Buildimport androidx.core.app.ActivityCompatimport android.widget.TimePickerimport androidx.recyclerview.widget.GridLayoutManagerimport com.example.mmmmeeting.adapter.GalleryAdapterimport android.provider.MediaStoreimport com.google.android.material.bottomnavigation.BottomNavigationViewimport com.example.mmmmeeting.fragment.FragCalendarimport com.example.mmmmeeting.fragment.FragChatimport com.example.mmmmeeting.fragment.FragAttendimport com.example.mmmmeeting.fragment.FragAccountimport com.example.mmmmeeting.fragment.FragHomeimport com.example.mmmmeeting.fragment.FragBoardimport com.example.mmmmeeting.activity.MeetingInfoActivityimport android.content.pm.ActivityInfoimport androidx.annotation .LayoutResimport com.prolificinteractive.materialcalendarview.MaterialCalendarViewimport com.prolificinteractive.materialcalendarview.CalendarModeimport com.example.mmmmeeting.decorators.SundayDecoratorimport com.example.mmmmeeting.decorators.SaturdayDecoratorimport com.example.mmmmeeting.decorators.OneDayDecoratorimport android.text.method.ScrollingMovementMethodimport com.example.mmmmeeting.activity.CalendarActivity.ApiSimulatorimport com.prolificinteractive.materialcalendarview.OnDateSelectedListenerimport com.prolificinteractive.materialcalendarview.CalendarDayimport com.example.mmmmeeting.activity.NoticeActivityimport android.os.AsyncTaskimport com.example.mmmmeeting.decorators.EventDecoratorimport com.google.firebase.storage.StorageReferenceimport android.view.View.OnFocusChangeListenerimport com.google.firebase.storage.FirebaseStorageimport com.example.mmmmeeting.view.ContentsItemViewimport com.example.mmmmeeting.activity.GalleryActivityimport com.google.firebase.storage.StorageMetadataimport com.google.firebase.storage.UploadTaskimport com.example.mmmmeeting.activity.CurrentMapActivityimport com.google.android.gms.maps.GoogleMapimport android.widget.ArrayAdapterimport android.widget.SeekBarimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport com.google.android.gms.maps.model.CircleOptionsimport com.google.android.gms.maps.CameraUpdateFactoryimport android.widget.SeekBar.OnSeekBarChangeListenerimport com.google.android.gms.maps.model.MarkerOptionsimport com.google.android.gms.maps.model.BitmapDescriptorFactoryimport com.google.android.gms.maps.SupportMapFragmentimport com.example.mmmmeeting.Info.VoteInfoimport org.json.JSONArrayimport org.json.JSONObjectimport android.widget.RatingBarimport org.json.JSONExceptionimport androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallbackimport com.google.android.gms.location.FusedLocationProviderClientimport com.google.android.gms.location.LocationRequestimport com.google.android.gms.location.LocationSettingsRequestimport com.google.android.gms.location.LocationServicesimport com.google.android.material.snackbar.Snackbarimport com.google.android.gms.location.LocationCallbackimport com.google.android.gms.location.LocationResultimport com.google.maps.android.SphericalUtilimport android.os.Looperimport android.widget.RatingBar.OnRatingBarChangeListenerimport com.example.mmmmeeting.activity.SearchAddressActivityimport kotlin.Throwsimport com.example.mmmmeeting.Info.MeetingInfoimport android.view.View.OnTouchListenerimport android.view.MotionEventimport android.content.ClipDataimport com.example.mmmmeeting.activity.inviteActivityimport com.example.mmmmeeting.activity.newLeaderActivityimport com.example.mmmmeeting.activity.MeetingDeleteActivityimport noman.googleplaces.PlacesListenerimport androidx.appcompat.app.AppCompatDialogimport android.graphics.drawable.ColorDrawableimport com.example.mmmmeeting.activity.MiddlePlaceActivity.BackgroundTaskimport android.graphics.drawable.BitmapDrawableimport android.graphics.Bitmapimport android.graphics.Typefaceimport com.example.mmmmeeting.activity.PlaceListActivityimport noman.googleplaces.NRPlacesimport noman.googleplaces.PlaceTypeimport noman.googleplaces.PlacesExceptionimport com.example.mmmmeeting.activity.MiddlePlaceActivityimport com.example.mmmmeeting.activity.SearchPlaceActivityimport com.example.mmmmeeting.activity.VoteActivityimport com.google.android.libraries.places.api.Placesimport com.google.android.libraries.places.widget.AutocompleteSupportFragmentimport com.google.android.libraries.places.widget.listener.PlaceSelectionListenerimport com.example.mmmmeeting.activity.MakeScheduleActivityimport com.example.mmmmeeting.adapter.AddressAdapterimport org.xmlpull.v1.XmlPullParserFactoryimport org.xmlpull.v1.XmlPullParserimport com.example.mmmmeeting.view.ReadScheduleView_newimport com.example.mmmmeeting.adapter.ChatAdapterimport com.google.firebase.database.ChildEventListenerimport com.google.firebase.database.DataSnapshotimport com.google.firebase.database.DatabaseErrorimport com.example.mmmmeeting.adapter.ScheduleAdapterimport androidx.swiperefreshlayout.widget.SwipeRefreshLayoutimport androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListenerimport androidx.recyclerview.widget.LinearLayoutManagerimport com.example.mmmmeeting.adapter.BoardAdapterimport com.example.mmmmeeting.adapter.CalUserResultAdapterimport com.example.mmmmeeting.adapter.CalUserAdapterimport com.example.mmmmeeting.fragment.FragAccount_Resultimport android.util.Patternsimport com.prolificinteractive.materialcalendarview.DayViewDecoratorimport android.graphics.drawable.Drawableimport com.prolificinteractive.materialcalendarview.DayViewFacadeimport com.prolificinteractive.materialcalendarview.spans.DotSpanimport android.text.style.StyleSpanimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.app.NotificationChannel
class MakeScheduleActivity constructor() : AppCompatActivity() {
    private var user: FirebaseUser? = null
    private var storageRef: StorageReference? = null
    private val pathList: ArrayList<String> = ArrayList()
    private var parent: LinearLayout? = null
    private var selectedEditText: EditText? = null
    private var contentsEditText: EditText? = null
    private var titleEditText: EditText? = null
    private val showInfo: TextView? = null
    private var scheduleInfo: ScheduleInfo? = null
    private val pathCount: Int = 0
    private val successCount: Int = 0
    private var meetingCode: String? = null
    private var info: Button? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_schedule)
        parent = findViewById<LinearLayout>(R.id.contentsLayout)
        contentsEditText = findViewById<EditText>(R.id.contentsEditText)
        titleEditText = findViewById<EditText>(R.id.titleEditText)
        info = findViewById<Button>(R.id.infoBtn)
        val bundle: Bundle? = getIntent().getExtras()

        // 새로 작성한 경우 FragHome에서 bundle로 미팅 이름 받아옴
        if (bundle != null && meetingCode != null) {
            meetingCode = bundle.getString("Code")
            Log.d("update Test2", meetingCode)

            // 수정하는 경우 ContentSceduleAct에서 수정할 Post의 미팅 이름 받아옴
        } else if (getIntent().getSerializableExtra("scheduleInfo") != null) {
            scheduleInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo
            if (scheduleInfo.getMeetingID() != null) {
                meetingCode = scheduleInfo.getMeetingID()
            }
        } else {
            // 글 작성 후에는 외부에서 받아온 미팅 이름이 없어져서 MakeSchedule 자체에서 다시 미팅 이름 전달한거 받음
            // storeUpload->onSuccess
            meetingCode = getIntent().getExtras().getString("Code")
        }
        findViewById<View>(R.id.onlineBtn).setOnClickListener(onClickListener)
        findViewById<View>(R.id.offlineBtn).setOnClickListener(onClickListener)
        info!!.setOnClickListener(onClickListener)
        contentsEditText.setOnFocusChangeListener(onFocusChangeListener)
        titleEditText.setOnFocusChangeListener(object : OnFocusChangeListener {
            public override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (hasFocus) {
                    selectedEditText = null
                }
            }
        })
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()

        // 스케쥴 DB 정보 담은 코드
        scheduleInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo?
        postInit()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> if (resultCode == Activity.RESULT_OK) {
                val path: String = data.getStringExtra(Util.INTENT_PATH)
                pathList.add(path)
                val contentsItemView: ContentsItemView = ContentsItemView(this)
                if (selectedEditText == null) {
                    parent.addView(contentsItemView)
                } else {
                    var i: Int = 0
                    while (i < parent.getChildCount()) {
                        if (parent.getChildAt(i) === selectedEditText.getParent()) {
                            parent.addView(contentsItemView, i + 1)
                            break
                        }
                        i++
                    }
                }
                contentsItemView.setOnFocusChangeListener(onFocusChangeListener)
            }
        }
    }

    var onClickListener: View.OnClickListener = object : View.OnClickListener {
        public override fun onClick(v: View) {
            when (v.getId()) {
                R.id.infoBtn -> {
                    val snackbar: Snackbar = Snackbar.make(v, "비대면 약속은 캘린더, 알림 기능만을 제공합니다!", Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction("확인", object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            snackbar.dismiss()
                        }
                    })
                    snackbar.show()
                }
                R.id.offlineBtn ->                     // 확인 버튼 누르면 Firestore에 업로드
                    storageUpload("offline")
                R.id.onlineBtn ->                     // 확인 버튼 누르면 Firestore에 업로드
                    storageUpload("online")
            }
        }
    }
    var onFocusChangeListener: OnFocusChangeListener = object : OnFocusChangeListener {
        public override fun onFocusChange(v: View, hasFocus: Boolean) {
            if (hasFocus) {
                selectedEditText = v as EditText?
            }
        }
    }

    // 글 ID 찾기, ScheduleInfo 정보 생성, 글 업로드
    private fun storageUpload(type: String) {
        val title: String = (findViewById<View>(R.id.titleEditText) as EditText).getText().toString()
        if (title.length > 0) {
            val contentsList: ArrayList<String> = ArrayList()
            val lateComerList: ArrayList<String> = ArrayList()
            user = FirebaseAuth.getInstance().getCurrentUser()

            // Firestore와 연동
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.getReference()
            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

            // 만약 새로 작성한 글이면 scInfo == null -> schedule 밑에 새 ID로 문서 생성
            // 기존 글을 수정하는거면 scInfo != null -> scInfo ID 찾아서 해당 글 문서에 다시 업로드
            val documentReference: DocumentReference = if (scheduleInfo == null) firebaseFirestore.collection("schedule").document() else firebaseFirestore.collection("schedule").document(scheduleInfo.getId())
            val date: Date = if (scheduleInfo == null) Date() else scheduleInfo.getCreatedAt()
            for (i in 0 until parent.getChildCount()) {
                val linearLayout: LinearLayout = parent.getChildAt(i) as LinearLayout
                for (ii in 0 until linearLayout.getChildCount()) {
                    val view: View = linearLayout.getChildAt(ii)
                    if (view is EditText) {
                        val text: String = (view as EditText).getText().toString()
                        if (text.length > 0) {
                            // 내용 리스트에 약속 설명 추가
                            contentsList.add(text)
                            if (scheduleInfo != null) {
                                finish()
                            }
                        }
                    }
                }
            }
            storeUpload(documentReference, ScheduleInfo(title, meetingCode, contentsList, date, user.getUid(), type))
        } else {
            Util.showToast(this@MakeScheduleActivity, "제목을 입력해주세요.")
        }
    }

    // db에 등록 성공했는지 검사, 다시 FragHome으로 돌아옴
    private fun storeUpload(documentReference: DocumentReference, scheduleInfo: ScheduleInfo) {
        documentReference.set(scheduleInfo.getScheduleInfo())
                .addOnSuccessListener(object : OnSuccessListener<Void?> {
                    public override fun onSuccess(aVoid: Void) {
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                        val resultIntent: Intent = Intent()
                        resultIntent.putExtra("scheduleInfo", scheduleInfo)
                        // 글 작성 후 다시 MakeSc로 돌아오면 meetingName이 사라져서 다시 전달함
                        resultIntent.putExtra("Code", meetingCode)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                })
                .addOnFailureListener(object : OnFailureListener {
                    public override fun onFailure(e: Exception) {
                        Log.w(TAG, "Error writing document", e)
                    }
                })
    }

    private fun postInit() {
        if (scheduleInfo != null) {
            titleEditText.setText(scheduleInfo.getTitle())
            val contentsList: ArrayList<String> = scheduleInfo.getContents()
            for (i in contentsList.indices) {
                val contents: String = contentsList.get(i)
                contentsEditText.setText(contents)
            }
        }
    }

    private fun myStartActivity(c: Class<*>, media: Int, requestCode: Int) {
        val intent: Intent = Intent(this, c)
        intent.putExtra(Util.INTENT_MEDIA, media)
        startActivityForResult(intent, requestCode)
    }

    companion object {
        private val TAG: String = "MakeScheduleActivity"
    }
}