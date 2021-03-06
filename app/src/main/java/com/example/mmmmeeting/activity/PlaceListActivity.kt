package com.example.mmmmeeting.activity

import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mmmmeeting.Info.MemberInfo
import com.example.mmmmeeting.Info.VoteInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.PlaceListActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.UnsupportedEncodingException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.ExecutionException

class PlaceListActivity constructor() : AppCompatActivity(), OnMapReadyCallback {
    var mMap: GoogleMap? = null
    var midpoint: LatLng = LatLng(37.64471, 127.05876976018965)
    var fl_place_list: LinearLayout? = null
    var place_list_view: LinearLayout? = null
    private var str_url: String? = null
    private var placeInfo: String? = null
    private var preferNum: Int = 0
    private var check: Int = 0
    private val Tag: String = "category Test"
    var scheduleId: String? = null
    var code: String? = null
    var id: String? = null
    var category: ArrayList<String> = ArrayList()
    var userRatings: ArrayList<Array<Float?>> = ArrayList()
    var attendPoint: ArrayList<Float> = ArrayList()
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var count //success
            : Int = 0
    var size: Int = 0
    var state: String? = null
    var result: Boolean = false //?????????????????? ??????
    var best: HashMap<String, Int>? = null
    var spinner: Spinner? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var radiustv: TextView? = null
    var search_result: TextView? = null
    var seekBar: SeekBar? = null
    var currentCategory: String? = null
    var mHandler: Handler = object : Handler() {
        public override fun handleMessage(msg: Message) {
            val bd: Bundle = msg.getData() /// ?????? ?????? ??????????????? ????????? ??????
            if (bd.getStringArrayList("arg") != null) {
                val categoryList: ArrayList<String>? = bd.getStringArrayList("arg") /// ????????? ???????????? ??? ??????
                radius
                // Category ?????? ????????? ??? ??????
                spinnerAdd(categoryList)
            } else {
                preferNum = bd.getInt("num")
            }
        }
    }
    var delayHandler: Handler = Handler()
    private fun spinnerAdd(categoryList: ArrayList<String>?) {
        arrayAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, categoryList)
        spinner!!.setAdapter(arrayAdapter)
        spinner!!.setOnItemSelectedListener(object : OnItemSelectedListener {
            public override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                layoutclear()
                currentCategory = categoryList!!.get(position)
                sendPosition(position)
                var radius: Int = seekBar!!.getProgress()
                radius = radius / 500
                radius = radius * 500
                val circle: CircleOptions = CircleOptions().center(midpoint)
                        .radius(radius.toDouble()) //????????? ?????? : m
                        .strokeWidth(0f)
                        .strokeColor(Color.parseColor("#40F28379"))
                        .fillColor(Color.parseColor("#40F28379"))
                mMap!!.addCircle(circle)
                val zoom: Int = 15 - radius / 900
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, zoom.toFloat()))
                categoryItem(currentCategory, radius)
            }

            public override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun categoryItem(category: String?, radius: Int) {
        when (category) {
            "?????????" -> {
                //shopping_mall + department_store
                Log.d(Tag, "Shopping")
                check = 0
                showPlaceInformation("shopping_mall", radius)
                showPlaceInformation("department_store", radius)
            }
            "????????????" -> {
                // amusement_park + aquarium +art_gallery +stadium +zoo
                Log.d(Tag, "Activity")
                check = 0
                showPlaceInformation("amusement_park", radius)
                showPlaceInformation("aquarium", radius)
                showPlaceInformation("art_gallery", radius)
                showPlaceInformation("stadium", radius)
                showPlaceInformation("zoo", radius)
            }
            "??????" -> {
                Log.d(Tag, "Cafe")
                check = 0
                val beforeTime: Long = System.currentTimeMillis() //?????? ?????? ?????? ?????? ????????????
                showPlaceInformation("cafe", radius)
                val afterTime: Long = System.currentTimeMillis() // ?????? ?????? ?????? ?????? ????????????
                val secDiffTime: Long = (afterTime - beforeTime) //??? ????????? ??? ??????
                println("?????? ??????(ms) : " + secDiffTime)
            }
            "??????" -> {
                Log.d(Tag, "restaurant")
                check = 0
                beforeTime = System.currentTimeMillis()
                showPlaceInformation("restaurant", radius)
                afterTime = System.currentTimeMillis() // ?????? ?????? ?????? ?????? ????????????
                secDiffTime = (afterTime - beforeTime) //??? ????????? ??? ??????
                println("?????? ??????(ms) : " + secDiffTime)
            }
            "??????" -> {
                Log.d(Tag, "park")
                check = 0
                showPlaceInformation("park", radius)
            }
        }
    }

    private fun sendPosition(position: Int) {
        val bd: Bundle = Bundle() /// ?????? ??????
        bd.putInt("num", position)
        val msg: Message = mHandler.obtainMessage() /// ????????? ????????? ????????? ????????? ??????
        msg.setData(bd) /// ???????????? ?????? ??????
        mHandler.handleMessage(msg)
    }

    //????????? ?????? : m
    private val radius: Unit
        private get() {
            val radius: IntArray = intArrayOf(0)
            seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                public override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    var progress: Int = progress
                    progress = progress / 500
                    progress = progress * 500
                    radiustv!!.setText("?????? ?????? : " + progress + "m")
                    radius.get(0) = progress
                }

                public override fun onStartTrackingTouch(seekBar: SeekBar) {}
                public override fun onStopTrackingTouch(seekBar: SeekBar) {
                    layoutclear()
                    val circle: CircleOptions = CircleOptions().center(midpoint)
                            .radius(radius.get(0).toDouble()) //????????? ?????? : m
                            .strokeWidth(0f)
                            .strokeColor(Color.parseColor("#40F28379"))
                            .fillColor(Color.parseColor("#40F28379"))
                    val zoom: Int = 15 - radius.get(0) / 900
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, zoom.toFloat()))
                    mMap!!.addCircle(circle)
                    categoryItem(currentCategory, radius.get(0))
                }
            })
        }

    private fun layoutclear() {
        count = 0
        mMap!!.clear()
        place_list_view!!.removeAllViews()
        val marker: MarkerOptions = MarkerOptions()
        marker.position(midpoint).title("????????????")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap!!.addMarker(marker)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 15f))
        Log.d(Tag, "Clear")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //?????? ?????????????????? ???????????? ????????????(?????? ????????????)
        val i: Intent = getIntent()
        midpoint = i.getParcelableExtra("midpoint")
        code = i.getStringExtra("code")
        scheduleId = i.getStringExtra("scheduleId")
        Log.d("Send", "meetingname ???????????? : " + code)
        setContentView(R.layout.activity_place_list)
        val apiKey: String = getString(R.string.api_key)

