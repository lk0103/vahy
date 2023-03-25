package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class RedoIcon(context: Context)
    : Icon(context) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.redo)!!.toBitmap()
        width = 300
        height = 300
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

}