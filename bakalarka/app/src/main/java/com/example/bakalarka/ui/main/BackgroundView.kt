package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.objects.*
import com.example.bakalarka.objects.menu.ScalePattern

class BackgroundView(context: Context, attrs: AttributeSet)
    : View(context, attrs) {
    var widthView = 1
    var heightView = 1
    private val paint = Paint()
    private val pattern = ScalePattern(context)
    var bgColor = R.color.main_menu_background

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        pattern.sizeChanged(w / 9, w / 9, 100, 150)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        canvas.drawColor(ContextCompat.getColor(context, bgColor))
        createPattern(canvas)
    }

    private fun createPattern(canvas: Canvas) {
        val size = widthView / 9
        val paddingX = widthView / 25
        val paddingY = heightView / 50
        val shiftX = size / 2 + paddingX / 2
        val shiftY = size / 4
        (-1..(widthView - 2 * paddingX) / (size + paddingX) + 1).forEach { i ->
            (0 .. (heightView - 2 * paddingY) / (size + paddingY)).forEach { j ->
                val xPos = (if (j % 2 == 1) i - 1 else i) * (size + paddingX)
                pattern.sizeChanged(
                    size, size,
                    xPos + if (j % 2 == 1) shiftX else 0,
                    j * size + paddingY * (j + 1) +
                            if (i % 2 == 1 && j % 2 == 0 ||
                                i % 2 == 0 && j % 2 == 1) shiftY else 0
                )
                pattern.draw(canvas, paint)
            }
        }
    }

    fun changeBackground(patternColor : Int, backgroundColor : Int){
        bgColor = backgroundColor
        pattern.changePattern(patternColor)
        invalidate()
    }
}