//        previous_marker = new ArrayList<Marker>();
        place_list_view = findViewById(R.id.place_list_view)
        spinner = findViewById(R.id.categoryList)
        radiustv = findViewById(R.id.radiusText)
        seekBar = findViewById(R.id.radiusBar)
        seekBar.setProgress(500)
        radiustv.setText("?????? ?????? : " + seekBar.getProgress() + "m")
        val mapFragment: SupportMapFragment? = getSupportFragmentManager()
                .findFragmentById(R.id.place_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        select()
        db.collection("vote").whereEqualTo("scheduleID", scheduleId).get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: com.google.android.gms.tasks.Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                id = document.getId() // document ??????(id)
                                println("list ??????")
                            }
                            if (id == null) {
                                val info: VoteInfo = VoteInfo(scheduleId)
                                db.collection("vote").add(info)
                                        .addOnSuccessListener(object : OnSuccessListener<DocumentReference> {
                                            public override fun onSuccess(documentReference: DocumentReference) {
                                                id = documentReference.getId()
                                                Log.d("Document Create", "Creating Success")
                                            }
                                        })
                                        .addOnFailureListener(object : OnFailureListener {
                                            public override fun onFailure(e: Exception) {
                                                Log.d("Document Create", "Error creating documents: ", task.getException())
                                            }
                                        })
                            }
                        } else {
                            Log.d("Document Read", "Error getting documents: ", task.getException())
                        }
                    }
                })
    }

    private fun select() {
        //1. DB?????? ?????? ???????????? (meeting ?????? -> ui?????? ?????? -> ui??? ?????? ??????)
        meetingFind()
        // ?????? ??????- ?????????
        val r: Runnable = object : Runnable {
            public override fun run() {
                addWeight()
                highest
                Log.d(Tag, "After Category: " + category)
                val bd: Bundle = Bundle() /// ?????? ??????
                bd.putStringArrayList("arg", category) // ????????? ??? ??????
                val msg: Message = mHandler.obtainMessage() /// ????????? ????????? ????????? ????????? ??????
                msg.setData(bd) /// ???????????? ?????? ??????
                mHandler.sendMessage(msg)
            }
        }
        mHandler.postDelayed(r, 3000) // 1??????
    }

    public override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marker: MarkerOptions = MarkerOptions()
        marker.position(midpoint).title("????????????")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap!!.addMarker(marker)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 15f))
        val circle: CircleOptions = CircleOptions().center(midpoint)
                .radius(500.0) //????????? ?????? : m
                .strokeWidth(0f)
                .strokeColor(Color.parseColor("#40F28379"))
                .fillColor(Color.parseColor("#40F28379"))
        mMap!!.addCircle(circle)

        //mMap.clear();
    }

    fun getCurrentAddress(latlng: LatLng?): String {

        //????????????... GPS??? ????????? ??????
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                    latlng!!.latitude,
                    latlng.longitude,
                    1)
        } catch (ioException: IOException) {
            //???????????? ??????
            Toast.makeText(this, "???????????? ????????? ????????????", Toast.LENGTH_LONG).show()
            return "???????????? ????????? ????????????"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "????????? GPS ??????", Toast.LENGTH_LONG).show()
            return "????????? GPS ??????"
        }
        if (addresses == null || addresses.size == 0) {
            Log.d(Tag, "?????? ?????????")
            //            Toast.makeText(this, "?????? ?????????", Toast.LENGTH_LONG).show();
            return "?????? ?????????"
        } else {
            val address: Address = addresses.get(0)
            return address.getAddressLine(0).toString()
        }
    }

    ////////////////////////
    //URL??????, JSON ????????????///
    ////////////////////////
    inner class Task constructor() : AsyncTask<String?, Void?, String?>() {
        private var str: String? = null
        private var receiveMsg: String? = null
        protected override fun doInBackground(vararg parms: String): String? {
            var url: URL? = null
            try {
                url = URL(str_url)
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    val tmp: InputStreamReader = InputStreamReader(conn.getInputStream(), "UTF-8")
                    val reader: BufferedReader = BufferedReader(tmp)
                    val buffer: StringBuffer = StringBuffer()
                    while ((reader.readLine().also({ str = it })) != null) {
                        buffer.append(str)
                    }
                    receiveMsg = buffer.toString()
                    reader.close()
                } else {
                    Log.i("?????? ??????", conn.getResponseCode().toString() + "??????")
                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return receiveMsg
        }
    }

    fun getPlaceJson(latitude: Double, longtitude: Double): String? {
        val str_origin: String = latitude.toString() + "," + longtitude
        println("??????????????? : " + str_origin)
        var resultText: String? = null
        try {
            resultText = Task().execute().get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return resultText
    }

    // 1-1. ?????? ???????????? ????????? ????????? ??????
    private fun meetingFind() {
        val docRef: DocumentReference = db.collection("meetings").document((code)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // ?????? ????????? ???????????? ??????
                        // ?????? ????????? ????????? ????????????
                        best = document.get("best") as HashMap<String, Int>?
                        val users: List<String>? = document.get("userID") as List<String>?
                        for (i in users!!.indices) {
                            userRating(users.get(i))
                        }
                    } else {
                        // ???????????? ?????? ??????
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
    }

    // 1-2 ????????? ?????? ????????????
    private fun userRating(userID: String) {
        val docRef: DocumentReference = db.collection("users").document(userID)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        val user: MemberInfo? = document.toObject(MemberInfo::class.java)
                        MapToArray(user!!.getRating(), userID)
                    }
                } else {
                    Log.d(Tag, "Task Fail : " + task.getException())
                }
            }
        })
    }

    // 1-3 ??? -> ????????? ?????? (?????? ??????, ???????????? ??????, ?????? ?????? ??????)
    private fun MapToArray(rating: Map<String, Float>, userID: String) {
        val temp: Array<Float?> = arrayOfNulls(rating.size)
        if (!best!!.isEmpty() && best!!.containsKey(userID)) {
            var point: Float = java.lang.Float.valueOf(best!!.get(userID).toString())
            if (point == 0f) {
                point = 0.09f
            } else if (point == -1f) {
                point = 0.07f
            } else if (point < 0) {
                point = 1 / Math.abs(point)
                point *= 0.1f
            } else {
                point *= 0.1f
            }
            attendPoint.add(point)
        } else {
            attendPoint.add(0.09f)
        }

        // ????????? ??????
        for (key: String in rating.keys) {
            when (key) {
                "restaurant" -> temp.get(0) = rating.get(key)
                "cafe" -> temp.get(1) = rating.get(key)
                "park" -> temp.get(2) = rating.get(key)
                "shopping" -> temp.get(3) = rating.get(key)
                "act" -> temp.get(4) = rating.get(key)
            }
        }
        userRatings.add(temp)
    }

    // 2. ????????? ??????
    private fun addWeight() {
        val variance: DoubleArray = DoubleArray(userRatings.size)
        for (i in userRatings.indices) {
            // 2-1. ?????? ?????????
            variance.get(i) = calVariance(userRatings.get(i))
        }
        val temp: DoubleArray = variance.clone()
        Arrays.sort(temp) //????????????
        Log.d(Tag, "original variance" + Arrays.toString(variance))
        Log.d(Tag, "sort variance" + Arrays.toString(temp))
        Log.d(Tag, "attend point" + attendPoint.toString())
        var n: Int = temp.size
        val m: Double
        if (n % 2 == 0) { //??????
            n = n - 1
            m = (temp.get(n / 2) + temp.get((n / 2) + 1)) / 2
        } else {
            n = n - 1
            m = temp.get((n + 1) / 2)
        }

        //????????? ??????
        for (i in userRatings.indices) {
            Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(i)))

            // ????????? ????????????
            if (variance.get(i) > m) { // ????????? ????????? ????????? ??????????????? ??????
                // ????????? ?????? ????????????
                Log.d(Tag, "variance update user is " + i)
                userRatings.set(i, ratingUpdate(userRatings.get(i)))
            }
            Log.d(Tag, "~~ variance update " + Arrays.toString(userRatings.get(i)))

            // ???????????? ????????????
            if (!attendPoint.isEmpty()) {
                val rating: Array<Float?> = userRatings.get(i)
                for (j in rating.indices) {
                    rating.get(j) = rating.get(j)!! * attendPoint.get(i)
                }
                userRatings.set(i, rating)
                Log.d(Tag, "~~ attend update (?????? ??????)" + Arrays.toString(userRatings.get(i)))
            }
        }
    }

    // 2-1. ?????? ??????
    private fun calVariance(rating: Array<Float>): Double {
        // ?????? = ?????? ????????? ??? / ????????? ???  => (?????? = ???- ??????)
        var avg: Double = 0.0

        // 1. ?????? ?????????
        for (i in rating.indices) {
            avg += rating.get(i)
        }
        avg /= rating.size.toDouble()

        //2. ?????? ??????
        var variance: Double = 0.0
        for (i in rating.indices) {
            variance += (rating.get(i) - avg) * (rating.get(i) - avg)
        }
        variance /= rating.size.toDouble() // 3. ??????
        return variance
    }

    // 2-2. ????????? ??????
    private fun ratingUpdate(rating: Array<Float>): Array<Float> {
        var n: Int = rating.size
        val m: Double
        if (n % 2 == 0) { //??????
            n = n - 1
            m = (rating.get(n / 2) + rating.get((n / 2) + 1)) / 2.toDouble()
        } else {
            n = n - 1
            m = rating.get((n + 1) / 2).toDouble()
        }
        for (i in 0 until n) {
            if (rating.get(i) > m) {
                rating.get(i) = rating.get(i) * 1.5f
            } else {
                rating.get(i) = rating.get(i) * 0.5f
            }
        }
        return rating
    }// ?????? ?????? 3??? ??????

    // 0=?????? , 1=??????, 2=??????, 3=?????????, 4=????????????
