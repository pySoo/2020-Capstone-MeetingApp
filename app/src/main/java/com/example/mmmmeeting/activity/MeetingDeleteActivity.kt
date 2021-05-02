package com.example.mmmmeeting.activityimport

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

com.google.firebase.firestore.GeoPointimport android.text.TextWatcherimport android.text.Editableimport android.widget.LinearLayoutimport android.view.ViewGroupimport android.view.LayoutInflaterimport com.example.mmmmeeting.Rimport com.bumptech.glide.Glideimport com.google.android.exoplayer2.SimpleExoPlayerimport com.example.mmmmeeting.Info.PostInfoimport android.widget.TextViewimport com.example.mmmmeeting.Info.ScheduleInfoimport android.util.TypedValueimport android.view.Gravityimport com.example.mmmmeeting.Info.ChatItemimport android.widget.BaseAdapterimport com.google.firebase.auth.FirebaseUserimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.firestore.FirebaseFirestoreimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.firebase.firestore.DocumentSnapshotimport android.app.Activityimport androidx.recyclerview.widget.RecyclerViewimport com.example.mmmmeeting.BoardDeleterimport androidx.cardview.widget.CardViewimport com.example.mmmmeeting.OnPostListenerimport android.content.Intentimport com.example.mmmmeeting.activity.ContentBoardActivityimport com.example.mmmmeeting.view.ReadContentsViewimport com.example.mmmmeeting.activity.MakePostActivityimport android.view.MenuInflaterimport com.example.mmmmeeting.Info.AddressItemsimport com.example.mmmmeeting.activity.MemberInitActivityimport com.example.mmmmeeting.Info.CalUserItemsimport android.widget.EditTextimport com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolderimport com.example.mmmmeeting.ScheduleDeleterimport com.example.mmmmeeting.OnScheduleListenerimport com.example.mmmmeeting.activity.CalendarActivityimport com.example.mmmmeeting.activity.ContentScheduleActivityimport android.view.View.OnLongClickListenerimport com.example.mmmmeeting.activity.AlarmActivityimport android.widget.Toastimport android.widget.FrameLayoutimport com.example.mmmmeeting.view.ReadScheduleViewimport com.example.mmmmeeting.activity.EditScheduleActivityimport com.example.mmmmeeting.Info.MeetingRoomItemsimport android.widget.RelativeLayoutimport com.example.mmmmeeting.activity.MeetingActivityimport com.google.firebase.firestore.QuerySnapshotimport com.google.firebase.firestore.QueryDocumentSnapshotimport android.location.LocationManagerimport androidx.core.content.ContextCompatimport android.content.pm.PackageManagerimport com.example.mmmmeeting.activity.GpsTrackerimport android.os.Bundleimport android.os.IBinderimport com.example.mmmmeeting.activity.GrahamScanimport com.example.mmmmeeting.activity.ToolbarActivityimport com.example.mmmmeeting.adapter.MeetingRoomListAdapterimport com.google.android.gms.tasks.OnSuccessListenerimport com.google.android.gms.tasks.OnFailureListenerimport com.google.firebase.firestore.FieldValueimport com.example.mmmmeeting.activity.MakeMeetingActivityimport com.example.mmmmeeting.activity.MeetingAttendActivityimport com.google.firebase.firestore.CollectionReferenceimport com.google.firebase.database.FirebaseDatabaseimport com.google.firebase.database.DatabaseReferenceimport com.google.android.gms.auth.api.signin.GoogleSignInClientimport com.google.android.gms.common.SignInButtonimport com.example.mmmmeeting.activity.MainActivityimport com.google.android.gms.auth.api.signin.GoogleSignInOptionsimport com.google.android.gms.auth.api.signin.GoogleSignInimport com.example.mmmmeeting.activity.SignActivityimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.android.gms.common.api.ApiExceptionimport com.google.firebase.auth.AuthCredentialimport com.google.firebase.auth.GoogleAuthProviderimport com.google.firebase.auth.AuthResultimport android.view.WindowManagerimport android.text.SpannableStringimport android.text.style.RelativeSizeSpanimport android.text.style.ForegroundColorSpanimport android.location.Geocoderimport androidx.appcompat.app.AppCompatActivityimport android.content.SharedPreferencesimport android.content.ComponentNameimport com.example.mmmmeeting.DeviceBootReceiverimport com.example.mmmmeeting.AlarmReceiverimport android.app.PendingIntentimport android.app.AlarmManagerimport android.os.Buildimport androidx.core.app.ActivityCompatimport android.widget.TimePickerimport androidx.recyclerview.widget.GridLayoutManagerimport com.example.mmmmeeting.adapter.GalleryAdapterimport android.provider.MediaStoreimport com.google.android.material.bottomnavigation.BottomNavigationViewimport com.example.mmmmeeting.fragment.FragCalendarimport com.example.mmmmeeting.fragment.FragChatimport com.example.mmmmeeting.fragment.FragAttendimport com.example.mmmmeeting.fragment.FragAccountimport com.example.mmmmeeting.fragment.FragHomeimport com.example.mmmmeeting.fragment.FragBoardimport com.example.mmmmeeting.activity.MeetingInfoActivityimport android.content.pm.ActivityInfoimport androidx.annotation .LayoutResimport com.prolificinteractive.materialcalendarview.MaterialCalendarViewimport com.prolificinteractive.materialcalendarview.CalendarModeimport com.example.mmmmeeting.decorators.SundayDecoratorimport com.example.mmmmeeting.decorators.SaturdayDecoratorimport com.example.mmmmeeting.decorators.OneDayDecoratorimport android.text.method.ScrollingMovementMethodimport com.example.mmmmeeting.activity.CalendarActivity.ApiSimulatorimport com.prolificinteractive.materialcalendarview.OnDateSelectedListenerimport com.prolificinteractive.materialcalendarview.CalendarDayimport com.example.mmmmeeting.activity.NoticeActivityimport android.os.AsyncTaskimport com.example.mmmmeeting.decorators.EventDecoratorimport com.google.firebase.storage.StorageReferenceimport android.view.View.OnFocusChangeListenerimport com.google.firebase.storage.FirebaseStorageimport com.example.mmmmeeting.view.ContentsItemViewimport com.example.mmmmeeting.activity.GalleryActivityimport com.google.firebase.storage.StorageMetadataimport com.google.firebase.storage.UploadTaskimport com.example.mmmmeeting.activity.CurrentMapActivityimport com.google.android.gms.maps.GoogleMapimport android.widget.ArrayAdapterimport android.widget.SeekBarimport android.widget.AdapterView.OnItemSelectedListenerimport android.widget.AdapterViewimport com.google.android.gms.maps.model.CircleOptionsimport com.google.android.gms.maps.CameraUpdateFactoryimport android.widget.SeekBar.OnSeekBarChangeListenerimport com.google.android.gms.maps.model.MarkerOptionsimport com.google.android.gms.maps.model.BitmapDescriptorFactoryimport com.google.android.gms.maps.SupportMapFragmentimport com.example.mmmmeeting.Info.VoteInfoimport org.json.JSONArrayimport org.json.JSONObjectimport android.widget.RatingBarimport org.json.JSONExceptionimport androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallbackimport com.google.android.gms.location.FusedLocationProviderClientimport com.google.android.gms.location.LocationRequestimport com.google.android.gms.location.LocationSettingsRequestimport com.google.android.gms.location.LocationServicesimport com.google.android.material.snackbar.Snackbarimport com.google.android.gms.location.LocationCallbackimport com.google.android.gms.location.LocationResultimport com.google.maps.android.SphericalUtilimport android.os.Looperimport android.widget.RatingBar.OnRatingBarChangeListenerimport com.example.mmmmeeting.activity.SearchAddressActivityimport kotlin.Throwsimport com.example.mmmmeeting.Info.MeetingInfoimport android.view.View.OnTouchListenerimport android.view.MotionEventimport android.content.ClipDataimport com.example.mmmmeeting.activity.inviteActivityimport com.example.mmmmeeting.activity.newLeaderActivityimport com.example.mmmmeeting.activity.MeetingDeleteActivityimport noman.googleplaces.PlacesListenerimport androidx.appcompat.app.AppCompatDialogimport android.graphics.drawable.ColorDrawableimport com.example.mmmmeeting.activity.MiddlePlaceActivity.BackgroundTaskimport android.graphics.drawable.BitmapDrawableimport android.graphics.Bitmapimport android.graphics.Typefaceimport com.example.mmmmeeting.activity.PlaceListActivityimport noman.googleplaces.NRPlacesimport noman.googleplaces.PlaceTypeimport noman.googleplaces.PlacesExceptionimport com.example.mmmmeeting.activity.MiddlePlaceActivityimport com.example.mmmmeeting.activity.SearchPlaceActivityimport com.example.mmmmeeting.activity.VoteActivityimport com.google.android.libraries.places.api.Placesimport com.google.android.libraries.places.widget.AutocompleteSupportFragmentimport com.google.android.libraries.places.widget.listener.PlaceSelectionListenerimport com.example.mmmmeeting.activity.MakeScheduleActivityimport com.example.mmmmeeting.adapter.AddressAdapterimport org.xmlpull.v1.XmlPullParserFactoryimport org.xmlpull.v1.XmlPullParserimport com.example.mmmmeeting.view.ReadScheduleView_newimport com.example.mmmmeeting.adapter.ChatAdapterimport com.google.firebase.database.ChildEventListenerimport com.google.firebase.database.DataSnapshotimport com.google.firebase.database.DatabaseErrorimport com.example.mmmmeeting.adapter.ScheduleAdapterimport androidx.swiperefreshlayout.widget.SwipeRefreshLayoutimport androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListenerimport androidx.recyclerview.widget.LinearLayoutManagerimport com.example.mmmmeeting.adapter.BoardAdapterimport com.example.mmmmeeting.adapter.CalUserResultAdapterimport com.example.mmmmeeting.adapter.CalUserAdapterimport com.example.mmmmeeting.fragment.FragAccount_Resultimport android.util.Patternsimport com.prolificinteractive.materialcalendarview.DayViewDecoratorimport android.graphics.drawable.Drawableimport com.prolificinteractive.materialcalendarview.DayViewFacadeimport com.prolificinteractive.materialcalendarview.spans.DotSpanimport android.text.style.StyleSpanimport android.content.BroadcastReceiverimport android.app.NotificationManagerimport android.app.NotificationChannel
class MeetingDeleteActivity constructor() : AppCompatActivity() {
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent: Intent = getIntent()
        val code: String = intent.getExtras().getString("Code")
        checkCode(code)
    }

    // 입력한 코드가 존재하는지 확인
    private fun checkCode(code: String) {
        val docRef: DocumentReference = db.collection("meetings").document(code)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        Log.d("Delete", "Data is : " + document.getId())
                        checkUser(code)
                    } else {
                        Log.d("Delete", "No Document")
                        startToast("존재하지 않는 코드입니다.")
                        myStartActivity(MainActivity::class.java)
                        finish()
                    }
                } else {
                    Log.d("Delete", "Task Fail : " + task.getException())
                }
            }
        })
    }

    // 존재하는 코드인 경우 해당 모임에 현재 유저가 존재하는지 확인
    private fun checkUser(code: String) {
        val user: FirebaseUser = FirebaseAuth.getInstance().getCurrentUser()
        val userdel: DocumentReference = db.collection("meetings").document(code)
        userdel.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.getData().get("userID").toString().contains(user.getUid())) {
                        // db에서 현재 유저 uid 삭제
                        if ((document.get("leader") == user.getUid())) {
                            userdel.update("leader", "")
                        }
                        userdel.update("userID", FieldValue.arrayRemove(user.getUid()))
                        startToast("모임에서 탈퇴했습니다.")
                        Log.d("Delete2", document.getId() + " => " + document.getData())
                        meetingMemberCheck(code) //모임의 모임원이 아무도 없는지 확인 후 없으면 모임 제거거
                        myStartActivity(MainActivity::class.java)
                        finish()
                    } else {
                        Log.d("Delete", "No Document")
                        startToast("해당 모임에 가입되어 있지 않습니다.")
                        myStartActivity(MainActivity::class.java)
                        finish()
                    }
                } else {
                    Log.d("Delete", "Task Fail : " + task.getException())
                }
            }
        })
    }

    private fun meetingMemberCheck(code: String) {
        val meetingdel: DocumentReference = db.collection("meetings").document(code)
        meetingdel.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot?> {
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
                        // 스케쥴, 게시판 DB에서 모임 이름으로 찾아서 삭제
                        scheduleDelete(code)
                        boardDelete(code)
                        chatDelete(code)
                        voteDelete(code)
                        meetingdel.delete()
                    }
                } else {
                    Log.d("Delete", "Task Fail : " + task.getException())
                }
            }
        })
    }

    private fun scheduleDelete(code: String) {
        val scheduleDel: CollectionReference = db.collection("schedule")
        scheduleDel.get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
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
        val boardDel: CollectionReference = db.collection("posts")
        boardDel.get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
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
        val voteDel: CollectionReference = db.collection("vote")
        voteDel.get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
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

    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun myStartActivity(c: Class<*>) {
        val intent: Intent = Intent(this, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}