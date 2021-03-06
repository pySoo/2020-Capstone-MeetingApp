package com.example.mmmmeeting.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.example.mmmmeeting.Info.PostInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.Util
import com.example.mmmmeeting.activity.MakePostActivity
import com.example.mmmmeeting.view.ContentsItemView
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*

class MakePostActivity constructor() : ToolbarActivity() {
    private var user: FirebaseUser? = null
    private val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageRef: StorageReference? = null
    private val pathList: ArrayList<String> = ArrayList()
    private val showList: ArrayList<String?> = ArrayList()
    private var parent: LinearLayout? = null
    private var buttonsBackgroundLayout: RelativeLayout? = null
    private var loaderLayout: RelativeLayout? = null
    private var selectedImageVIew: ImageView? = null
    private var selectedEditText: EditText? = null
    private var descriptionText: EditText? = null
    private var titleEditText: EditText? = null
    private var postInfo: PostInfo? = null
    private var pathCount: Int = 0
    private var successCount: Int = 0
    private var meetingcode: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)
        setToolbarTitle("????????? ??????")
        parent = findViewById(R.id.contentsLayout)
        buttonsBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout)
        loaderLayout = findViewById(R.id.loaderLyaout)
        descriptionText = findViewById(R.id.contentsEditText)
        titleEditText = findViewById(R.id.titleEditText)
        findViewById<View>(R.id.check).setOnClickListener(onClickListener)
        findViewById<View>(R.id.image).setOnClickListener(onClickListener)
        findViewById<View>(R.id.delete).setOnClickListener(onClickListener)
        buttonsBackgroundLayout.setOnClickListener(onClickListener)
        titleEditText.setOnFocusChangeListener(object : OnFocusChangeListener {
            public override fun onFocusChange(v: View, hasFocus: Boolean) {
                if (hasFocus) {
                    selectedEditText = null
                }
            }
        })

        // ?????????
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        val bundle: Bundle? = getIntent().getExtras()

        // ?????? ????????? ?????? FragBoard?????? bundle??? ?????? ?????? ?????????
        if (bundle != null && meetingcode != null) {
            meetingcode = bundle.getString("Code")
            Log.d("update Test2", meetingcode)

            // ???????????? ?????? ContentBoardAct?????? ????????? Post??? ?????? ?????? ?????????
        } else if (getIntent().getSerializableExtra("postInfo") != null) {
            postInfo = getIntent().getSerializableExtra("postInfo") as PostInfo?
            if (postInfo.getMeetingID() != null) {
                meetingcode = postInfo.getMeetingID()
            }
        } else {
            // ??? ?????? ????????? ???????????? ????????? ?????? ????????? ???????????? MakePost ???????????? ?????? ?????? ?????? ???????????? ??????
            // storeUpload->onSuccess
            meetingcode = getIntent().getExtras()!!.getString("Code")
        }
        postInit()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val path: String = data!!.getStringExtra(Util.INTENT_PATH)
            pathList.add(path)
            val contentsItemView: ContentsItemView = ContentsItemView(this)
            if (selectedEditText == null) {
                parent!!.addView(contentsItemView)
            } else {
                for (i in 0 until parent!!.getChildCount()) {
                    if (parent!!.getChildAt(i) === selectedEditText!!.getParent()) {
                        parent!!.addView(contentsItemView, i + 1)
                        break
                    }
                }
            }
            contentsItemView.setImage(path)
            contentsItemView.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    buttonsBackgroundLayout!!.setVisibility(View.VISIBLE)
                    selectedImageVIew = v as ImageView?
                }
            })
            contentsItemView.setOnFocusChangeListener(onFocusChangeListener)
        }
    }

    var onClickListener: View.OnClickListener = object : View.OnClickListener {
        public override fun onClick(v: View) {
            when (v.getId()) {
                R.id.check -> if (postInfo != null) {
                    edit()
                } else {
                    storageUpload()
                }
                R.id.image -> myStartActivity(GalleryActivity::class.java, Util.GALLERY_IMAGE, 0)
                R.id.buttonsBackgroundLayout -> if (buttonsBackgroundLayout!!.getVisibility() == View.VISIBLE) {
                    buttonsBackgroundLayout!!.setVisibility(View.GONE)
                }
                R.id.delete -> {
                    val selectedView: View = selectedImageVIew!!.getParent() as View
                    val path: String?
                    val contSize: Int
                    val now: Int = parent!!.indexOfChild(selectedView) - 1
                    if (postInfo == null) {
                        contSize = 0
                        path = pathList.get(now)
                    } else {
                        contSize = postInfo.getContents().size
                        // ?????? ????????? ????????? ???
                        if (now >= contSize) {
                            path = pathList.get(now - contSize)
                        } else {
                            // db??? ????????? ?????? ????????? ???
                            path = postInfo.getContents().get(now)
                        }
                    }

                    // ?????? ???????????? db??? ????????? ???????????? ?????? ??????..
                    if (Util.isStorageUrl(path)) {
                        val desertRef: StorageReference = storageRef!!.child("posts/" + postInfo.getId() + "/" + Util.storageUrlToName(path))
                        desertRef.delete().addOnSuccessListener(object : OnSuccessListener<Void?> {
                            public override fun onSuccess(aVoid: Void?) {
                                Util.showToast(this@MakePostActivity, "????????? ?????????????????????.")
                                val temp: ArrayList<String?>? = postInfo.getContents()
                                temp!!.removeAt(parent!!.indexOfChild(selectedView) - 1)
                                firebaseFirestore.collection("posts").document(postInfo.getId())
                                        .update("contents", temp)
                                parent!!.removeView(selectedView)
                                buttonsBackgroundLayout!!.setVisibility(View.GONE)
                            }
                        }).addOnFailureListener(object : OnFailureListener {
                            public override fun onFailure(exception: Exception) {
                                Util.showToast(this@MakePostActivity, "????????? ??????????????? ?????????????????????.")
                            }
                        })
                    } else {
                        // ?????? ?????? ????????? ???
                        if (postInfo == null) {
                            pathList.removeAt(now)
                        } else {
                            pathList.removeAt(now - postInfo.getContents().size)
                        }
                        println(parent!!.indexOfChild(selectedView) - 1)
                        parent!!.removeView(selectedView)
                        buttonsBackgroundLayout!!.setVisibility(View.GONE)
                    }
                }
            }
        }
    }
    var onFocusChangeListener: OnFocusChangeListener = object : OnFocusChangeListener {
        public override fun onFocusChange(v: View, hasFocus: Boolean) {
            if (hasFocus) {
                selectedEditText = v as EditText?
            }
        }
    }

    // ???????????? ??????
    private fun edit() {
        val title: String = (findViewById<View>(R.id.titleEditText) as EditText).getText().toString()
        val description: String = (findViewById<View>(R.id.contentsEditText) as EditText).getText().toString()
        if (title.length > 0) {
            loaderLayout!!.setVisibility(View.VISIBLE)
            val contentsList: ArrayList<String?>? = postInfo.getContents()

            // ?????? ?????? ????????? ????????? postInfo == null -> posts ?????? ??? ID??? ?????? ??????
            // ?????? ?????? ?????????????????? postInfo != null -> postInfo ID ????????? ?????? ??? ????????? ?????? ?????????
            val documentReference: DocumentReference = firebaseFirestore.collection("posts").document(postInfo.getId())
            val date: Date? = postInfo.getCreatedAt()
            postInfo.setTitle(title)
            postInfo.setDescription(description)

            // ????????? ????????? ????????? ??? ?????????, ????????? ??????
            if (pathList.size == 0) {
                documentReference.update("title", title)
                documentReference.update("description", description)
                val resultIntent: Intent = Intent()
                resultIntent.putExtra("postinfo", postInfo)
                // ??? ?????? ??? ?????? MakePost??? ???????????? meetingName??? ???????????? ?????? ?????????
                resultIntent.putExtra("Code", meetingcode)
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            // ?????? ?????? ????????? ???????????? ????????? contents ????????? db?????? ??????
            for (i in pathList.indices) {
                pathCount = i
                successCount++
                val path: String = pathList.get(pathCount)
                contentsList!!.add(path)
                println("cont1: " + contentsList)
                val pathArray: Array<String> = path.split("\\.").toTypedArray()
                // posts ???????????? ?????? ID ????????? ?????? ????????? ?????? ?????????
                val mountainImagesRef: StorageReference = storageRef!!.child("posts/" + postInfo.getId() + "/" + (contentsList.size - 1) + "." + pathArray.get(pathArray.size - 1))
                try {
                    val stream: InputStream = FileInputStream(File(pathList.get(pathCount)))
                    val metadata: StorageMetadata = StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size - 1)).build()
                    val uploadTask: UploadTask = mountainImagesRef.putStream(stream, metadata)
                    uploadTask.addOnFailureListener(object : OnFailureListener {
                        public override fun onFailure(exception: Exception) {}
                    }).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                        public override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                            val index: Int = taskSnapshot.getMetadata()!!.getCustomMetadata("index")!!.toInt()
                            println("index: " + index)
                            mountainImagesRef.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri> {
                                public override fun onSuccess(uri: Uri) {
                                    contentsList.set(index, uri.toString())
                                    postInfo.setContents(contentsList)
                                    // ????????? ????????? ???????????? ??? ????????? ??? db??? contentlist ??????
                                    successCount--
                                    updateDB(postInfo)
                                    println("cont2: " + contentsList)
                                }
                            })
                        }
                    })
                } catch (e: FileNotFoundException) {
                    Log.e("??????", "??????: " + e.toString())
                }
            }
        } else {
            Util.showToast(this@MakePostActivity, "????????? ??????????????????.")
        }
    }

    private fun updateDB(postInfo: PostInfo?) {
        if (successCount == 0) {
            val documentReference: DocumentReference = firebaseFirestore.collection("posts").document(postInfo.getId())
            documentReference.update("title", postInfo.getTitle())
            documentReference.update("description", postInfo.getDescription())
            documentReference.update("contents", postInfo.getContents())
            val resultIntent: Intent = Intent()
            resultIntent.putExtra("postinfo", postInfo)
            // ??? ?????? ??? ?????? MakePost??? ???????????? meetingName??? ???????????? ?????? ?????????
            resultIntent.putExtra("Code", meetingcode)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // ?????? ????????? ?????? ???
    private fun storageUpload() {
        val title: String = (findViewById<View>(R.id.titleEditText) as EditText).getText().toString()
        val description: String = (findViewById<View>(R.id.contentsEditText) as EditText).getText().toString()
        if (title.length > 0) {
            loaderLayout!!.setVisibility(View.VISIBLE)
            val contentsList: ArrayList<String?> = ArrayList()
            user = FirebaseAuth.getInstance().getCurrentUser()
            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            // ?????? ?????? ????????? ????????? postInfo == null -> posts ?????? ??? ID??? ?????? ??????
            // ?????? ?????? ?????????????????? postInfo != null -> postInfo ID ????????? ?????? ??? ????????? ?????? ?????????
            val documentReference: DocumentReference = firebaseFirestore.collection("posts").document()
            val date: Date = Date()
            for (i in 0 until parent!!.getChildCount()) {
                val linearLayout: LinearLayout = parent!!.getChildAt(i) as LinearLayout
                for (ii in 0 until linearLayout.getChildCount()) {
                    val view: View = linearLayout.getChildAt(ii)
                    if (view is EditText) {
                        val text: String = view.getText().toString()
                        if (text.length > 0) {
                            // ?????? ???????????? ?????? ?????? ??????
                        }
                    } else if (!Util.isStorageUrl(pathList.get(pathCount))) {
                        val path: String = pathList.get(pathCount)
                        successCount++
                        contentsList.add(path)
                        val pathArray: Array<String> = path.split("\\.").toTypedArray()
                        // posts ???????????? ?????? ID ????????? ?????? ????????? ?????? ?????????
                        val mountainImagesRef: StorageReference = storageRef!!.child("posts/" + documentReference.getId() + "/" + pathCount + "." + pathArray.get(pathArray.size - 1))
                        try {
                            val stream: InputStream = FileInputStream(File(pathList.get(pathCount)))
                            val metadata: StorageMetadata = StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size - 1)).build()
                            val uploadTask: UploadTask = mountainImagesRef.putStream(stream, metadata)
                            uploadTask.addOnFailureListener(object : OnFailureListener {
                                public override fun onFailure(exception: Exception) {}
                            }).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
                                public override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                                    val index: Int = taskSnapshot.getMetadata()!!.getCustomMetadata("index")!!.toInt()
                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri> {
                                        public override fun onSuccess(uri: Uri) {
                                            successCount--
                                            contentsList.set(index, uri.toString())
                                            if (successCount == 0) {
                                                storeUpload(documentReference, PostInfo(title, description, contentsList, user!!.getUid(), date, meetingcode))
                                            }
                                        }
                                    })
                                }
                            })
                        } catch (e: FileNotFoundException) {
                            Log.e("??????", "??????: " + e.toString())
                        }
                        pathCount++
                    }
                }
            }
            if (successCount == 0) {
                storeUpload(documentReference, PostInfo(title, description, contentsList, user!!.getUid(), date, meetingcode))
            }
        } else {
            Util.showToast(this@MakePostActivity, "????????? ??????????????????.")
        }
    }

    // db??? ?????? ??????????????? ??????, ?????? FragBoard??? ?????????
    private fun storeUpload(documentReference: DocumentReference, postInfo: PostInfo) {
        documentReference.set(postInfo.getPostInfo())
                .addOnSuccessListener(object : OnSuccessListener<Void?> {
                    public override fun onSuccess(aVoid: Void?) {
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                        loaderLayout!!.setVisibility(View.GONE)
                        val resultIntent: Intent = Intent()
                        resultIntent.putExtra("postinfo", postInfo)
                        // ??? ?????? ??? ?????? MakePost??? ???????????? meetingName??? ???????????? ?????? ?????????
                        resultIntent.putExtra("Code", meetingcode)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                })
                .addOnFailureListener(object : OnFailureListener {
                    public override fun onFailure(e: Exception) {
                        Log.w(TAG, "Error writing document", e)
                        loaderLayout!!.setVisibility(View.GONE)
                    }
                })
    }

    // ????????? ?????? ????????? ???
    private fun postInit() {
        if (postInfo != null) {
            titleEditText.setText(postInfo.getTitle())
            descriptionText.setText(postInfo.getDescription())
            val contentsList: ArrayList<String?>? = postInfo.getContents()
            for (i in contentsList!!.indices) {
                val contents: String? = contentsList.get(i)
                if (Util.isStorageUrl(contents)) {
                    showList.add(contents)
                    val contentsItemView: ContentsItemView = ContentsItemView(this)
                    parent!!.addView(contentsItemView)
                    contentsItemView.setImage(contents)
                    // ????????? ????????? ?????? ?????? ????????? ???
                    contentsItemView.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            buttonsBackgroundLayout!!.setVisibility(View.VISIBLE)
                            selectedImageVIew = v as ImageView?
                        }
                    })
                }
            }
        }
    }

    private fun myStartActivity(c: Class<*>, media: Int, requestCode: Int) {
        val intent: Intent = Intent(this, c)
        intent.putExtra(Util.INTENT_MEDIA, media)
        startActivityForResult(intent, requestCode)
    }

    companion object {
        private val TAG: String = "WritePostActivity"
    }
}