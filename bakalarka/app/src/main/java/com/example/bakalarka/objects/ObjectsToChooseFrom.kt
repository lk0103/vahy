package com.example.bakalarka.objects

import android.graphics.*
import com.example.vahy.objects.ScreenObject

class ObjectsToChooseFrom (private var insideObject :
                           MutableList<EquationObject>)
    : ScreenObject(false) {
    val collums = 2
    val rows = Math.ceil(insideObject.size.toDouble() / collums.toDouble()).toInt()

    init {
        z = 2
        width = 200
        height = 500
        x = 20
        y = 1800
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        width = w
        height = h - 2 * (h / 40)
        x = xStart
        y = h / 40

        val widthBox = width / collums
        val heightBox = height / rows
        val widthMargins = widthBox / 20
        val heightMargins = heightBox / 20
        val widthObj = widthBox - 2 * widthMargins
        val heightObj = heightBox - 2 * heightMargins
        var row = 0
        var col = 0
        (0 until insideObject.size).forEach { i ->
            val xMiddle = x + widthBox * col + widthBox / 2
            val yMiddle = y + heightBox * row + heightBox / 2
            insideObject[i].sizeChanged(widthObj, heightObj, xMiddle, yMiddle)
            col++
            if (col >= collums){
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

}