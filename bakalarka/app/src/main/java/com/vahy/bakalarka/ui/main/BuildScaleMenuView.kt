package com.vahy.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.vahy.bakalarka.R
import com.vahy.bakalarka.equation.Equation
import com.vahy.bakalarka.objects.menu.*
import com.vahy.vahy.objects.ScreenObject

class BuildScaleMenuView(context: Context, attrs: AttributeSet)
    : View(context, attrs) {
    var screenTouchDisabled = false
    private val screenObjects = mutableListOf<ScreenObject>()
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    var checkSolution = false
    var indexTouchedEquation = -1

    private var messageTimer : CountDownTimer? = null

    init {
        screenObjects.add(DoneIcon(context))
        screenObjects.add(TaskBuildSolution(context))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun changeSizeScreenObjects() {
        screenObjects.forEach { obj ->
            val widthIcon = widthView / 7
            val widthPadding = widthView / 100
            val heightPadding = heightView / 10
            if (obj is DoneIcon) {
                obj.sizeChanged(
                    widthIcon - widthPadding * 2, heightView - 2 * heightPadding,
                    widthView - widthIcon + widthPadding,
                    heightPadding + heightView / 2 - widthIcon / 2
                )
            } else if (obj is TaskBuildSolution) {
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
        if (event == null || screenTouchDisabled) return true
        val ex = event.x.toInt()
        val ey = event.y.toInt()

        val action = event.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            if (screenObjects.any { it is DoneIcon && it.isIn(ex, ey) })
                checkSolution = true
            else{
                indexTouchedEquation = screenObjects.filter { it is TaskBuildSolution }
                    .map { (it as TaskBuildSolution).getIndexTouchedEquation(ex, ey) }
                    .firstOrNull() ?: -1
            }
        }
        return true
    }

    fun failSuccessShow(){
        screenTouchDisabled = true
        messageTimer = object : CountDownTimer(3000, 3000){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                screenTouchDisabled = false
                invalidate()
            }
        }.start()
    }

    fun cancelMessage() {
        messageTimer?.cancel()
        screenTouchDisabled = false
        invalidate()
        messageTimer = null
    }


    fun setEquations(eq : List<Equation>){
        indexTouchedEquation = -1
        screenObjects.filter { it is TaskBuildSolution }
            .forEach { (it as TaskBuildSolution).setEquations(eq)}
        invalidate()
    }

    fun getEquations() : List<Equation> =
        (screenObjects.filter { it is TaskBuildSolution }
            .first() as TaskBuildSolution).getEquations()

    fun setIndexMarked(ix : Int){
        if (screenObjects.any { it is TaskBuildSolution && it.setIndexMarked(ix) })
            invalidate()
    }
}