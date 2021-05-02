package com.example.mmmmeeting.fragmentimport

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.mmmmeeting.Info.MemberInfo
import com.google.firebase.database.Query
import java.text.SimpleDateFormat
import java.util.*

com.google.firebase.firestore.GeoPointimport android.text.TextWatcherimport android.text.Editableimport android.widget.LinearLayoutimport android.view.ViewGroupimport android.view.LayoutInflaterimport com.example.mmmmeeting.Rimport com.bumptech.glide.Glideimport com.google.android.exoplayer2.SimpleExoPlayerimport com.example.mmmmeeting.Info.PostInfoimport android.widget.TextViewimport com.example.mmmmeeting.Info.ScheduleInfoimport android.util.TypedValueimport android.view.Gravityimport com.example.mmmmeeting.Info.ChatItemimport android.widget.BaseAdapterimport com.google.firebase.auth.FirebaseUserimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.firebase.firestore.DocumentSnapshotimport android.app.Activityimport androidx.recyclerview.widget.RecyclerViewimport com.example.mmmmeeting.BoardDeleterimport androidx.cardview.widget.CardViewimport com.example.mmmmeeting.OnPostListenerimport android.content.Intentimport com.example.mmmmeeting.activity.ContentBoardActivityimport com.example.mmmmeeting.view.ReadContentsViewimport com.example.mmmmeeting.activity.MakePostActivityimport android.view.MenuInflaterimport com.example.mmmmeeting.Info.AddressItemsimport com.example.mmmmeeting.activity.MemberInitActivityimport com.example.mmmmeeting.Info.CalUserItemsimport android.widget.EditTextimport com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolderimport com.example.mmmmeeting.ScheduleDeleterimport com.example.mmmmeeting.OnScheduleListenerimport com.example.mmmmeeting.activity.CalendarActivityimport com.example.mmmmeeting.activity.ContentScheduleActivityimport android.view.View.OnLongClickListenerimport com.example.mmmmeeting.activity.AlarmActivityimport android.widget.Toastimport android.widget.FrameLayoutimport com.example.mmmmeeting.view.ReadScheduleViewimport com.example.mmmmeeting.activity.EditScheduleActivityimport com.example.mmmmeeting.Info.MeetingRoomItemsimport android.widget.RelativeLayoutimport com.example.mmmmeeting.activity.MeetingActivityimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.QueryDocumentSnapshotimport android.location.LocationManagerimport androidx.core.content.ContextCompatimport android.content.pm.PackageManagerimport com.example.mmmmeeting.activity.GpsTrackerimport android.os.Bundleimport android.os.IBinderimport com.example.mmmmeeting.activity.GrahamScanimport com.example.mmmmeeting.activity.ToolbarActivityimport com.example.mmmmeeting.adapter.MeetingRoomListAdapterimport com.google.android.gms.tasks.OnSuccessListenerimport com.google.android.gms.tasks.OnFailureListenerimport com.google.firebase.firestore.FieldValueimport com.example.mmmmeeting.activity.MakeMeetingActivityimport com.example.mmmmeeting.activity.MeetingAttendActivityimport com.google.firebase.firestore.CollectionReferenceimport com.google.firebase.database.FirebaseDatabaseimport com.google.firebase.database.DatabaseReferenceimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.common.SignInButtonimport com.example.mmmmeeting.activity.MainActivityimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.example.mmmmeeting.activity.SignActivityimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.android.gms.common.api.ApiExceptionimport com.google.firebase.auth.AuthCredentialimport com.google.firebase.auth.GoogleAuthProviderimport com.google.firebase.auth.AuthResultimport android.view.WindowManagerimport android.text.SpannableStringimport android.text.style.RelativeSizeSpanimport android.text.style.ForegroundColorSpanimport android.location.Geocoderimport androidx.appcompat.app.AppCompatActivityimport android.content.SharedPreferencesimport android.content.ComponentNameimport com.example.mmmmeeting.DeviceBootReceiverimport com.example.mmmmeeting.AlarmReceiverimport android.app.PendingIntentimport android.app.AlarmManagerimport android.os.Buildimport androidx.core.app.ActivityCompatimport android.widget.TimePickerimport androidx.recyclerview.widget.GridLayoutManagerimport com.example.mmmmeeting.adapter.GalleryAdapterimport android.provider.MediaStoreimport com.google.android.material.bottomnavigation.BottomNavigationViewimport com.example.mmmmeeting.fragment.FragCalendarimport com.example.mmmmeeting.fragment.FragChatimport com.example.mmmmeeting.fragment.FragAttendimport com.example.mmmmeeting.fragment.FragAccountimport com.example.mmmmeeting.fragment.FragHomeimport com.example.mmmmeeting.fragment.FragBoardimport com.example.mmmmeeting.activity.MeetingInfoActivityimport android.content.pm.ActivityInfoimport androidx.annotation .LayoutResimport com.prolificinteractive.materialcalendarview.MaterialCalendarViewimport com.prolificinteractive.materialcalendarview.CalendarModeimport com.example.mmmmeeting.decorators.SundayDecoratorimport com.example.mmmmeeting.decorators.SaturdayDecoratorimport com.example.mmmmeeting.decorators.OneDayDecoratorimport android.text.method.ScrollingMovementMethodimport com.example.mmmmeeting.activity.CalendarActivity.ApiSimulatorimport com.prolificinteractive.materialcalendarview.OnDateSelectedListenerimport com.prolificinteractive.materialcalendarview.CalendarDayimport com.example.mmmmeeting.activity.NoticeActivityimport android.os.AsyncTaskimport com.example.mmmmeeting.decorators.EventDecoratorimport com.google.firebase.storage.StorageReferenceimport android.view.View.OnFocusChangeListenerimport com.google.firebase.storage.FirebaseStorageimport com.example.mmmmeeting.view.ContentsItemViewimport com.example.mmmmeeting.activity.GalleryActivityimport com.google.firebase.storage.StorageMetadataimport com.google.firebase.storage.UploadTaskimport com.example.mmmmeeting.activity.CurrentMapActivityimport com.google.android.gms.maps.GoogleMapimport android.widget.ArrayAdapterimport android.widget.SeekBarimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport com.google.android.gms.maps.model.CircleOptionsimport com.google.android.gms.maps.CameraUpdateFactoryimport android.widget.SeekBar.OnSeekBarChangeListenerimport com.google.android.gms.maps.model.MarkerOptionsimport com.google.android.gms.maps.model.BitmapDescriptorFactoryimport com.google.android.gms.maps.SupportMapFragmentimport com.example.mmmmeeting.Info.VoteInfoimport org.json.JSONArrayimport org.json.JSONObjectimport android.widget.RatingBarimport org.json.JSONExceptionimport androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallbackimport com.google.android.gms.location.FusedLocationProviderClientimport com.google.android.gms.location.LocationRequestimport com.google.android.gms.location.LocationSettingsRequestimport com.google.android.gms.location.LocationServicesimport com.google.android.material.snackbar.Snackbarimport com.google.android.gms.location.LocationCallbackimport com.google.android.gms.location.LocationResultimport com.google.maps.android.SphericalUtilimport android.os.Looperimport android.widget.RatingBar.OnRatingBarChangeListenerimport com.example.mmmmeeting.activity.SearchAddressActivityimport kotlin.Throwsimport com.example.mmmmeeting.Info.MeetingInfoimport android.view.View.OnTouchListenerimport android.view.MotionEventimport android.content.ClipDataimport com.example.mmmmeeting.activity.inviteActivityimport com.example.mmmmeeting.activity.newLeaderActivityimport com.example.mmmmeeting.activity.MeetingDeleteActivityimport noman.googleplaces.PlacesListenerimport androidx.appcompat.app.AppCompatDialogimport android.graphics.drawable.ColorDrawableimport com.example.mmmmeeting.activity.MiddlePlaceActivity.BackgroundTaskimport android.graphics.drawable.BitmapDrawableimport android.graphics.Bitmapimport android.graphics.Typefaceimport com.example.mmmmeeting.activity.PlaceListActivityimport noman.googleplaces.NRPlacesimport noman.googleplaces.PlaceTypeimport noman.googleplaces.PlacesExceptionimport com.example.mmmmeeting.activity.MiddlePlaceActivityimport com.example.mmmmeeting.activity.SearchPlaceActivityimport com.example.mmmmeeting.activity.VoteActivityimport com.google.android.libraries.places.api.Placesimport com.google.android.libraries.places.widget.AutocompleteSupportFragmentimport com.google.android.libraries.places.widget.listener.PlaceSelectionListenerimport com.example.mmmmeeting.activity.MakeScheduleActivityimport com.example.mmmmeeting.adapter.AddressAdapterimport org.xmlpull.v1.XmlPullParserFactoryimport org.xmlpull.v1.XmlPullParserimport com.example.mmmmeeting.view.ReadScheduleView_newimport com.example.mmmmeeting.adapter.ChatAdapterimport com.google.firebase.database.ChildEventListenerimport com.google.firebase.database.DataSnapshotimport com.google.firebase.database.DatabaseErrorimport com.example.mmmmeeting.adapter.ScheduleAdapterimport androidx.swiperefreshlayout.widget.SwipeRefreshLayoutimport androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListenerimport androidx.recyclerview.widget.LinearLayoutManagerimport com.example.mmmmeeting.adapter.BoardAdapterimport com.example.mmmmeeting.adapter.CalUserResultAdapterimport com.example.mmmmeeting.adapter.CalUserAdapterimport com.example.mmmmeeting.fragment.FragAccount_Resultimport android.util.Patternsimport com.prolificinteractive.materialcalendarview.DayViewDecoratorimport android.graphics.drawable.Drawableimport com.prolificinteractive.materialcalendarview.DayViewFacadeimport com.prolificinteractive.materialcalendarview.spans.DotSpanimport android.text.style.StyleSpanimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.app.NotificationChannel
class FragChat constructor() : Fragment() {
    var user: FirebaseUser? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var chatRef: DatabaseReference? = null
    private val memberInfo: MemberInfo? = null
    private var userName: String? = null
    private var et: EditText? = null
    private var listView: ListView? = null
    private val messageItems: ArrayList<ChatItem> = ArrayList<ChatItem>()
    private var adapter: ChatAdapter? = null
    private var meetingCode: String? = null
    private var sp: SharedPreferences? = null
    private var profilePath: String? = null
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.frag_chat, container, false)
        user = FirebaseAuth.getInstance().getCurrentUser()
        sp = getActivity()!!.getSharedPreferences("sp", Context.MODE_PRIVATE)
        profilePath = sp.getString("profilePath", "")
        et = view.findViewById<EditText>(R.id.et)
        listView = view.findViewById(R.id.listview)
        listView.setVisibility(View.INVISIBLE)
        var bundle: Bundle? = getArguments()
        if (bundle != null) {
            bundle = getArguments()
            meetingCode = bundle.getString("Code")
            userName = bundle.getString("userName")
        }

        // meetingName 기준으로 분리함
        firebaseDatabase = FirebaseDatabase.getInstance()
        chatRef = firebaseDatabase.getReference("chat").child(meetingCode) as DatabaseReference?
        val chatQuery: Query = chatRef.orderByChild("timestamp")


        //RealtimeDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것을 듣는 리스너 추가
        chatQuery.addChildEventListener(object : ChildEventListener {
            public override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                val messageItem: ChatItem = dataSnapshot.getValue<ChatItem>(ChatItem::class.java)
                val cal: Calendar = Calendar.getInstance()
                val temp: Calendar = Calendar.getInstance()
                if (messageItems.size == 0) {
                    cal.setTimeInMillis(messageItem.getTimestamp())
                    val month: Int = cal.get(Calendar.MONTH)
                    val day: Int = cal.get(Calendar.DAY_OF_MONTH)
                    val date: ChatItem = ChatItem()
                    val nyear: Int = cal.get(Calendar.YEAR)
                    date.setId("date")
                    date.setTime(nyear.toString() + "년 " + (month + 1) + "월 " + day + "일")
                    messageItems.add(date)

                    //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                    messageItems.add(messageItem)
                } else {
                    println(messageItems)
                    if (messageItems.get(messageItems.size - 1).getTimestamp() == null) {
                        //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                        messageItems.add(messageItem)
                        adapter.notifyDataSetChanged()
                        listView.setSelection(messageItems.size - 1)
                    } else {
                        cal.setTimeInMillis(messageItems.get(messageItems.size - 1).getTimestamp())
                        val year: Int = cal.get(Calendar.YEAR)
                        val month: Int = cal.get(Calendar.MONTH)
                        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
                        temp.setTimeInMillis(messageItem.getTimestamp())
                        val nyear: Int = temp.get(Calendar.YEAR)
                        val nmonth: Int = temp.get(Calendar.MONTH)
                        val nday: Int = temp.get(Calendar.DAY_OF_MONTH)
                        println("date: " + month + " " + day)
                        println("now: " + nmonth + " " + nday)

                        // 마지막 메세지보다 날짜가 지난 경우
                        if ((year < nyear) || (month < nmonth) || (month == nmonth && day < nday)) {
                            val date: ChatItem = ChatItem()
                            date.setId("date")
                            date.setTime(nyear.toString() + "년 " + (nmonth + 1) + "월 " + nday + "일")
                            messageItems.add(date)
                        }
                        //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                        messageItems.add(messageItem)
                        adapter.notifyDataSetChanged()
                        listView.setSelection(messageItems.size - 1)
                    }
                }
            }

            public override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            public override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            public override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            public override fun onCancelled(databaseError: DatabaseError) {}
        })
        adapter = ChatAdapter(messageItems, getLayoutInflater())
        listView.setAdapter(adapter)
        listView.setVisibility(View.VISIBLE)
        val button: Button = view.findViewById<View>(R.id.msgBtn) as Button
        button.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                clickSend(v)
            }
        })
        return view
    }

    fun clickSend(view: View?) {
        val timeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")
        val name: String? = userName
        val message: String = et.getText().toString()

        //메세지 작성 시간 문자열로
        val calendar: Calendar = Calendar.getInstance()
        val time: String = timeFormat.format(calendar.getTime())
        val now: Date = Date()
        val timestamp: Long = now.getTime()

        //DB에 저장할 값들(닉네임, 메세지, 시간)
        val messageItem: ChatItem = ChatItem(user.getUid(), name, message, time, timestamp, profilePath)
        chatRef.push().setValue(messageItem)

        //EditText에 있는 글씨 지우기
        et.setText("")

        //소프트키패드 안보이도록
        val imm: InputMethodManager = getActivity()!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(getActivity()!!.getCurrentFocus()!!.getWindowToken(), 0)
    }
}