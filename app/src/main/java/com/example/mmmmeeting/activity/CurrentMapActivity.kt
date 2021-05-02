// 출석 체크시 현재 위치가 약속 장소 100m 이내인지 검사
package com.example.mmmmeeting.activity

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import com.example.mmmmeeting.Info.ScheduleInfo
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.CurrentMapActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.SphericalUtil
import java.io.IOException
import java.util.*

class CurrentMapActivity constructor() : AppCompatActivity(), OnMapReadyCallback, OnRequestPermissionsResultCallback {
    private var mMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    var needRequest: Boolean = false
    var REQUIRED_PERMISSIONS: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION) // 외부 저장소
    var mCurrentLocatiion: Location? = null
    var currentPosition: LatLng? = null
    var previousPosition: LatLng? = null
    var addedMarker: Marker? = null
    var tracking: Int = 0
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var location: Location? = null
    private var flag: Boolean = false
    private var place: String? = null
    private var placeLatLng: LatLng? = null
    private val calDate: Date? = null
    private var hour: Int = 0
    private var minute: Int = 0
    private var timePoint: Double = 0.0
    private var scID: String? = null
    private val meetingID: String? = null
    var user: FirebaseUser? = null
    var db: FirebaseFirestore? = null
    var handler: Handler? = null
    var markerOptions: MarkerOptions? = null
    var cal: Calendar? = null
    private var mLayout: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_current_map)
        mLayout = findViewById(R.id.layout_current)
        locationRequest = LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS.toLong())
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS.toLong())
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment: SupportMapFragment? = getSupportFragmentManager()
                .findFragmentById(R.id.currentMap) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        val button: Button = findViewById<View>(R.id.currentButton) as Button
        // 버튼 누를 때마다 실시간 위치 조회 toggle
        button.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                flag = false

                // tracking : 1 실시간 위치 조회 중
                tracking = 1 - tracking
            }
        })

        // DB로부터 약속 시간, 장소 가져오기 위함
        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().getCurrentUser()
        val scInfo: ScheduleInfo = getIntent().getSerializableExtra("scheduleInfo") as ScheduleInfo
        scID = scInfo.getId()
        hour = getIntent().getSerializableExtra("hour") as Int
        minute = getIntent().getSerializableExtra("minute") as Int
        cal = Calendar.getInstance()
        val docRef: DocumentReference = db!!.collection("schedule").document((scID)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 약속 장소 위도, 경도 가져옴
                        val placeMap: Map<String, Any>? = document.getData()!!.get("meetingPlace") as Map<String, Any>?
                        place = placeMap!!.get("name") as String?
                        val latLng: GeoPoint? = placeMap.get("latlng") as GeoPoint?
                        val lat: Double = latLng!!.getLatitude()
                        val lng: Double = latLng.getLongitude()
                        placeLatLng = LatLng(lat, lng)

                        // 핸들러에 메시지를 보내 DB로부터 장소 받은 것 알림
                        val bd: Bundle = Bundle()
                        bd.putString("arg", "get")
                        val msg: Message = handler!!.obtainMessage()
                        msg.setData(bd)
                        handler!!.sendMessage(msg)
                    } else {
                        Log.d("Attend", "No Document")
                    }
                } else {
                    Log.d("Attend", "Task Fail : " + task.getException())
                }
            }
        })
        handler = object : Handler() {
            public override fun handleMessage(msg: Message) {
                val bd: Bundle = msg.getData()
                // db에서 약속장소 받아온 경우 약속 장소에 마크 생성
                if (bd.getString("arg") != null) {
                    setMarker()
                    // 위치 추적 중인 경우 약속 시간과 현재 시간 비교
                } else if (bd.getString("check") != null) {
                    // 약속 시간 확인
                    val now: Date = Date()
                    cal.setTime(now)
                    // 현재 시간 받아오기
                    val nowHour: Int = cal.get(Calendar.HOUR_OF_DAY)
                    val nowMinute: Int = cal.get(Calendar.MINUTE)
                    var lateMinute: Int = nowMinute - minute
                    val timePointMap: HashMap<String, Double> = HashMap()
                    // 출석 시간이 지난 경우
                    if (nowHour > hour || (nowHour == hour && nowMinute > minute)) {
                        Toast.makeText(getApplicationContext(), "약속시간이 지났습니다.", Toast.LENGTH_SHORT).show()
                        // 시단위가 바뀐 경우
                        if (nowHour > hour) {
                            lateMinute = nowMinute + 60 * (nowHour - hour) - minute
                        } else {
                            lateMinute = nowMinute - minute
                        }
                        timePoint = -1.0 * lateMinute.toDouble() * 0.2
                        timePoint = String.format("%.1f", timePoint).toDouble()
                        Toast.makeText(getApplicationContext(), "포인트 " + (-1 * timePoint) + "점 차감!", Toast.LENGTH_SHORT).show()
                        timePointMap.put(user!!.getUid(), timePoint)
                        db!!.collection("schedule").document((scID)!!).update("timePoint", timePointMap)
                        mFusedLocationClient.removeLocationUpdates(locationCallback)
                        finish()
                    } else if (((minute < 10) && (nowHour == hour - 1) && (nowMinute >= minute + 60 - 10)) ||
                            (nowHour == hour && nowMinute >= minute - 10)) {
                        val str: String? = bd.getString("check")
                        // 500m 이내인 경우
                        if ((str == "ok")) {
                            Toast.makeText(getApplicationContext(), "약속시간을 잘 지키셨군요!", Toast.LENGTH_SHORT).show()
                            Toast.makeText(getApplicationContext(), "포인트 5점 획득!", Toast.LENGTH_SHORT).show()
                            timePoint = 5.0
                            timePointMap.put(user!!.getUid(), timePoint)
                            db!!.collection("schedule").document((scID)!!).update("timePoint", timePointMap)
                            mFusedLocationClient.removeLocationUpdates(locationCallback)
                            finish()
                            // 500m 이내가 아님
                        } else if ((str == "no")) {
                            Toast.makeText(getApplicationContext(), "500m 이내가 아닙니다.", Toast.LENGTH_SHORT).show()
                            // flag: callback 중지, tracking 0: 위치 추적 x
                            flag = true
                            tracking = 0
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "약속시간 10분전부터 출석체크 가능합니다.", Toast.LENGTH_SHORT).show()
                        mFusedLocationClient.removeLocationUpdates(locationCallback)
                        finish()
                    }
                }
            }
        }
    }

    // 약속 장소 마크 표시
    private fun setMarker() {
        markerOptions = MarkerOptions()
        markerOptions!!.position((placeLatLng)!!)
        markerOptions!!.title("약속 장소")
        markerOptions!!.snippet(place)
        markerOptions!!.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        if (addedMarker != null) mMap!!.clear()
        addedMarker = mMap!!.addMarker(markerOptions)
    }

    public override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // 지도의 초기위치를 서울로 이동
        setDefaultLocation()

        // 위치 퍼미션을 가지고 있는지 체크
        val hasFineLocationPermission: Int = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission: Int = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 이미 퍼미션을 가지고 있다면 위치 업데이트 시작
            startLocationUpdates()
        } else {
            // 사용자가 퍼미션 거부를 한 적이 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS.get(0))) {

                // 권한 요청 메시지 발송
                Snackbar.make((mLayout)!!, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", object : View.OnClickListener {
                    public override fun onClick(view: View) {
                        ActivityCompat.requestPermissions(this@CurrentMapActivity, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE)
                    }
                }).show()
            } else {
                // 퍼미션 거부를 한 적이 없는 경우 -> 묻지 않고 퍼미션 요청
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE)
            }
        }
        mMap!!.getUiSettings().setMyLocationButtonEnabled(true)
    }

    var locationCallback: LocationCallback = object : LocationCallback() {
        public override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            // 계속 위치 추적하는 낭비 막기 위해 버튼 누를때마다 위치 받아오게 동작
            if (flag) {
                return
            }
            val locationList: List<Location> = locationResult.getLocations()
            if (locationList.size > 0) {
                location = locationList.get(locationList.size - 1)
                Log.d(TAG, "location size>0")
                previousPosition = currentPosition
                currentPosition = LatLng(location!!.getLatitude(), location!!.getLongitude())
                if (previousPosition == null) previousPosition = currentPosition
                if ((addedMarker != null) && (tracking == 1) && (previousPosition !== currentPosition)) {
                    val radius: Double = 500.0 // 500m distance.
                    val distance: Double = SphericalUtil.computeDistanceBetween(currentPosition, addedMarker!!.getPosition())

                    // 반경 이내일 경우 Handler 통해 출석 체크
                    if ((distance <= radius)) {
                        val bd: Bundle = Bundle()
                        bd.putString("check", "ok")
                        val msg: Message = handler!!.obtainMessage()
                        msg.setData(bd)
                        handler!!.sendMessage(msg)
                    } else {
                        val bd: Bundle = Bundle()
                        bd.putString("check", "no")
                        val msg: Message = handler!!.obtainMessage()
                        msg.setData(bd)
                        handler!!.sendMessage(msg)
                    }
                }
                val markerSnippet: String = ("위도:" + location!!.getLatitude().toString() + " 경도:" + location!!.getLongitude().toString())

                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location)
                mCurrentLocatiion = location
            }
        }
    }

    private fun startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            val hasFineLocationPermission: Int = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
            val hasCoarseLocationPermission: Int = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 없음")
                return
            }
            mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            if (checkPermission()) mMap!!.setMyLocationEnabled(true)
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermission()) {
            mFusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)
            if (mMap != null) mMap!!.setMyLocationEnabled(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if (mFusedLocationClient != null) {
            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
        }
    }

    fun getCurrentAddress(latlng: LatLng): String {
        //지오코더: GPS를 주소로 변환
        val geocoder: Geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
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

        // 주소 없는 경우
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        } else {
            val address: Address = addresses.get(0)
            return address.getAddressLine(0).toString()
        }
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    fun setCurrentLocation(location: Location?) {
        if (currentMarker != null) currentMarker!!.remove()
        val currentLatLng: LatLng = LatLng(location!!.getLatitude(), location.getLongitude())
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        markerOptions.title("현재 위치")
        markerOptions.draggable(true)
        currentMarker = mMap!!.addMarker(markerOptions)
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng)
        mMap!!.moveCamera(cameraUpdate)
    }

    fun setDefaultLocation() {
        // 기본 위치 = 서울 시청
        val DEFAULT_LOCATION: LatLng = LatLng(37.56, 126.97)
        val markerTitle: String = "위치정보 가져올 수 없음"
        val markerSnippet: String = "위치 퍼미션과 GPS 활성 요부 확인하세요"
        if (currentMarker != null) currentMarker!!.remove()
        val markerOptions: MarkerOptions = MarkerOptions()
        markerOptions.position(DEFAULT_LOCATION)
        markerOptions.title(markerTitle)
        markerOptions.snippet(markerSnippet)
        markerOptions.draggable(true)
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        currentMarker = mMap!!.addMarker(markerOptions)
        val cameraUpdate: CameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15f)
        mMap!!.moveCamera(cameraUpdate)
    }

    // 런타임 퍼미션 처리을 위한 메서드들
    private fun checkPermission(): Boolean {
        val hasFineLocationPermission: Int = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission: Int = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메서드
    public override fun onRequestPermissionsResult(permsRequestCode: Int,
                                                   permissions: Array<String>,
                                                   grandResults: IntArray) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result: Boolean = true

            // 모든 퍼미션 허용 여부 체크
            for (result: Int in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

                // 퍼미션을 허용 -> 위치 업데이트 시작
                startLocationUpdates()
            } else {
                // 퍼미션 거부 -> 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료
                if ((ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS.get(0))
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS.get(1)))) {
                    Snackbar.make((mLayout)!!, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", object : View.OnClickListener {
                        public override fun onClick(view: View) {
                            finish()
                        }
                    }).show()
                } else {
                    Snackbar.make((mLayout)!!, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", object : View.OnClickListener {
                        public override fun onClick(view: View) {
                            finish()
                        }
                    }).show()
                }
            }
        }
    }

    // GPS 활성화를 위한 메서드
    private fun showDialogForLocationServiceSetting() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@CurrentMapActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?"))
        builder.setCancelable(true)
        builder.setPositiveButton("설정", object : DialogInterface.OnClickListener {
            public override fun onClick(dialog: DialogInterface, id: Int) {
                val callGPSSettingIntent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
            }
        })
        builder.setNegativeButton("취소", object : DialogInterface.OnClickListener {
            public override fun onClick(dialog: DialogInterface, id: Int) {
                dialog.cancel()
            }
        })
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->                 //사용자 GPS 활성 여부 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음")
                        needRequest = true
                        return
                    }
                }
        }
    }

    companion object {
        private val TAG: String = "googlemap_example"
        private val GPS_ENABLE_REQUEST_CODE: Int = 2001
        private val UPDATE_INTERVAL_MS: Int = 1000
        private val FASTEST_UPDATE_INTERVAL_MS: Int = 500
        private val PERMISSIONS_REQUEST_CODE: Int = 100
    }
}