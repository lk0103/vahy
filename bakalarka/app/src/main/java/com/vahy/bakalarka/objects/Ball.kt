package com.vahy.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R

class Ball(private val context: Context, value : Int,
           dragFrom : Boolean = false,
           dragTo : Boolean = true)
    : ScaleVariable(value, dragFrom, dragTo) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.ball1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, R.drawable.ball1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Ball(context , value, dragFrom, dragTo))

    override fun evaluate(): Int = value

}
