package com.example.mmmmeeting.adapter

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mmmmeeting.Info.CalUserItems
import com.example.mmmmeeting.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CalUserAdapter constructor() : BaseAdapter() {
    var items: ArrayList<CalUserItems> = ArrayList()
    var context // activity 정보 저장
            : Context? = null
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var profilePath: String? = null

    // 아이템 추가
    fun addItem(item: CalUserItems) {
        items.add(item)
    }

    // 아이템 크기
    public override fun getCount(): Int {
        return items.size
    }

    // 위치의 아이템
    public override fun getItem(position: Int): CalUserItems {
        return items.get(position)
    }

    // 위치 (아이템ID)
    public override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun reset() {
        items.removeAll(items)
    }

    // 뷰 설정
    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView: View = convertView
        context = parent.getContext() // activity 정보 읽기
        val listItem: CalUserItems = items.get(position) //position 해당하는 listItem]
        println("caluser:" + listItem)
        println(items)

        //list_item inflate => convertView 참조
        if (convertView == null) {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.cal_user_item, parent, false)
        }
        val profile: ImageView = convertView.findViewById(R.id.profile)
        val finalConvertView: View = convertView
        db.collection("users").document(listItem.getId()).get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val document: DocumentSnapshot = task.getResult()
                    if (document.exists()) {
                        if (document.get("profilePath") != null) {
                            println(listItem.getId())
                            profilePath = document.get("profilePath").toString()
                            Glide.with(finalConvertView).load(profilePath).centerCrop().override(500).into(profile)
                        }
                    }
                }
            }
        })

        // 텍스트뷰 설정
        val username: TextView = convertView.findViewById(R.id.userName)
        val money: EditText = convertView.findViewById(R.id.calMoney)
        money.setOnKeyListener(object : View.OnKeyListener {
            public override fun onKey(view: View, KeyCode: Int, keyEvent: KeyEvent): Boolean {
                if (KeyCode == KeyEvent.KEYCODE_ENTER) return true
                return false
            }
        })

        // 텍스트뷰에 글자 지정
        username.setText(listItem.getUserName())

        //예전 리스너를 삭제한다
        clearTextChangedListener(money)

        //값을 불려오고 해당 리스너를 적용한다
        money.setText(listItem.getMoney())
        money.addTextChangedListener(listItem.mTextWatcher)
        return convertView
    }

    private fun clearTextChangedListener(editText: EditText) {
        //리스트 목록의 모든 리스너를 대상으로 검사하여 삭제해 준다
        val count: Int = getCount()
        for (i in 0 until count) editText.removeTextChangedListener(items.get(i).mTextWatcher)
    }
}