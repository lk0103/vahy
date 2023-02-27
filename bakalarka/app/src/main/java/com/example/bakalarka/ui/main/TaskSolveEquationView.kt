package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.NumberPicker
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.objects.Ball
import com.example.bakalarka.objects.Cube
import com.example.bakalarka.objects.Cylinder
import com.example.bakalarka.objects.ScaleVariable
import com.example.bakalarka.objects.menu.*
import com.example.vahy.objects.ScreenObject

class TaskSolveEquationView(context: Context, attrs: AttributeSet)
    : View(context, attrs) {
    private val screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    init {
        screenObjects.add(DoneIcon(context))
        screenObjects.add(TaskSolveEquation(context, mutableMapOf()))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        screenObjects.forEach { obj ->
            val widthIcon = widthView / 10
            val widthPadding = widthView / 100
            val heightPadding = heightView / 10
            if (obj is DoneIcon) {
                obj.sizeChanged(
                    widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthView - widthIcon + widthPadding,
                    heightPadding + heightView / 2 - widthIcon / 2
                )
            } else if (obj is TaskSolveEquation) {
                obj.sizeChanged(
                    widthIcon * 9 - widthPadding * 2, heightView - 2 * heightPadding,
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
        }
        return true
    }


    fun setMapSolutions(solutions : Map<ScaleVariable, Int>){
        screenObjects.filter { it is TaskSolveEquation }
            .forEach { (it as TaskSolveEquation).setSolutions(solutions)}
    }
}