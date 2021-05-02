package com.example.mmmmeeting.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mmmmeeting.Info.AddressItems
import com.example.mmmmeeting.R
import com.example.mmmmeeting.activity.MemberInitActivity
import java.util.*

class AddressAdapter constructor() : BaseAdapter() {
    var items: ArrayList<AddressItems> = ArrayList()
    var context // activity 정보 저장
            : Context? = null

    // 아이템 추가
    fun addItem(item: AddressItems) {
        items.add(item)
    }

    // 아이템 크기
    public override fun getCount(): Int {
        return items.size
    }

    // 위치의 아이템
    public override fun getItem(position: Int): Any {
        return items.get(position)
    }

    // 위치 (아이템ID)
    public override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 뷰 설정
    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView: View = convertView
        context = parent.getContext() // activity 정보 읽기
        val listItem: AddressItems = items.get(position) //position 해당하는 listItem

        //list_item inflate => convertView 참조
        if (convertView == null) {
            val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.sub_address, parent, false)
        }

        // 텍스트뷰 설정
        val road: TextView = convertView.findViewById(R.id.addr_road)
        val jibun: TextView = convertView.findViewById(R.id.addr_jibun)
        val post: TextView = convertView.findViewById(R.id.postno)

        // 텍스트뷰에 글자 지정
        road.setText(listItem.getRoad())
        jibun.setText(listItem.getJibun())
        post.setText(listItem.getPost())
        val addressItem: LinearLayout = convertView.findViewById<View>(R.id.addressItem) as LinearLayout
        addressItem.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                // MemberInitActivity 주소 정보 전달
                val intent: Intent = Intent(context, MemberInitActivity::class.java)
                intent.putExtra("road", listItem.getRoad())
                (context as Activity?)!!.setResult(0, intent)
                //                intent.putExtra("post", listItem.getJibun());
//                intent.putExtra("jibun", listItem.getPost());
//                context.startActivity(intent);
                (context as Activity?)!!.finish()
            }
        })
        return convertView
    }
}