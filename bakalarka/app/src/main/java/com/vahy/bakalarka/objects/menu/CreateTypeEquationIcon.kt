package com.vahy.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R

class CreateTypeEquationIcon(context: Context, private val numberOfEquations: Int)
    : Icon(context) {

    init {
        if (numberOfEquations <= 1)
            imageType = R.drawable.equation_icon
        else
            imageType = R.drawable.system_equation_icon

        image = ContextCompat.getDrawable(context, imageType)!!.toBitmap()
        width = image?.width ?: 300
        height = image?.height ?: 300
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    fun getType() = numberOfEquations
}