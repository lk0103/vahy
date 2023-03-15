package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject
import java.lang.Math.sin

class ArmOfScale(private val context : Context, private var angle : Float = 0F) :
    ScreenObject(false, false){
    private val defaultWidthScale = 1000
    private val defaultHeightScale = 607
    private var widthOfScale = 100
    private var heightOfScale = 100
    var matrix = Matrix()
    init {
        image = ContextCompat.getDrawable(context, R.drawable.arm_of_scale)!!.toBitmap()
        z = 2
        defaultSizeValues()
        widthOfScale = defaultWidthScale
        heightOfScale = defaultHeightScale

        rotate(angle)
        image = Bitmap.createScaledBitmap(image!!, width, height, true);
    }

    private fun defaultSizeValues() {
        width = 1000
        height = 245
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, R.drawable.arm_of_scale)!!.toBitmap()
    }

    fun changeSizeInScaleView(widthView : Int, heightView : Int, padding : Int,
                              scaleWidthProportion : Pair<Int, Int>){
        sizeChanged(
            widthView * scaleWidthProportion.first / scaleWidthProportion.second - padding * 2,
            heightView - padding * 2,
            widthView / 500 + padding, heightView / 20 + padding
        )
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        if (image == null)
            return
        defaultSizeValues()
        x = xStart
        y = yStart
        widthOfScale = w
        heightOfScale = w * defaultHeightScale / defaultWidthScale
        while (heightOfScale + y >= h){
            widthOfScale -= 5
            heightOfScale -= 5
        }
        width = widthOfScale
        height = heightOfScale * height / defaultHeightScale
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        rotationMatrix()
    }


    //////////////////////////////////////
    override fun draw(canvas: Canvas, paint: Paint) {
        if (image == null && !visibility)
            return
        canvas.drawBitmap(image!!, matrix, null)
        paint.color = Color.RED
    }

    private fun rotationMatrix() {
        matrix = Matrix()
        matrix.postRotate(angle, width / 2F, height/ 2F)
        matrix.postTranslate(x.toFloat(), y.toFloat())
    }

    fun rotate(a : Float){
        angle = a
        rotationMatrix()
    }
}