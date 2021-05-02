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
    var result: Boolean = false //투표리스트에 없음
    var best: HashMap<String, Int>? = null
    var spinner: Spinner? = null
    var arrayAdapter: ArrayAdapter<String>? = null
    var radiustv: TextView? = null
    var search_result: TextView? = null
    var seekBar: SeekBar? = null
    var currentCategory: String? = null
    var mHandler: Handler = object : Handler() {
        public override fun handleMessage(msg: Message) {
            val bd: Bundle = msg.getData() /// 전달 받은 메세지에서 번들을 받음
            if (bd.getStringArrayList("arg") != null) {
                val categoryList: ArrayList<String>? = bd.getStringArrayList("arg") /// 번들에 들어있는 값 꺼냄
                radius
                // Category 찾은 다음에 쓸 함수
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
                        .radius(radius.toDouble()) //반지름 단위 : m
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
            "쇼핑몰" -> {
                //shopping_mall + department_store
                Log.d(Tag, "Shopping")
                check = 0
                showPlaceInformation("shopping_mall", radius)
                showPlaceInformation("department_store", radius)
            }
            "액티비티" -> {
                // amusement_park + aquarium +art_gallery +stadium +zoo
                Log.d(Tag, "Activity")
                check = 0
                showPlaceInformation("amusement_park", radius)
                showPlaceInformation("aquarium", radius)
                showPlaceInformation("art_gallery", radius)
                showPlaceInformation("stadium", radius)
                showPlaceInformation("zoo", radius)
            }
            "카페" -> {
                Log.d(Tag, "Cafe")
                check = 0
                val beforeTime: Long = System.currentTimeMillis() //코드 실행 전에 시간 받아오기
                showPlaceInformation("cafe", radius)
                val afterTime: Long = System.currentTimeMillis() // 코드 실행 후에 시간 받아오기
                val secDiffTime: Long = (afterTime - beforeTime) //두 시간에 차 계산
                println("카페 시간(ms) : " + secDiffTime)
            }
            "식당" -> {
                Log.d(Tag, "restaurant")
                check = 0
                beforeTime = System.currentTimeMillis()
                showPlaceInformation("restaurant", radius)
                afterTime = System.currentTimeMillis() // 코드 실행 후에 시간 받아오기
                secDiffTime = (afterTime - beforeTime) //두 시간에 차 계산
                println("식당 시간(ms) : " + secDiffTime)
            }
            "공원" -> {
                Log.d(Tag, "park")
                check = 0
                showPlaceInformation("park", radius)
            }
        }
    }

    private fun sendPosition(position: Int) {
        val bd: Bundle = Bundle() /// 번들 생성
        bd.putInt("num", position)
        val msg: Message = mHandler.obtainMessage() /// 핸들에 전달할 메세지 구조체 받기
        msg.setData(bd) /// 메세지에 번들 넣기
        mHandler.handleMessage(msg)
    }

    //반지름 단위 : m
    private val radius: Unit
        private get() {
            val radius: IntArray = intArrayOf(0)
            seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                public override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    var progress: Int = progress
                    progress = progress / 500
                    progress = progress * 500
                    radiustv!!.setText("검색 범위 : " + progress + "m")
                    radius.get(0) = progress
                }

                public override fun onStartTrackingTouch(seekBar: SeekBar) {}
                public override fun onStopTrackingTouch(seekBar: SeekBar) {
                    layoutclear()
                    val circle: CircleOptions = CircleOptions().center(midpoint)
                            .radius(radius.get(0).toDouble()) //반지름 단위 : m
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
        marker.position(midpoint).title("중간지점")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap!!.addMarker(marker)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 15f))
        Log.d(Tag, "Clear")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //이전 엑티비티에서 중간지점 받아오기(일단 주석처리)
        val i: Intent = getIntent()
        midpoint = i.getParcelableExtra("midpoint")
        code = i.getStringExtra("code")
        scheduleId = i.getStringExtra("scheduleId")
        Log.d("Send", "meetingname 전달받기 : " + code)
        setContentView(R.layout.activity_place_list)
        val apiKey: String = getString(R.string.api_key)

