// 장소 검색 화면
package com.example.mmmmeeting.activityimport

import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Address
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.firestore.DocumentReference
import java.io.IOException
import java.util.*

com.google.firebase.firestore.GeoPointimport android.text.TextWatcherimport android.text.Editableimport android.widget.LinearLayoutimport android.view.ViewGroupimport android.view.LayoutInflaterimport com.example.mmmmeeting.Rimport com.bumptech.glide.Glideimport com.google.android.exoplayer2.SimpleExoPlayerimport com.example.mmmmeeting.Info.PostInfoimport android.widget.TextViewimport com.example.mmmmeeting.Info.ScheduleInfoimport android.util.TypedValueimport android.view.Gravityimport com.example.mmmmeeting.Info.ChatItemimport android.widget.BaseAdapterimport com.google.firebase.auth.FirebaseUserimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.firebase.firestore.DocumentSnapshotimport android.app.Activityimport androidx.recyclerview.widget.RecyclerViewimport com.example.mmmmeeting.BoardDeleterimport androidx.cardview.widget.CardViewimport com.example.mmmmeeting.OnPostListenerimport android.content.Intentimport com.example.mmmmeeting.activity.ContentBoardActivityimport com.example.mmmmeeting.view.ReadContentsViewimport com.example.mmmmeeting.activity.MakePostActivityimport android.view.MenuInflaterimport com.example.mmmmeeting.Info.AddressItemsimport com.example.mmmmeeting.activity.MemberInitActivityimport com.example.mmmmeeting.Info.CalUserItemsimport android.widget.EditTextimport com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolderimport com.example.mmmmeeting.ScheduleDeleterimport com.example.mmmmeeting.OnScheduleListenerimport com.example.mmmmeeting.activity.CalendarActivityimport com.example.mmmmeeting.activity.ContentScheduleActivityimport android.view.View.OnLongClickListenerimport com.example.mmmmeeting.activity.AlarmActivityimport android.widget.Toastimport android.widget.FrameLayoutimport com.example.mmmmeeting.view.ReadScheduleViewimport com.example.mmmmeeting.activity.EditScheduleActivityimport com.example.mmmmeeting.Info.MeetingRoomItemsimport android.widget.RelativeLayoutimport com.example.mmmmeeting.activity.MeetingActivityimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.QueryDocumentSnapshotimport android.location.LocationManagerimport androidx.core.content.ContextCompatimport android.content.pm.PackageManagerimport com.example.mmmmeeting.activity.GpsTrackerimport android.os.Bundleimport android.os.IBinderimport com.example.mmmmeeting.activity.GrahamScanimport com.example.mmmmeeting.activity.ToolbarActivityimport com.example.mmmmeeting.adapter.MeetingRoomListAdapterimport com.google.android.gms.tasks.OnSuccessListenerimport com.google.android.gms.tasks.OnFailureListenerimport com.google.firebase.firestore.FieldValueimport com.example.mmmmeeting.activity.MakeMeetingActivityimport com.example.mmmmeeting.activity.MeetingAttendActivityimport com.google.firebase.firestore.CollectionReferenceimport com.google.firebase.database.FirebaseDatabaseimport com.google.firebase.database.DatabaseReferenceimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.common.SignInButtonimport com.example.mmmmeeting.activity.MainActivityimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.example.mmmmeeting.activity.SignActivityimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.android.gms.common.api.ApiExceptionimport com.google.firebase.auth.AuthCredentialimport com.google.firebase.auth.GoogleAuthProviderimport com.google.firebase.auth.AuthResultimport android.view.WindowManagerimport android.text.SpannableStringimport android.text.style.RelativeSizeSpanimport android.text.style.ForegroundColorSpanimport android.location.Geocoderimport androidx.appcompat.app.AppCompatActivityimport android.content.SharedPreferencesimport android.content.ComponentNameimport com.example.mmmmeeting.DeviceBootReceiverimport com.example.mmmmeeting.AlarmReceiverimport android.app.PendingIntentimport android.app.AlarmManagerimport android.os.Buildimport androidx.core.app.ActivityCompatimport android.widget.TimePickerimport androidx.recyclerview.widget.GridLayoutManagerimport com.example.mmmmeeting.adapter.GalleryAdapterimport android.provider.MediaStoreimport com.google.android.material.bottomnavigation.BottomNavigationViewimport com.example.mmmmeeting.fragment.FragCalendarimport com.example.mmmmeeting.fragment.FragChatimport com.example.mmmmeeting.fragment.FragAttendimport com.example.mmmmeeting.fragment.FragAccountimport com.example.mmmmeeting.fragment.FragHomeimport com.example.mmmmeeting.fragment.FragBoardimport com.example.mmmmeeting.activity.MeetingInfoActivityimport android.content.pm.ActivityInfoimport androidx.annotation .LayoutResimport com.prolificinteractive.materialcalendarview.MaterialCalendarViewimport com.prolificinteractive.materialcalendarview.CalendarModeimport com.example.mmmmeeting.decorators.SundayDecoratorimport com.example.mmmmeeting.decorators.SaturdayDecoratorimport com.example.mmmmeeting.decorators.OneDayDecoratorimport android.text.method.ScrollingMovementMethodimport com.example.mmmmeeting.activity.CalendarActivity.ApiSimulatorimport com.prolificinteractive.materialcalendarview.OnDateSelectedListenerimport com.prolificinteractive.materialcalendarview.CalendarDayimport com.example.mmmmeeting.activity.NoticeActivityimport android.os.AsyncTaskimport com.example.mmmmeeting.decorators.EventDecoratorimport com.google.firebase.storage.StorageReferenceimport android.view.View.OnFocusChangeListenerimport com.google.firebase.storage.FirebaseStorageimport com.example.mmmmeeting.view.ContentsItemViewimport com.example.mmmmeeting.activity.GalleryActivityimport com.google.firebase.storage.StorageMetadataimport com.google.firebase.storage.UploadTaskimport com.example.mmmmeeting.activity.CurrentMapActivityimport com.google.android.gms.maps.GoogleMapimport android.widget.ArrayAdapterimport android.widget.SeekBarimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport com.google.android.gms.maps.model.CircleOptionsimport com.google.android.gms.maps.CameraUpdateFactoryimport android.widget.SeekBar.OnSeekBarChangeListenerimport com.google.android.gms.maps.model.MarkerOptionsimport com.google.android.gms.maps.model.BitmapDescriptorFactoryimport com.google.android.gms.maps.SupportMapFragmentimport com.example.mmmmeeting.Info.VoteInfoimport org.json.JSONArrayimport org.json.JSONObjectimport android.widget.RatingBarimport org.json.JSONExceptionimport androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallbackimport com.google.android.gms.location.FusedLocationProviderClientimport com.google.android.gms.location.LocationRequestimport com.google.android.gms.location.LocationSettingsRequestimport com.google.android.gms.location.LocationServicesimport com.google.android.material.snackbar.Snackbarimport com.google.android.gms.location.LocationCallbackimport com.google.android.gms.location.LocationResultimport com.google.maps.android.SphericalUtilimport android.os.Looperimport android.widget.RatingBar.OnRatingBarChangeListenerimport com.example.mmmmeeting.activity.SearchAddressActivityimport kotlin.Throwsimport com.example.mmmmeeting.Info.MeetingInfoimport android.view.View.OnTouchListenerimport android.view.MotionEventimport android.content.ClipDataimport com.example.mmmmeeting.activity.inviteActivityimport com.example.mmmmeeting.activity.newLeaderActivityimport com.example.mmmmeeting.activity.MeetingDeleteActivityimport noman.googleplaces.PlacesListenerimport androidx.appcompat.app.AppCompatDialogimport android.graphics.drawable.ColorDrawableimport com.example.mmmmeeting.activity.MiddlePlaceActivity.BackgroundTaskimport android.graphics.drawable.BitmapDrawableimport android.graphics.Bitmapimport android.graphics.Typefaceimport com.example.mmmmeeting.activity.PlaceListActivityimport noman.googleplaces.NRPlacesimport noman.googleplaces.PlaceTypeimport noman.googleplaces.PlacesExceptionimport com.example.mmmmeeting.activity.MiddlePlaceActivityimport com.example.mmmmeeting.activity.SearchPlaceActivityimport com.example.mmmmeeting.activity.VoteActivityimport com.google.android.libraries.places.api.Placesimport com.google.android.libraries.places.widget.AutocompleteSupportFragmentimport com.google.android.libraries.places.widget.listener.PlaceSelectionListenerimport com.example.mmmmeeting.activity.MakeScheduleActivityimport com.example.mmmmeeting.adapter.AddressAdapterimport org.xmlpull.v1.XmlPullParserFactoryimport org.xmlpull.v1.XmlPullParserimport com.example.mmmmeeting.view.ReadScheduleView_newimport com.example.mmmmeeting.adapter.ChatAdapterimport com.google.firebase.database.ChildEventListenerimport com.google.firebase.database.DataSnapshotimport com.google.firebase.database.DatabaseErrorimport com.example.mmmmeeting.adapter.ScheduleAdapterimport androidx.swiperefreshlayout.widget.SwipeRefreshLayoutimport androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListenerimport androidx.recyclerview.widget.LinearLayoutManagerimport com.example.mmmmeeting.adapter.BoardAdapterimport com.example.mmmmeeting.adapter.CalUserResultAdapterimport com.example.mmmmeeting.adapter.CalUserAdapterimport com.example.mmmmeeting.fragment.FragAccount_Resultimport android.util.Patternsimport com.prolificinteractive.materialcalendarview.DayViewDecoratorimport android.graphics.drawable.Drawableimport com.prolificinteractive.materialcalendarview.DayViewFacadeimport com.prolificinteractive.materialcalendarview.spans.DotSpanimport android.text.style.StyleSpanimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.app.NotificationChannel
/*
 * 위치를 직접 검색해서 목적지로 설정한다.
 */   class SearchPlaceActivity constructor() : AppCompatActivity(), View.OnClickListener, OnMapReadyCallback {
    var mMap: GoogleMap? = null
    var previous_marker: List<Marker>? = null
    private var address: String? = null
    private var arrival: LatLng? = null
    var btn_vote: Button? = null
    var btn_delete: Button? = null
    private var layout_search: View? = null
    private var scheduleInfo: ScheduleInfo? = null
    private var scheduleId: String? = null
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var id: String? = null
    var size: Int = 0
    var state: String = "valid"
    var result: Boolean = false //투표리스트에 없음
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_place_search)
        val apiKey: String = getString(R.string.api_key)
        previous_marker = ArrayList()
        layout_search = findViewById<View>(R.id.layout_search)
        btn_vote = findViewById<Button>(R.id.btn_vote)
        btn_delete = findViewById<Button>(R.id.btn_delete)
        btn_vote!!.setOnClickListener(this)
        btn_delete!!.setOnClickListener(this)
        scheduleInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo?
        scheduleId = scheduleInfo.getId()
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey)
        }
        val autocompleteFragment: AutocompleteSupportFragment = getSupportFragmentManager().findFragmentById(R.id.search_map) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            public override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId())
                address = place.getName()
                println("address: " + address)
                mMap.clear()
                val p: com.example.mmmmeeting.activity.SearchPlaceActivity.Point = getPointFromGeoCoder(address)
                arrival = LatLng(p.X_value(), p.Y_value())
                val markerOptions: MarkerOptions = MarkerOptions()
                markerOptions.position(arrival)
                markerOptions.title("목적지")
                markerOptions.snippet(address)
                mMap.addMarker(markerOptions)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrival, 12f))
            }

            public override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status)
            }
        })
        val mapFragment: SupportMapFragment = getSupportFragmentManager()
                .findFragmentById(R.id.show_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    public override fun onClick(v: View) {
        when (v.getId()) {
            R.id.btn_vote -> if (address == null) {
                val snackbar: Snackbar = Snackbar.make(layout_search, "목적지를 입력해주세요^^", Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("확인", object : View.OnClickListener {
                    public override fun onClick(v: View) {
                        snackbar.dismiss()
                    }
                })
                snackbar.show()
            } else {
                db.collection("vote").whereEqualTo("scheduleID", scheduleId).get()
                        .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                            public override fun onComplete(task: Task<QuerySnapshot>) {
                                if (task.isSuccessful()) {
                                    for (document: QueryDocumentSnapshot in task.getResult()) {
                                        id = document.getId() // document 이름(id)
                                        println("list 있음")
                                    }
                                    if (id == null) {
                                        val info: VoteInfo = VoteInfo(scheduleId)
                                        info.setState("valid")
                                        db.collection("vote").add(info)
                                                .addOnSuccessListener(object : OnSuccessListener<DocumentReference?> {
                                                    public override fun onSuccess(documentReference: DocumentReference) {
                                                        id = documentReference.getId()
                                                        Log.d("Document Create", "Creating Success")
                                                    }
                                                })
                                                .addOnFailureListener(object : OnFailureListener {
                                                    public override fun onFailure(e: Exception) {
                                                        Log.d("Document Create", "Error creating documents: ", task.getException())
                                                    }
                                                })
                                    }
                                } else {
                                    Log.d("Document Read", "Error getting documents: ", task.getException())
                                }
                            }
                        })
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@SearchPlaceActivity)
                builder.setTitle("투표 추가") // 제목
                        .setMessage("투표리스트에 추가하시겠습니까?") // 메세지
                        // .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("추가", object : DialogInterface.OnClickListener {
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //투표리스트에 추가
                                checkList(arrival) // 투표리스트에 존재하는지 확인
                                val docRef: DocumentReference = db.collection("vote").document(id)
                                val delayHandler: Handler = Handler()
                                val r: Runnable = object : Runnable {
                                    public override fun run() {
                                        if ((state == "valid")) { // 투표 시작 전 상태
                                            if (result) {
                                                result = false
                                                Toast.makeText(this@SearchPlaceActivity, "이미 추가된 장소입니다.", Toast.LENGTH_SHORT).show()
                                            } else {
                                                val map: HashMap<String, Any> = HashMap()
                                                val location: GeoPoint = GeoPoint(arrival!!.latitude, arrival!!.longitude)
                                                val voter: List<String> = ArrayList()
                                                map.put("latlng", location)
                                                map.put("vote", 0)
                                                map.put("name", address!!)
                                                map.put("voter", voter)
                                                if (size >= 5) { // 리스트에 5개 이상 존재할 때
                                                    Toast.makeText(this@SearchPlaceActivity, "더이상 투표리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    db.collection("vote").document(id).update("place", FieldValue.arrayUnion(map))
                                                    Toast.makeText(this@SearchPlaceActivity, "투표리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        } else {
                                            Toast.makeText(this@SearchPlaceActivity, "이미 투표가 시작되었습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                delayHandler.postDelayed(r, 1500) // 1.5초후
                                docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
                                    public override fun onComplete(task: Task<DocumentSnapshot>) {
                                        if (task.isSuccessful()) {
                                            val document: DocumentSnapshot = task.getResult()
                                            if (document.exists()) {
                                                // 해당 문서가 존재하는 경우
                                                size = document.get("place").size
                                                state = document.getData().get("state").toString() // 투표 상태
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
                        })
                        .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                            // 취소 버튼 클릭시
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //취소 이벤트...
                            }
                        })
                val dialog: AlertDialog = builder.create() // 알림창 객체 생성
                dialog.show() // 알림창 띄우기
            }
            R.id.btn_delete -> if (address == null) {
                val snackbar: Snackbar = Snackbar.make(layout_search, "목적지를 입력해주세요^^", Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("확인", object : View.OnClickListener {
                    public override fun onClick(v: View) {
                        snackbar.dismiss()
                    }
                })
                snackbar.show()
            } else {
                db.collection("vote").whereEqualTo("scheduleID", scheduleId).get()
                        .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                            public override fun onComplete(task: Task<QuerySnapshot>) {
                                if (task.isSuccessful()) {
                                    for (document: QueryDocumentSnapshot in task.getResult()) {
                                        id = document.getId() // document 이름(id)
                                        println("list 있음")
                                    }
                                    if (id == null) {
                                        val info: VoteInfo = VoteInfo(scheduleId)
                                        info.setState("valid")
                                        db.collection("vote").add(info)
                                                .addOnSuccessListener(object : OnSuccessListener<DocumentReference?> {
                                                    public override fun onSuccess(documentReference: DocumentReference) {
                                                        id = documentReference.getId()
                                                        Log.d("Document Create", "Creating Success")
                                                    }
                                                })
                                                .addOnFailureListener(object : OnFailureListener {
                                                    public override fun onFailure(e: Exception) {
                                                        Log.d("Document Create", "Error creating documents: ", task.getException())
                                                    }
                                                })
                                    }
                                } else {
                                    Log.d("Document Read", "Error getting documents: ", task.getException())
                                }
                            }
                        })
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@SearchPlaceActivity)
                builder.setTitle("투표 삭제") // 제목
                        .setMessage("투표리스트에서 삭제하시겠습니까?") // 메세지
                        // .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("삭제", object : DialogInterface.OnClickListener {
                            // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                            public override fun onClick(dialog: DialogInterface, whichButton: Int) { //투표리스트에 추가
                                checkList(arrival) // 투표리스트에 존재하는지 확인
                                val docRef: DocumentReference = db.collection("vote").document(id)
                                val delayHandler: Handler = Handler()
                                val r: Runnable = object : Runnable {
                                    public override fun run() {
                                        if ((state == "valid")) { // 투표 시작 전 상태
                                            if (result) {
                                                result = false
                                                val map: HashMap<String, Any> = HashMap()
                                                val location: GeoPoint = GeoPoint(arrival!!.latitude, arrival!!.longitude)
                                                val voter: List<String> = ArrayList()
                                                map.put("latlng", location)
                                                map.put("vote", 0)
                                                map.put("name", address!!)
                                                map.put("voter", voter)
                                                docRef.update("place", FieldValue.arrayRemove(map))
                                                Toast.makeText(this@SearchPlaceActivity, "투표리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(this@SearchPlaceActivity, "투표리스트에 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            Toast.makeText(this@SearchPlaceActivity, "이미 투표가 시작되었습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                delayHandler.postDelayed(r, 1500) // 1.5초후
                                docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
                                    public override fun onComplete(task: Task<DocumentSnapshot>) {
                                        if (task.isSuccessful()) {
                                            val document: DocumentSnapshot = task.getResult()
                                            if (document.exists()) {
                                                // 해당 문서가 존재하는 경우
                                                size = document.get("place").size
                                                state = document.getData().get("state").toString() // 투표 상태
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

    //투표리스트에 해당 장소가 있는지 확인
    private fun checkList(location: LatLng?) {
        val docRef: DocumentReference = db.collection("vote").document(id)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        val list: List<HashMap<String, Any>> = document.get("place")
                        for (i in list.indices) {
                            var hashMap: HashMap<String, Any> = HashMap()
                            hashMap = list.get(i)
                            val geo: GeoPoint? = hashMap.get("latlng") as GeoPoint?
                            val loc: LatLng = LatLng(geo.getLatitude(), geo.getLongitude())
                            if ((loc == location)) {
                                result = true // 투표리스트에 이미 있음
                                Log.d("CheckList", "Success")
                            }
                        }
                    } else {
                        // 존재하지 않는 문서
                        Log.d("CheckList", "No Document")
                    }
                } else {
                    Log.d("CheckList", "Task Fail : " + task.getException())
                }
            }
        })
    }

    public override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val SEOUL: LatLng = LatLng(37.56, 126.97)
        val marker: MarkerOptions = MarkerOptions()
        marker.position(SEOUL)
        marker.visible(false)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 8f))
        mMap.clear()
    }

    internal inner class Point constructor() {
        // 위도
        var x: Double = 0.0

        // 경도
        var y: Double = 0.0
        var addr: String? = null

        // 포인트를 받았는지 여부
        var havePoint: Boolean = false
        public override fun toString(): String {
            val builder: StringBuilder = StringBuilder()
            builder.append("x : ")
            builder.append(x)
            builder.append(" y : ")
            builder.append(y)
            builder.append(" addr : ")
            builder.append(addr)
            return builder.toString()
        }

        fun X_value(): Double {
            return x
        }

        fun Y_value(): Double {
            return y
        }
    }

    private fun getPointFromGeoCoder(addr: String?): com.example.mmmmeeting.activity.SearchPlaceActivity.Point {
        val point: com.example.mmmmeeting.activity.SearchPlaceActivity.Point = com.example.mmmmeeting.activity.SearchPlaceActivity.Point()
        point.addr = addr
        val geocoder: Geocoder = Geocoder(this)
        val listAddress: List<Address>
        try {
            listAddress = geocoder.getFromLocationName(addr, 1)
        } catch (e: IOException) {
            e.printStackTrace()
            point.havePoint = false
            return point
        }
        if (listAddress.isEmpty()) {
            point.havePoint = false
            return point
        }
        point.havePoint = true
        point.y = listAddress.get(0).getLongitude()
        point.x = listAddress.get(0).getLatitude()
        return point
    }

    companion object {
        private val TAG: String = SearchPlaceActivity::class.java.getSimpleName()
    }
}