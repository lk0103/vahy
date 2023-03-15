package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.*
import android.util.Log
import com.example.bakalarka.objects.ScaleVariable
import com.example.vahy.objects.ScreenObject

class TaskSolveEquation(private val context: Context, private var variables : List<ScaleVariable>)
    : ScreenObject(false, false) {
    private var colums = 1
    private var numberPickers = mutableMapOf<ScaleVariable, CustomNumberPicker>()


    init {
        width = 100
        height = 10
        colums = variables.size
        variables.forEach { numberPickers[it] = CustomNumberPicker(context) }
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        width = w
        height = h
        x = xStart
        y = yStart
        changeSizeObjects()
    }

    fun changeSizeObjects(){
        var i = 0
        variables.forEach { variable ->
            val sizeColumn = width / colums
            val xMargin = 0
            val w = sizeColumn / 6
            val h = height * 3 / 4
            val startX = x + sizeColumn * i + sizeColumn / 2 - width / 10
            val startY = y + height / 2
            variable.sizeChanged(w, h, startX, startY)

            val numberPicker = numberPickers[variable]
            numberPicker?.sizeChanged(w * 3 / 2 - xMargin * 2, height,
                startX + width / 6 + xMargin, y )
            i++
        }
    }

    override fun draw(canvas: Canvas, paint: Paint){
        variables.forEach { variable ->
            variable.draw(canvas, paint)
            numberPickers[variable]?.draw(canvas, paint)
        }


        (0 until colums).forEach { j ->
            val sizeColumn = width / colums
            val xText = x + sizeColumn * j + sizeColumn / 2F
            val yText = y + height * 3 / 4F

            paint.color = Color.BLACK
            paint.textSize = width / 10F
            paint.textAlign = Paint.Align.CENTER

            canvas.drawText("=", xText, yText, paint)
        }
    }

    fun setVariables(v : List<ScaleVariable>){
        variables = v
        colums = variables.size
        numberPickers = mutableMapOf()
        variables.forEach { numberPickers[it] = CustomNumberPicker(context) }
        changeSizeObjects()
    }

    fun touch(x1 : Int, y1: Int){
        numberPickers.values.filter { it.isIn(x1, y1)}
            .forEach { it.touch(x1, y1) }
    }

    fun getUserSolutions() : Map<String, Int> =
        numberPickers.mapValues { it.value.value }.mapKeys { it.key::class.toString() }
}