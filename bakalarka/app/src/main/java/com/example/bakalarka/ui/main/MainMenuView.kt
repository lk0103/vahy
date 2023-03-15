package com.example.bakalarka.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.bakalarka.objects.*
import com.example.bakalarka.objects.menu.Icon
import com.example.bakalarka.objects.menu.LevelNumber
import com.example.bakalarka.objects.menu.RestartIcon
import kotlin.random.Random

class MainMenuView(context: Context, attrs: AttributeSet)
    : View(context, attrs) {
    private val numOfLevels = 4
    private val screenObjects = mutableListOf<Pair<HolderOfWeights, LevelNumber>>()
    private val restartIcon = RestartIcon(context)
    private val objForHolders = listOf(
        Pair(listOf(Cylinder(context, 1), Cylinder(context, 1)),
            listOf(Weight(context, Random.nextInt(2, 20)))),
        Pair(listOf(com.example.bakalarka.objects.Package(context),
                    com.example.bakalarka.objects.Package(context)),
            listOf(Ball(context, 1))),
        Pair(listOf(Ball(context, 1), Ball(context, 1), Ball(context, 1)),
            listOf(Cube(context, 1))),
        Pair(listOf(Cube(context, 1), Cube(context, 1)),
            listOf(Cylinder(context, 1)))
    )
    var widthView = 1
    var heightView = 1
    private val paint = Paint()

    init {
        (0 until numOfLevels).forEach { i ->
            val holder = HolderOfWeights(context, true)

            objForHolders[i].first.forEach { holder.putEquationObjIntoHolder(it) }
            holder.addEquationObjectBox(BallonBox())
            objForHolders[i].second.forEach { holder.putEquationObjIntoHolder(it) }


            holder.inMainMenu = true
            screenObjects.add(
                Pair(holder,
                    LevelNumber(context, i + 1))
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        val blockWidth = widthView / numOfLevels
        val padding = blockWidth / 10
        val yStart = listOf(-15, -50, -25, -10)
        screenObjects.forEach { (holder, number) ->
            val i = (number.number - 1)
            holder.changeSizeInMainMenu(blockWidth - padding * 2, heightView,
                i * blockWidth + padding, yStart[i])

            number.sizeChanged(blockWidth * 7 / 12 - padding * 2, heightView / 2,
                i * blockWidth + padding, 0)
            val (xNum, yNum) = holder.getPositionTopMiddleBowl()
            number.moveIntoBowl(xNum, yNum)
        }
        restartIcon.sizeChanged(widthView/12, heightView/12,
            widthView * 7 / 8, heightView * 19 / 24)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return

        screenObjects.forEach { (holder, number) ->
            number.draw(canvas, paint)
            holder.draw(canvas, paint)
        }
        restartIcon.draw(canvas, paint)
    }

    fun clickLevel(event: MotionEvent?): Int {
        if (event == null)
            return -1
        val level = screenObjects.filter { it.first.isIn(event.x.toInt(), event.y.toInt()) }
            .map { it.second }.firstOrNull()
        if (level != null)
            return level.number
        return -1
    }

    fun clickedIcon(event: MotionEvent?): Icon?{
        if (event == null)
            return null
        return if (restartIcon.isIn(event.x.toInt(), event.y.toInt())) restartIcon
                else null
    }

    fun changeLockedLevels(lastUnlockedLevel : Int){
        screenObjects.map { it.second }.forEach {
            it.setLocked(it.number > lastUnlockedLevel)
        }
        invalidate()
    }
}