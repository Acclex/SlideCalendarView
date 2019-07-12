package com.acclex.calendarview.view

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acclex.calendarview.R
import com.acclex.calendarview.bean.DateBean
import com.acclex.calendarview.bean.TYPE_DATE_BLANK
import com.acclex.calendarview.bean.TYPE_DATE_NORMAL
import com.acclex.calendarview.bean.TYPE_DATE_TITLE
import kotlinx.android.synthetic.main.item_calendar_date.view.*
import kotlinx.android.synthetic.main.item_calendar_title.view.*

/**
 * Created by acclex on 2019-07-12
 */
class DateAdapter : RecyclerView.Adapter<DateAdapter.DateHolder>() {
    private val mList: MutableList<DateBean> = mutableListOf()
    private var onRvItemClickListener: ((DateBean, View, Int) -> Unit)? = null


    fun setNewList(list: MutableList<DateBean>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: ((DateBean, View, Int) -> Unit)?) {
        onRvItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateHolder =
        when (viewType) {
            TYPE_DATE_BLANK -> DateHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_calendar_blank,
                    parent,
                    false
                )
            )
            TYPE_DATE_TITLE -> DateHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_calendar_title,
                    parent,
                    false
                )
            )
            else -> DateHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_calendar_date,
                    parent,
                    false
                )
            )
        }

    override fun getItemCount(): Int = mList.size

    override fun getItemViewType(position: Int): Int = mList[position].type

    override fun onBindViewHolder(holder: DateHolder, position: Int) {
        holder.bindView(mList[position])
        holder.itemView.setOnClickListener {
            if (mList[position].type == TYPE_DATE_NORMAL) {
                onRvItemClickListener?.invoke(mList[position], holder.itemView, position)
            }
        }
    }

    class DateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(bean: DateBean) {
            when (bean.type) {
                TYPE_DATE_BLANK -> {
                }
                TYPE_DATE_TITLE -> {
                    itemView.tv_calender_title.text = bean.groupName
                }
                else -> {
                    itemView.run {
                        tv_day.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.color_47555f
                            )
                        )
                        tv_day.setBackgroundColor(Color.WHITE)
                    }

                    if (bean.day <= 0) {
                        itemView.tv_day.text = ""
                    } else {
                        itemView.tv_day.text = bean.day.toString()
                    }

                    when {
                        bean.isChooseDay && bean.isToday -> {
                            itemView.tv_day.setTextColor(Color.WHITE)
                            itemView.tv_day.background = ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.shape_radius4_color_52c8ff
                            )
                        }

                        bean.isChooseDay -> {
                            itemView.tv_day.setTextColor(Color.WHITE)
                            itemView.tv_day.background = ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.shape_radius4_color_52c8ff
                            )
                        }
                        bean.isToday -> {
                            itemView.tv_day.setTextColor(
                                ContextCompat.getColor(
                                    itemView.context,
                                    R.color.color_52C8FF
                                )
                            )
                            itemView.tv_day.background = ContextCompat.getDrawable(
                                itemView.context,
                                R.drawable.shape_transparent_52c8ff_4dp
                            )
                        }
                    }
                }
            }
        }
    }
}