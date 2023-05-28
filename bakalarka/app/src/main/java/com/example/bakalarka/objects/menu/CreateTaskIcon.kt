package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class CreateTaskIcon(context: Context)
    : Icon(context) {

    init {
        imageType = R.drawable.build_icon_pencil_line
        image = ContextCompat.getDrawable(context, imageType)!!.toBitmap()
        width = image?.width ?: 0
        height = image?.height ?: 0
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}