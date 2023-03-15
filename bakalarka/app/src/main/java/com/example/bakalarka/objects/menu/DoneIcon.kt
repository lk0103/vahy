package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.objects.ScaleVariable
import com.example.vahy.objects.ScreenObject

class DoneIcon(context: Context)
    : Icon(context) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.done)!!.toBitmap()
        width = 300
        height = 300
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}