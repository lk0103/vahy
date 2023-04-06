package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.equation.Equation
import com.example.vahy.objects.ScreenObject


class TaskBuildScaleEq(private val context: Context,
                       private var equations : List<Equation> = listOf())
    : ScreenObject(false, false) {
    private var blocks = 1
    private var indexMarked = -1
    private var equationsBlocks : MutableList<EquationStringBlock> = mutableListOf()

    init {
        width = 10
        height = 10
        blocks = equations.size
        equations.forEach { eq ->
            equationsBlocks.add(EquationStringBlock(context, eq.toString()))
        }
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        width = w
        height = h
        x = xStart
        y = yStart

        changeSizeObjects()
    }

    private fun changeSizeObjects() {
        var heightRow = height / blocks
        var widthRow = width
        if (vertical() < horizontal()) {
            heightRow = height
            widthRow = width / blocks
        }
        (0 until blocks).forEach { i ->
            val yText = if (heightRow == height) y + heightRow / 2F + heightRow / 10F
            else y + heightRow * i + heightRow / 2F + heightRow / 10F
            val xText = if (widthRow == width) x + widthRow / 2F
            else x + widthRow * i + widthRow / 2F
            equationsBlocks[i].textSize = calculateTextSize(heightRow, widthRow, i)
            equationsBlocks[i].sizeChanged(widthRow, heightRow, xText.toInt(), yText.toInt())
        }
    }

    override fun draw(canvas: Canvas, paint: Paint){
        equationsBlocks.forEach { it.draw(canvas, paint) }
    }


    private fun vertical(): Float {
        val heightRow = height / blocks
        val widthRow = width
        return smallestTextSize(heightRow, widthRow)
    }

    private fun horizontal(): Float {
        val heightRow = height
        val widthRow = width / blocks
        return smallestTextSize(heightRow, widthRow)
    }

    private fun smallestTextSize(heightRow: Int, widthRow: Int): Float {
        var smallestTextSize = heightRow / 2F + 1
        (0 until blocks).forEach { i ->
            val textSize = calculateTextSize(heightRow, widthRow, i)
            if (smallestTextSize > textSize) {
                smallestTextSize = textSize
            }
        }
        return smallestTextSize
    }


    private fun calculateTextSize(heightRow: Int, widthRow: Int, i: Int) : Float{
        val paint = Paint()
        paint.textSize = heightRow / 2F
        val eqStr = equations[i].toString()

        val bounds = Rect()
        paint.getTextBounds(eqStr, 0, eqStr.length, bounds)

        while (bounds.width() > widthRow * 9 / 10) {
            paint.textSize -= 2F
            paint.getTextBounds(eqStr, 0, eqStr.length, bounds)
        }
        return paint.textSize
    }

    fun getIndexTouchedEquation(x1 : Int, y1 : Int) : Int{
        (0 until blocks).forEach { i ->
            if (equationsBlocks[i].isIn(x1, y1))
                return i
        }
        return -1
    }

    fun setIndexMarked(ix : Int) : Boolean{
        if (indexMarked != ix) {
            indexMarked = ix
            (0 until blocks).forEach { i ->
                if (i != ix)
                    equationsBlocks[i].isMarked = false
                else
                    equationsBlocks[i].isMarked = true
            }
            return true
        }
        return false
    }

    fun setEquations(eq : List<Equation>){
        equations = eq
        blocks = equations.size

        equationsBlocks = mutableListOf()
        equations.forEach { eq ->
            equationsBlocks.add(EquationStringBlock(context, eq.toString()))
        }
        if (equations.size > 1)
            setIndexMarked(0)
    }

    fun getEquations() : List<Equation> = equations
}