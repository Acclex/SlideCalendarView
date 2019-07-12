package com.acclex.calendarview.bean

/**
 * Created by acclex on 2019-07-11
 */
//view类型：空白，年月标题，日期
const val TYPE_DATE_BLANK = 0
const val TYPE_DATE_TITLE = 1
const val TYPE_DATE_NORMAL = 2

data class DateBean(
    var year: Int = 2019, var month: Int, var day: Int, var type: Int,
    var isToday: Boolean = false, //是否是当天
    var isChooseDay: Boolean = false // 是否是选择日期
) {
    // 分组
    val groupName: String
        get() {
            val sMonth = if (month < 10) String.format("0%d", month) else String.format("%d", month)
            return year.toString() + "年" + sMonth + "月"
        }

    // 当日的准确日期
    val date: String
        get() {
            val sMonth = if (month < 10) String.format("0%d", month) else String.format("%d", month)
            val sDate = if (day < 10) String.format("0%d", day) else String.format("%d", day)
            return year.toString() + sMonth + sDate
        }

}