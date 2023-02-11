package com.example.vahy.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.objects.EquationObject
import com.example.bakalarka.objects.ScaleValue
import com.example.bakalarka.objects.ScaleVariable

class OpenPackage(context : Context, touchable : Boolean = true)
    : ScreenObject(touchable) {

    private var insideObject = mutableListOf<EquationObject>()

    init {
        image = ContextCompat.getDrawable(context, R.drawable.open_package)!!.toBitmap()
        z = 2
        width = 300
        height = 241
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun addObjectIn(obj : EquationObject){
        if (obj is ScaleValue || obj is ScaleVariable)
            insideObject.add(obj)
    }
}