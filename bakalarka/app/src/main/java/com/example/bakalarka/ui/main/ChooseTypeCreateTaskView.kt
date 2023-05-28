package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.bakalarka.objects.menu.CreateTypeEquationIcon
import com.example.vahy.objects.ScreenObject

class ChooseTypeCreateTaskView(context: Context, attrs: AttributeSet)
    : View(context, attrs){

    private var screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    var continueToCreateType = -1

    init {
        screenObjects.add(CreateTypeEquationIcon(context, 1))
        screenObjects.add(CreateTypeEquationIcon(context, 2))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        screenObjects.forEach { obj ->
            val widthIcon = widthView / 4

            val yStart = heightView / 2 - widthIcon / 2
            if (obj is CreateTypeEquationIcon) {
                val xStart = if (obj.getType() <= 1) widthView * 7 / 24 - widthIcon / 2
                        else widthView * 17 / 24 - widthIcon / 2
                obj.sizeChanged(widthIcon, widthIcon, xStart, yStart)
            } else
                obj.sizeChanged(widthView, heightView, 0, 0)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        screenObjects.forEach { it.draw(canvas, paint) }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true

        val action = event.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            isClickedTypeCreateTask(event)
        }
        return true
    }

    private fun isClickedTypeCreateTask(event: MotionEvent) {
        val typeCreateIcons = screenObjects.filter {
            it is CreateTypeEquationIcon && it.isIn(event.x.toInt(), event.y.toInt())
        }.map { it as CreateTypeEquationIcon }
        if (typeCreateIcons.size == 1) {
            continueToCreateType = typeCreateIcons[0].getType()
        }
    }
}