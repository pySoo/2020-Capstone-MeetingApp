// ContentScheduleActivity 화면 (가운데 정렬)
package com.example.mmmmeeting.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mmmmeeting.Info.ScheduleInfo
import com.example.mmmmeeting.R
import com.google.android.exoplayer2.SimpleExoPlayer
import java.text.SimpleDateFormat
import java.util.*

class ReadScheduleView_new : LinearLayout {
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

    // 약속 정보
    fun setScheduleInfo(postInfo: ScheduleInfo) {
        val createdAtTextView = findViewById<TextView>(R.id.createAtTextView)
        createdAtTextView.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(postInfo.createdAt)
        val contentsLayout = findViewById<LinearLayout>(R.id.contentsLayout)
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val contentsList = postInfo.contents
        for (i in contentsList!!.indices) {
            if (i == moreIndex) {
                val textView = TextView(context)
                textView.layoutParams = layoutParams
                textView.text = "더보기..."
                contentsLayout.addView(textView)
                break
            }
            val contents = contentsList[i]
            val textView = layoutInflater!!.inflate(R.layout.view_contents_text, this, false) as TextView
            textView.text = contents
            textView.gravity = Gravity.CENTER
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
            contentsLayout.addView(textView)
        }
    }
}