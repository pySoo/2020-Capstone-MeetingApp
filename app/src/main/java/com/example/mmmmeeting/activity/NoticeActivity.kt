package com.example.mmmmeeting.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mmmmeeting.AlarmReceiver
import com.example.mmmmeeting.DeviceBootReceiver
import com.example.mmmmeeting.R
import java.text.SimpleDateFormat
import java.util.*

class NoticeActivity constructor() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        val picker: TimePicker = findViewById<View>(R.id.timePicker) as TimePicker
        picker.setIs24HourView(true)


        // 앞서 설정한 값으로 보여주기
        // 없으면 디폴트 값은 현재시간
        val sharedPreferences: SharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE)
        val millis: Long = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis())
        val nextNotifyTime: Calendar = GregorianCalendar()
        nextNotifyTime.setTimeInMillis(millis)

        /*
            Date nextDate = nextNotifyTime.getTime();
            String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(nextDate);
            Toast.makeText(getApplicationContext(),"[처음 실행시] 다음 알람은 " + date_text + "으로 알람이 설정되었습니다!", Toast.LENGTH_SHORT).show();

             */


        // 이전 설정값으로 TimePicker 초기화
        val currentTime: Date = nextNotifyTime.getTime()
        val HourFormat: SimpleDateFormat = SimpleDateFormat("kk", Locale.getDefault())
        val MinuteFormat: SimpleDateFormat = SimpleDateFormat("mm", Locale.getDefault())
        val pre_hour: Int = HourFormat.format(currentTime).toInt()
        val pre_minute: Int = MinuteFormat.format(currentTime).toInt()
        if (Build.VERSION.SDK_INT >= 23) {
            picker.setHour(pre_hour)
            picker.setMinute(pre_minute)
        } else {
            picker.setCurrentHour(pre_hour)
            picker.setCurrentMinute(pre_minute)
        }
        val button: Button = findViewById<View>(R.id.button) as Button
        button.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(arg0: View) {
                val hour: Int
                val hour_24: Int
                val minute: Int
                val am_pm: String
                if (Build.VERSION.SDK_INT >= 23) {
                    hour_24 = picker.getHour()
                    minute = picker.getMinute()
                } else {
                    hour_24 = picker.getCurrentHour()
                    minute = picker.getCurrentMinute()
                }
                if (hour_24 > 12) {
                    am_pm = "PM"
                    hour = hour_24 - 12
                } else {
                    hour = hour_24
                    am_pm = "AM"
                }
                val da: Calendar? = getIntent().getSerializableExtra("alarm") as Calendar?
                if (da != null) {
                    diaryNotification(da)
                }

                //  Intent intent = getIntent();
                var day: IntArray = IntArray(2)
                day = getIntent().getSerializableExtra("meetingdate") as IntArray

                // 현재 지정된 시간으로 알람 시간 설정
                val calendar: Calendar = Calendar.getInstance()
                calendar.setTimeInMillis(System.currentTimeMillis())
                calendar.set(Calendar.MONTH, day.get(0))
                calendar.set(Calendar.DAY_OF_MONTH, day.get(1))
                calendar.set(Calendar.HOUR_OF_DAY, hour_24)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)

                /*
                    // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                    if (calendar.before(Calendar.getInstance())) {
                        calendar.add(Calendar.DATE, 1);
                    }*/
                val currentDateTime: Date = calendar.getTime()
                val date_text: String = SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime)
                Toast.makeText(getApplicationContext(), date_text + "으로 약속이 설정되었습니다!", Toast.LENGTH_SHORT).show()

                //  Preference에 설정한 값 저장
                calendar.set(Calendar.HOUR_OF_DAY, hour_24 - 1)
                val editor: SharedPreferences.Editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit()
                editor.putLong("nextNotifyTime", calendar.getTimeInMillis())
                editor.apply()
                diaryNotification(calendar)
                val cal: Calendar = calendar
                //                    cal.set(Calendar.HOUR_OF_DAY, hour_24-9); //db와 9시간 차 없애줘야함(UTC+9)
                cal.set(Calendar.HOUR_OF_DAY, hour_24)
                val dateTime: Date = cal.getTime()
                val intentT: Intent = Intent()
                intentT.putExtra("fulldate", dateTime) //전체 날짜 전달
                setResult(RESULT_OK, intentT)
                finish()
            }
        })
    }

    fun diaryNotification(calendar: Calendar) {
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        Boolean dailyNotify = sharedPref.getBoolean(SettingsActivity.KEY_PREF_DAILY_NOTIFICATION, true);
        val dailyNotify: Boolean = true // 무조건 알람을 사용
        val schedule: String = getIntent().getSerializableExtra("scInfo") as String
        println("확인확인     " + schedule)
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
        } /*
        else { //Disable Daily Notifications
            if (PendingIntent.getBroadcast(this, index, alarmIntent, 0) != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent);
                //Toast.makeText(this,"Notifications were disabled",Toast.LENGTH_SHORT).show();
            }
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        }*/
    }
}