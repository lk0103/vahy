package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import com.example.bakalarka.objects.*
import com.example.vahy.objects.ScreenObject

class TaskSolveSolution(private val context: Context, private var variables : List<ScaleVariable>)
    : ScreenObject(false, false) {
    private var colums = 1
    private var numberPickers = mutableMapOf<ScaleVariable, Weight>()
    private var draggedToOriginal : MutableMap<Weight, Weight> = mutableMapOf()

    init {
        width = 100
        height = 10
        colums = variables.size
        variables.forEach { numberPickers[it] = Weight(context, 1)}
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
            val w = sizeColumn / 3
            val h = height * 4 / 5
            var startX = x + sizeColumn * i + sizeColumn / 2 - width / 10
            val startY = y + height / 2
            variable.sizeChanged(w, h, startX, startY)

            startX += 2 * width / 10
            val weight = numberPickers[variable]
            weight?.sizeChanged(w * 2, height * 40 / 39, startX, startY)
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
        draggedToOriginal.keys.forEach { it.draw(canvas, paint) }
    }

    fun setVariables(v : List<ScaleVariable>){
        variables = v
        colums = variables.size
        numberPickers = mutableMapOf()
        variables.forEach { numberPickers[it] = Weight(context, 1) }
        changeSizeObjects()
    }

    fun onTouchDown(x1 : Int, y1: Int){
        draggedToOriginal = numberPickers.values.filter { it.isIn(x1, y1)}
            .map { Pair(it.makeCopy() as Weight, it) }.toMap().toMutableMap()
    }

    fun onTouchMove(event: MotionEvent) {
        draggedToOriginal.keys.forEach { it.move(event.x.toInt(), event.y.toInt()) }

    }

    fun onTouchUp() {
        plusMinus1ToDragged()
        draggedToOriginal = mutableMapOf()
    }

    private fun plusMinus1ToDragged(){
        val tolerance = 0
        draggedToOriginal.forEach { (dragged, original) ->
            if (dragged.y < original.y - tolerance) {
                if (original.evaluate() < 30)
                    original.increment()
            } else if (dragged.y > original.y + tolerance) {
                if (original.evaluate() > 0)
                    original.decrement()
            }
        }
    }

    fun getUserSolutions() : Map<String, Int> =
        numberPickers.mapValues { it.value.evaluate() }.mapKeys { it.key::class.toString() }
}