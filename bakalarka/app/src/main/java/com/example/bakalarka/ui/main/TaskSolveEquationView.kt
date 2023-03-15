package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.objects.ScaleVariable
import com.example.bakalarka.objects.menu.*
import com.example.vahy.objects.ScreenObject

class TaskSolveEquationView(context: Context, attrs: AttributeSet)
    : View(context, attrs) {
    private val screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    var checkSolution = false

    init {
        screenObjects.add(DoneIcon(context))
        screenObjects.add(TaskSolveEquation(context, listOf()))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        screenObjects.forEach { obj ->
            val widthIcon = widthView / 8
            val widthPadding = widthView / 100
            val heightPadding = heightView / 10
            if (obj is DoneIcon) {
                obj.sizeChanged(
                    widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthView - widthIcon + widthPadding,
                    heightView / 2 - widthIcon / 2
                )
            } else if (obj is TaskSolveEquation) {
                obj.sizeChanged(
                    widthView - widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthPadding, heightPadding
                )
            } else
                obj.sizeChanged(widthView, heightView, 0, 0)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        paint.color = ContextCompat.getColor(context, R.color.icons_color)
        paint.strokeWidth = 4F
        canvas.drawLine(0F, heightView.toFloat() - 10,
            widthView.toFloat(), heightView.toFloat() - 10, paint)

        screenObjects.forEach { it.draw(canvas, paint) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true

        val action = event.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            val touchedTaskSolve = screenObjects.filter { it is TaskSolveEquation &&
                    it.isIn(event.x.toInt(), event.y.toInt()) }
            if (touchedTaskSolve.size > 0){
                touchedTaskSolve.forEach {
                    (it as TaskSolveEquation).touch(event.x.toInt(), event.y.toInt())
                }
                invalidate()
            }

            if (screenObjects.any { it is DoneIcon && it.isIn(event.x.toInt(), event.y.toInt()) })
                checkSolution = true
        }
        return true
    }


    fun setMapSolutions(variables : List<ScaleVariable>){
        screenObjects.filter { it is TaskSolveEquation }
            .forEach { (it as TaskSolveEquation).setVariables(variables)}
        invalidate()
    }

    fun getUserSolutions() : Map<String, Int> =
        (screenObjects.filter { it is TaskSolveEquation }
            .first() as TaskSolveEquation).getUserSolutions()
}