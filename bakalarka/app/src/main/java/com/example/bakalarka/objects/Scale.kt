package com.example.bakalarka.objects

import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.bakalarka.R
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.ui.main.CreateTaskMenuView
import com.example.vahy.ScalesView
import com.example.vahy.objects.ScreenObject
import kotlin.random.Random

class Scale(private val context: Context, dragFrom : Boolean = true, dragTo : Boolean = true) :
    ScreenObject(dragFrom, dragTo) {

    private val screenObjects = mutableListOf<ScreenObject>()
    var buildEquationTask = false

    private var angleOfScale = 0F
    private var maxAngleScaleAnim = 5F
    private var targetAngle = 0F

    private val leftHolder = HolderOfWeights(context, true, angleOfScale)
    private val rightHolder = HolderOfWeights(context, false, angleOfScale)

    private val scaleWidthSmaller = Pair(37, 48)
    private val scaleWidthBigger = Pair(4, 5)
    private val scaleWidthIcon = Pair(1, 8)
    private var scaleWidthProportion = scaleWidthSmaller

    private var rotationAnimation : CountDownTimer? = null

    var isIcon = false
    var inMenu = false

    init {
        screenObjects.add(BaseOfScale(context))
        screenObjects.add(leftHolder)
        screenObjects.add(rightHolder)
        screenObjects.add(ArmOfScale(context, angleOfScale))
    }

    fun changeLayout(containsBrackets: Boolean) {
        if (isIcon) {
            scaleWidthProportion = scaleWidthIcon
            return
        }
        if (containsBrackets) scaleWidthProportion = scaleWidthSmaller
        else scaleWidthProportion = scaleWidthBigger
    }

    fun changeSizeScreenObjects(widthView : Int, heightView : Int, padding : Int) {
        val pad = if (isIcon) padding / 2 else padding * 2
        val biggerNumEquationBoxes = getMaxNumberBoxes()
        screenObjects.forEach { obj ->
            if (obj is BaseOfScale) {
                obj.changeSizeInScaleView(widthView, heightView, pad, scaleWidthProportion)
                x = obj.x
                y = obj.y
                width = obj.width
                height = obj.height
            } else if (obj is ArmOfScale) {
                obj.changeSizeInScaleView(widthView, heightView, pad, scaleWidthProportion)
            } else if (obj is HolderOfWeights) {
                obj.changeSizeInScaleView(widthView, heightView, pad,
                                    scaleWidthProportion, biggerNumEquationBoxes)
            }else obj.sizeChanged(widthView, heightView, 0, 0)
        }
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        x = xStart
        y = yStart

        screenObjects.forEach { obj -> obj.sizeChanged(w, h, xStart, yStart) }
        width = screenObjects.filter { it is BaseOfScale }.firstOrNull()?.width ?: w
        height = screenObjects.filter { it is BaseOfScale }.firstOrNull()?.height ?: h
    }

    override fun draw(canvas: Canvas, paint : Paint) {
        if (inMenu){
            drawBackground(paint, canvas)
        }
        for (obj in screenObjects) {
            obj.draw(canvas, paint)
        }
    }

    private fun drawBackground(paint: Paint, canvas: Canvas) {
        val shift = width / 12
        val shiftY = height / 50
        val halfWidth = width / 2F
        val middleX = x + halfWidth - halfWidth / 40
        val halfHeight = height / 2F
        val middleY = y + halfHeight
        val rectF = RectF(
            middleX - halfWidth - shift, middleY - halfHeight - shiftY,
            middleX + halfWidth + shift , middleY + halfHeight + shiftY
        )

        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        canvas.drawRoundRect(rectF, 40F, 40F, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3F
        paint.color = ContextCompat.getColor(context, R.color.border_for_scale_icon)
        canvas.drawRoundRect(rectF, 40F, 40F, paint)
    }

    fun loadEquation(equation: Equation, screenVariableToStringVar : MutableMap<String, String>){
        screenObjects.filter { it is ContainerForEquationBoxes }.forEach {
            when (it){
                leftHolder -> (it as ContainerForEquationBoxes)
                    .setEquation(equation.left, screenVariableToStringVar)
                rightHolder -> (it as ContainerForEquationBoxes)
                    .setEquation(equation.right, screenVariableToStringVar)
            }
        }
    }

    fun checkEquality(equation : Equation, scaleView: ScalesView){
        scaleView.changeConstantSizeBorders()
        changeSizeBoxesInHolders(if (scaleView.isSystemOf2Eq) scaleView.getMaxNumBoxes()
                                else getMaxNumberBoxes())

        if (scaleView.isCreateTask)
            return
        val comparison = equation.compareLeftRight()
        changeAngleByEqualityValue(comparison, scaleView)
    }

    fun changeSizeBoxesInHolders(biggerNumEquationBoxes : Int) {
        getHolders().forEach { it.setBiggerNumberBoxes(biggerNumEquationBoxes) }
    }

    fun getMaxNumberBoxes() = Math.max(
        leftHolder.getNumberBoxes(),
        rightHolder.getNumberBoxes()
    )

    fun changeAngleByEqualityValue(comparison: Int, view: View) {
        targetAngle = maxAngleScaleAnim * comparison * (if (inMenu) 2 else 1)
        if (angleOfScale != targetAngle) {
            rotationScaleAnimation(view)
        }
    }

    fun rotationScaleAnimation(view : View){
        setViewParametersRotation(true, view)

        var deltaAngle = 0.08F
        val countDownInterval = 18L
        val countDownTime = ((Math.max(targetAngle, angleOfScale) -
                Math.min(targetAngle, angleOfScale)) / deltaAngle * countDownInterval ).toLong()
        deltaAngle *= if (targetAngle > angleOfScale) 1 else -1

        object : CountDownTimer(countDownTime, countDownInterval){
            override fun onTick(p0: Long) {
                angleOfScale += deltaAngle
                rotateScale(angleOfScale, view)
            }

            override fun onFinish() {
                finishRotation(view)
            }
        }.start()
    }

    private fun setViewParametersRotation(value : Boolean, view: View) {
        if (view is ScalesView) {
            view.isRotating = value
            view.screenTouchDisabled = value
        }
        else if (view is CreateTaskMenuView) {
            view.isRotating = value
        }
    }

    private fun finishRotation(view: View) {
        angleOfScale = targetAngle
        rotateScale(angleOfScale, view)

        setViewParametersRotation(false, view)
        rotationAnimation = null
    }

    fun rotateScale(angle : Float, view : View){
        angleOfScale = angle
        val arm : ArmOfScale? = screenObjects.filter { it is ArmOfScale }.firstOrNull() as ArmOfScale?
        if (arm == null)
            return
        arm.rotate(angle)
        getHolders().forEach { it.rotateScale(angle) }
        view.invalidate()
    }

    fun earthquakeAnimation(view : View){
        setViewParametersRotation(true, view)

        val countDownInterval = 18L
        val countDownTime = 800L

        getHolders().forEach { it.beforeEarthquakeAnim() }

        object : CountDownTimer(countDownTime, countDownInterval){
            override fun onTick(p0: Long) {
                getHolders().forEach { it.changePosition() }
                view.invalidate()
            }

            override fun onFinish() {
                getHolders().forEach { it.afterEarthquakeAnim() }
                setViewParametersRotation(false, view)
                view.invalidate()
            }
        }.start()
    }

    fun containersToDragFrom() : List<ScreenObject> =
        screenObjects.filter { it.visibility && it.dragFrom }

    fun containersToManipulate() : List<ContainerForEquationBoxes> = screenObjects.filter {
        it is ContainerForEquationBoxes}.filter {it.visibility && it.dragFrom && it.dragTo}
            as List<ContainerForEquationBoxes>

    fun getHolders() =
        screenObjects.filter { it is HolderOfWeights }
            .map { it as HolderOfWeights }

}