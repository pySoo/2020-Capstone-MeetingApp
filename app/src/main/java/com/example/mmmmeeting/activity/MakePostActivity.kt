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
        setToolbarTitle("게시글 작성")
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

        // 저장소
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        val bundle: Bundle? = getIntent().getExtras()

        // 새로 작성한 경우 FragBoard에서 bundle로 미팅 이름 받아옴
        if (bundle != null && meetingcode != null) {
            meetingcode = bundle.getString("Code")
            Log.d("update Test2", meetingcode)

            // 수정하는 경우 ContentBoardAct에서 수정할 Post의 미팅 이름 받아옴
        } else if (getIntent().getSerializableExtra("postInfo") != null) {
            postInfo = getIntent().getSerializableExtra("postInfo") as PostInfo?
            if (postInfo.getMeetingID() != null) {
                meetingcode = postInfo.getMeetingID()
            }
        } else {
            // 글 작성 후에는 외부에서 받아온 미팅 이름이 없어져서 MakePost 자체에서 다시 미팅 이름 전달한거 받음
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
                        // 새로 추가한 사진일 때
                        if (now >= contSize) {
                            path = pathList.get(now - contSize)
                        } else {
                            // db에 올라가 있는 사진일 때
                            path = postInfo.getContents().get(now)
                        }
                    }

                    // 혹시 모르니까 db에 올라간 형식인지 다시 검사..
                    if (Util.isStorageUrl(path)) {
                        val desertRef: StorageReference = storageRef!!.child("posts/" + postInfo.getId() + "/" + Util.storageUrlToName(path))
                        desertRef.delete().addOnSuccessListener(object : OnSuccessListener<Void?> {
                            public override fun onSuccess(aVoid: Void?) {
                                Util.showToast(this@MakePostActivity, "파일을 삭제하였습니다.")
                                val temp: ArrayList<String?>? = postInfo.getContents()
                                temp!!.removeAt(parent!!.indexOfChild(selectedView) - 1)
                                firebaseFirestore.collection("posts").document(postInfo.getId())
                                        .update("contents", temp)
                                parent!!.removeView(selectedView)
                                buttonsBackgroundLayout!!.setVisibility(View.GONE)
                            }
                        }).addOnFailureListener(object : OnFailureListener {
                            public override fun onFailure(exception: Exception) {
                                Util.showToast(this@MakePostActivity, "파일을 삭제하는데 실패하였습니다.")
                            }
                        })
                    } else {
                        // 방금 올린 사진일 때
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

    // 수정하는 경우
    private fun edit() {
        val title: String = (findViewById<View>(R.id.titleEditText) as EditText).getText().toString()
        val description: String = (findViewById<View>(R.id.contentsEditText) as EditText).getText().toString()
        if (title.length > 0) {
            loaderLayout!!.setVisibility(View.VISIBLE)
            val contentsList: ArrayList<String?>? = postInfo.getContents()

            // 만약 새로 작성한 글이면 postInfo == null -> posts 밑에 새 ID로 문서 생성
            // 기존 글을 수정하는거면 postInfo != null -> postInfo ID 찾아서 해당 글 문서에 다시 업로드
            val documentReference: DocumentReference = firebaseFirestore.collection("posts").document(postInfo.getId())
            val date: Date? = postInfo.getCreatedAt()
            postInfo.setTitle(title)
            postInfo.setDescription(description)

            // 사진을 바꾸지 않았을 때 타이틀, 설명만 수정
            if (pathList.size == 0) {
                documentReference.update("title", title)
                documentReference.update("description", description)
                val resultIntent: Intent = Intent()
                resultIntent.putExtra("postinfo", postInfo)
                // 글 작성 후 다시 MakePost로 돌아오면 meetingName이 사라져서 다시 전달함
                resultIntent.putExtra("Code", meetingcode)
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            // 새로 올린 사진은 저장소에 올리고 contents 리스트 db에도 반영
            for (i in pathList.indices) {
                pathCount = i
                successCount++
                val path: String = pathList.get(pathCount)
                contentsList!!.add(path)
                println("cont1: " + contentsList)
                val pathArray: Array<String> = path.split("\\.").toTypedArray()
                // posts 테이블의 문서 ID 받아서 해당 문서에 정보 업로드
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
                                    // 새로운 파일들 저장소에 다 올렸을 때 db에 contentlist 저장
                                    successCount--
                                    updateDB(postInfo)
                                    println("cont2: " + contentsList)
                                }
                            })
                        }
                    })
                } catch (e: FileNotFoundException) {
                    Log.e("로그", "에러: " + e.toString())
                }
            }
        } else {
            Util.showToast(this@MakePostActivity, "제목을 입력해주세요.")
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
            // 글 작성 후 다시 MakePost로 돌아오면 meetingName이 사라져서 다시 전달함
            resultIntent.putExtra("Code", meetingcode)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    // 처음 올리는 글일 때
    private fun storageUpload() {
        val title: String = (findViewById<View>(R.id.titleEditText) as EditText).getText().toString()
        val description: String = (findViewById<View>(R.id.contentsEditText) as EditText).getText().toString()
        if (title.length > 0) {
            loaderLayout!!.setVisibility(View.VISIBLE)
            val contentsList: ArrayList<String?> = ArrayList()
            user = FirebaseAuth.getInstance().getCurrentUser()
            val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
            // 만약 새로 작성한 글이면 postInfo == null -> posts 밑에 새 ID로 문서 생성
            // 기존 글을 수정하는거면 postInfo != null -> postInfo ID 찾아서 해당 글 문서에 다시 업로드
            val documentReference: DocumentReference = firebaseFirestore.collection("posts").document()
            val date: Date = Date()
            for (i in 0 until parent!!.getChildCount()) {
                val linearLayout: LinearLayout = parent!!.getChildAt(i) as LinearLayout
                for (ii in 0 until linearLayout.getChildCount()) {
                    val view: View = linearLayout.getChildAt(ii)
                    if (view is EditText) {
                        val text: String = view.getText().toString()
                        if (text.length > 0) {
                            // 내용 리스트에 약속 설명 추가
                        }
                    } else if (!Util.isStorageUrl(pathList.get(pathCount))) {
                        val path: String = pathList.get(pathCount)
                        successCount++
                        contentsList.add(path)
                        val pathArray: Array<String> = path.split("\\.").toTypedArray()
                        // posts 테이블의 문서 ID 받아서 해당 문서에 정보 업로드
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
                            Log.e("로그", "에러: " + e.toString())
                        }
                        pathCount++
                    }
                }
            }
            if (successCount == 0) {
                storeUpload(documentReference, PostInfo(title, description, contentsList, user!!.getUid(), date, meetingcode))
            }
        } else {
            Util.showToast(this@MakePostActivity, "제목을 입력해주세요.")
        }
    }

    // db에 등록 성공했는지 검사, 다시 FragBoard로 돌아옴
    private fun storeUpload(documentReference: DocumentReference, postInfo: PostInfo) {
        documentReference.set(postInfo.getPostInfo())
                .addOnSuccessListener(object : OnSuccessListener<Void?> {
                    public override fun onSuccess(aVoid: Void?) {
                        Log.d(TAG, "DocumentSnapshot successfully written!")
                        loaderLayout!!.setVisibility(View.GONE)
                        val resultIntent: Intent = Intent()
                        resultIntent.putExtra("postinfo", postInfo)
                        // 글 작성 후 다시 MakePost로 돌아오면 meetingName이 사라져서 다시 전달함
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

    // 작성한 글이 보이게 함
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
                    // 이미지 누르면 삭제 버튼 보이게 함
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