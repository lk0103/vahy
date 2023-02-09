package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class Ballon(private val context: Context, private val value : Int,
             touchable : Boolean = false,
             draggable : Boolean = true)
    : ScaleValue(value, touchable, draggable) {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.ballon1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, width, height, true);
    }

    fun setImageParamForScale(){
        image = ContextCompat.getDrawable(context, R.drawable.ballon2)!!.toBitmap()
        width = 200
        height = 300
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        image = ContextCompat.getDrawable(context, R.drawable.ballon2)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, w, h, true)
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        x = xStart
        y = yStart
        width = h * width / height
        height = h
        widthBordingBox = w
        heightBordingBox = h
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun drawValue(paint: Paint, canvas: Canvas) {
        val xBox = x
        val yBox = y - height / 5

        paint.color = Color.BLACK
        paint.textSize = width * 5F / 19F
        paint.textAlign = Paint.Align.CENTER

        canvas.drawText(
            value.toString(),
            xBox.toFloat(),
            yBox.toFloat(), paint
        )
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Ballon(context, value, touchable, draggable))

    override fun evaluate(): Int = value
}