package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.objects.menu.HomeIcon
import com.example.bakalarka.objects.menu.ProgressBar
import com.example.bakalarka.objects.menu.ReplayIcon
import com.example.vahy.objects.ScreenObject

class TaskMainMenuView(context: Context, attrs: AttributeSet)
    : View(context, attrs){
//    : View(context, attrs), GestureDetector.OnGestureListener,
//    GestureDetector.OnDoubleTapListener{

    private val maxNumberOfTasks = 10
    private val numberOfTasks = 3
    private val screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()
    var previousEquation = false
//    var restoreOriginalEquation = false
    var leaveTask = false
    var isTouchable = true

    init {
        val progressBar = ProgressBar(context)
        progressBar.updateValue(numberOfTasks)
        progressBar.updateMaxValue(maxNumberOfTasks)
        screenObjects.add(HomeIcon(context))
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
            val widthIcon = widthView / 5
            val widthPadding = widthView / 100
            val heightPadding = heightView / 10
            if (obj is HomeIcon) {
                obj.sizeChanged(
                    widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthPadding, heightView / 2 - widthIcon / 2
                )
            } else if (obj is ReplayIcon) {
                obj.sizeChanged(
                    widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthView - widthIcon + widthPadding,
                    heightView / 2 - widthIcon / 2
                )
            }  else if (obj is ProgressBar) {
                val widthProgBar = widthView - widthIcon * 3 - widthPadding * 2
                obj.sizeChanged(
                    widthProgBar, heightView / 2,
                    widthView / 2 - widthProgBar / 2 + widthPadding, heightView / 4
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
        if (event == null || !isTouchable) return true

        val action = event.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            val replayIcons = screenObjects.filter { it is ReplayIcon &&
                    it.isIn(event.x.toInt(), event.y.toInt()) }
            if (replayIcons.size > 0){
                previousEquation = true
            }

            val leaveIcons = screenObjects.filter { it is HomeIcon &&
                    it.isIn(event.x.toInt(), event.y.toInt()) }
            if (leaveIcons.size > 0){
                leaveTask = true
            }
        }
        return true
    }

    fun setProgressBarPar(numTasks : Int, currentTask : Int){
        screenObjects.filter { it is ProgressBar }.map { it as ProgressBar }
            .forEach {
                it.updateValue(currentTask)
                it.updateMaxValue(numTasks)
            }
    }


//    override fun onDoubleTap(event: MotionEvent?): Boolean {
//        if (event == null) return true
//
//        val action = event.action
//        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
//            val replayIcons = screenObjects.filter { it is ReplayIcon &&
//                    it.isIn(event.x.toInt(), event.y.toInt()) }
//            if (replayIcons.size > 0){
//                restoreOriginalEquation = true
//            }
//        }
//        return true
//    }
//
//
//    override fun onLongPress(event: MotionEvent?) {}
//
//    override fun onDown(p0: MotionEvent?): Boolean = true
//
//    override fun onShowPress(p0: MotionEvent?) {}
//
//    override fun onSingleTapUp(p0: MotionEvent?): Boolean = true
//
//    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean = true
//
//    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean = true
//
//    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean = true
//
//    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean = true
}