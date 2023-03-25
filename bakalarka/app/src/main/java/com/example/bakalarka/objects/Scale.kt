package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.Log
import androidx.core.view.GestureDetectorCompat
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.objects.menu.FailSuccessIcon
import com.example.vahy.ScalesView
import com.example.vahy.equation.Addition
import com.example.vahy.objects.Bin
import com.example.vahy.objects.OpenPackage
import com.example.vahy.objects.ScreenObject

class Scale(context: Context, dragFrom : Boolean = true, dragTo : Boolean = true) :
    ScreenObject(dragFrom, dragTo) {

    private val screenObjects = mutableListOf<ScreenObject>()
    var buildEquationTask = false

    private var angleOfScale = 0F
    private var maxAngleScaleAnim = 5F

    private val leftHolder = HolderOfWeights(context, true, angleOfScale)
    private val rightHolder = HolderOfWeights(context, false, angleOfScale)

    private val scaleWidthSmaller = Pair(37, 48)
    private val scaleWidthBigger = Pair(4, 5)
    private val scaleWidthIcon = Pair(1, 10)
    private var scaleWidthProportion = scaleWidthSmaller

    var isIcon = false

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
        val biggerNumEquationBoxes = Math.max(leftHolder.getNumberBoxes(),
                                                rightHolder.getNumberBoxes())
        screenObjects.forEach { obj ->
            if (obj is BaseOfScale) {
                obj.changeSizeInScaleView(widthView, heightView, padding, scaleWidthProportion)
                x = obj.x
                y = obj.y
                width = obj.width
                height = obj.height
            } else if (obj is ArmOfScale) {
                obj.changeSizeInScaleView(widthView, heightView, padding, scaleWidthProportion)
            } else if (obj is HolderOfWeights) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                                    scaleWidthProportion, biggerNumEquationBoxes)
            }else obj.sizeChanged(widthView, heightView, 0, 0)
        }
    }

    override fun draw(canvas: Canvas, paint : Paint) {
        for (obj in screenObjects) {
            obj.draw(canvas, paint)
        }
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
        if (buildEquationTask)
            return
        val comparison = equation.compareLeftRight()
        val newAngle = maxAngleScaleAnim * comparison
        if (angleOfScale != newAngle){
            rotationScaleAnimation(newAngle, scaleView)
        }
        val biggerNumEquationBoxes = Math.max(leftHolder.getNumberBoxes(),
            rightHolder.getNumberBoxes())
        screenObjects.filter { it is HolderOfWeights }.forEach {
            (it as HolderOfWeights).setBiggerNumberBoxes(biggerNumEquationBoxes)
        }
    }

    fun rotationScaleAnimation(targetAngle : Float, scaleView : ScalesView){
        scaleView.screenTouchDisabled = true
        var deltaAngle = 0.08F
        val countDownInterval = 18L
        val countDownTime = ((Math.max(targetAngle, angleOfScale) -
                Math.min(targetAngle, angleOfScale)) / deltaAngle * countDownInterval ).toLong()
        deltaAngle *= if (targetAngle > angleOfScale) 1 else -1

        object : CountDownTimer(countDownTime, countDownInterval){
            override fun onTick(p0: Long) {
                angleOfScale += deltaAngle
                rotateScale(angleOfScale, scaleView)
            }

            override fun onFinish() {
                angleOfScale = targetAngle
                rotateScale(angleOfScale, scaleView)
                scaleView.screenTouchDisabled = false
            }
        }.start()

    }

    fun rotateScale(angle : Float, scaleView : ScalesView){
        angleOfScale= angle
        val arm : ArmOfScale? = screenObjects.filter { it is ArmOfScale }.firstOrNull() as ArmOfScale?
        if (arm == null)
            return
        arm.rotate(angle)
        screenObjects.filter { it is HolderOfWeights }.forEach { (it as HolderOfWeights).rotateScale(angle) }
        scaleView.invalidate()
    }

    fun containersToDragFrom() : List<ScreenObject> =
        screenObjects.filter { it.visibility && it.dragFrom }

    fun containersToManipulate() : List<ContainerForEquationBoxes> = screenObjects.filter {
        it is ContainerForEquationBoxes}.filter {it.visibility && it.dragFrom && it.dragTo}
            as List<ContainerForEquationBoxes>

}