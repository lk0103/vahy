package com.example.vahy.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.objects.ObjectsToChooseFrom

class Bin(private val context : Context) :
    ScreenObject(false, true){

    init {
        image = ContextCompat.getDrawable(context, R.drawable.bin1)!!.toBitmap()
        z = 2
        defaultSizeValues()
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    private fun defaultSizeValues() {
        width = 200
        height = 300
    }

    override fun reloadImage(w : Int, h : Int){
        image = ContextCompat.getDrawable(context, R.drawable.bin1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    fun changeSizeInScaleView(widthView : Int, heightView : Int, padding : Int,
                              objsToChooseFromVis: Boolean, openPackageVis : Boolean){
        if (openPackageVis && objsToChooseFromVis)
            sizeChanged(widthView / 10 - padding * 2, heightView - padding * 2,
                widthView * 33 / 48 + padding, padding)

        else if (openPackageVis && !objsToChooseFromVis)
            sizeChanged(widthView / 9 - padding * 2, heightView - padding * 2,
                widthView * 78 / 96 + padding, heightView / 10)
        else
            sizeChanged(
                widthView / 9 - padding * 2, heightView - padding * 2,
                widthView * 81 / 96 + padding,
                heightView * 3 / 5 + padding
            )
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        defaultSizeValues()
        super.sizeChanged(w, h, xStart, yStart)
    }
}