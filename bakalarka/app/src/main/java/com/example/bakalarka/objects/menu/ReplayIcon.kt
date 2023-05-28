package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject

class ReplayIcon(context: Context)
    : Icon(context) {

    init {
        imageType = R.drawable.redo
        image = ContextCompat.getDrawable(context, imageType)!!.toBitmap()
        width = 300
        height = 300
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

}