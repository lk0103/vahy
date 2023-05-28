package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.objects.menu.*
import com.example.vahy.objects.ScreenObject

class TaskMenuView(context: Context, attrs: AttributeSet)
    : View(context, attrs){

    private val maxNumberOfTasks = 10
    private val numberOfTasks = 3
    private var screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    var isInCreateTask = false

    var previousEquation = false
    var leaveTask = false
    var isTouchable = true
    var createTask = false
    var goBackToChooseType = false

    init {
        val progressBar = ProgressBar(context)
        progressBar.updateValue(numberOfTasks)
        progressBar.updateMaxValue(maxNumberOfTasks)
        screenObjects.add(HomeIcon(context))
        screenObjects.add(progressBar)
        screenObjects.add(ReplayIcon(context))
    }

    fun setMainMenuInChooseCreateTask(){
        screenObjects = screenObjects.filter { it is HomeIcon }.toMutableList()
        invalidate()
    }

    fun setMainMenuInCreateTaskForBuild(){
        screenObjects = screenObjects.filter { it !is HomeIcon }.toMutableList()
        screenObjects.add(BackIcon(context))
        changeSizeScreenObjects()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        screenObjects.forEach { obj ->
            val widthPadding = widthView / 100

            val h = Math.max(heightView - heightView / 10, 10)
            val widthIcon = h + widthPadding * 2

            val yStart = 0
            if (obj is HomeIcon) {
                obj.sizeChanged(h, h, widthPadding, yStart)
            }
            else if (obj is BackIcon) {
                obj.sizeChanged(h, h, widthPadding, yStart)
            }
            else if (obj is ReplayIcon) {
                obj.sizeChanged(h, h, widthView - widthIcon + widthPadding, yStart)
            }  else if (obj is ProgressBar) {
                val widthProgBar = widthView - widthIcon * 7 / 2 - widthPadding * 2
                obj.sizeChanged(
                    widthProgBar, heightView / 2,
                    widthView / 2 - widthProgBar / 2  + widthPadding, heightView / 4
                )
            } else if (obj is CreateTaskIcon){
                val xStart = widthView / 2 - widthIcon / 2
                obj.sizeChanged(h, h, xStart, yStart + heightView / 20)
            }else
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

        screenObjects.filter { !isInCreateTask || (it !is ProgressBar && it !is CreateTaskIcon) }
            .forEach { it.draw(canvas, paint) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || !isTouchable) return true

        val action = event.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            isClickedBackButton(event)
            isClickedHomeIcon(event)
            isClickedCreateTaskIcon(event)
            isClickedBackIcon(event)
        }
        return true
    }

    private fun isClickedCreateTaskIcon(event: MotionEvent) {
        val CreateIcons = screenObjects.filter {
            it is CreateTaskIcon &&
                    it.isIn(event.x.toInt(), event.y.toInt())
        }
        if (CreateIcons.size > 0) {
            createTask = true
        }
    }

    private fun isClickedBackIcon(event: MotionEvent) {
        val backIcons = screenObjects.filter {
            it is BackIcon &&
                    it.isIn(event.x.toInt(), event.y.toInt())
        }
        if (backIcons.size > 0) {
            goBackToChooseType = true
        }
    }

    private fun isClickedHomeIcon(event: MotionEvent) {
        val leaveIcons = screenObjects.filter {
            it is HomeIcon &&
                    it.isIn(event.x.toInt(), event.y.toInt())
        }
        if (leaveIcons.size > 0) {
            leaveTask = true
        }
    }

    private fun isClickedBackButton(event: MotionEvent) {
        val backButtons = screenObjects.filter {
            it is ReplayIcon &&
                    it.isIn(event.x.toInt(), event.y.toInt())
        }
        if (backButtons.size > 0) {
            previousEquation = true
        }
    }

    fun setProgressBarOrEditIcon(numTasks : Int, currentTask : Int){
        if (currentTask < numTasks) {
            setProgressBar(currentTask, numTasks)
        }else{
            setCreateTaskIcon()
        }
    }

    private fun setProgressBar(currentTask: Int, numTasks: Int) {
        screenObjects.filter { it is ProgressBar }.map { it as ProgressBar }
            .forEach {
                it.updateValue(currentTask)
                it.updateMaxValue(numTasks)
            }
    }

    private fun setCreateTaskIcon() {
        if (screenObjects.filter { it is CreateTaskIcon }.size > 0)
            return
        screenObjects = screenObjects.filter { it !is ProgressBar }.toMutableList()
        screenObjects.add(CreateTaskIcon(context))
        changeSizeScreenObjects()
    }
}