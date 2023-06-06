package com.vahy.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.vahy.bakalarka.R
import com.vahy.bakalarka.equation.Equation
import com.vahy.bakalarka.objects.menu.*
import com.vahy.vahy.objects.ScreenObject

class CreateTaskMenuView(context: Context, attrs: AttributeSet)
    : View(context, attrs){

    private var screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    var continueToSolve = false
    var isRotating = false
    var indexTouchedEquation = -1

    init {
        screenObjects.add(NextIcon(context))
    }

    fun setCreateTaskForBuild(equations : List<Equation>){
        screenObjects.add(TaskBuildSolution(context, equations))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        screenObjects.sortBy { it is DoneIcon }
        val widthPadding = widthView / 100
        val h = Math.max(heightView - heightView / 10, 10)
        val yStart = 0

        screenObjects.forEach { obj ->
            if (obj is NextIcon) {
                val xStart = widthView - h - 2 * widthPadding
                obj.sizeChanged(h, h, xStart, yStart)
            }
            else
                obj.sizeChanged(widthView, heightView, 0, 0)
        }
        changeSizeEquationStringBlocks()
    }

    private fun changeSizeEquationStringBlocks() {
        if (widthView <= 10 || heightView <= 10)
            return

        val widthPadding = widthView / 100
        val h = Math.max(heightView - heightView / 10, 10)
        val yStart = 0
        val xStart = widthPadding * 2
        val scaleNextIconWidth = h + 12 * widthPadding

        screenObjects.filter { it is TaskBuildSolution }.forEach { obj ->
            obj.sizeChanged(widthView - scaleNextIconWidth, h, xStart, yStart)
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
            isClickedContinueToSolve(event)
            indexTouchedEquation = screenObjects.filter { it is TaskBuildSolution }
                .map {
                    (it as TaskBuildSolution).getIndexTouchedEquation(event.x.toInt(), event.y.toInt())
                }
                .firstOrNull() ?: -1
        }
        return true
    }

    private fun isClickedContinueToSolve(event: MotionEvent) {
        val CreateIcons = screenObjects.filter {
            it is NextIcon &&
                    it.isIn(event.x.toInt(), event.y.toInt())
        }
        if (CreateIcons.size > 0) {
            continueToSolve = true
        }
    }

    fun changeEquationBoxes(equations: List<Equation>){
        screenObjects.filter { it is TaskBuildSolution }
            .forEach { (it as TaskBuildSolution).updateEquations(equations) }
        changeSizeEquationStringBlocks()
        invalidate()
    }

    fun setIndexMarked(ix : Int){
        if (screenObjects.any { it is TaskBuildSolution && it.setIndexMarked(ix) })
            invalidate()
    }
}