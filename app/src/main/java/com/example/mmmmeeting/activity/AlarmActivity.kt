// 약속 시간 알림을 위한 Activity
package com.example.mmmmeeting.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mmmmeeting.AlarmReceiver
import com.example.mmmmeeting.DeviceBootReceiver
import java.util.*

class AlarmActivity constructor() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val calendar: Calendar
        calendar = getIntent().getSerializableExtra("alarm") as Calendar

        //  Preference에 설정한 값 저장
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        calendar.set(Calendar.HOUR_OF_DAY, hour - 1)
        val editor: SharedPreferences.Editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit()
        editor.putLong("nextNotifyTime", calendar.getTimeInMillis())
        editor.apply()
        diaryNotification(calendar)
        setResult(RESULT_OK)
        finish()
    }

    fun diaryNotification(calendar: Calendar) {
        val dailyNotify: Boolean = true // 무조건 알람을 사용
        val y: String = calendar.get(Calendar.YEAR).toString()
        val m: String = calendar.get(Calendar.MONTH).toString()
        val d: String = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val h: String = calendar.get(Calendar.HOUR_OF_DAY).toString()
        val s: String = calendar.get(Calendar.MINUTE).toString()
        val hh: String = (calendar.get(Calendar.HOUR_OF_DAY) + 1).toString() //원래 시간
        val one: String = y + m
        val two: String = d + h + s
        val in1: Int = one.toInt()
        val in2: Int = two.toInt()
        val index: Int = in1 - in2 //약속 id로
        val time: String = hh + "시 " + s + "분"
        val schedule: String = getIntent().getSerializableExtra("date") as String
        val pm: PackageManager = getPackageManager()
        val receiver: ComponentName = ComponentName(this, DeviceBootReceiver::class.java)
        val alarmIntent: Intent = Intent(this, AlarmReceiver::class.java)
        alarmIntent.putExtra("index", index)
        alarmIntent.putExtra("scInfo", schedule)
        alarmIntent.putExtra("time", time)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, index, alarmIntent, 0)
        val alarmManager: AlarmManager? = getSystemService(ALARM_SERVICE) as AlarmManager?

        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP)
        }
    }
}