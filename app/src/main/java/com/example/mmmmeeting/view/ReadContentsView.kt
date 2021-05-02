package com.example.mmmmeeting.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mmmmeeting.Info.PostInfo
import com.example.mmmmeeting.R
import com.google.android.exoplayer2.SimpleExoPlayer
import java.text.SimpleDateFormat
import java.util.*

class ReadContentsView : LinearLayout {
    private var context: Context
    private var layoutInflater: LayoutInflater? = null
    val playerArrayList = ArrayList<SimpleExoPlayer>()
    private var moreIndex = -1

    constructor(context: Context) : super(context) {
        this.context = context
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        this.context = context
        initView()
    }

    private fun initView() {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        layoutInflater = getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater!!.inflate(R.layout.view_post, this, true)
    }

    fun setMoreIndex(moreIndex: Int) {
        this.moreIndex = moreIndex
    }

    // 게시판 정보
    fun setPostInfo(postInfo: PostInfo) {
        val createdAtTextView = findViewById<TextView>(R.id.createAtTextView)
        createdAtTextView.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postInfo.createdAt)
        val contentsLayout = findViewById<LinearLayout>(R.id.contentsLayout)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val contentsList = postInfo.contents
        val description = layoutInflater!!.inflate(R.layout.view_contents_text, this, false) as TextView
        description.text = postInfo.description
        description.textSize = 20f
        contentsLayout.addView(description)
        for (i in contentsList!!.indices) {
            if (i == moreIndex) {
                val textView = TextView(context)
                textView.layoutParams = layoutParams
                textView.text = "더보기..."
                contentsLayout.addView(textView)
                break
            }
            val contents = contentsList[i]
            val imageView = layoutInflater!!.inflate(R.layout.view_contents_image, this, false) as ImageView
            contentsLayout.addView(imageView)
            Glide.with(this).load(contents).override(1000).thumbnail(0.1f).into(imageView)
        }
    }
}