package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class Cube(private val context: Context, value : Int,
           dragFrom : Boolean = false,
           dragTo : Boolean = true)
    : ScaleVariable(value, dragFrom, dragTo) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.cube1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, width, height, true);
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, R.drawable.cube1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Cube(context , value, dragFrom, dragTo))

    override fun evaluate(): Int = value
}