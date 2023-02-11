package com.example.bakalarka.objects

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class Weight(private val context: Context, value : Int,
             touchable : Boolean = false,
             draggable : Boolean = true)
    : ScaleValue(value, touchable, draggable)  {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.weight)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        image = ContextCompat.getDrawable(context, R.drawable.weight)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Weight(context , value, touchable, draggable))


    override fun drawValue(paint: Paint, canvas: Canvas) {
        val xBox = x
        val yBox = y + height / 4

        paint.color = Color.WHITE
        paint.textSize = width * 7F / 19F
        paint.textAlign = Paint.Align.CENTER

        canvas.drawText(
            value.toString(),
            xBox.toFloat(),
            yBox.toFloat(), paint
        )
    }

    override fun evaluate(): Int = value

    override fun decrement(){
        if (value > 1)
            value--
    }

}