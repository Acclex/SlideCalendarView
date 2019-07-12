package com.acclex.calendarview.bean

/**
 * Created by acclex on 2019-07-11
 */
data class MonthBean(var year: Int, var month: Int, var dateList: MutableList<DateBean> = mutableListOf())
