package com.example.mmmmeeting.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.MeetingActivity
import com.example.mmmmeeting.fragment.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MeetingActivity constructor() : ToolbarActivity() {
    private var bottomNavigationView: BottomNavigationView? = null
    private val bundle: Bundle = Bundle()
    private val fm: FragmentManager? = null
    private val ft: FragmentTransaction? = null
    private val fragCalendar: FragCalendar? = null
    private val fragChat: FragChat? = null
    private val fragAlarm: FragAttend? = null
    private var fragAccount: FragAccount? = null
    private var fragment_ac: Fragment? = null
    var meetingCode: String? = null
    var userName: String? = null
    private var fr_check: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frag_default)
        val getName: String? = getIntent().getExtras()!!.getString("Name")
        meetingCode = getIntent().getExtras()!!.getString("Code")
        Log.d("Grid send code ", "meetingCode is " + meetingCode)
        setToolbarTitle(getName)
        val fragHome: FragHome = FragHome()
        bundle.putString("Code", meetingCode)
        fragHome.setArguments(bundle)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragHome)
                .commit()
        fragment_ac = Fragment()
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
        val userId: String = user!!.getUid()
        db.collection("users").document(userId).get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        userName = document.get("name").toString()
                        bundle.putString("userName", userName)
                    }
                }
            }
        })
        bottomNavigationView = findViewById(R.id.bottomNavi)
        // 메뉴 바 아이콘을 눌렀을 때의 화면 동작
        // 각 화면 코드는 fragment 폴더에 있음
        bottomNavigationView.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            public override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.menu_home -> {
                        val fragHome: FragHome = FragHome()
                        bundle.putString("Code", meetingCode)
                        fragHome.setArguments(bundle)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragHome)
                                .commit()
                        return true
                    }
                    R.id.menu_chat -> {
                        val fragChat: FragChat = FragChat()
                        bundle.putString("Code", meetingCode)
                        fragChat.setArguments(bundle)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragChat)
                                .commit()
                        return true
                    }
                    R.id.menu_board -> {
                        val fragBoard: FragBoard = FragBoard()
                        bundle.putString("Code", meetingCode)
                        fragBoard.setArguments(bundle)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragBoard)
                                .commit()
                        return true
                    }
                    R.id.menu_alarm -> {
                        val fragAlarm: FragAttend = FragAttend()
                        bundle.putString("Code", meetingCode)
                        fragAlarm.setArguments(bundle)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, fragAlarm)
                                .commit()
                        return true
                    }
                    R.id.menu_account -> {
                        fragAccount = FragAccount()
                        bundle.putString("Code", meetingCode)
                        fragAccount!!.setArguments(bundle)
                        if (fr_check == false) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_frame, fragAccount!!)
                                    .commit()
                        }
                        if (fr_check) {
                            //getSupportFragmentManager().beginTransaction().hide(fragAccount).commit();
                            //fragAccount_result = new FragAccount_Result();
                            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment_ac!!).commit()
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    //Fragment fragment,
    fun replaceFragment(fragment: Fragment?, check: Boolean) {
        if (check) {
            fragment_ac = fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, (fragment_ac)!!).commit()
        }
        if (check == false) {
            fragAccount = FragAccount()
            bundle.putString("Code", meetingCode)
            fragAccount!!.setArguments(bundle)
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragAccount!!).commit()
        }
        fr_check = check
        //fragmentManager.beginTransaction().hide(fragAccount).commit();
    }

    //메뉴바 코드
    public override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.meetinginfo, menu)
        return true
    }

    public override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.meetingInfo -> {
                // 리더인지 확인
                checkLeader()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkLeader() {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val docRef: DocumentReference = db.collection("meetings").document((meetingCode)!!)
        docRef.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        // 해당 문서가 존재하는 경우
                        Log.d("Document Read", document.getId() + " => " + document.getData())
                        val intent: Intent = Intent(this@MeetingActivity, MeetingInfoActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra("Name", getIntent().getExtras()!!.getString("Name"))
                        intent.putExtra("Description", getIntent().getExtras()!!.getString("Description"))
                        intent.putExtra("Code", meetingCode)

                        // 해당 모임의 리더 확인
                        if ((document.get("leader").toString() == mAuth.getUid())) {
                            intent.putExtra("isLeader", true)
                            startActivity(intent)
                            return
                        } else {
                            intent.putExtra("isLeader", false)
                            startActivity(intent)
                        }
                        Log.d("Attend", "Data is : " + document.getId())
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
}