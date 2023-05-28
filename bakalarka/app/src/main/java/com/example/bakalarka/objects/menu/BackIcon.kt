package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class BackIcon(context: Context)
    : Icon(context) {

    init {
        imageType = R.drawable.arrow_back
        image = ContextCompat.getDrawable(context, imageType)!!.toBitmap()
        width = image!!.width
        height = image!!.height
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}