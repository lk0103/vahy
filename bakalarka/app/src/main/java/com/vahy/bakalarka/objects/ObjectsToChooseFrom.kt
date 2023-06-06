package com.vahy.bakalarka.objects

import android.graphics.*
import com.vahy.vahy.objects.ScreenObject
import kotlin.math.ceil

class ObjectsToChooseFrom (private var insideObject :
                           MutableList<EquationObject>)
    : ScreenObject(true, false) {
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

    fun changeSizeInScaleView(widthView : Int, heightView : Int, padding : Int,
                              openPackageVis : Boolean){
        if (openPackageVis)
            sizeChanged(
                widthView / 5 - widthView / 25 - padding * 2, heightView / 2 - padding * 2,
                widthView * 4 / 5 + widthView / 50, 10
            )
        else
            sizeChanged(
                widthView / 5 - widthView / 25 - padding * 2, heightView * 3 / 5 - padding * 2,
                widthView * 4 / 5 + widthView / 50, 10
            )
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        width = w
        height = h
        x = xStart
        y = yStart

        changeSizeInsideObj()
    }

    private fun changeSizeInsideObj() {
        val widthBox = width / Math.max(collums, 1)
        val heightBox = height / Math.max(rows, 1)
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
        if (!visibility)
            return
        drawFrame(paint, canvas)

        for (obj in insideObject){
            obj.draw(canvas, paint)
        }
    }

    private fun drawFrame(paint: Paint, canvas: Canvas) {
        paint.color = Color.BLACK
        paint.strokeWidth = 3F
        roundedRectangle(canvas, paint, x.toFloat(), y.toFloat(),
            x + width.toFloat(), y + height.toFloat())


        paint.color = Color.WHITE
        paint.strokeWidth = 0F
        roundedRectangle(canvas, paint, x + 3F, y + 3F,
            x + width - 3F, y + height - 3F)

    }

    private fun roundedRectangle(canvas: Canvas, paint: Paint, x1 : Float,
                                 y1 : Float, x2 : Float, y2 : Float) {
        canvas.drawRoundRect(RectF(x1, y1, x2, y2), 20F,20F, paint)
    }


    override fun returnDraggedObject(x1: Int, y1: Int): EquationObject? =
        insideObject.filter { it.isIn(x1, y1)}.firstOrNull()?.makeCopy()

    override fun returnClickedObject(x1: Int, y1: Int): EquationObject? =
        insideObject.filter { it.isIn(x1, y1)}.firstOrNull()

    fun getInsideObjects() : MutableList<EquationObject> = insideObject

    override fun returnPackages() : MutableList<Package> =
        insideObject.flatMap { it.returnPackages()}.toMutableList()
}