package com.acclex.calendarview.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.acclex.calendarview.R
import com.acclex.calendarview.bean.*
import com.acclex.calendarview.util.dip2px
import java.util.*

/**
 * Created by acclex on 2019-07-12
 */
class CalendarView : LinearLayout {
    private var startWithSunday = true
    private var maxMonth = 2
    private var beforeMonth = 1
    private val rvDate = RecyclerView(context)
    private val adapter = DateAdapter()
    private val mList = mutableListOf<DateBean>()

    private val calendar = Calendar.getInstance()
    private val currentYear = calendar.get(Calendar.YEAR)
    private val currentMonth = calendar.get(Calendar.MONTH) + 1
    private val currentDate = calendar.get(Calendar.DATE)

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyle: Int
    ) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView)
        val isShowWeek = typedArray.getBoolean(R.styleable.CalendarView_showWeek, true)
        startWithSunday = typedArray.getBoolean(R.styleable.CalendarView_startWithSunday, true)
        maxMonth = typedArray.getInt(R.styleable.CalendarView_maxMonth, maxMonth)
        beforeMonth = typedArray.getInt(R.styleable.CalendarView_beforeMonth, beforeMonth)
        typedArray.recycle()
        orientation = VERTICAL
        if (isShowWeek) {
            addWeekView()
        }
        initDate()
        addCalendarView()
    }

    private fun addWeekView() {
        val week = LinearLayout(context).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.color_F1F5F8))
            val headParams = LayoutParams(LayoutParams.MATCH_PARENT, dip2px(context, 30f))
            layoutParams = headParams
            orientation = HORIZONTAL
            setPadding(dip2px(context, 15f), 0, dip2px(context, 15f), 0)
        }
        val weekDay = if (startWithSunday) listOf("日", "一", "二", "三", "四", "五", "六") else
            listOf("一", "二", "三", "四", "五", "六", "日")
        val itemParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
            weight = 1.5f
        }
        for (i in weekDay) {
            val tv = TextView(context).apply {
                layoutParams = itemParams
                gravity = Gravity.CENTER
                setTextColor(ContextCompat.getColor(context, R.color.color_92a0aa))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                text = i
            }
            week.addView(tv)
        }
        addView(week)
    }

    private fun initDate() {
        val monthList = mutableListOf<MonthBean>()

        val calendar = Calendar.getInstance()
        // 控制月份本月之前显示几个月
        calendar.add(Calendar.MONTH, -beforeMonth)
        for (i in 0 until maxMonth) {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val bean = MonthBean(year, month)
            monthList.add(bean)
            calendar.add(Calendar.MONTH, 1)
        }

        for (bean in monthList) {
            val dateList = mutableListOf<DateBean>()
            // 这个月的第一天
            calendar.set(bean.year, bean.month - 1, 1)
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            // 第一天之前空几天 起始的天数不同的话，
            val firstOffset = if (startWithSunday) dayOfWeek - 1 else dayOfWeek - 2
            //当月最后一天
            calendar.add(Calendar.MONTH, 1)
            calendar.add(Calendar.DATE, -1)
            val dayOfSum = calendar.get(Calendar.DATE)
            // 这个月起始的空占位符
            for (i in 0 until firstOffset) {
                val dateBean = DateBean(
                    year = currentYear,
                    month = currentMonth + 1,
                    day = 0,
                    type = TYPE_DATE_BLANK
                )
                dateList.add(dateBean)
            }
            // 这个月的所有日期
            for (i in 0 until dayOfSum) {
                val dateBean = DateBean(
                    year = currentYear,
                    month = currentMonth + 1,
                    day = i + 1,
                    type = TYPE_DATE_NORMAL
                )
                checkIsToday(dateBean)
                dateList.add(dateBean)
            }
            // 这个月结束的空白占位符
            val lastDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val lastOffset = 7 - lastDayOfWeek
            for (i in 0 until lastOffset) {
                val dateBean = DateBean(
                    year = currentYear,
                    month = currentMonth + 1,
                    day = 0,
                    type = TYPE_DATE_BLANK
                )
                dateList.add(dateBean)
            }
            bean.dateList = dateList
        }
        // 添加头部月份title，同时完成整个月份Adapter内List的构造
        for (bean in monthList) {
            val titleBean = DateBean(
                year = bean.year,
                month = bean.month,
                day = 0,
                type = TYPE_DATE_TITLE
            )
            mList.add(titleBean)
            mList.addAll(bean.dateList)
        }
    }

    private fun addCalendarView() {
        val rvParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        val gridLayoutManager = GridLayoutManager(context, 7)
        this.adapter.setNewList(mList)
        rvDate.apply {
            adapter = this@CalendarView.adapter
            layoutParams = rvParams
            layoutManager = gridLayoutManager
            addItemDecoration(CalendarDecoration(context) { position ->
                //返回年月栏数据，如2019年1月
                if (position < mList.size) {
                    mList[position].groupName
                } else {
                    ""
                }
            })
            setPadding(dip2px(context, 15f), 0, dip2px(context, 15f), 0)
            clipChildren = false
            clipToPadding = false

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(i: Int): Int {
                    //设置每行item个数，若是title则占7个位置，若是空白或日期则占一个位置
                    return if (mList[i].type == TYPE_DATE_TITLE) 7 else 1
                }
            }
        }
        addView(rvDate)
        val todayPos = mList.indexOfFirst { it.isToday }
        if (todayPos != -1) rvDate.scrollToPosition(todayPos) else
            rvDate.scrollToPosition(mList.size - 1)
    }

    private fun checkIsToday(bean: DateBean) {
        if (bean.year == currentYear && bean.month == currentMonth && bean.day == currentDate) {
            bean.isToday = true
            bean.isChooseDay = true
        }
    }

    fun setDateClick(listener: ((DateBean, View, Int) -> Unit)?) {
        adapter.setOnItemClickListener(listener)
    }


    fun updateSelectDay(bean: DateBean) {
        val beforeBean = mList.find { it.isChooseDay }
        val beforePosition = mList.indexOfFirst { it.isChooseDay }
        if (bean == beforeBean) return
        beforeBean?.run { isChooseDay = false }
        bean.isChooseDay = true
        val selectPos = mList.indexOf(bean)
        if (beforePosition != -1) adapter.notifyItemChanged(beforePosition)
        if (selectPos != -1) adapter.notifyItemChanged(selectPos)
    }
}