package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class Cube(private val context: Context, private val value : Int,
           touchable : Boolean = false,
           draggable : Boolean = true)
    : ScaleVariable(value, touchable, draggable) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.cube1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, width, height, true);
    }

    override fun reloadImage(w : Int, h : Int){
        image = ContextCompat.getDrawable(context, R.drawable.cube1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Cube(context , value, touchable, draggable))

    override fun evaluate(): Int = value
}