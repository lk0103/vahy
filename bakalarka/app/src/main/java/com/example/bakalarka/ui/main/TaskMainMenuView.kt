package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.objects.ArmOfScale
import com.example.bakalarka.objects.BaseOfScale
import com.example.bakalarka.objects.HolderOfWeights
import com.example.bakalarka.objects.ObjectsToChooseFrom
import com.example.bakalarka.objects.menu.ArrowBackIcon
import com.example.bakalarka.objects.menu.ProgressBar
import com.example.bakalarka.objects.menu.ReplayIcon
import com.example.vahy.objects.Bin
import com.example.vahy.objects.OpenPackage
import com.example.vahy.objects.ScreenObject

class TaskMainMenuView(context: Context, attrs: AttributeSet)
    : View(context, attrs) {
    private val maxNumberOfTasks = 10
    private val numberOfTasks = 3
    private val screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    init {
        val progressBar = ProgressBar(context)
        progressBar.updateValue(numberOfTasks)
        progressBar.updateMaxValue(maxNumberOfTasks)
        screenObjects.add(ArrowBackIcon(context))
        screenObjects.add(progressBar)
        screenObjects.add(ReplayIcon(context))
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
            if (obj is ArrowBackIcon) {
                obj.sizeChanged(
                    widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthPadding, heightPadding + heightView / 2 - widthIcon / 2
                )
            } else if (obj is ReplayIcon) {
                obj.sizeChanged(
                    widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthView - widthIcon + widthPadding,
                    heightPadding + heightView / 2 - widthIcon / 2
                )
            } else if (obj is ProgressBar) {
                obj.sizeChanged(
                    widthView - widthIcon * 2 - widthPadding * 2, heightView / 2,
                    widthIcon + widthPadding, heightView / 4
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
        return super.onTouchEvent(event)
    }
}