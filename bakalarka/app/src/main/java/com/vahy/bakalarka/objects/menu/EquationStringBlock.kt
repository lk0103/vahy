package com.vahy.bakalarka.objects.menu

import android.content.Context
import android.graphics.*
import android.graphics.Paint.Style
import androidx.core.content.ContextCompat
import com.vahy.bakalarka.R
import com.vahy.vahy.objects.ScreenObject

class EquationStringBlock(private val context: Context, private var equation : String)
            :ScreenObject(false, false){
    var isMarked = false
    var textSize = 0F

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        x = xStart
        y = yStart
        width = w
        height = h
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Style.FILL
        paint.textSize = textSize
        textBackground(paint, canvas)
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(equation, x.toFloat(), y.toFloat(), paint)
    }

    private fun textBackground(paint: Paint, canvas: Canvas) {
        val bounds = Rect()
        paint.getTextBounds(equation, 0, equation.length, bounds)
        paint.color = Color.BLACK
        paint.strokeWidth = 0F
        val text = Math.max(bounds.height(), height / 4)
        roundedRectangle(
            canvas, paint, x - bounds.width() * 13F / 24 - 8,
            y - text - 3F - 10 - 3,
            x + bounds.width() * 13F / 24 + 8, y + text / 2F + 3 + 3
        )
        if (isMarked)
            paint.color = ContextCompat.getColor(context, R.color.equationStringBg)
        else
            paint.color = Color.WHITE

        paint.strokeWidth = 0F
        roundedRectangle(
            canvas, paint, x - bounds.width() * 13F / 24 - 5, y - text - 10F - 3,
            x + bounds.width() * 13F / 24 + 5, y + text / 2F + 3
        )
    }

    private fun roundedRectangle(canvas: Canvas, paint: Paint, x1 : Float,
                                 y1 : Float, x2 : Float, y2 : Float) {
        canvas.drawRoundRect(RectF(x1, y1, x2, y2), 40F,40F, paint)
    }

    override fun isIn(x1 : Int, y1: Int) : Boolean =
        (x1 >= x - width / 2 && x1 <= x + width / 2 &&
                y1 >= y - height / 2 && y1 <= y + height / 2)

    fun changeEquation(eq : String){
        equation = if (eq == "=") " = " else eq
    }
}