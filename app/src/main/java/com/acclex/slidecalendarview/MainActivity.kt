package com.acclex.slidecalendarview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendar.setDateClick { dateBean, _, _ ->
            calendar.updateSelectDay(dateBean)
            Toast.makeText(applicationContext, dateBean.date, Toast.LENGTH_SHORT).show()
        }
    }
}
