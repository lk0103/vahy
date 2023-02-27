package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject
import java.lang.Math.sin

class ArmOfScale(context : Context, private var angle : Float = 0F) :
    ScreenObject(false, false){
    private var defaultWidthScale = 100
    private var defaultHeightScale = 100
    private var widthOfScale = 100
    private var heightOfScale = 100
    var matrix = Matrix()
    init {
        image = ContextCompat.getDrawable(context, R.drawable.arm_of_scale)!!.toBitmap()
        z = 2
        width = 1000
        height = 245
        defaultWidthScale = 1000
        defaultHeightScale = 607
        widthOfScale = defaultWidthScale
        heightOfScale = defaultHeightScale

        rotate(angle)
        image = Bitmap.createScaledBitmap(image!!, width, height, true);
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        if (image == null)
            return
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
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        rotationMatrix()
    }

    //////////////////////////////////////
    override fun draw(canvas: Canvas, paint: Paint) {
        if (image == null)
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

    fun getPositionLeftHolder() : Pair<Int, Int> {
        val paddding = width * 17 / 96
        val r = width / 2 - paddding
        val radians = 2 * Math.PI * angle.toDouble() / 360
        return Pair((x + paddding + (r - (r * Math.cos(radians)))).toInt(),
            (y + heightOfScale / 5 - r * sin(radians)).toInt())
    }

    fun getPositionRightHolder() : Pair<Int, Int> {
        val paddding = width * 37 / 192
        val r = width / 2 - paddding
        val radians = 2 * Math.PI * -angle.toDouble() / 360
        return Pair((x + width - paddding - (r - (r * Math.cos(radians)))).toInt(),
            (y + heightOfScale / 5 - 2 * r * sin(radians)).toInt())
    }
}