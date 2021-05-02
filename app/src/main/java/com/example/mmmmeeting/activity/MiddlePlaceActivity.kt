// 중간지점 화면
package com.example.mmmmeeting.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
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
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.bumptech.glide.Glide
import com.example.mmmmeeting.Info.ScheduleInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.MiddlePlaceActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.SphericalUtil
import noman.googleplaces.*
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

class MiddlePlaceActivity constructor() : AppCompatActivity(), OnMapReadyCallback, PlacesListener {
    private var scheduleInfo: ScheduleInfo? = null
    private var scheduleId: String? = null
    var code: String? = null

    //private ArrayList<LatLng> position;
    private var userTime: DoubleArray

    //##
    private var midP: LatLng? = null
    private var countTry: Int = 0
    private var midpoint_select: LinearLayout? = null
    private var duration: String? = null
    private var Dur: Double = 0.0
    private var str_url: String? = null
    private var midAdr: String? = null
    private var latVector: Double = 0.0
    private var lonVector: Double = 0.0
    private var avgTime: Int = 0
    private val avgDist: Double = 0.0
    private var flag: Boolean = false
    private var findSub_flg: Boolean = false
    private var flagCount: Int = 0
    private var users: Array<Point?>
    private var member_num: ArrayList<Int>? = null
    private var centers: ArrayList<Point?>? = null
    private var latitude: Double = 0.0
    private var longtitude: Double = 0.0
    private var mid // 현재 중간지점
            : LatLng? = null
    private var tem // 이전 중간지점
            : LatLng? = null
    private var temp: Int = 1000
    private var address: Array<String?>
    private var m_name: Array<String?>
    private var sub_name: String? = null
    private var mMap: GoogleMap? = null
    var progressDialog: AppCompatDialog? = null
    var i: Int = 0
    var j: Int = 0
    var previous_marker: MutableList<Marker>? = null
    var radius: Int = 1000
    var mHandler: Handler = object : Handler() {
        public override fun handleMessage(msg: Message) {
            val bd: Bundle = msg.getData() /// 전달 받은 메세지에서 번들을 받음
            if (bd.getString("flag") != null) {
                val flagStr: String? = bd.getString("flag")
                // placessuccess가 2번 이상인 상황: flag 설정해서 동작하지 못하게 함
                if (flagStr === "NO") {
                    flag = true
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_middle)
        progressDialog = AppCompatDialog(this@MiddlePlaceActivity)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setContentView(R.layout.progress_loading)
        val img_loading_frame: ImageView? = progressDialog!!.findViewById<View>(R.id.iv_frame_loading) as ImageView?
        Glide.with(getApplicationContext()).load(R.drawable.wemeetnow).into((img_loading_frame)!!)
        midpoint_select = findViewById<View>(R.id.midpoint_select) as LinearLayout?
        val mapFragment: SupportMapFragment? = getSupportFragmentManager()
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    public override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //시작했을때 지도화면 서울로 보이게 지정
        val SEOUL: LatLng = LatLng(37.56, 126.97)
        val marker: MarkerOptions = MarkerOptions()
        marker.position(SEOUL)
        marker.visible(false)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 8f))
        mMap!!.clear()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        scheduleInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo?
        code = scheduleInfo.getMeetingID()
        scheduleId = scheduleInfo.getId()
        db.collection("meetings").document((code)!!)
                .get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                    public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                        if (task.isSuccessful()) {
                            val document: DocumentSnapshot = task.getResult()
                            if (document.exists()) {
                                // 해당 문서가 존재하는 경우
                                // document에서 이름이 userID인 필드의 데이터 얻어옴
                                val users: List<*>? = document.getData()!!.get("userID") as List<*>?
                                val addr: Array<String?> = arrayOfNulls(users!!.size)
                                val name: Array<String?> = arrayOfNulls(users.size)
                                // userID가 동일한 user 문서에서 이름, 주소 읽어오기
                                for (m in users.indices) {
                                    val docRef: DocumentReference = db.collection("users").document(users.get(m).toString())
                                    docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
                                        public override fun onComplete(task: com.google.android.gms.tasks.Task<DocumentSnapshot>) {
                                            if (task.isSuccessful()) {
                                                val document: DocumentSnapshot = task.getResult()
                                                if (document.exists()) {
                                                    addr.get(i) = document.getData()!!.get("address").toString()
                                                    name.get(i) = document.getData()!!.get("name").toString()
                                                    i++
                                                } else {
                                                    // 존재하지 않는 문서
                                                    Log.d("Attend", "No Document")
                                                }
                                                if (i == users.size) {
                                                    //clustering(addr, name); // 중간지점 찾기 시작
                                                    address = addr
                                                    m_name = name
                                                    val Btask: BackgroundTask = BackgroundTask()
                                                    Btask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                                                }
                                            } else {
                                                Log.d("Attend", "Task Fail : " + task.getException())
                                            }
                                        }
                                    })
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

    internal inner class BackgroundTask constructor() : AsyncTask<String?, Void?, String>() {
        override fun onPreExecute() {
            progressDialog!!.show()
            super.onPreExecute()
        }

        protected override fun doInBackground(vararg strings: String): String {
            val abc: String = "clustering OK!!!"
            clustering(address, m_name) // 중간지점 찾기 시작
            runOnUiThread(object : Runnable {
                public override fun run() {
                    val bitmapdraw1: BitmapDrawable = getResources().getDrawable(R.drawable.profile2) as BitmapDrawable
                    val b: Bitmap = bitmapdraw1.getBitmap()
                    val UserMarker: Bitmap = Bitmap.createScaledBitmap(b, 120, 120, false)
                    for (k in address.indices) {
                        mMap!!.addMarker(MarkerOptions().position(LatLng(users.get(k).getX(), users.get(k).getY())).title(m_name.get(k)).icon(BitmapDescriptorFactory.fromBitmap(UserMarker)))
                    }
                    midAdr = getCurrentAddress(midP)
                    //##
                    //String midAdr = "서울특별시 상계8동 동일로 1545";
                    findSub(midP)
                    val bitmapdraw3: BitmapDrawable = getResources().getDrawable(R.drawable.middleplace) as BitmapDrawable
                    val bit: Bitmap = bitmapdraw3.getBitmap()
                    val MidMarker: Bitmap = Bitmap.createScaledBitmap(bit, 100, 100, false)
                    val markerOptions: MarkerOptions = MarkerOptions()
                    markerOptions.position((midP)!!)
                    markerOptions.title("중간지점")
                    markerOptions.snippet(midAdr).icon(BitmapDescriptorFactory.fromBitmap(MidMarker))
                    mMap!!.addMarker(markerOptions)
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(midP, 11f))


                    //LinearLayout 정의
                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    //LinearLayout 정의
                    val rl_params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    rl_params.setMargins(10, 0, 10, 0)

                    //LinearLayout 생성
                    val ly: RelativeLayout = RelativeLayout(this@MiddlePlaceActivity)
                    ly.setLayoutParams(rl_params)
                    //ly.setOrientation(LinearLayout.HORIZONTAL);
                    val tv_mid: TextView = TextView(this@MiddlePlaceActivity)
                    val id: Int = 1
                    tv_mid.setId(id)
                    tv_mid.setText("중간지점 주소 : " + midAdr)
                    tv_mid.setTypeface(null, Typeface.BOLD)
                    tv_mid.setTextSize(16f)
                    tv_mid.setLayoutParams(rl_params)
                    ly.addView(tv_mid)
                    val width: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, getResources().getDisplayMetrics()).toInt()
                    val height: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics()).toInt()
                    val btn_mid: Button = Button(this@MiddlePlaceActivity)
                    btn_mid.setText("선택")
                    val btn_params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(width, height)
                    btn_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
                    //btn_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
                    btn_params.addRule(RelativeLayout.BELOW, tv_mid.getId())
                    btn_params.setMargins(0, 10, 30, 0)
                    btn_mid.setLayoutParams(btn_params)
                    btn_mid.setBackground(getDrawable(R.drawable.button_shape))
                    btn_mid.setTextColor(Color.WHITE)
                    ly.addView(btn_mid)
                    midpoint_select!!.addView(ly)
                    midpoint_select!!.setVisibility(View.VISIBLE)
                    btn_mid.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(view: View) {
                            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MiddlePlaceActivity)
                            //제목
                            alertDialogBuilder.setTitle("중간지점 선택")

                            //AlertDialog 세팅
                            if (sub_name == null) {
                                sub_name = "없습니다."
                                val s: SpannableString = SpannableString("가장 가까운 역이 " + sub_name + "\n이 곳을 중간지점으로 선택하시겠습니까?\n" + midAdr)
                                val i: Int = 33 + sub_name!!.length
                                s.setSpan(RelativeSizeSpan(0.7f), i, i + midAdr!!.length, 0)
                                s.setSpan(ForegroundColorSpan(Color.parseColor("#62ABD9")), i, i + midAdr!!.length, 0)
                                alertDialogBuilder.setMessage(s)
                                        .setCancelable(false)
                                        .setPositiveButton("아니오", object : DialogInterface.OnClickListener {
                                            public override fun onClick(dialogInterface: DialogInterface, i: Int) {
                                                //프로그램 종료
                                                finish()
                                            }
                                        }).setNegativeButton("네", object : DialogInterface.OnClickListener {
                                            public override fun onClick(dialogInterface: DialogInterface, i: Int) {

                                                //장소리스트 화면으로 넘어감
                                                val intent: Intent = Intent(this@MiddlePlaceActivity, PlaceListActivity::class.java)
                                                val bundle: Bundle = Bundle()
                                                bundle.putParcelable("midpoint", midP)
                                                bundle.putString("code", code)
                                                bundle.putString("scheduleId", scheduleId)
                                                intent.putExtras(bundle)
                                                Log.d("Send", "meetingname 전달 : " + code)
                                                startActivity(intent)
                                            }
                                        })
                            } else {
                                sub_name += "역 입니다."
                                val s: SpannableString = SpannableString("가장 가까운 역은 " + sub_name + "\n이 곳을 중간지점으로 선택하시겠습니까?\n")
                                alertDialogBuilder.setMessage(s)
                                        .setCancelable(false)
                                        .setPositiveButton("아니오", object : DialogInterface.OnClickListener {
                                            public override fun onClick(dialogInterface: DialogInterface, i: Int) {
                                                //프로그램 종료
                                                finish()
                                            }
                                        }).setNegativeButton("네", object : DialogInterface.OnClickListener {
                                            public override fun onClick(dialogInterface: DialogInterface, i: Int) {

                                                //장소리스트 화면으로 넘어감
                                                val intent: Intent = Intent(this@MiddlePlaceActivity, PlaceListActivity::class.java)
                                                val bundle: Bundle = Bundle()
                                                bundle.putParcelable("midpoint", midP)
                                                bundle.putString("code", code)
                                                bundle.putString("scheduleId", scheduleId)
                                                intent.putExtras(bundle)
                                                Log.d("Send", "meetingname 전달 : " + code)
                                                startActivity(intent)
                                            }
                                        })
                            }
                            //다이얼로그 생성
                            val alertDialog: AlertDialog = alertDialogBuilder.create()

                            //다이얼로그 보여주기
                            alertDialog.show()
                        }
                    })
                }
            })
            return abc
        }

        override fun onPostExecute(s: String) {
            progressDialog!!.dismiss()
            super.onPostExecute(s)
            Toast.makeText(this@MiddlePlaceActivity, "로딩 완료", Toast.LENGTH_SHORT).show()
        }
    }

    // 중간 지점 찾기 시작!
    private fun clustering(addr: Array<String?>, name: Array<String?>) {
        val user_map: HashMap<String, ArrayList<String?>> = HashMap() //{"도","주소"}
        var q: Int = 0
        for (str: String? in addr) {

            //getPointFromGeoCoder(str);
            Log.d("Clustering", "addr : " + addr.get(q))
            val area: Array<String> = addr.get(q)!!.split(" ").toTypedArray()
            var do_name: String = area.get(0)
            when (do_name) {
                "서울특별시", "인천광역시" -> do_name = "경기도"
                "대전광역시", "세종특별자치시" -> do_name = "충청남도"
                "대구광역시" -> do_name = "전라북도"
                "울산광역시", "부산광역시" -> do_name = "경상남도"
                "광주광역시" -> do_name = "전라남도"
                else -> do_name = area.get(0)
            }
            Log.d("Clustering", "구역 : " + addr.get(q))
            if (user_map.containsKey(do_name)) {
                /*
                 * 이미 키가 존재할 경우
                 * ArrayList를 기존의 Points로 초기화하고 새 Points 추가함
                 */
                val user_point: ArrayList<String?> = (user_map.get(do_name))!!
                user_point.add(addr.get(q))
                user_map.put(do_name, user_point)
            } else {
                /*
                 * 키가 존재하지 않을 경우
                 * ArrayList를 초기화하고 Points추가
                 */
                val user_point: ArrayList<String?> = ArrayList()
                user_point.add(addr.get(q))
                user_map.put(do_name, user_point)
            }
            Log.d("Clustering", " 클러스터링중 : " + user_map)
            q++
        }
        users = arrayOfNulls(addr.size)
        for (i in addr.indices) {
            users.get(i) = getPointFromGeoCoder(addr.get(i))
        }


        //클러스터링된 맵을 반복문을 돌면서 centroid와 size를 저장한다.
        centers = ArrayList()
        member_num = ArrayList()
        var points: Array<Point?> //주소좌표 담을 공간


        /*클러스터링 결과 맵의 size가
         *  1. 1인 경우 : 모든 유저가 같은 구역에 있음(Ex)모두 경기도, 서울특별시, 충청도 등)
         *  2. 모임원들 수와 같은 경우 : 모든 유저가 다른 구역에 흩어져있음
         *
         * => 이럴 경우엔 바로 graham을 사용해서 무게중심 구하고 중간지점 구하기
         */if (user_map.size == 1 || user_map.size == addr.size) {
            //int k=0;
            points = arrayOfNulls(addr.size)
            for (i in points.indices) {
                points.get(i) = getPointFromGeoCoder(addr.get(i))
                Log.d("Clustering", "points : " + points.get(i))
            }
            val k_result: ArrayList<LatLng?>? = kmeans(2, points)
            Log.d("Clustering", "k_result 크기 : " + k_result!!.size)
            for (j in k_result.indices) {
                centers!!.add(Point(k_result.get(j)!!.latitude, k_result.get(j)!!.longitude))
            }
            for (elem: Map.Entry<Int, ArrayList<Point?>> in cmap!!.entries) {
                //System.out.println( String.format("키 -> %s, 값 -> %s", elem.getKey(), elem.getValue()) );
                val p: ArrayList<Point?> = elem.value
                member_num!!.add(p.size)
            }
            Log.d("Clustering", "센터 크기  : " + centers!!.size)
            Log.d("Clustering", "membernum 크기  : " + member_num!!.size)
            for (i in centers!!.indices) {
                Log.d("Clustering", "center출력  : " + centers!!.get(i))
                // 인원수에 비례하여 평균점 계산
                latitude += centers!!.get(i)!!.x * member_num!!.get(i)
                longtitude += centers!!.get(i)!!.y * member_num!!.get(i)
            }
            latitude /= addr.size.toDouble()
            longtitude /= addr.size.toDouble()
            mid = LatLng(latitude, longtitude) // 현재 중간지점
            Log.d("Clustering", "현재 중간지점 : " + mid)
            tem = mid // 이전 중간지점
            FindMid()
        } //군집이 존재할 경우
        else {
            Log.d("Clustering", " 군집이 존재합니다.")
            for (elem: Map.Entry<String, ArrayList<String?>> in user_map.entries) {
                //System.out.println( String.format("키 -> %s, 값 -> %s", elem.getKey(), elem.getValue()) );
                val p: ArrayList<String?> = elem.value
                points = arrayOfNulls(p.size)
                //Point[] point = p.toArray(new Point[p.size()]);
                Log.d("Clustering", " map의 key,value : " + elem.key + "," + elem.value)
                for (i in p.indices) {
                    points.get(i) = getPointFromGeoCoder(p.get(i))
                    Log.d("Clustering", "포인트가 존재한다 : " + points.get(i))
                }
                var hull: Array<Point?>? = arrayOfNulls(points.size)
                Log.d("Clustering", "포인트의 길이는 : " + points.size)
                if (points.size > 2) {
                    hull = GrahamScan.convexHull(points)
                    centers!!.add(PolygonCenter(hull))
                } else {
                    centers!!.add(PolygonCenter(points))
                }
                member_num!!.add(p.size)
                //curPoint = new LatLng(center.x, center.y);
            }
            latitude = 0.0
            longtitude = 0.0
            Log.d("Clustering", "센터 크기  : " + centers!!.size)
            Log.d("Clustering", "membernum 크기  : " + member_num!!.size)
            for (i in centers!!.indices) {
                // mMap.addMarker(new MarkerOptions().position(new LatLng(centers.get(i).x, centers.get(i).y)).title("centroid " + i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                Log.d("Clustering", "center출력  : " + centers!!.get(i))
                // 인원수에 비례하여 평균점 계산
                latitude += centers!!.get(i)!!.x * member_num!!.get(i)
                longtitude += centers!!.get(i)!!.y * member_num!!.get(i)
            }
            latitude /= addr.size.toDouble()
            longtitude /= addr.size.toDouble()
            mid = LatLng(latitude, longtitude) // 현재 중간지점
            Log.d("Clustering", "현재 중간지점 : " + mid)
            tem = mid // 이전 중간지점
            FindMid()
            Log.d("Clustering", "center_p(클러스터의 중심들) : " + centers)
        }
    }

    // 지오코딩(주소->좌표)
    private fun getPointFromGeoCoder(addr: String?): Point {
        val geocoder: Geocoder = Geocoder(this)
        var listAddress: List<Address>? = null
        try {
            listAddress = geocoder.getFromLocationName(addr, 10)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (listAddress!!.isEmpty()) {
            println("주소가 없습니다.")
        }
        return Point(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude())
    }

    private var pt: Point? = null

    // 무게중심 구하기
    private fun PolygonCenter(hull: Array<Point?>?): Point? {
        var area: Double = 0.0
        var factor: Double = 0.0
        var clat: Double = 0.0
        var clng: Double = 0.0
        if (hull!!.size == 1) {
            pt = hull.get(0)
        } else if (hull.size == 2) {
            clat = (hull.get(0)!!.x + hull.get(1)!!.x) / 2.0
            clng = (hull.get(0)!!.y + hull.get(1)!!.y) / 2.0
            pt = Point(clat, clng)
        } else {
            var j: Int = 0
            for (i in hull.indices) {
                j = (i + 1) % hull.size
                factor = (hull.get(i)!!.x * hull.get(j)!!.y) - (hull.get(j)!!.x * hull.get(i)!!.y)
                area += factor
                clat += (hull.get(i)!!.x + hull.get(j)!!.x) * factor
                clng += (hull.get(i)!!.y + hull.get(j)!!.y) * factor
            }
            area *= 0.5
            clat = clat / (area * 6)
            clng = clng / (area * 6)
            pt = Point(clat, clng)
        }
        return pt
    }

    //중간지점 찾기
    fun FindMid() {
        Log.d("Clustering", " 처음 좌표: " + mid)
        //이동시간 저장할 공간
        userTime = DoubleArray(centers!!.size)
        latVector = 0.0
        lonVector = 0.0
        avgTime = 0 // 현재 평균 이동시간

        //유저들 이동시간 받아오기
        for (i in centers!!.indices) {
            //이동시간 받기
            val time: Double = getPathTime(mid, LatLng(centers!!.get(i)!!.x, centers!!.get(i)!!.y))
            //이동시간 저장
            userTime.get(i) = time
            Log.d("Clustering", "현재 position부터 중간점까지의 이동시간 : " + time)

            // 총 이동시간의 합
            avgTime += (time * member_num!!.get(i)).toInt()
        }

        //이동시간의 평균
        avgTime /= centers!!.size
        Log.d("Clustering", " 평균 시간 : " + avgTime)
        for (i in centers!!.indices) {
            //중간지점부터 사용자 위치까지의 단위벡터 구하기
            val unitVector: Point = getUnitVector(mid, LatLng(centers!!.get(i)!!.x, centers!!.get(i)!!.y))
            val t: Double = (userTime.get(i) * member_num!!.get(i)) - avgTime
            Log.d("Clustering", i.toString() + " 가중치 시간 : " + t)

            //시간가중치와 단위벡터의 곱
            latVector += unitVector.getX() * t
            lonVector += unitVector.getY() * t
        }
        if (avgTime < 250) {
            latVector /= (avgTime * centers!!.size * 6).toDouble()
            lonVector /= (avgTime * centers!!.size * 6).toDouble()
        } else {
            //가중치와 단위벡터의 곱의 합을 클러스터 수로 나눈다.
            latVector /= (avgTime * centers!!.size).toDouble()
            lonVector /= (avgTime * centers!!.size).toDouble()
        }
        Log.d("Clustering", " 시간벡터 : " + latVector + "," + lonVector)

        //최적의 경로인지 확인하기 위함
        var isOptimized: Boolean = false

        //새로운 점이 최적인가?
        for (i in userTime.indices) {
            //임의로 최적의 경로 확인
            Log.d("Clustering", i.toString() + "차이 : " + Math.abs(userTime.get(i) * member_num!!.get(i) - avgTime))
        }
        // 현재 평균시간이 이전 평균시간보다 크거나 같으면 그 전이 최적!
        if (temp <= avgTime) {
            isOptimized = true
        }
        //최적이라면 => 중간지점 출력(midPoint)
        if (isOptimized == true) {
            midP = tem
        }
        val check: Boolean = true
        //최적이 아니라면, 새로운 위치로 바꾸기
        if (isOptimized == false) {
            tem = mid
            temp = avgTime
            mid = LatLng(mid!!.latitude + latVector,
                    mid!!.longitude + lonVector)
            countTry++
            Log.d("Clustering", "중간지점 count : " + countTry)
            Log.d("Clustering", "중간지점은 여기! : " + tem)
            for (i in userTime.indices) {
                Log.d("Clustering", "사용자들로부터 중간지점까지의 이동시간은 : " + userTime.get(i))
            }
            // 최대 3번 FindMid 실행
            if (countTry < 4) {
                FindMid()
            } else {
                midP = tem
            }
        }
        println("update Check : " + check)
    }

    //kmeans클러스터링
    private var cp: ArrayList<LatLng?>? = null
    private var prevcenter: ArrayList<LatLng?>? = null
    private var cmap: MutableMap<Int, ArrayList<Point?>>? = null
    fun kmeans(k: Int, pos: Array<Point?>): ArrayList<LatLng?>? {
        Log.d("Clustering", " 클러스터개수는 " + k + "개입니다.")
        //클러스터만큼의 센트로이드 랜덤생성
        cp = ArrayList()
        setCentroids(k, pos)
        var iter_i: Int = 0
        do {
            Log.d("Clustering", "--------------" + iter_i + "번째" + "--------------")
            Log.d("Clustering", "----<<assignment>>----")
            assignment(pos)
            iter_i++
        } while (Stop())
        return prevcenter
    }

    fun setCentroids(k_num: Int, pos: Array<Point?>) {
        Log.d("Clustering", " 센트로이드 초기화")

        //center.clear();
        cp = ArrayList()
        val result: IntArray = IntArray(k_num) //k개만큼의 난수를 담을 배열
        val result_point: Array<LatLng?> = arrayOfNulls(k_num)
        run({
            var i: Int = 0
            while (i < k_num) {
                val rValue: Int = (Math.random() * pos.size).toInt()
                result.get(i) = rValue
                for (j in 0 until i) {
                    if (result.get(i) == result.get(j)) {
                        i--
                        break
                    } // i번째 난수가 지금까지 도출된 난수와 비교하여 중복이라면 i번째 난수를 다시 출력하도록 i--
                }
                Log.d("Clustering", "랜덤 centroid: " + users.get(rValue)!!.x + "," + users.get(rValue)!!.y)
                result_point.get(i) = LatLng(users.get(rValue)!!.x, users.get(rValue)!!.y)
                i++
            }
        })
        for (j in result_point.indices) {
            cp!!.add(result_point.get(j))
        }
        Log.d("Clustering", "랜덤 centroid 개수 : " + k_num + "/" + cp!!.size)
        prevcenter = cp
    }

    fun assignment(pos: Array<Point?>) {
        cmap = HashMap()
        for (i in pos.indices) {
            var nearest: Double = 0.0
            var nearest_num: Int = 0
            for (j in cp!!.indices) {
                Log.d("Clustering", "cp출력 :" + cp!!.get(j))
                val pos_list: ArrayList<LatLng> = changeToList(pos)
                val dist: Double = SphericalUtil.computeDistanceBetween(pos_list.get(i), cp!!.get(j))
                if (j == 0) {
                    nearest = dist
                    nearest_num = 0
                } else {
                    if (nearest - dist > 0) {
                        nearest = dist
                        nearest_num = j
                    }
                }
                Log.d("Clustering", i.toString() + "번째 사람의 nearest :" + nearest + ", nearest num : " + nearest_num)
            }
            if (cmap.containsKey(nearest_num)) {
                /*
                 * 이미 키가 존재할 경우
                 * ArrayList를 기존의 Points로 초기화하고 새 Points 추가함
                 */
                val user_point: ArrayList<Point?> = (cmap.get(nearest_num))!!
                user_point.add(pos.get(i))
                cmap.put(nearest_num, user_point)
            } else {
                /*
                 * 키가 존재하지 않을 경우
                 * ArrayList를 초기화하고 Points추가
                 */
                val user_point: ArrayList<Point?> = ArrayList()
                user_point.add(pos.get(i))
                cmap.put(nearest_num, user_point)
            }
        }
        Log.d("Clustering", "cmap 출력 : " + cmap)
        Log.d("Clustering", "----<<updateCenter>>----")
        //update
        updateCenter()
    }

    fun updateCenter() {
        //center.clear();
        cp = ArrayList()
        // 방법2
        for (elem: Map.Entry<Int, ArrayList<Point?>> in cmap!!.entries) {
            Log.d("Clustering", "클러스터 출력(key) : " + elem.key)
            Log.d("Clustering", "클러스터 출력(value) : " + elem.value)
            val cluster_points: Array<Point?> = elem.value.toTypedArray()
            var p: Point?
            p = PolygonCenter(cluster_points)
            cp!!.add(LatLng(p!!.x, p.y))
        }
        for (i in cp!!.indices) {
            Log.d("Clustering", "무게중심 출력 : " + cp!!.get(i))
        }
    }

    fun Stop(): Boolean {
        var check: Boolean = true
        var count_ch: Int = 0
        for (i in cp!!.indices) {
            val diff_lat: Double = cp!!.get(i)!!.latitude - prevcenter!!.get(i)!!.latitude
            val diff_lng: Double = cp!!.get(i)!!.longitude - prevcenter!!.get(i)!!.longitude
            Log.d("Clustering", "prevcenter : " + prevcenter!!.get(i)!!.latitude + "," + prevcenter!!.get(i)!!.longitude)
            if (diff_lat == 0.0 && diff_lng == 0.0) {
                count_ch++
            }
        }
        if (count_ch == cp!!.size) {
            Log.d("Clustering", "이전과 차이없음")
            Log.d("Clustering", "클러스터링 완료! ")
            check = false
        }
        prevcenter = ArrayList()
        prevcenter = cp
        for (i in cp!!.indices) {
            Log.d("Clustering", "centroid : " + cp!!.get(i))
            Log.d("Clustering", "prevcenter 재지정 : " + prevcenter!!.get(i))
        }
        return check
    }

    //단위벡터 구하기
    fun getUnitVector(start: LatLng?, end: LatLng): Point {
        var v_x: Double = end.latitude - start!!.latitude
        var v_y: Double = end.longitude - start.longitude
        val u: Double = Math.sqrt(Math.pow(v_x, 2.0) + Math.pow(v_y, 2.0))
        v_x /= u
        v_y /= u
        val p: Point = Point(v_x, v_y)
        return p
    }

    //이동시간 구하기
    fun getPathTime(start: LatLng?, end: LatLng): Double {
        println("들어왔습니다.")
        val getJS: String = getJSON(start, end)
        try {
            val jsonObject: JSONObject = JSONObject(getJS)
            val route: JSONObject = jsonObject.getJSONObject("route")
            println("route 출력 : " + route)
            val traOb: JSONObject = route.getJSONArray("traoptimal").get(0) as JSONObject
            val summary: JSONObject = traOb.getJSONObject("summary")

            //총 이동시간 => 이건 leg마다 다르니까 step에 같이 출력하기
            duration = summary.getString("duration")
            println("duration출력 : " + duration)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        var totalT: Double = 0.0
        if (duration == null) {
            println("이동시간 정보가 없습니다. " + "시작 : " + start)
            totalT = 0.0
        } else {
            Dur = duration!!.toDouble()
            Dur = Dur / 60000
            println("이동시간은 다음과 같다 : " + Dur)
            totalT = Dur
        }
        return totalT
    }

    // Point 타입 좌표 배열을 ArrayList<LatLng> 타입으로 변환
    private fun changeToList(polygon_point: Array<Point?>): ArrayList<LatLng> {
        val polygonList: ArrayList<LatLng> = ArrayList()
        for (i in polygon_point.indices) {
            val temp: LatLng = LatLng(polygon_point.get(i).getX(), polygon_point.get(i).getY())
            polygonList.add(temp)
        }
        Log.d("TEST CHECK", polygonList.toString())
        //mMap.addPolygon(makePolygon(polygonList).strokeColor(Color.RED));
        return polygonList
    }

    // 중간지점 근처 역 찾기
    private fun findSub(midP: LatLng?) {
        val apiKey: String = getString(R.string.api_key)
        previous_marker = ArrayList()
        if (previous_marker != null) previous_marker.clear() //지역정보 마커 클리어
        NRPlaces.Builder()
                .listener(this@MiddlePlaceActivity)
                .key(apiKey)
                .latlng(midP!!.latitude, midP.longitude) //중간지점 위치
                .radius(radius) //500 미터 내에서 검색
                .type(PlaceType.SUBWAY_STATION) //지하철
                .language("ko", "KR")
                .build()
                .execute()
    }

    // 500 미터 내에 없을 경우 찾을 때 까지 500 미터 늘려서 다시 계산
    public override fun onPlacesFailure(e: PlacesException) {
        val apiKey: String = getString(R.string.api_key)
        if (radius < 3000) {
            radius += 1000
            NRPlaces.Builder()
                    .listener(this@MiddlePlaceActivity)
                    .key(apiKey)
                    .latlng(midP!!.latitude, midP!!.longitude) //현재 위치
                    .radius(radius) //500 미터 내에서 검색
                    .type(PlaceType.SUBWAY_STATION) //지하철
                    .language("ko", "KR")
                    .build()
                    .execute()
        } else {
            findSub_flg = false
        }
    }

    public override fun onPlacesStart() {}
    public override fun onPlacesSuccess(places: List<Place>) {
        flagCount++
        if (flagCount >= 2) {
            val bd: Bundle = Bundle() /// 번들 생성
            bd.putString("flag", "NO") // 번들에 값 넣기
            val msg: Message = mHandler.obtainMessage() /// 핸들에 전달할 메세지 구조체 받기
            msg.setData(bd) /// 메세지에 번들 넣기
            mHandler.handleMessage(msg)
        }
        if (flag) return
        val bitmapdraw2: BitmapDrawable = getResources().getDrawable(R.drawable.subway_st) as BitmapDrawable
        val c: Bitmap = bitmapdraw2.getBitmap()
        val MiddleMarker: Bitmap = Bitmap.createScaledBitmap(c, 100, 100, false)
        // 중간지점과 가장 가까운 역 표시
        runOnUiThread(object : Runnable {
            public override fun run() {
                var distance: Double = radius.toDouble()
                var temp: Double = 0.0
                var latlng: LatLng? = LatLng(0, 0)
                var i: Int = 1
                val size: Int = places.size
                var name: String? = null
                for (place: Place in places) {
                    val latLng: LatLng = LatLng(place.getLatitude(), place.getLongitude())
                    temp = distance
                    distance = SphericalUtil.computeDistanceBetween(midP, latLng)
                    println(place.getName() + " 거리 : " + distance)
                    if (distance < temp) {
                        latlng = latLng
                        name = place.getName()
                    }
                    if (i == size) {
                        sub_name = name
                        val markerSnippet: String = getCurrentAddress(latlng)
                        val markerOptions: MarkerOptions = MarkerOptions()
                        markerOptions.position((latlng)!!)
                        markerOptions.title(name + "역")
                        markerOptions.snippet(markerSnippet).icon(BitmapDescriptorFactory.fromBitmap(MiddleMarker))
                        val item: Marker = mMap!!.addMarker(markerOptions)
                        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12));
                        previous_marker!!.add(item)
                        midP = latlng
                    }
                    i++
                }
                //중복 마커 제거
                val hashSet: HashSet<Marker> = HashSet()
                hashSet.addAll((previous_marker)!!)
                previous_marker!!.clear()
                previous_marker!!.addAll(hashSet)
            }
        })
    }

    public override fun onPlacesFinished() {}
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
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
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
                conn.setRequestMethod("GET")
                //네이버 플랫폼에서 발급받은 키
                conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "p16r9d98f3")
                conn.setRequestProperty("X-NCP-APIGW-API-KEY", "kCciijwt6AT7OGT6mx7IUDHXfV6NYrW41O03R3cj")
                conn.setDoInput(true)
                conn.connect()
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

    fun getJSON(depart: LatLng?, arrival: LatLng): String {
        val str_origin: String = depart!!.longitude.toString() + "," + depart.latitude
        val str_dest: String = arrival.longitude.toString() + "," + arrival.latitude
        str_url = ("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?" +
                "start=" + str_origin + "&goal=" + str_dest)
        var resultText: String = "값이 없음"
        try {
            resultText = Task().execute().get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        return resultText
    }
}