package com.vio.calendar.ui.calendar

import android.content.Context
import android.view.ViewGroup
import android.graphics.Bitmap
import android.view.View
import android.widget.BaseAdapter


class CalendarGridAdapter(private val mContext: Context?) : BaseAdapter() {
    private val mis_fotos: Array<Bitmap>? = null

    override fun getCount(): Int {
        return mis_fotos!!.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
//        val imageView: ImageView
//        if (convertView == null) {
//            imageView = ImageView(mContext)
////            imageView.setLayoutParams(LinearLayout.LayoutParams(width / 3, height / 3))
////            imageView.setScaleType(ImageView.setScaleType(ImageView.ScaleType.FIT_XY))
//            imageView.setPadding(0, 0, 0, 0)
//        } else {
//            imageView = convertView as ImageView?
//        }
//        imageView.setImageBitmap(mis_fotos!![position])
        return null
    }
}