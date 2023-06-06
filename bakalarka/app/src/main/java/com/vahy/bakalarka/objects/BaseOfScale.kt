package com.vahy.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R
import com.vahy.vahy.objects.ScreenObject

class BaseOfScale(private val context : Context) :
    ScreenObject(false, false){

    var shiftX = 10

    init {
        image = ContextCompat.getDrawable(context, R.drawable.base_of_scale1)!!.toBitmap()
        z = 2
        defaultSizeValues()
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, R.drawable.base_of_scale1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    fun changeSizeInScaleView(widthView : Int, heightView : Int, padding : Int,
                              scaleWidthProportion : Pair<Int, Int>){
        sizeChanged(
            widthView * scaleWidthProportion.first / scaleWidthProportion.second - padding * 2,
            heightView - padding * 2,
            widthView / 500 + padding + shiftX,  padding
        )
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        defaultSizeValues()
        super.sizeChanged(w, h, xStart, yStart)
    }

    private fun defaultSizeValues() {
        width = 1000
        height = 607
    }
}