// ????????? ?????? ???// ??????????????????//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(0)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(1)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(2)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(3)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(4)));
    // 3-2 ?????????
    private val highest: Unit
        private get() {

//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(0)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(1)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(2)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(3)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(4)));
            val avgRating: Array<Float> = arrayOfNulls(userRatings.get(0).length)
            for (i in 0 until userRatings.get(0).length) { // ??????????????????
                var sum: Float? = 0f
                for (j in userRatings.indices) { // ????????? ?????? ???
                    sum!! += (userRatings.get(j).get(i))!!
                }
                avgRating.get(i) = sum!! / userRatings.size
            }
            Log.d(Tag, "user average Rating Val " + Arrays.toString(avgRating))
            val index: ArrayList<Int> = ArrayList()
            for (i in 0..2) { // ?????? ?????? 3??? ??????
                var tempIndex: Int = 0
                var high: Float = avgRating.get(0)
                for (j in avgRating.indices) {
                    if (high <= avgRating.get(j)) {
                        high = avgRating.get(j)
                        tempIndex = j
                    }
                }
                Log.d(Tag, avgRating.get(tempIndex).toString() + "is highest val / " + tempIndex + " is added")
                avgRating.get(tempIndex) = 0f
                index.add(tempIndex)
            }

            // 0=?????? , 1=??????, 2=??????, 3=?????????, 4=????????????
            this.category.clear()
            for (i in index.indices) {
                when (index.get(i)) {
                    0 -> this.category.add("??????")
                    1 -> this.category.add("??????")
                    2 -> this.category.add("??????")
                    3 -> this.category.add("?????????")
                    4 -> this.category.add("????????????")
                }
            }
        }

    /*
    ??????????????? ???????????? ????????? Runnable ????????? Handler??? ???????????????!
    Handler??? ????????? ?????? ????????? ???????????? ??????
    ??????: ???????????? ?????? -> showPlaceInfo -> placeSuccess -> sortRating -> showUI
     */
    // ?????? ???????????? ?????? -> ?????? ?????? (0.4:0.6) ????????? ?????? ?????? ??????
    // ?????? ???????????? num?????? ArrayList??? ?????? -> showUI ????????? ?????????
    private fun showPlaceInformation(type: String, radius: Int) {

        //LatLng[] placeList;
        val ratingMap: HashMap<Int, Float> = HashMap()
        println("sortRating start!")
        var num: Int
        // 1???
        if (preferNum == 0) {
            num = 8
            // 2???
        } else if (preferNum == 1) {
            num = 5
            // 3???
        } else {
            num = 3
        }
        str_url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + midpoint.latitude + "," + midpoint.longitude +
                "&radius=" + radius + "&types=" + type + "&key=AIzaSyDZFlYs370FtbLuByL1cebdJdh8R-KF1xk&language=ko")
        placeInfo = getPlaceJson(midpoint.latitude, midpoint.longitude)
        println("placeInfo ??????: " + placeInfo)
        val resultArray: JSONArray
        try {
            println("try?????????")
            val jsonObject: JSONObject = JSONObject(placeInfo)
            println("???????????? JSON : " + placeInfo)
            resultArray = jsonObject.getJSONArray("results")
            val resultSize: Int = resultArray.length()
            val placeList: Array<LatLng?> = arrayOfNulls(resultSize)
            val place_name: Array<String?> = arrayOfNulls(resultSize)
            val rating: FloatArray = FloatArray(resultSize)
            var i: Int = 0
            do {
                val resultObject: JSONObject = resultArray.getJSONObject(i)
                var gm: String?
                gm = resultObject.getString("geometry")
                val geometry: JSONObject = JSONObject(gm)
                println("geometry: " + geometry)
                var sloc: String?
                sloc = geometry.getString("location")
                val location: JSONObject = JSONObject(sloc)
                println("?????? : " + location)
                val loc_lat: Float = location.getString("lat").toFloat()
                val loc_lng: Float = location.getString("lng").toFloat()

                //????????????
                val placeloc: LatLng = LatLng(loc_lat.toDouble(), loc_lng.toDouble())
                placeList.get(i) = placeloc
                println("???????????? ?????? :" + placeloc + "lat : " + loc_lat + "lng : " + loc_lng)
                place_name.get(i) = resultObject.getString("name")
                println("?????? ??????: " + place_name)

                //??????
                //String placeAddress = getCurrentAddress(placeloc);
                if (resultObject.has("rating")) {
                    rating.get(i) = resultObject.getString("rating").toFloat()
                } else {
                    rating.get(i) = 0.0f
                }
                ratingMap.put(i, rating.get(i))
                println("??????: " + rating.get(i))
                i++
            } while (i < resultSize)

            // Sort ??? ??? ???????????? ??????????????? array ??????
            // ratingList??? ratingMap??? key?????? ????????? ??????
            // Sort ??? ratingList??? (5,4,2,1,0)?????? placeList.get(?????? ?????????) -> ???????????? place ?????????
            val ratingList: ArrayList<Int>? = ArrayList(ratingMap.keys)
            for (j in placeList.indices) {
                val latitude: Double = placeList.get(j)!!.latitude - midpoint.latitude
                val longitude: Double = placeList.get(j)!!.longitude - midpoint.longitude

                // 0~1????????? ??????, 0??? ????????? ?????? ?????? ????????? ?????????
                var euclidean: Double = Math.sqrt(Math.pow(latitude, 2.0) + Math.pow(longitude, 2.0))
                // 0~5??? ????????? ????????? ????????? ?????? ???????????? ??????????????? ??????+????????? ???????????? ?????? -> ?????? ???????????? ?????????
                // 0~3.5??? ????????? ??????
                euclidean = Math.log10(1.0 / euclidean)

                // double?????? ????????? float?????? ??????
                val distancePoint: Float = java.lang.Float.valueOf(euclidean.toString())
                //System.out.println(distancePoint);
                val rat: Float = ratingMap.get(j)!!.toFloat()
                if (radius == 500) {
                    ratingMap.put(j, rat)
                } else if (500 < radius && radius <= 1000) {
                    // rating ?????? + ?????? ????????? ???????????? [0.1:0.3]?????? ????????? ?????? ??????
                    ratingMap.put(j, (0.1 * rat + 0.1 * distancePoint).toFloat())
                } else if (1000 < radius && radius <= 2000) {
                    ratingMap.put(j, (0.2 * rat + 0.1 * distancePoint).toFloat())
                } else {
                    ratingMap.put(j, (0.3 * rat + 0.1 * distancePoint).toFloat())
                }
                println(j.toString() + "??? ?????? ??????" + ratingMap.get(j))
            }

            // ?????? ????????? ???????????? ??????
            // ratingMap??? value??? ?????? ????????? ???????????? ??????, ratingMap??? key?????? ratingList??? ???
            // ratingList = {5,4,2,1,0}
            Collections.sort(ratingList, Comparator({ o1: Int, o2: Int -> (ratingMap.get(o2)!!.compareTo((ratingMap.get(o1))!!)) }))
            println("ratingList: " + ratingList)
            if (ratingList!!.size == 0 || ratingList == null) {
                val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER
                lp.setMargins(10, 100, 10, 10)
                val view1: TextView = TextView(this@PlaceListActivity)
                view1.setText("???????????? ?????????\n?????? ??????????????? ???????????? ?????????\n???????????? ????????????.\n\n????????? ???????????????!")
                view1.setTextSize(20f)
                view1.setTextColor(Color.BLACK)
                view1.setBackgroundColor(Color.WHITE)
                view1.setGravity(Gravity.CENTER)
                view1.setPadding(20, 50, 20, 20)
                view1.setLayoutParams(lp)
                //?????? ?????? ??????
                place_list_view!!.addView(view1)
                println("0?????? " + ratingList)
            } else {
                // ????????? num??? ????????? 5?????? ?????????, 5??? ?????? ?????? ????????? ????????? ????????? ?????? ????????? ?????? ??????
                if (check == 100) {
                    place_list_view!!.removeAllViews()
                }
                println("0????????? " + ratingList)
                if (ratingList.size < num) num = ratingList.size

                // ?????? num??? ?????? ?????? placeList??? ??????
                for (j in 0 until num) {
                    val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    val fl_param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    fl_place_list = LinearLayout(this@PlaceListActivity)
                    fl_place_list!!.setOrientation(LinearLayout.VERTICAL)
                    //param.bottomMargin = 100;
                    fl_place_list!!.setLayoutParams(fl_param)
                    fl_place_list!!.setBackgroundColor(Color.WHITE)
                    fl_place_list!!.setPadding(20, 10, 0, 30)
                    val rl_param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    val pl_name: RelativeLayout = RelativeLayout(this@PlaceListActivity)
                    pl_name.setLayoutParams(rl_param)
                    val placeAddress: String = getCurrentAddress(placeList.get(ratingList.get(j)))
                    //?????? ??????, ?????? ????????????
                    val pInfo: TextView = TextView(this@PlaceListActivity)
                    val s: SpannableString = SpannableString(place_name.get(ratingList.get(j)).toString() + "\n" + placeAddress)
                    s.setSpan(RelativeSizeSpan(1.8f), 0, place_name.get(ratingList.get(j)).length, 0)
                    s.setSpan(ForegroundColorSpan(Color.BLACK), 0, place_name.get(ratingList.get(j)).length, 0)
                    pInfo.setText(s)
                    pInfo.setLayoutParams(rl_param)
                    pl_name.addView(pInfo)

                    //????????????
                    val delete: Button = Button(this@PlaceListActivity)
                    val btn_param2: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(90, 90)
                    btn_param2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
                    btn_param2.setMargins(0, 0, 20, 0)
                    delete.setLayoutParams(btn_param2)
                    delete.setPadding(0, 20, 5, 0)
                    delete.setId(2 * (i + 1))
                    delete.setBackground(ContextCompat.getDrawable(this@PlaceListActivity, R.drawable.delete))
                    pl_name.addView(delete)

                    //????????????
                    val add: Button = Button(this@PlaceListActivity)
                    val btn_param: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(90, 90)
                    btn_param.addRule(RelativeLayout.LEFT_OF, delete.getId())
                    btn_param.setMargins(0, 0, 10, 0)
                    add.setLayoutParams(btn_param)
                    add.setPadding(0, 20, 5, 0)
                    add.setId(2 * i + 1)
                    add.setBackground(ContextCompat.getDrawable(this@PlaceListActivity, R.drawable.add_location))
                    pl_name.addView(add)
                    fl_place_list!!.addView(pl_name)

                    //LinearLayout ??????
                    val ly: LinearLayout = LinearLayout(this@PlaceListActivity)
                    //LinearLayout.LayoutParams lyparams = param;
                    ly.setLayoutParams(param)
                    ly.setOrientation(LinearLayout.HORIZONTAL)
                    val rate_tv: TextView = TextView(this@PlaceListActivity)
                    rate_tv.setText("?????? : " + rating.get(ratingList.get(j)) + " | ")
                    rate_tv.setLayoutParams(param)
                    ly.addView(rate_tv)
                    val rb: RatingBar = RatingBar(this@PlaceListActivity, null, android.R.attr.ratingBarStyleSmall)
                    rb.setNumStars(5)
                    rb.setRating(rating.get(ratingList.get(j)))
                    rb.setStepSize(0.1.toFloat())
                    rb.setPadding(0, 5, 0, 0)
                    rb.setLayoutParams(param)
                    ly.addView(rb)
                    fl_place_list!!.addView(ly)
                    place_list_view!!.addView(fl_place_list)
                    val finalPlaceName: String? = place_name.get(ratingList.get(j))
                    val pl: LatLng? = placeList.get(ratingList.get(j))
                    //???????????? ??????
                    add.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            checkList(pl) // ?????????????????? ??????????????? ??????
                            val docRef: DocumentReference = db.collection("vote").document((id)!!)
                            val r: Runnable = object : Runnable {
                                public override fun run() {
                                    if ((state == "valid")) {
                                        if (result) {
                                            result = false
                                            Toast.makeText(this@PlaceListActivity, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val map: HashMap<String, Any?> = HashMap()
                                            val location: GeoPoint = GeoPoint(pl!!.latitude, pl.longitude)
                                            val voter: List<String> = ArrayList()
                                            map.put("latlng", location)
                                            map.put("vote", 0)
                                            map.put("name", finalPlaceName)
                                            map.put("voter", voter)
                                            if (size >= 5) { // ???????????? 5??? ?????? ????????? ???
                                                Toast.makeText(this@PlaceListActivity, "????????? ?????????????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show()
                                            } else {
                                                db.collection("vote").document((id)!!).update("place", FieldValue.arrayUnion(map))
                                                Toast.makeText(this@PlaceListActivity, "?????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(this@PlaceListActivity, "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            delayHandler.postDelayed(r, 1000) // 1??????
                            docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                                public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                                    if (task.isSuccessful()) {
                                        val document: DocumentSnapshot = task.getResult()
                                        if (document.exists()) {
                                            // ?????? ????????? ???????????? ??????
                                            val list: List<HashMap<String, Any>>? = document.get("place") as List<HashMap<String, Any>>?
                                            size = list!!.size
                                            state = document.getData()!!.get("state").toString() // ?????? ??????
                                            delayHandler.sendEmptyMessage(0)
                                            Log.d("Attend", "Find document")
                                        } else {
                                            // ???????????? ?????? ??????
                                            Log.d("Attend", "No Document")
                                        }
                                    } else {
                                        Log.d("Attend", "Task Fail : " + task.getException())
                                    }
                                }
                            })
                        }
                    })
                    //???????????? ??????
                    delete.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            checkList(pl) // ?????????????????? ??????????????? ??????
                            val docRef: DocumentReference = db.collection("vote").document((id)!!)
                            val r: Runnable = object : Runnable {
                                public override fun run() {
                                    if ((state == "valid")) {
                                        if (result) {
                                            result = false
                                            val map: HashMap<String, Any?> = HashMap()
                                            val location: GeoPoint = GeoPoint(pl!!.latitude, pl.longitude)
                                            val voter: List<String> = ArrayList()
                                            map.put("latlng", location)
                                            map.put("vote", 0)
                                            map.put("name", finalPlaceName)
                                            map.put("voter", voter)
                                            println("size : " + size)
                                            docRef.update("place", FieldValue.arrayRemove(map))
                                            Toast.makeText(this@PlaceListActivity, "????????????????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@PlaceListActivity, "?????????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(this@PlaceListActivity, "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            delayHandler.postDelayed(r, 1000) // 1??????
                            docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                                public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                                    if (task.isSuccessful()) {
                                        val document: DocumentSnapshot = task.getResult()
                                        if (document.exists()) {
                                            // ?????? ????????? ???????????? ??????
                                            val list: List<HashMap<String, Any>>? = document.get("place") as List<HashMap<String, Any>>?
                                            size = list!!.size
                                            state = document.getData()!!.get("state").toString() // ?????? ??????
                                            delayHandler.sendEmptyMessage(0)
                                            Log.d("Attend", "Find document")
                                        } else {
                                            // ???????????? ?????? ??????
                                            Log.d("Attend", "No Document")
                                        }
                                    } else {
                                        Log.d("Attend", "Task Fail : " + task.getException())
                                    }
                                }
                            })
                        }
                    })
                    val markerOptions: MarkerOptions = MarkerOptions()
                    markerOptions.position((pl)!!)
                    markerOptions.title(finalPlaceName)
                    markerOptions.snippet(placeAddress)
                    mMap!!.addMarker(markerOptions)
                    check = 1
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            println("No Result")
            //Toast.makeText(this, "?????? ?????? ?????? ?????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
            if (check == 0) {
                val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER
                lp.setMargins(10, 100, 10, 10)
                val view1: TextView = TextView(this@PlaceListActivity)
                view1.setText("???????????? ?????????\n?????? ??????????????? ???????????? ?????????\n???????????? ????????????.\n\n????????? ???????????????!")
                view1.setTextSize(20f)
                view1.setTextColor(Color.BLACK)
                view1.setBackgroundColor(Color.WHITE)
                view1.setGravity(Gravity.CENTER)
                view1.setPadding(20, 50, 20, 20)
                view1.setLayoutParams(lp)
                //?????? ?????? ??????
                place_list_view!!.addView(view1)
                check = 100
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
            if (check == 0) {
                val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER
                lp.setMargins(10, 100, 10, 10)
                val view1: TextView = TextView(this@PlaceListActivity)
                view1.setText("???????????? ?????????\n?????? ??????????????? ???????????? ?????????\n???????????? ????????????.\n\n????????? ???????????????!")
                view1.setTextSize(20f)
                view1.setTextColor(Color.BLACK)
                view1.setBackgroundColor(Color.WHITE)
                view1.setGravity(Gravity.CENTER)
                view1.setPadding(20, 50, 20, 20)
                view1.setLayoutParams(lp)
                //?????? ?????? ??????
                place_list_view!!.addView(view1)
                check = 100
            }
        }
    }

    //?????????????????? ?????? ????????? ????????? ??????
    private fun checkList(location: LatLng?) {
        val docRef: DocumentReference = db.collection("vote").document((id)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // ?????? ????????? ???????????? ??????
                        val list: List<HashMap<String, Any>>? = document.get("place") as List<HashMap<String, Any>>?
                        for (i in list!!.indices) {
                            var hashMap: HashMap<String, Any> = HashMap()
                            hashMap = list.get(i)
                            val geo: GeoPoint? = hashMap.get("latlng") as GeoPoint?
                            val loc: LatLng = LatLng(geo!!.getLatitude(), geo.getLongitude())
                            if ((loc == location)) {
                                result = true // ?????????????????? ?????? ??????
                                Log.d("CheckList", "Success")
                            }
                        }
                    } else {
                        // ???????????? ?????? ??????
                        Log.d("CheckList", "No Document")
                    }
                } else {
                    Log.d("CheckList", "Task Fail : " + task.getException())
                }
            }
        })
    }
}