// 약속 목록의 카드뷰 생성, 게시글 생성, 수정, 삭제와 연결됨
package com.example.mmmmeeting.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.TypedValue
import android.view.*
import android.view.View.OnLongClickListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mmmmeeting.Info.ScheduleInfo
import com.example.mmmmeeting.OnScheduleListener
import com.example.mmmmeeting.R
import com.example.mmmmeeting.ScheduleDeleter
import com.example.mmmmeeting.activity.AlarmActivity
import com.example.mmmmeeting.activity.CalendarActivity
import com.example.mmmmeeting.activity.ContentScheduleActivity
import com.example.mmmmeeting.activity.EditScheduleActivity
import com.example.mmmmeeting.view.ReadScheduleView
import com.google.android.exoplayer2.SimpleExoPlayer
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter constructor(private val activity: Activity, private val mDataset: ArrayList<ScheduleInfo>) : RecyclerView.Adapter<ScheduleAdapter.MainViewHolder>() {
    private val scheduleDeleter //Firestore db에서 삭제 되도록 연동
            : ScheduleDeleter
    private val playerArrayListArrayList: ArrayList<ArrayList<SimpleExoPlayer?>> = ArrayList()
    private val MORE_INDEX: Int = 2

    class MainViewHolder constructor(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    fun setOnPostListener(onPostListener: OnScheduleListener?) {
        scheduleDeleter.setOnPostListener(onPostListener)
    }

    public override fun getItemViewType(position: Int): Int {
        return position
    }

    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val cardView: CardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schedule, parent, false) as CardView
        val mainViewHolder: MainViewHolder = MainViewHolder(cardView)
        cardView.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val type: String? = mDataset.get(mainViewHolder.getAdapterPosition()).getType()
                val intent: Intent
                if ((type == "online")) {
                    intent = Intent(activity, CalendarActivity::class.java)
                    intent.putExtra("scheduleInfo", mDataset.get(mainViewHolder.getAdapterPosition()))
                } else {
                    intent = Intent(activity, ContentScheduleActivity::class.java)
                    intent.putExtra("scheduleInfo", mDataset.get(mainViewHolder.getAdapterPosition()))
                }
                activity.startActivity(intent)
            }
        })
        cardView.setOnLongClickListener(object : OnLongClickListener {
            public override fun onLongClick(v: View): Boolean {
                val scInfo: ScheduleInfo = mDataset.get(mainViewHolder.getAdapterPosition())
                if (scInfo.getMeetingDate() != null) {
                    val meetingDate: Date? = scInfo.getMeetingDate()
                    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분")
                    val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
                    builder.setTitle("알림 설정") // 제목
                            .setMessage("약속 날짜는 " + sdf.format(meetingDate) + "입니다." + "\n" + "약속 미리 알림을 받으시겠습니까?") // 메세지
                            .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                                // 확인 버튼 클릭시 설정, 오른쪽 버튼입니다.
                                public override fun onClick(dialog: DialogInterface, whichButton: Int) { //약속 날짜를 확정 //db로 해당 날짜 올리기
                                    val cal: Calendar = Calendar.getInstance()
                                    cal.setTime(meetingDate)
                                    val intent: Intent = Intent(activity, AlarmActivity::class.java)
                                    intent.putExtra("alarm", cal)
                                    intent.putExtra("date", scInfo.getTitle())
                                    activity.startActivityForResult(intent, 0)
                                    Toast.makeText(activity.getApplicationContext(), sdf.format(meetingDate) + "에 설정되었습니다.", Toast.LENGTH_SHORT).show()
                                }
                            })
                            .setNegativeButton("취소", object : DialogInterface.OnClickListener {
                                // 취소 버튼 클릭시
                                public override fun onClick(dialog: DialogInterface, whichButton: Int) { //취소 이벤트...
                                }
                            })
                    val dialog: AlertDialog = builder.create() // 알림창 객체 생성
                    dialog.show() // 알림창 띄우기
                } else {
                    Toast.makeText(activity.getApplicationContext(), "아직 모임 날짜가 없습니다.", Toast.LENGTH_SHORT).show()
                }
                return true
            }
        })
        cardView.findViewById<View>(R.id.menu).setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                showPopup(v, mainViewHolder.getAdapterPosition())
            }
        })
        return mainViewHolder
    }

    public override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cardView: CardView = holder.cardView
        val titleTextView: TextView = cardView.findViewById(R.id.titleTextView)
        val meetingDateView: TextView = cardView.findViewById(R.id.meetingDate)
        val meetingPlaceView: TextView = cardView.findViewById(R.id.meetingPlace)
        val frame: FrameLayout = cardView.findViewById(R.id.frame)
        val postInfo: ScheduleInfo = mDataset.get(position)
        val type: String? = postInfo.getType()
        titleTextView.setText(postInfo.getTitle())
        if (postInfo.getMeetingDate() != null) {
            val meetingDate: Date? = postInfo.getMeetingDate()
            val sdf: SimpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일 a hh시 mm분")
            meetingDateView.setText("모임 날짜 : " + sdf.format(meetingDate))
        } else {
            meetingDateView.setText("모임 날짜 : 미정")
        }
        if ((type == "offline")) {
            if (postInfo.getMeetingPlace() != null) {
                meetingPlaceView.setText("모임 장소 : " + postInfo.getMeetingPlace())
            } else {
                meetingPlaceView.setText("모임 장소 : 미정")
            }
            if (postInfo.getMeetingDate() != null && postInfo.getMeetingPlace() != null) {
                frame.setVisibility(View.VISIBLE)
            } else {
                frame.setVisibility(View.INVISIBLE)
            }
            val readScheduleView: ReadScheduleView = cardView.findViewById(R.id.readScheduleView)
            val contentsLayout: LinearLayout = cardView.findViewById(R.id.contentsLayout)
            if (contentsLayout.getTag() == null || !(contentsLayout.getTag() == postInfo)) {
                contentsLayout.setTag(postInfo)
                contentsLayout.removeAllViews()
                readScheduleView.setMoreIndex(MORE_INDEX)
                readScheduleView.setScheduleInfo(postInfo)
                val playerArrayList: ArrayList<SimpleExoPlayer?>? = readScheduleView.getPlayerArrayList()
                if (playerArrayList != null) {
                    playerArrayListArrayList.add(playerArrayList)
                }
            }
        } else {
            meetingPlaceView.setText(postInfo.getContents().get(0))
            meetingPlaceView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            val createdAtTextView: TextView = cardView.findViewById(R.id.createAtTextView)
            createdAtTextView.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postInfo.getCreatedAt()))
            if (postInfo.getMeetingDate() != null) {
                frame.setVisibility(View.VISIBLE)
            } else {
                frame.setVisibility(View.INVISIBLE)
            }
        }
    }

    public override fun getItemCount(): Int {
        return mDataset.size
    }

    private fun showPopup(v: View, position: Int) {
        val popup: PopupMenu = PopupMenu(activity, v)
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            public override fun onMenuItemClick(menuItem: MenuItem): Boolean {
                when (menuItem.getItemId()) {
                    R.id.modify -> {
                        myStartActivity(EditScheduleActivity::class.java, mDataset.get(position))
                        return true
                    }
                    R.id.delete -> {
                        scheduleDeleter.storageDelete(mDataset.get(position))
                        return true
                    }
                    else -> return false
                }
            }
        })
        val inflater: MenuInflater = popup.getMenuInflater()
        inflater.inflate(R.menu.post, popup.getMenu())
        popup.show()
    }

    private fun myStartActivity(c: Class<*>, postInfo: ScheduleInfo) {
        val intent: Intent = Intent(activity, c)
        intent.putExtra("scheduleInfo", postInfo)
        activity.startActivity(intent)
    }

    fun playerStop() {
        for (i in playerArrayListArrayList.indices) {
            val playerArrayList: ArrayList<SimpleExoPlayer?> = playerArrayListArrayList.get(i)
            for (ii in playerArrayList.indices) {
                val player: SimpleExoPlayer? = playerArrayList.get(ii)
                if (player!!.getPlayWhenReady()) {
                    player.setPlayWhenReady(false)
                }
            }
        }
    }

    init {
        scheduleDeleter = ScheduleDeleter(activity)
    }
}