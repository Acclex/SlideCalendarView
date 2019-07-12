package com.acclex.calendarview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import com.acclex.calendarview.R
import com.acclex.calendarview.util.dip2px
import com.acclex.calendarview.util.sp2px

/**
 * Created by acclex on 2019-07-12
 */
class CalendarDecoration(context: Context, private val mCallBack: ((Int) -> String)) :
    RecyclerView.ItemDecoration() {
    // 悬停栏背景画笔
    private val paint = Paint()
    // 悬停栏文字画笔
    private val textPaint = TextPaint()
    // 悬停栏高度个内边距
    private val top = dip2px(context, 32f)
    private var mTopPadding = 0f

    init {
        paint.color = ContextCompat.getColor(context, R.color.color_E6ffffff)
        textPaint.isAntiAlias = true
        textPaint.textSize = sp2px(context, 13f).toFloat()
        textPaint.color = ContextCompat.getColor(context, R.color.color_47555f)
        textPaint.textAlign = Paint.Align.CENTER
        val fontMetrics = textPaint.fontMetrics
        mTopPadding = -((fontMetrics.bottom - fontMetrics.top) / 2 + fontMetrics.top)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        //悬停月份栏
        val manager = parent.layoutManager as GridLayoutManager
        val position = manager.findFirstVisibleItemPosition()
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val viewHolder = parent.findViewHolderForAdapterPosition(position)
        val item = viewHolder?.itemView
        var flag = false
        // 如果要到了这个月的最后一排，那么会有一个向上滑动的动画
        if (isLast(position) && null != item) {
            if (item.height + item.top < top) {
                c.save()
                flag = true
                c.translate(0f, (item.height + item.top - top).toFloat())
            }
        }
        val rect = RectF(
            0f,
            parent.paddingTop.toFloat(),
            parent.right.toFloat(),
            (parent.paddingTop + top).toFloat()
        )
        c.drawRect(rect, paint)
        c.drawText(
            mCallBack(position),
            rect.centerX(),
            rect.centerY() + mTopPadding,
            textPaint
        )

        if (flag) {
            c.restore()
        }
    }

    /**
     * 判断是否是该月的最后一排
     */
    private fun isLast(pos: Int): Boolean = mCallBack(pos) != mCallBack(pos + 7)
}