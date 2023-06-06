package com.vahy.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R

class DoneIcon(context: Context)
    : Icon(context) {

    init {
        imageType = R.drawable.done
        image = ContextCompat.getDrawable(context, imageType)!!.toBitmap()
        width = 300
        height = 300
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }
}