package com.example.mmmmeeting.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.inviteActivity
import com.google.firebase.firestore.FirebaseFirestore

class inviteActivity constructor() : AppCompatActivity() {
    var phone: EditText? = null
    var send: Button? = null
    private val MY_PERMISSION_REQUEST_SMS: Int = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite)

        // 문자 권한이 허가되어있지 않은 경우
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("info")
                builder.setMessage("SMS 허가가 없으면 앱이 정상 작동하지 않을 수 있습니다.")
                builder.setNeutralButton("OK", object : DialogInterface.OnClickListener {
                    public override fun onClick(dialog: DialogInterface, which: Int) {
                        ActivityCompat.requestPermissions(this@inviteActivity, arrayOf(Manifest.permission.SEND_SMS), MY_PERMISSION_REQUEST_SMS)
                    }
                })
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), MY_PERMISSION_REQUEST_SMS)
            }
        }
        send = findViewById(R.id.sendBtn)
        phone = findViewById(R.id.phone_number)

        // 문자 전송 버튼 클릭시 동작
        send.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                val intent: Intent = getIntent()
                val meetingname: String? = intent.getExtras()!!.getString("Name")
                val code: String? = intent.getExtras()!!.getString("Code")
                sendSMS(meetingname, code)
                Log.d("SendSMS", code)
                Log.d("SendSMS", meetingname)
            }
        })
    }

    // 문자보내기
    private fun sendSMS(meetingname: String?, code: String?) {
        val phoneNo: String = phone!!.getText().toString() // phone Edit Text 에 있는 숫자 저장
        val sms: String = meetingname + "에서의 초대 코드 : " + code
        try {
            phoneNo.toInt()

            //번호로 문자 전송
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, sms, null, null)
            Log.d("SendSMS", phoneNo + "//" + sms)
            Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(getApplicationContext(), "문자 전송 실패, 숫자만 입력해주세요!", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    // 권한 허가 요청
    public override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSION_REQUEST_SMS -> {
                if (grantResults.size > 0 && grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}