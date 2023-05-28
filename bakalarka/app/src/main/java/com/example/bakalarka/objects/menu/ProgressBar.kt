package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject


class ProgressBar(private val context: Context)
    : ScreenObject(false, false) {
    private var maxValue = 10
    private var value = 1
    private val widthBorder = 3

    init {
        width = 100
        height = 10
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        width = w
        height = h
        x = xStart
        y = yStart
    }

    override fun draw(canvas: Canvas, paint: Paint){
        drawBorder(paint, canvas)
        drawBar(paint, canvas)

        if (value > 0) {
            drawProgress(paint, canvas)
        }
    }

    private fun drawProgress(paint: Paint, canvas: Canvas) {
        paint.color = ContextCompat.getColor(context, R.color.icons_color)
        val x1 = x + widthBorder * 3F
        val x2 = x + widthBar()
        val heightPadding = if (x2 - x1 < 50) height / 16 else 0
        val y1 = y + widthBorder * 3.5F + heightPadding
        val y2 = y + height - widthBorder * 3.5F - heightPadding
        roundedRectangle(canvas, paint, x1, y1, x2, y2)
    }

    private fun drawBar(paint: Paint, canvas: Canvas) {
        paint.color = ContextCompat.getColor(context, R.color.menu_background)
        paint.strokeWidth = 0F
        roundedRectangle(
            canvas, paint,
            x + widthBorder.toFloat(), y + widthBorder.toFloat(),
            x + widthBorder + width - widthBorder * 2F,
            y + height - widthBorder.toFloat()
        )
    }

    private fun drawBorder(paint: Paint, canvas: Canvas) {
        paint.color = Color.BLACK
        paint.strokeWidth = widthBorder.toFloat()
        roundedRectangle(
            canvas, paint, x.toFloat(), y.toFloat(),
            x + width.toFloat(), y + height.toFloat()
        )
    }

    private fun roundedRectangle(canvas: Canvas, paint: Paint, x1 : Float,
                                y1 : Float, x2 : Float, y2 : Float) {
        canvas.drawRoundRect(RectF(x1, y1, x2, y2), 40F, 40F, paint)
    }

    fun widthBar() : Float = (width - 3F * widthBorder) * Math.min(value, maxValue) / maxValue


    fun updateValue(newValue : Int){
        value = newValue
    }

    fun updateMaxValue(newMax : Int){
        maxValue = newMax
    }
}