package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject

open class Icon(protected var context: Context)
    : ScreenObject(false, false) {

    var imageType = R.drawable.done
    init {
        width = 300
        height = 300
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, imageType)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        if (image == null)
            return
        x = xStart
        y = yStart
        height = w * height / width
        width = w
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
            return
        }

        if (height < (image?.height ?: 0) || width < (image?.width ?: 0)){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}