package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.objects.EquationObject

class FailSuccessIcon(context: Context, success : Boolean)
    : EquationObject() {

    init {
        val img = if (success) R.drawable.pass else R.drawable.fail
        image = ContextCompat.getDrawable(context, img)!!.toBitmap()
        width = 512
        height = 512
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    fun changeSizeInScaleView(widthView : Int, heightView : Int){
        val w = widthView * 5 / 8
        val h = heightView * 5 / 8
        sizeChanged(
            w, h,widthView / 3, heightView / 2
        )
    }
}