package com.example.mmmmeeting.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mmmmeeting.R
import com.example.mmmmeeting.Util
import com.example.mmmmeeting.activity.GalleryActivity
import com.example.mmmmeeting.adapter.GalleryAdapter
import java.util.*

class GalleryActivity constructor() : ToolbarActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setToolbarTitle("갤러리")
        if ((ContextCompat.checkSelfPermission(this@GalleryActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this@GalleryActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@GalleryActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                Util.showToast(this@GalleryActivity, "권한을 허용해 주세요.")
            }
        } else {
            recyclerInit()
        }
    }

    public override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                    recyclerInit()
                } else {
                    finish()
                    Util.showToast(this@GalleryActivity, "권한을 허용해 주세요.")
                }
            }
        }
    }

    private fun recyclerInit() {
        val numberOfColumns: Int = 2
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(GridLayoutManager(this, numberOfColumns))
        val mAdapter: RecyclerView.Adapter<*> = GalleryAdapter(this, getImagesPath(this))
        recyclerView.setAdapter(mAdapter)
    }

    fun getImagesPath(activity: Activity): ArrayList<String?> {
        val uri: Uri
        val listOfAllImages: ArrayList<String?> = ArrayList()
        val cursor: Cursor?
        val column_index_data: Int
        var PathOfImage: String? = null
        val projection: Array<String>
        val intent: Intent = getIntent()
        val media: Int = intent.getIntExtra(Util.INTENT_MEDIA, Util.GALLERY_IMAGE)
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        cursor = activity.getContentResolver().query(uri, projection, null, null, null)
        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data)
            listOfAllImages.add(PathOfImage)
        }
        return listOfAllImages
    }
}