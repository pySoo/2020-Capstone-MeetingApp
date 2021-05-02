package com.example.mmmmeeting.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.mmmmeeting.R

class ContentsItemView : LinearLayout {
    private var imageView: ImageView? = null

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet) {
        initView()
    }

    private fun initView() {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        addView(layoutInflater.inflate(R.layout.view_contents_image, this, false))
        imageView = findViewById(R.id.contentsImageView)
    }

    fun setImage(path: String?) {
        Glide.with(this).load(path).override(1000).into(imageView!!)
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        imageView!!.setOnClickListener(onClickListener)
    }
}