//        previous_marker = new ArrayList<Marker>();
        place_list_view = findViewById(R.id.place_list_view)
        spinner = findViewById(R.id.categoryList)
        radiustv = findViewById(R.id.radiusText)
        seekBar = findViewById(R.id.radiusBar)
        seekBar.setProgress(500)
        radiustv.setText("검색 범위 : " + seekBar.getProgress() + "m")
        val mapFragment: SupportMapFragment? = getSupportFragmentManager()
                .findFragmentById(R.id.place_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        select()
        db.collection("vote").whereEqualTo("scheduleID", scheduleId).get()
                .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    public override fun onComplete(task: com.google.android.gms.tasks.Task<QuerySnapshot>) {
                        if (task.isSuccessful()) {
                            for (document: QueryDocumentSnapshot in task.getResult()) {
                                id = document.getId() // document 이름(id)
                                println("list 있음")
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
        //1. DB에서 별점 읽어오기 (meeting 에서 -> ui목록 접근 -> ui의 별점 읽기)
        meetingFind()
        // 다음 동작- 가중치
        val r: Runnable = object : Runnable {
            public override fun run() {
                addWeight()
                highest
                Log.d(Tag, "After Category: " + category)
                val bd: Bundle = Bundle() /// 번들 생성
                bd.putStringArrayList("arg", category) // 번들에 값 넣기
                val msg: Message = mHandler.obtainMessage() /// 핸들에 전달할 메세지 구조체 받기
                msg.setData(bd) /// 메세지에 번들 넣기
                mHandler.sendMessage(msg)
            }
        }
        mHandler.postDelayed(r, 3000) // 1초후
    }

    public override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marker: MarkerOptions = MarkerOptions()
        marker.position(midpoint).title("중간지점")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mMap!!.addMarker(marker)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(midpoint, 15f))
        val circle: CircleOptions = CircleOptions().center(midpoint)
                .radius(500.0) //반지름 단위 : m
                .strokeWidth(0f)
                .strokeColor(Color.parseColor("#40F28379"))
                .fillColor(Color.parseColor("#40F28379"))
        mMap!!.addCircle(circle)

        //mMap.clear();
    }

    fun getCurrentAddress(latlng: LatLng?): String {

        //지오코더... GPS를 주소로 변환
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                    latlng!!.latitude,
                    latlng.longitude,
                    1)
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }
        if (addresses == null || addresses.size == 0) {
            Log.d(Tag, "주소 미발견")
            //            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견"
        } else {
            val address: Address = addresses.get(0)
            return address.getAddressLine(0).toString()
        }
    }

    ////////////////////////
    //URL연결, JSON 받아오기///
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
                    Log.i("통신 결과", conn.getResponseCode().toString() + "에러")
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
        println("현재위치는 : " + str_origin)
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

    // 1-1. 미팅 이름으로 사용자 테이블 접근
    private fun meetingFind() {
        val docRef: DocumentReference = db.collection("meetings").document((code)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        // 찾은 모임의 사용자 테이블로
                        best = document.get("best") as HashMap<String, Int>?
                        val users: List<String>? = document.get("userID") as List<String>?
                        for (i in users!!.indices) {
                            userRating(users.get(i))
                        }
                    } else {
                        // 존재하지 않는 문서
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
    }

    // 1-2 사용자 별점 가져오기
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

    // 1-3 맵 -> 배열로 변경 (계산 편리, 카테고리 정렬, 출석 점수 반영)
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

        // 배열에 저장
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

    // 2. 가중치 계산
    private fun addWeight() {
        val variance: DoubleArray = DoubleArray(userRatings.size)
        for (i in userRatings.indices) {
            // 2-1. 분산 구하기
            variance.get(i) = calVariance(userRatings.get(i))
        }
        val temp: DoubleArray = variance.clone()
        Arrays.sort(temp) //오름차순
        Log.d(Tag, "original variance" + Arrays.toString(variance))
        Log.d(Tag, "sort variance" + Arrays.toString(temp))
        Log.d(Tag, "attend point" + attendPoint.toString())
        var n: Int = temp.size
        val m: Double
        if (n % 2 == 0) { //짝수
            n = n - 1
            m = (temp.get(n / 2) + temp.get((n / 2) + 1)) / 2
        } else {
            n = n - 1
            m = temp.get((n + 1) / 2)
        }

        //가중치 적용
        for (i in userRatings.indices) {
            Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(i)))

            // 호불호 업데이트
            if (variance.get(i) > m) { // 사용자 분산이 분산의 중앙값보다 크면
                // 사용자 별점 업데이트
                Log.d(Tag, "variance update user is " + i)
                userRatings.set(i, ratingUpdate(userRatings.get(i)))
            }
            Log.d(Tag, "~~ variance update " + Arrays.toString(userRatings.get(i)))

            // 출석점수 업데이트
            if (!attendPoint.isEmpty()) {
                val rating: Array<Float?> = userRatings.get(i)
                for (j in rating.indices) {
                    rating.get(j) = rating.get(j)!! * attendPoint.get(i)
                }
                userRatings.set(i, rating)
                Log.d(Tag, "~~ attend update (존재 유저)" + Arrays.toString(userRatings.get(i)))
            }
        }
    }

    // 2-1. 분산 계산
    private fun calVariance(rating: Array<Float>): Double {
        // 분산 = 편차 제곱의 합 / 변량의 수  => (편차 = 값- 평균)
        var avg: Double = 0.0

        // 1. 평균 구하기
        for (i in rating.indices) {
            avg += rating.get(i)
        }
        avg /= rating.size.toDouble()

        //2. 편차 제곱
        var variance: Double = 0.0
        for (i in rating.indices) {
            variance += (rating.get(i) - avg) * (rating.get(i) - avg)
        }
        variance /= rating.size.toDouble() // 3. 분산
        return variance
    }

    // 2-2. 가중치 설정
    private fun ratingUpdate(rating: Array<Float>): Array<Float> {
        var n: Int = rating.size
        val m: Double
        if (n % 2 == 0) { //짝수
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
    }// 가장 높은 3개 항목

    // 0=식당 , 1=카페, 2=공원, 3=쇼핑몰, 4=액티비티
// 사용자 평가 합// 카테고리마다//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(0)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(1)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(2)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(3)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(4)));
    // 3-2 최고값
    private val highest: Unit
        private get() {

//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(0)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(1)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(2)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(3)));
//        Log.d(Tag, "Start rating is update " + Arrays.toString(userRatings.get(4)));
            val avgRating: Array<Float> = arrayOfNulls(userRatings.get(0).length)
            for (i in 0 until userRatings.get(0).length) { // 카테고리마다
                var sum: Float? = 0f
                for (j in userRatings.indices) { // 사용자 평가 합
                    sum!! += (userRatings.get(j).get(i))!!
                }
                avgRating.get(i) = sum!! / userRatings.size
            }
            Log.d(Tag, "user average Rating Val " + Arrays.toString(avgRating))
            val index: ArrayList<Int> = ArrayList()
            for (i in 0..2) { // 가장 높은 3개 항목
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

            // 0=식당 , 1=카페, 2=공원, 3=쇼핑몰, 4=액티비티
            this.category.clear()
            for (i in index.indices) {
                when (index.get(i)) {
                    0 -> this.category.add("식당")
                    1 -> this.category.add("카페")
                    2 -> this.category.add("공원")
                    3 -> this.category.add("쇼핑몰")
                    4 -> this.category.add("액티비티")
                }
            }
        }

    /*
    순차적으로 실행하기 위해서 Runnable 객체를 Handler에 넘겨주었음!
    Handler가 메세지 큐에 있는걸 순서대로 실행
    순서: 카테고리 계산 -> showPlaceInfo -> placeSuccess -> sortRating -> showUI
     */
    // 별점 순서대로 정렬 -> 거리 점수 (0.4:0.6) 반영한 최종 점수 구함
    // 최종 점수대로 num개를 ArrayList에 저장 -> showUI 함수에 넘겨줌
    private fun showPlaceInformation(type: String, radius: Int) {

        //LatLng[] placeList;
        val ratingMap: HashMap<Int, Float> = HashMap()
        println("sortRating start!")
        var num: Int
        // 1위
        if (preferNum == 0) {
            num = 8
            // 2위
        } else if (preferNum == 1) {
            num = 5
            // 3위
        } else {
            num = 3
        }
        str_url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + midpoint.latitude + "," + midpoint.longitude +
                "&radius=" + radius + "&types=" + type + "&key=AIzaSyDZFlYs370FtbLuByL1cebdJdh8R-KF1xk&language=ko")
        placeInfo = getPlaceJson(midpoint.latitude, midpoint.longitude)
        println("placeInfo 출력: " + placeInfo)
        val resultArray: JSONArray
        try {
            println("try들어옴")
            val jsonObject: JSONObject = JSONObject(placeInfo)
            println("장소정보 JSON : " + placeInfo)
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
                println("장소 : " + location)
                val loc_lat: Float = location.getString("lat").toFloat()
                val loc_lng: Float = location.getString("lng").toFloat()

                //장소위치
                val placeloc: LatLng = LatLng(loc_lat.toDouble(), loc_lng.toDouble())
                placeList.get(i) = placeloc
                println("장소위치 출력 :" + placeloc + "lat : " + loc_lat + "lng : " + loc_lng)
                place_name.get(i) = resultObject.getString("name")
                println("장소 이름: " + place_name)

                //주소
                //String placeAddress = getCurrentAddress(placeloc);
                if (resultObject.has("rating")) {
                    rating.get(i) = resultObject.getString("rating").toFloat()
                } else {
                    rating.get(i) = 0.0f
                }
                ratingMap.put(i, rating.get(i))
                println("별점: " + rating.get(i))
                i++
            } while (i < resultSize)

            // Sort 할 때 인덱스만 가져오려고 array 생성
            // ratingList는 ratingMap의 key값만 가지고 있음
            // Sort 후 ratingList가 (5,4,2,1,0)이면 placeList.get(해당 인덱스) -> 순위대로 place 가져옴
            val ratingList: ArrayList<Int>? = ArrayList(ratingMap.keys)
            for (j in placeList.indices) {
                val latitude: Double = placeList.get(j)!!.latitude - midpoint.latitude
                val longitude: Double = placeList.get(j)!!.longitude - midpoint.longitude

                // 0~1사이에 분포, 0에 가까울 수록 중간 지점과 가깝다
                var euclidean: Double = Math.sqrt(Math.pow(latitude, 2.0) + Math.pow(longitude, 2.0))
                // 0~5점 사이에 분포한 선호도 값과 비슷하게 분포하도록 역수+로그를 이용해서 변환 -> 점수 높을수록 가까움
                // 0~3.5점 사이에 분포
                euclidean = Math.log10(1.0 / euclidean)

                // double형인 점수를 float으로 변환
                val distancePoint: Float = java.lang.Float.valueOf(euclidean.toString())
                //System.out.println(distancePoint);
                val rat: Float = ratingMap.get(j)!!.toFloat()
                if (radius == 500) {
                    ratingMap.put(j, rat)
                } else if (500 < radius && radius <= 1000) {
                    // rating 점수 + 거리 점수의 가중치를 [0.1:0.3]으로 환산한 최종 점수
                    ratingMap.put(j, (0.1 * rat + 0.1 * distancePoint).toFloat())
                } else if (1000 < radius && radius <= 2000) {
                    ratingMap.put(j, (0.2 * rat + 0.1 * distancePoint).toFloat())
                } else {
                    ratingMap.put(j, (0.3 * rat + 0.1 * distancePoint).toFloat())
                }
                println(j.toString() + "번 최종 점수" + ratingMap.get(j))
            }

            // 최종 점수로 내림차순 정렬
            // ratingMap의 value인 최종 점수를 기준으로 정렬, ratingMap의 key값이 ratingList의 값
            // ratingList = {5,4,2,1,0}
            Collections.sort(ratingList, Comparator({ o1: Int, o2: Int -> (ratingMap.get(o2)!!.compareTo((ratingMap.get(o1))!!)) }))
            println("ratingList: " + ratingList)
            if (ratingList!!.size == 0 || ratingList == null) {
                val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER
                lp.setMargins(10, 100, 10, 10)
                val view1: TextView = TextView(this@PlaceListActivity)
                view1.setText("중간지점 근처에\n현재 카테고리에 해당하는 장소가\n존재하지 않습니다.\n\n범위를 늘려보세요!")
                view1.setTextSize(20f)
                view1.setTextColor(Color.BLACK)
                view1.setBackgroundColor(Color.WHITE)
                view1.setGravity(Gravity.CENTER)
                view1.setPadding(20, 50, 20, 20)
                view1.setLayoutParams(lp)
                //부모 뷰에 추가
                place_list_view!!.addView(view1)
                println("0일때 " + ratingList)
            } else {
                // 지금은 num을 임시로 5개로 했는데, 5개 보다 검색 결과가 작으면 인덱스 에러 때문에 다시 세팅
                if (check == 100) {
                    place_list_view!!.removeAllViews()
                }
                println("0아닐때 " + ratingList)
                if (ratingList.size < num) num = ratingList.size

                // 상위 num개 만큼 다시 placeList에 저장
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
                    //장소 이름, 주소 출력부분
                    val pInfo: TextView = TextView(this@PlaceListActivity)
                    val s: SpannableString = SpannableString(place_name.get(ratingList.get(j)).toString() + "\n" + placeAddress)
                    s.setSpan(RelativeSizeSpan(1.8f), 0, place_name.get(ratingList.get(j)).length, 0)
                    s.setSpan(ForegroundColorSpan(Color.BLACK), 0, place_name.get(ratingList.get(j)).length, 0)
                    pInfo.setText(s)
                    pInfo.setLayoutParams(rl_param)
                    pl_name.addView(pInfo)

                    //삭제버튼
                    val delete: Button = Button(this@PlaceListActivity)
                    val btn_param2: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(90, 90)
                    btn_param2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
                    btn_param2.setMargins(0, 0, 20, 0)
                    delete.setLayoutParams(btn_param2)
                    delete.setPadding(0, 20, 5, 0)
                    delete.setId(2 * (i + 1))
                    delete.setBackground(ContextCompat.getDrawable(this@PlaceListActivity, R.drawable.delete))
                    pl_name.addView(delete)

                    //추가버튼
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

                    //LinearLayout 생성
                    val ly: LinearLayout = LinearLayout(this@PlaceListActivity)
                    //LinearLayout.LayoutParams lyparams = param;
                    ly.setLayoutParams(param)
                    ly.setOrientation(LinearLayout.HORIZONTAL)
                    val rate_tv: TextView = TextView(this@PlaceListActivity)
                    rate_tv.setText("별점 : " + rating.get(ratingList.get(j)) + " | ")
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
                    //투표추가 버튼
                    add.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            checkList(pl) // 투표리스트에 존재하는지 확인
                            val docRef: DocumentReference = db.collection("vote").document((id)!!)
                            val r: Runnable = object : Runnable {
                                public override fun run() {
                                    if ((state == "valid")) {
                                        if (result) {
                                            result = false
                                            Toast.makeText(this@PlaceListActivity, "이미 추가된 장소입니다.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val map: HashMap<String, Any?> = HashMap()
                                            val location: GeoPoint = GeoPoint(pl!!.latitude, pl.longitude)
                                            val voter: List<String> = ArrayList()
                                            map.put("latlng", location)
                                            map.put("vote", 0)
                                            map.put("name", finalPlaceName)
                                            map.put("voter", voter)
                                            if (size >= 5) { // 리스트에 5개 이상 존재할 때
                                                Toast.makeText(this@PlaceListActivity, "더이상 투표리스트에 추가할 수 없습니다.", Toast.LENGTH_SHORT).show()
                                            } else {
                                                db.collection("vote").document((id)!!).update("place", FieldValue.arrayUnion(map))
                                                Toast.makeText(this@PlaceListActivity, "투표리스트에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    } else {
                                        Toast.makeText(this@PlaceListActivity, "이미 투표가 시작되었습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            delayHandler.postDelayed(r, 1000) // 1초후
                            docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                                public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                                    if (task.isSuccessful()) {
                                        val document: DocumentSnapshot = task.getResult()
                                        if (document.exists()) {
                                            // 해당 문서가 존재하는 경우
                                            val list: List<HashMap<String, Any>>? = document.get("place") as List<HashMap<String, Any>>?
                                            size = list!!.size
                                            state = document.getData()!!.get("state").toString() // 투표 상태
                                            delayHandler.sendEmptyMessage(0)
                                            Log.d("Attend", "Find document")
                                        } else {
                                            // 존재하지 않는 문서
                                            Log.d("Attend", "No Document")
                                        }
                                    } else {
                                        Log.d("Attend", "Task Fail : " + task.getException())
                                    }
                                }
                            })
                        }
                    })
                    //투표삭제 버튼
                    delete.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(v: View) {
                            checkList(pl) // 투표리스트에 존재하는지 확인
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
                                            Toast.makeText(this@PlaceListActivity, "투표리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@PlaceListActivity, "투표리스트에 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(this@PlaceListActivity, "이미 투표가 시작되었습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            delayHandler.postDelayed(r, 1000) // 1초후
                            docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                                public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                                    if (task.isSuccessful()) {
                                        val document: DocumentSnapshot = task.getResult()
                                        if (document.exists()) {
                                            // 해당 문서가 존재하는 경우
                                            val list: List<HashMap<String, Any>>? = document.get("place") as List<HashMap<String, Any>>?
                                            size = list!!.size
                                            state = document.getData()!!.get("state").toString() // 투표 상태
                                            delayHandler.sendEmptyMessage(0)
                                            Log.d("Attend", "Find document")
                                        } else {
                                            // 존재하지 않는 문서
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
            //Toast.makeText(this, "현재 범위 내에 검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
            if (check == 0) {
                val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.gravity = Gravity.CENTER
                lp.setMargins(10, 100, 10, 10)
                val view1: TextView = TextView(this@PlaceListActivity)
                view1.setText("중간지점 근처에\n현재 카테고리에 해당하는 장소가\n존재하지 않습니다.\n\n범위를 늘려보세요!")
                view1.setTextSize(20f)
                view1.setTextColor(Color.BLACK)
                view1.setBackgroundColor(Color.WHITE)
                view1.setGravity(Gravity.CENTER)
                view1.setPadding(20, 50, 20, 20)
                view1.setLayoutParams(lp)
                //부모 뷰에 추가
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
                view1.setText("중간지점 근처에\n현재 카테고리에 해당하는 장소가\n존재하지 않습니다.\n\n범위를 늘려보세요!")
                view1.setTextSize(20f)
                view1.setTextColor(Color.BLACK)
                view1.setBackgroundColor(Color.WHITE)
                view1.setGravity(Gravity.CENTER)
                view1.setPadding(20, 50, 20, 20)
                view1.setLayoutParams(lp)
                //부모 뷰에 추가
                place_list_view!!.addView(view1)
                check = 100
            }
        }
    }

    //투표리스트에 해당 장소가 있는지 확인
    private fun checkList(location: LatLng?) {
        val docRef: DocumentReference = db.collection("vote").document((id)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        val list: List<HashMap<String, Any>>? = document.get("place") as List<HashMap<String, Any>>?
                        for (i in list!!.indices) {
                            var hashMap: HashMap<String, Any> = HashMap()
                            hashMap = list.get(i)
                            val geo: GeoPoint? = hashMap.get("latlng") as GeoPoint?
                            val loc: LatLng = LatLng(geo!!.getLatitude(), geo.getLongitude())
                            if ((loc == location)) {
                                result = true // 투표리스트에 이미 있음
                                Log.d("CheckList", "Success")
                            }
                        }
                    } else {
                        // 존재하지 않는 문서
                        Log.d("CheckList", "No Document")
                    }
                } else {
                    Log.d("CheckList", "Task Fail : " + task.getException())
                }
            }
        })
    }
}