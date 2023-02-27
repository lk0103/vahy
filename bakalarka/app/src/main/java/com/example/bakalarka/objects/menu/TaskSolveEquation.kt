package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.*
import com.example.bakalarka.objects.ScaleVariable
import com.example.vahy.objects.ScreenObject

class TaskSolveEquation(private val context: Context, private var solutions : Map<ScaleVariable, Int>)
    : ScreenObject(false, false) {
    private var colums = 1
    private var pickedNumbers = mutableMapOf<ScaleVariable, CustomNumberPicker>()


    init {
        width = 100
        height = 10
        colums = solutions.size
        solutions.keys.forEach { pickedNumbers[it] = CustomNumberPicker(context) }
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
        solutions.keys.forEach { variable ->
            val sizeColumn = width / colums
            val w = sizeColumn / 6
            val h = height * 3 / 4
            val startX = x + sizeColumn * i + sizeColumn / 2 - w
            val startY = y + height / 2
            variable.sizeChanged(w, h, startX, startY)

            val numberPicker = pickedNumbers[variable]
            numberPicker?.sizeChanged(w * 2, height, startX + w * 3 / 2, y )
            i++
        }
    }

    override fun draw(canvas: Canvas, paint: Paint){
        solutions.keys.forEach { variable ->
            variable.draw(canvas, paint)
            pickedNumbers[variable]?.draw(canvas, paint)
        }


        (0 until colums).forEach { j ->
            val sizeColumn = width / colums
            val xText = x + sizeColumn * j + sizeColumn / 2F
            val yText = y + height * 2 / 3F

            paint.color = Color.BLACK
            paint.textSize = width / 10F
            paint.textAlign = Paint.Align.CENTER

            canvas.drawText("=", xText, yText, paint)
        }
    }

    fun setSolutions(s : Map<ScaleVariable, Int>){
        solutions = s
        colums = solutions.size
        solutions.keys.forEach { pickedNumbers[it] = CustomNumberPicker(context) }
        changeSizeObjects()
    }

    fun touch(x1 : Int, y1: Int){
        pickedNumbers.values.filter { it.isIn(x1, y1)}
            .forEach { it.touch(x1, y1) }
    }
}