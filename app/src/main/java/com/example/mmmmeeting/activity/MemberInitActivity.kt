package com.example.mmmmeeting.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import com.bumptech.glide.Glide
import com.example.mmmmeeting.Info.MemberInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.Util
import com.example.mmmmeeting.activity.MemberInitActivity
import com.google.android.gms.tasks.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

class MemberInitActivity constructor() : ToolbarActivity(), View.OnClickListener, OnRatingBarChangeListener {
    private var checkButton: Button? = null
    private var addressSearch: Button? = null
    private var addressTv: TextView? = null
    private var nameTv: TextView? = null
    private var restaurant: RatingBar? = null
    private var cafe: RatingBar? = null
    private var shopping: RatingBar? = null
    private var park: RatingBar? = null
    private var act: RatingBar? = null
    private var sp: SharedPreferences? = null
    private var profileImageVIew: ImageView? = null
    private var profilePath: String? = null
    private var buttonBackgroundLayout: RelativeLayout? = null
    var storage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageRef: StorageReference = storage.getReference()
    var user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        checkButton = findViewById(R.id.checkButton)
        addressSearch = findViewById(R.id.addressSearchBtn)
        addressTv = findViewById(R.id.addressText)
        nameTv = findViewById<View>(R.id.nameEditText) as EditText?
        profileImageVIew = findViewById(R.id.profileImageView)
        buttonBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout)
        restaurant = findViewById(R.id.restaurantRate)
        cafe = findViewById(R.id.cafetRate)
        shopping = findViewById(R.id.shoppingRate)
        park = findViewById(R.id.parkRate)
        act = findViewById(R.id.actRate)
        sp = getSharedPreferences("sp", MODE_PRIVATE)
        beforeInfo()
        println("main:" + profilePath)
        checkButton.setOnClickListener(this)
        addressSearch.setOnClickListener(this)
        profileImageVIew.setOnClickListener(this)
        restaurant.setOnRatingBarChangeListener(this)
        cafe.setOnRatingBarChangeListener(this)
        shopping.setOnRatingBarChangeListener(this)
        park.setOnRatingBarChangeListener(this)
        act.setOnRatingBarChangeListener(this)
        buttonBackgroundLayout.setOnClickListener(this)
        findViewById<View>(R.id.delete).setOnClickListener(this)
        findViewById<View>(R.id.gallery).setOnClickListener(this)
    }

    private fun beforeInfo() {
        // ?????? ?????? ??? ???????????? -> ??? ?????? ??? ???????????? ????????? ??????
        val name: String? = sp!!.getString("name", "")
        val address: String? = sp!!.getString("address", "")
        val restaurantBar: Float = sp!!.getFloat("restaurant", 0f)
        val cafeBar: Float = sp!!.getFloat("cafe", 0f)
        val shoppingBar: Float = sp!!.getFloat("shopping", 0f)
        val parkBar: Float = sp!!.getFloat("park", 0f)
        val actBar: Float = sp!!.getFloat("act", 0f)
        profilePath = sp!!.getString("profilePath", "")


        // ?????? ??????
        nameTv!!.setText(name)
        addressTv!!.setText(address)
        restaurant!!.setRating(restaurantBar)
        cafe!!.setRating(cafeBar)
        shopping!!.setRating(shoppingBar)
        park!!.setRating(parkBar)
        act!!.setRating(actBar)
        if ((profilePath == "")) {
            profileImageVIew!!.setImageResource(R.drawable.profile)
        } else if (profilePath != null) {
            Glide.with(this).load(profilePath).centerCrop().override(500).into((profileImageVIew)!!)
        }
    }

    //    @Override
    //    protected void onResume() {
    //        super.onResume();
    //
    //        // ?????? ?????? ??? ????????? ???????????? ??????????????? ??????
    //        if(getIntent().getExtras()!=null) {
    //            Intent intent = getIntent();
    //            addressTv.setText(intent.getExtras().getString("road"));
    //
    //            String name = sp.getString("name", "");
    //            float restaurantBar = sp.getFloat("restaurant",0);
    //            float cafeBar = sp.getFloat("cafe",0);
    //            float shoppingBar = sp.getFloat("shopping",0);
    //            float parkBar = sp.getFloat("park",0);
    //            float actBar = sp.getFloat("act",0);
    //            profilePath = sp.getString("profilePath","");
    //            Log.d("info Test " , "resume value" + nameTv.getText().toString());
    //
    //            nameTv.setText(name);
    //            restaurant.setRating(restaurantBar);
    //            cafe.setRating(cafeBar);
    //            shopping.setRating(shoppingBar);
    //            park.setRating(parkBar);
    //            act.setRating(actBar);
    //
    //            if(profilePath.equals("")){
    //                profileImageVIew.setImageResource(R.drawable.profile);
    //
    //            }else if(profilePath!=null) {
    //                Glide.with(this).load(profilePath).centerCrop().override(500).into(profileImageVIew);
    //            }
    //        }
    //
    //    }
    private fun save() {
        val editor: SharedPreferences.Editor = sp!!.edit() // editor ????????? ??????
        // ????????? ?????? ??? ??????
        editor.putString("name", nameTv!!.getText().toString())
        editor.putString("address", addressTv!!.getText().toString())
        editor.putFloat("restaurant", restaurant!!.getRating())
        editor.putFloat("cafe", cafe!!.getRating())
        editor.putFloat("shopping", shopping!!.getRating())
        editor.putFloat("park", park!!.getRating())
        editor.putFloat("act", act!!.getRating())
        editor.putString("profilePath", profilePath)
        Log.d("info Test ", "work value" + nameTv!!.getText().toString())
    }

    public override fun onClick(v: View) {
        when (v.getId()) {
            R.id.checkButton ->                 // ??????, ?????? ?????? ??????
                if ((findViewById<View>(R.id.addressText) as TextView).getText().toString().length == 0) {
                    Toast.makeText(this, "????????? ???????????????.", Toast.LENGTH_SHORT).show()
                    break
                } else if ((findViewById<View>(R.id.nameEditText) as EditText).getText().toString().length == 0) {
                    Toast.makeText(this, "????????? ???????????????.", Toast.LENGTH_SHORT).show()
                    break
                } else {
                    profileUpdate()
                    break
                }
            R.id.addressSearchBtn -> {
                save()
                val intent: Intent = Intent(this, SearchAddressActivity::class.java)
                startActivityForResult(intent, 0)
            }
            R.id.profileImageView -> {
                save()
                buttonBackgroundLayout!!.setVisibility(View.VISIBLE)
            }
            R.id.buttonsBackgroundLayout -> buttonBackgroundLayout!!.setVisibility(View.GONE)
            R.id.gallery -> myStartActivity(GalleryActivity::class.java, Util.GALLERY_IMAGE, 0)
            R.id.delete -> {
                val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
                println("delpath:" + profilePath)
                if (Util.isProfileUrl(profilePath)) {
                    val mountainImagesRef: StorageReference = storageRef.child("users/" + user!!.getUid() + "/profileImage.jpg")
                    mountainImagesRef.delete().addOnSuccessListener(object : OnSuccessListener<Void?> {
                        public override fun onSuccess(aVoid: Void?) {
                            Util.showToast(this@MemberInitActivity, "????????? ????????? ?????????????????????.")
                            db.collection("users").document(user.getUid())
                                    .update("profilePath", null)
                            profilePath = ""
                            println("db:" + profilePath)
                            profileImageVIew!!.setImageResource(R.drawable.profile)
                            buttonBackgroundLayout!!.setVisibility(View.GONE)
                        }
                    }).addOnFailureListener(object : OnFailureListener {
                        public override fun onFailure(exception: Exception) {
                            Util.showToast(this@MemberInitActivity, "????????? ?????? ????????? ?????????????????????.")
                        }
                    })
                } else {
                    // ?????? ?????? ????????? ???
                    profilePath = ""
                    println("del:" + profilePath)
                    profileImageVIew!!.setImageResource(R.drawable.profile)
                    buttonBackgroundLayout!!.setVisibility(View.GONE)
                }
            }
        }
    }

    // ????????? ?????? ?????? db??? ??????
    private fun profileUpdate() {
        val address: String = (findViewById<View>(R.id.addressText) as TextView).getText().toString()
        val name: String = (findViewById<View>(R.id.nameEditText) as EditText).getText().toString()


        // ?????? ?????? ?????? ?????? -> db??????
        val memberInfo: MemberInfo = MemberInfo(name, address)
        memberInfo.setRating("restaurant", restaurant!!.getRating())
        memberInfo.setRating("cafe", cafe!!.getRating())
        memberInfo.setRating("park", park!!.getRating())
        memberInfo.setRating("shopping", shopping!!.getRating())
        memberInfo.setRating("act", act!!.getRating())
        Log.d("Rating Change", memberInfo.getRating().toString())
        if (user != null) {
            if (profilePath != null) {
                if ((profilePath == "") || Util.isProfileUrl(profilePath)) {
                    if (!(profilePath == "")) memberInfo.setProfilePath(profilePath)
                    storeUploader(memberInfo)
                } else {
                    println(profilePath)
                    println("profile")
                    val mountainImagesRef: StorageReference = storageRef.child("users/" + user!!.getUid() + "/profileImage.jpg")
                    try {
                        val stream: InputStream = FileInputStream(File(profilePath))
                        val uploadTask: UploadTask = mountainImagesRef.putStream(stream)
                        uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot?, Task<Uri>> {
                            @Throws(Exception::class)
                            public override fun then(task: Task<UploadTask.TaskSnapshot?>): Task<Uri> {
                                if (!task.isSuccessful()) {
                                    throw (task.getException())!!
                                }
                                return mountainImagesRef.getDownloadUrl()
                            }
                        }).addOnCompleteListener(object : OnCompleteListener<Uri> {
                            public override fun onComplete(task: Task<Uri>) {
                                if (task.isSuccessful()) {
                                    val downloadUri: Uri = task.getResult()
                                    memberInfo.setProfilePath(downloadUri.toString())
                                    println("profilepath" + downloadUri.toString())
                                    storeUploader(memberInfo)
                                } else {
                                    startToast("????????? ???????????? ?????????????????????.")
                                }
                            }
                        })
                    } catch (e: FileNotFoundException) {
                        Log.e("??????", "??????: " + e.toString())
                    }
                }
            } else {
                storeUploader(memberInfo)
            }
        } else {
            startToast("??????????????? ??????????????????.")
        }
    }

    private fun storeUploader(memberInfo: MemberInfo) {
        db.collection("users").document(user!!.getUid()).set(memberInfo)
                .addOnSuccessListener(object : OnSuccessListener<Void?> {
                    public override fun onSuccess(aVoid: Void?) {
                        val editor: SharedPreferences.Editor = sp!!.edit() // editor ????????? ??????

                        // ????????? ?????? ??? ??????
                        editor.putString("name", nameTv!!.getText().toString())
                        editor.putString("address", addressTv!!.getText().toString())
                        editor.putFloat("restaurant", restaurant!!.getRating())
                        editor.putFloat("cafe", cafe!!.getRating())
                        editor.putFloat("shopping", shopping!!.getRating())
                        editor.putFloat("park", park!!.getRating())
                        editor.putFloat("act", act!!.getRating())
                        if (memberInfo.getProfilePath() != null) {
                            println(memberInfo.getProfilePath())
                            editor.putString("profilePath", memberInfo.getProfilePath())
                        } else {
                            editor.remove("profilePath")
                            println("remove")
                        }
                        editor.commit() // ?????? ??????
                        startToast("???????????? ????????? ?????????????????????.")
                        myStartActivity(MainActivity::class.java)
                        finish()
                    }
                })
                .addOnFailureListener(object : OnFailureListener {
                    public override fun onFailure(e: Exception) {
                        startToast("???????????? ????????? ?????????????????????.")
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

    private fun myStartActivity(c: Class<*>, media: Int, requestCode: Int) {
        val intent: Intent = Intent(this, c)
        intent.putExtra(Util.INTENT_MEDIA, media)
        startActivityForResult(intent, requestCode)
    }

    public override fun onRatingChanged(ratingBar: RatingBar, rating: Float, fromUser: Boolean) {
        when (ratingBar.getId()) {
            R.id.restaurantRate -> Log.d("Rate Test", "restaurant change" + restaurant!!.getRating())
            R.id.cafetRate -> Log.d("Rate Test", "cafe change" + cafe!!.getRating())
            R.id.shoppingRate -> Log.d("Rate Test", "shopping change" + shopping!!.getRating())
            R.id.parkRate -> Log.d("Rate Test", "subway change" + park!!.getRating())
            R.id.actRate -> Log.d("Rate Test", "subway change" + act!!.getRating())
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            if (data != null) {
                addressTv!!.setText(data.getExtras()!!.getString("road"))
                Log.d("Test ", data.getExtras()!!.getString("road"))
            }
        }
        if (resultCode == RESULT_OK) {
            profilePath = data!!.getStringExtra(Util.INTENT_PATH)
            println(profilePath)
            Glide.with(this).load(profilePath).centerCrop().override(500).into((profileImageVIew)!!)
            buttonBackgroundLayout!!.setVisibility(View.GONE)
        }
    }
}