package com.example.bakalarka.objects

import android.graphics.*
import com.example.vahy.objects.ScreenObject
import kotlin.math.ceil

class ObjectsToChooseFrom (private var insideObject :
                           MutableList<EquationObject>)
    : ScreenObject(false) {
    val collums = 2
    var rows = Math.ceil(insideObject.size.toDouble() / collums.toDouble()).toInt()

    init {
        z = 2
        width = 200
        height = 500
        x = 20
        y = 1800
    }

    fun setInsideObject(objects : MutableList<EquationObject>){
        insideObject = objects
        rows = ceil(insideObject.size.toDouble() / collums.toDouble()).toInt()
        changeSizeInsideObj()
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        width = w
        height = h
        x = xStart
        y = yStart

        changeSizeInsideObj()
    }

    private fun changeSizeInsideObj() {
        val widthBox = width / collums
        val heightBox = height / rows
        val widthMargins = widthBox / 20
        val heightMargins = heightBox / 20
        val widthObj = widthBox - 2 * widthMargins
        val heightObj = heightBox - 2 * heightMargins
        var row = 0
        var col = 0
        (0 until insideObject.size).forEach { i ->
            var xMiddle = x + widthBox * col + widthBox / 2
            val yMiddle = y + heightBox * row + heightBox / 2
            if (row == rows - 1 && insideObject.size % collums != 0){
                val wBox = width / (insideObject.size % collums)
                xMiddle = x + wBox * col + wBox / 2
            }
            insideObject[i].sizeChanged(widthObj, heightObj, xMiddle, yMiddle)
            col++
            if (col >= collums) {
                col = 0
                row++
            }
        }
    }

    override fun draw(canvas: Canvas, paint: Paint){
        drawFrame(paint, canvas)

        for (obj in insideObject){
            obj.draw(canvas, paint)
        }
    }

    private fun drawFrame(paint: Paint, canvas: Canvas) {
        paint.color = Color.BLACK
        paint.strokeWidth = 3F
        canvas.drawRect(
            Rect(
                x, y, x + width, y + height
            ),
            paint
        )

        paint.color = Color.WHITE
        paint.strokeWidth = 0F
        canvas.drawRect(
            Rect(
                x + 3, y + 3, x + width - 3, y + height - 3
            ),
            paint
        )
    }

    override fun returnDraggedObject(x1: Int, y1: Int): EquationObject? =
        insideObject.filter { it.onTouch(x1, y1)}.firstOrNull()?.makeCopy()

    override fun returnClickedObject(x1: Int, y1: Int): EquationObject? =
        insideObject.filter { it.onTouch(x1, y1)}.firstOrNull()

    fun getInsideObjects() : MutableList<EquationObject> = insideObject

    override fun returnPackages() : MutableList<Package> =
        insideObject.flatMap { it.returnPackages()}.toMutableList()
}