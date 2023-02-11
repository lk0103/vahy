package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class Ball(private val context: Context, value : Int,
           touchable : Boolean = false,
           draggable : Boolean = true)
    : ScaleVariable(value, touchable, draggable) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.ball1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        image = ContextCompat.getDrawable(context, R.drawable.ball1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Ball(context , value, touchable, draggable))

    override fun evaluate(): Int = value

}

fun Ball.getWidth(): Int = width