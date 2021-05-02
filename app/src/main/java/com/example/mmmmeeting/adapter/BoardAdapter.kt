// 게시판의 카드뷰 생성, 게시글 생성, 수정, 삭제와 연결됨
package com.example.mmmmeeting.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mmmmeeting.BoardDeleter
import com.example.mmmmeeting.Info.PostInfo
import com.example.mmmmeeting.OnPostListener
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.ContentBoardActivity
import com.example.mmmmeeting.activity.MakePostActivity
import com.example.mmmmeeting.view.ReadContentsView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class BoardAdapter(private val activity: Activity, private val mDataset: ArrayList<PostInfo>) : RecyclerView.Adapter<BoardAdapter.MainViewHolder>() {
    private val boardDeleter //Firestore db에서 삭제 되도록 연동
            : BoardDeleter
    private val playerArrayListArrayList = ArrayList<ArrayList<SimpleExoPlayer?>>()
    private val MORE_INDEX = 2
    var profilePath: String? = null
    var db = FirebaseFirestore.getInstance()

    class MainViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    fun setOnPostListener(onPostListener: OnPostListener?) {
        boardDeleter.setOnPostListener(onPostListener)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.list_board, parent, false) as CardView
        val mainViewHolder = MainViewHolder(cardView)
        cardView.setOnClickListener {
            val intent = Intent(activity, ContentBoardActivity::class.java)
            intent.putExtra("postInfo", mDataset[mainViewHolder.adapterPosition])
            activity.startActivity(intent)
        }
        cardView.findViewById<View>(R.id.menu).setOnClickListener { v -> showPopup(v, mainViewHolder.adapterPosition) }
        return mainViewHolder
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cardView = holder.cardView
        val titleTextView = cardView.findViewById<TextView>(R.id.titleTextView)
        val profileView = cardView.findViewById<ImageView>(R.id.userProfile)
        val userName = cardView.findViewById<TextView>(R.id.userName)
        val postInfo = mDataset[position]
        db.collection("users").document(postInfo.publisher).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document.exists()) {
                    val name = document["name"].toString()
                    userName.text = name
                    if (document["profilePath"] != null) {
                        profilePath = document["profilePath"].toString()
                        Glide.with(profileView).load(profilePath).centerCrop().override(500).into(profileView)
                    }
                    titleTextView.text = postInfo.title
                    val readContentsVIew: ReadContentsView = cardView.findViewById(R.id.readContentsView)
                    val contentsLayout = cardView.findViewById<LinearLayout>(R.id.contentsLayout)
                    if (contentsLayout.tag == null || contentsLayout.tag != postInfo) {
                        contentsLayout.tag = postInfo
                        contentsLayout.removeAllViews()
                        readContentsVIew.setMoreIndex(MORE_INDEX)
                        readContentsVIew.setPostInfo(postInfo)
                        val playerArrayList = readContentsVIew.playerArrayList
                        if (playerArrayList != null) {
                            playerArrayListArrayList.add(playerArrayList)
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    private fun showPopup(v: View, position: Int) {
        val popup = PopupMenu(activity, v)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.modify -> {
                    myStartActivity(MakePostActivity::class.java, mDataset[position])
                    true
                }
                R.id.delete -> {
                    boardDeleter.storageDelete(mDataset[position])
                    true
                }
                else -> false
            }
        })
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.post, popup.menu)
        popup.show()
    }

    private fun myStartActivity(c: Class<*>, postInfo: PostInfo) {
        val intent = Intent(activity, c)
        intent.putExtra("postInfo", postInfo)
        activity.startActivity(intent)
    }

    fun playerStop() {
        for (i in playerArrayListArrayList.indices) {
            val playerArrayList = playerArrayListArrayList[i]
            for (ii in playerArrayList.indices) {
                val player = playerArrayList[ii]
                if (player!!.playWhenReady) {
                    player.playWhenReady = false
                }
            }
        }
    }

    init {
        boardDeleter = BoardDeleter(activity)
    }
}