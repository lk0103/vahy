package com.vahy.bakalarka.objects

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R

class Weight(private val context: Context, value : Int,
             dragFrom : Boolean = false,
             dragTo : Boolean = false)
    : ScaleValue(value, dragFrom, dragTo)  {

    init {
        image = ContextCompat.getDrawable(context, R.drawable.weight)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, R.drawable.weight)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Weight(context , value, dragFrom, dragTo))

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

    override fun isNotValidValue(): Boolean = value <= 0
}