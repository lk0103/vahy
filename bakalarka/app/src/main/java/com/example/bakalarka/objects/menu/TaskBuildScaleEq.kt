package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.bakalarka.equation.Equation
import com.example.vahy.objects.ScreenObject

class TaskBuildScaleEq(private val context: Context,
                       private var equations : List<Equation> = listOf())
    : ScreenObject(false, false) {
    private var blocks = 1

    init {
        width = 10
        height = 10
        blocks = equations.size
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        width = w
        height = h
        x = xStart
        y = yStart
    }

    override fun draw(canvas: Canvas, paint: Paint){
        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER

        var heightRow = height / blocks
        var widthRow = width
        if (vertical(paint) < horizontal(paint)){
            heightRow = height
            widthRow = width / blocks
        }

        (0 until blocks).forEach { i ->
            val yText = if (heightRow == height) y + heightRow / 2F + heightRow / 10F
                        else y + heightRow * i + heightRow / 2F + heightRow / 10F
            val xText = if (widthRow == width) x + widthRow / 2F
                        else x + widthRow * i + widthRow / 2F
            calculateTextSize(heightRow, widthRow, i, paint)
            canvas.drawText(equations[i].toString(), xText, yText, paint)
        }
    }

    private fun vertical(paint: Paint): Float {
        val heightRow = height / blocks
        val widthRow = width
        return smallestTextSize(heightRow, widthRow, paint)
    }

    private fun horizontal(paint: Paint): Float {
        val heightRow = height
        val widthRow = width / blocks
        return smallestTextSize(heightRow, widthRow, paint)
    }

    private fun smallestTextSize(heightRow: Int, widthRow: Int, paint: Paint): Float {
        var smallestTextSize = heightRow / 2F + 1
        (0 until blocks).forEach { i ->
            calculateTextSize(heightRow, widthRow, i, paint)
            if (smallestTextSize > paint.textSize) {
                smallestTextSize = paint.textSize
            }
        }
        return smallestTextSize
    }

    private fun calculateTextSize(heightRow: Int, widthRow: Int, i: Int, paint: Paint) {
        paint.textSize = heightRow / 2F
        val eqStr = equations[i].toString()

        val bounds = Rect()
        paint.getTextBounds(eqStr, 0, eqStr.length, bounds)

        while (bounds.width() > widthRow * 9 / 10) {
            paint.textSize -= 2F
            paint.getTextBounds(eqStr, 0, eqStr.length, bounds)
        }
    }

    fun setEquations(eq : List<Equation>){
        equations = eq
        blocks = equations.size
    }

    fun getEquations() : List<Equation> = equations
}