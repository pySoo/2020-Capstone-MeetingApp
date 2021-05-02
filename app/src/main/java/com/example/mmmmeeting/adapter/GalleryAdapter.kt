package com.example.mmmmeeting.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mmmmeeting.R
import com.example.mmmmeeting.Util
import com.example.mmmmeeting.adapter.GalleryAdapter.GalleryViewHolder
import java.util.*

class GalleryAdapter constructor(private val activity: Activity, private val mDataset: ArrayList<String?>) : RecyclerView.Adapter<GalleryViewHolder>() {
    class GalleryViewHolder constructor(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val cardView: CardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_gallery, parent, false) as CardView
        val galleryViewHolder: GalleryViewHolder = GalleryViewHolder(cardView)
        cardView.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val resultIntent: Intent = Intent()
                resultIntent.putExtra(Util.INTENT_PATH, mDataset.get(galleryViewHolder.getAdapterPosition()))
                activity.setResult(Activity.RESULT_OK, resultIntent)
                activity.finish()
            }
        })
        return galleryViewHolder
    }

    public override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val cardView: CardView = holder.cardView
        val imageView: ImageView = cardView.findViewById(R.id.imageView)
        Glide.with(activity).load(mDataset.get(position)).centerCrop().override(500).into(imageView)
    }

    public override fun getItemCount(): Int {
        return mDataset.size
    }
}