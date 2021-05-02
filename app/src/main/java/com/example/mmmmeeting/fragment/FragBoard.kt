// 게시판 프래그먼트
package com.example.mmmmeeting.fragmentimport

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import java.util.*

com.google.firebase.firestore.GeoPointimport android.text.TextWatcherimport android.text.Editableimport android.widget.LinearLayoutimport android.view.ViewGroupimport android.view.LayoutInflaterimport com.example.mmmmeeting.Rimport com.bumptech.glide.Glideimport com.google.android.exoplayer2.SimpleExoPlayerimport com.example.mmmmeeting.Info.PostInfoimport android.widget.TextViewimport com.example.mmmmeeting.Info.ScheduleInfoimport android.util.TypedValueimport android.view.Gravityimport com.example.mmmmeeting.Info.ChatItemimport android.widget.BaseAdapterimport com.google.firebase.auth.FirebaseUserimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.firebase.firestore.DocumentSnapshotimport android.app.Activityimport androidx.recyclerview.widget.RecyclerViewimport com.example.mmmmeeting.BoardDeleterimport androidx.cardview.widget.CardViewimport com.example.mmmmeeting.OnPostListenerimport android.content.Intentimport com.example.mmmmeeting.activity.ContentBoardActivityimport com.example.mmmmeeting.view.ReadContentsViewimport com.example.mmmmeeting.activity.MakePostActivityimport android.view.MenuInflaterimport com.example.mmmmeeting.Info.AddressItemsimport com.example.mmmmeeting.activity.MemberInitActivityimport com.example.mmmmeeting.Info.CalUserItemsimport android.widget.EditTextimport com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolderimport com.example.mmmmeeting.ScheduleDeleterimport com.example.mmmmeeting.OnScheduleListenerimport com.example.mmmmeeting.activity.CalendarActivityimport com.example.mmmmeeting.activity.ContentScheduleActivityimport android.view.View.OnLongClickListenerimport com.example.mmmmeeting.activity.AlarmActivityimport android.widget.Toastimport android.widget.FrameLayoutimport com.example.mmmmeeting.view.ReadScheduleViewimport com.example.mmmmeeting.activity.EditScheduleActivityimport com.example.mmmmeeting.Info.MeetingRoomItemsimport android.widget.RelativeLayoutimport com.example.mmmmeeting.activity.MeetingActivityimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.QueryDocumentSnapshotimport android.location.LocationManagerimport androidx.core.content.ContextCompatimport android.content.pm.PackageManagerimport com.example.mmmmeeting.activity.GpsTrackerimport android.os.Bundleimport android.os.IBinderimport com.example.mmmmeeting.activity.GrahamScanimport com.example.mmmmeeting.activity.ToolbarActivityimport com.example.mmmmeeting.adapter.MeetingRoomListAdapterimport com.google.android.gms.tasks.OnSuccessListenerimport com.google.android.gms.tasks.OnFailureListenerimport com.google.firebase.firestore.FieldValueimport com.example.mmmmeeting.activity.MakeMeetingActivityimport com.example.mmmmeeting.activity.MeetingAttendActivityimport com.google.firebase.firestore.CollectionReferenceimport com.google.firebase.database.FirebaseDatabaseimport com.google.firebase.database.DatabaseReferenceimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.common.SignInButtonimport com.example.mmmmeeting.activity.MainActivityimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.example.mmmmeeting.activity.SignActivityimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.android.gms.common.api.ApiExceptionimport com.google.firebase.auth.AuthCredentialimport com.google.firebase.auth.GoogleAuthProviderimport com.google.firebase.auth.AuthResultimport android.view.WindowManagerimport android.text.SpannableStringimport android.text.style.RelativeSizeSpanimport android.text.style.ForegroundColorSpanimport android.location.Geocoderimport androidx.appcompat.app.AppCompatActivityimport android.content.SharedPreferencesimport android.content.ComponentNameimport com.example.mmmmeeting.DeviceBootReceiverimport com.example.mmmmeeting.AlarmReceiverimport android.app.PendingIntentimport android.app.AlarmManagerimport android.os.Buildimport androidx.core.app.ActivityCompatimport android.widget.TimePickerimport androidx.recyclerview.widget.GridLayoutManagerimport com.example.mmmmeeting.adapter.GalleryAdapterimport android.provider.MediaStoreimport com.google.android.material.bottomnavigation.BottomNavigationViewimport com.example.mmmmeeting.fragment.FragCalendarimport com.example.mmmmeeting.fragment.FragChatimport com.example.mmmmeeting.fragment.FragAttendimport com.example.mmmmeeting.fragment.FragAccountimport com.example.mmmmeeting.fragment.FragHomeimport com.example.mmmmeeting.fragment.FragBoardimport com.example.mmmmeeting.activity.MeetingInfoActivityimport android.content.pm.ActivityInfoimport androidx.annotation .LayoutResimport com.prolificinteractive.materialcalendarview.MaterialCalendarViewimport com.prolificinteractive.materialcalendarview.CalendarModeimport com.example.mmmmeeting.decorators.SundayDecoratorimport com.example.mmmmeeting.decorators.SaturdayDecoratorimport com.example.mmmmeeting.decorators.OneDayDecoratorimport android.text.method.ScrollingMovementMethodimport com.example.mmmmeeting.activity.CalendarActivity.ApiSimulatorimport com.prolificinteractive.materialcalendarview.OnDateSelectedListenerimport com.prolificinteractive.materialcalendarview.CalendarDayimport com.example.mmmmeeting.activity.NoticeActivityimport android.os.AsyncTaskimport com.example.mmmmeeting.decorators.EventDecoratorimport com.google.firebase.storage.StorageReferenceimport android.view.View.OnFocusChangeListenerimport com.google.firebase.storage.FirebaseStorageimport com.example.mmmmeeting.view.ContentsItemViewimport com.example.mmmmeeting.activity.GalleryActivityimport com.google.firebase.storage.StorageMetadataimport com.google.firebase.storage.UploadTaskimport com.example.mmmmeeting.activity.CurrentMapActivityimport com.google.android.gms.maps.GoogleMapimport android.widget.ArrayAdapterimport android.widget.SeekBarimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport com.google.android.gms.maps.model.CircleOptionsimport com.google.android.gms.maps.CameraUpdateFactoryimport android.widget.SeekBar.OnSeekBarChangeListenerimport com.google.android.gms.maps.model.MarkerOptionsimport com.google.android.gms.maps.model.BitmapDescriptorFactoryimport com.google.android.gms.maps.SupportMapFragmentimport com.example.mmmmeeting.Info.VoteInfoimport org.json.JSONArrayimport org.json.JSONObjectimport android.widget.RatingBarimport org.json.JSONExceptionimport androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallbackimport com.google.android.gms.location.FusedLocationProviderClientimport com.google.android.gms.location.LocationRequestimport com.google.android.gms.location.LocationSettingsRequestimport com.google.android.gms.location.LocationServicesimport com.google.android.material.snackbar.Snackbarimport com.google.android.gms.location.LocationCallbackimport com.google.android.gms.location.LocationResultimport com.google.maps.android.SphericalUtilimport android.os.Looperimport android.widget.RatingBar.OnRatingBarChangeListenerimport com.example.mmmmeeting.activity.SearchAddressActivityimport kotlin.Throwsimport com.example.mmmmeeting.Info.MeetingInfoimport android.view.View.OnTouchListenerimport android.view.MotionEventimport android.content.ClipDataimport com.example.mmmmeeting.activity.inviteActivityimport com.example.mmmmeeting.activity.newLeaderActivityimport com.example.mmmmeeting.activity.MeetingDeleteActivityimport noman.googleplaces.PlacesListenerimport androidx.appcompat.app.AppCompatDialogimport android.graphics.drawable.ColorDrawableimport com.example.mmmmeeting.activity.MiddlePlaceActivity.BackgroundTaskimport android.graphics.drawable.BitmapDrawableimport android.graphics.Bitmapimport android.graphics.Typefaceimport com.example.mmmmeeting.activity.PlaceListActivityimport noman.googleplaces.NRPlacesimport noman.googleplaces.PlaceTypeimport noman.googleplaces.PlacesExceptionimport com.example.mmmmeeting.activity.MiddlePlaceActivityimport com.example.mmmmeeting.activity.SearchPlaceActivityimport com.example.mmmmeeting.activity.VoteActivityimport com.google.android.libraries.places.api.Placesimport com.google.android.libraries.places.widget.AutocompleteSupportFragmentimport com.google.android.libraries.places.widget.listener.PlaceSelectionListenerimport com.example.mmmmeeting.activity.MakeScheduleActivityimport com.example.mmmmeeting.adapter.AddressAdapterimport org.xmlpull.v1.XmlPullParserFactoryimport org.xmlpull.v1.XmlPullParserimport com.example.mmmmeeting.view.ReadScheduleView_newimport com.example.mmmmeeting.adapter.ChatAdapterimport com.google.firebase.database.ChildEventListenerimport com.google.firebase.database.DataSnapshotimport com.google.firebase.database.DatabaseErrorimport com.example.mmmmeeting.adapter.ScheduleAdapterimport androidx.swiperefreshlayout.widget.SwipeRefreshLayoutimport androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListenerimport androidx.recyclerview.widget.LinearLayoutManagerimport com.example.mmmmeeting.adapter.BoardAdapterimport com.example.mmmmeeting.adapter.CalUserResultAdapterimport com.example.mmmmeeting.adapter.CalUserAdapterimport com.example.mmmmeeting.fragment.FragAccount_Resultimport android.util.Patternsimport com.prolificinteractive.materialcalendarview.DayViewDecoratorimport android.graphics.drawable.Drawableimport com.prolificinteractive.materialcalendarview.DayViewFacadeimport com.prolificinteractive.materialcalendarview.spans.DotSpanimport android.text.style.StyleSpanimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.app.NotificationChannel
class FragBoard constructor() : Fragment() {
    private var firebaseFirestore: FirebaseFirestore? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var boardAdapter: BoardAdapter? = null
    private var postList: ArrayList<PostInfo>? = null
    private var updating: Boolean = false
    private var topScrolled: Boolean = false
    private var meetingCode: String? = null
    var check: Int = 0
    var text: TextView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.frag_board, container, false)
        text = view.findViewById<View>(R.id.text) as TextView?
        var bundle: Bundle? = getArguments()
        if (bundle != null) {
            bundle = getArguments()
            meetingCode = bundle.getString("Code")
        }
        Log.d("get Name Test: ", meetingCode)
        firebaseFirestore = FirebaseFirestore.getInstance()
        postList = ArrayList<PostInfo>()
        boardAdapter = BoardAdapter(getActivity(), postList)
        boardAdapter.setOnPostListener(onPostListener)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        refreshLayout = view.findViewById<View>(R.id.swipe_refresh) as SwipeRefreshLayout?
        refreshLayout.setOnRefreshListener(object : OnRefreshListener {
            public override fun onRefresh() {
                recyclerView.postDelayed(object : Runnable {
                    public override fun run() {
                        postList = ArrayList<PostInfo>()
                        boardAdapter = BoardAdapter(getActivity(), postList)
                        boardAdapter.setOnPostListener(onPostListener)
                        recyclerView.setHasFixedSize(true)
                        recyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
                        recyclerView.setAdapter(boardAdapter)
                        postsUpdate(false)
                        refreshLayout.setRefreshing(false)
                    }
                }, 800)
            }
        })
        view.findViewById<View>(R.id.write_post).setOnClickListener(onClickListener)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(getActivity()))
        recyclerView.setAdapter(boardAdapter)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            public override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager: RecyclerView.LayoutManager = recyclerView.getLayoutManager()
                val firstVisibleItemPosition: Int = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (newState == 1 && firstVisibleItemPosition == 0) {
                    topScrolled = true
                }
                if (newState == 0 && topScrolled) {
                    postsUpdate(true)
                    topScrolled = false
                }
            }

            public override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager: RecyclerView.LayoutManager = recyclerView.getLayoutManager()
                val visibleItemCount: Int = layoutManager.getChildCount()
                val totalItemCount: Int = layoutManager.getItemCount()
                val firstVisibleItemPosition: Int = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                val lastVisibleItemPosition: Int = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (totalItemCount - 3 <= lastVisibleItemPosition && !updating) {
                    postsUpdate(false)
                }
                if (0 < firstVisibleItemPosition) {
                    topScrolled = false
                }
            }
        })
        postsUpdate(false)
        return view
    }

    public override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    public override fun onDetach() {
        super.onDetach()
    }

    public override fun onPause() {
        super.onPause()
        boardAdapter.playerStop()
    }

    var onClickListener: View.OnClickListener = object : View.OnClickListener {
        public override fun onClick(v: View) {
            when (v.getId()) {
                R.id.write_post -> myStartActivity(MakePostActivity::class.java)
            }
        }
    }
    var onPostListener: OnPostListener = object : OnPostListener {
        public override fun onDelete(postInfo: PostInfo?) {
            Log.e("로그: ", "삭제 성공")
        }

        public override fun onModify() {
            Log.e("로그: ", "수정 성공")
        }
    }

    private fun postsUpdate(clear: Boolean) {
        updating = true
        val date: Date = if (postList!!.size == 0 || clear) Date() else postList!!.get(postList!!.size - 1).getCreatedAt()
        val collectionReference: CollectionReference = firebaseFirestore.collection("posts")
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(10).get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                    public override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            if (clear) {
                                postList!!.clear()
                            }
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData())
                                if ((document.getData().get("meetingID").toString() == meetingCode)) {
                                    Log.d("update Test", meetingCode)
                                    check = 1
                                    postList!!.add(PostInfo(
                                            document.getData().get("title").toString(),
                                            document.getData().get("description").toString(),
                                            document.getData().get("meetingID").toString(),
                                            document.getData().get("contents") as ArrayList<String?>?,
                                            document.getData().get("publisher").toString(),
                                            Date(document.getDate("createdAt").getTime()),
                                            document.getId()))

                                    //findDB(document.getData().get("publisher").toString());
                                }
                            }
                            if (check == 0) {
                                text.setText("작성된 게시글이 없습니다." + "\n" + "새로운 게시글을 작성해보세요!")
                            } else {
                                text.setText("")
                            }
                            boardAdapter.notifyDataSetChanged()
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException())
                        }
                        updating = false
                    }
                })
    }

    private fun myStartActivity(c: Class<*>) {
        val intent: Intent = Intent(getActivity(), c)
        intent.putExtra("Code", meetingCode)
        startActivityForResult(intent, 0)
    }

    companion object {
        private val TAG: String = "HomeFragment"
    }
}