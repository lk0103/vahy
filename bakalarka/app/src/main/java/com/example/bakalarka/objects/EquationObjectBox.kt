package com.example.bakalarka.objects

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.example.vahy.objects.ScreenObject

open class EquationObjectBox ()
    : ScreenObject(true, true) {
    var insideObject = mutableListOf <EquationObject>()
    protected var draggedObj : EquationObject? = null
    protected var positions = mutableListOf<List<Int>>()
    protected var widthEqObj = 0
    protected var heightEqObj = 0
    protected var maxNumberOfObj = 0
    protected var cols = 0.0
    protected var rows = 0.0

    var constantSizeRange = mutableListOf<Int>()

    override fun draw(canvas: Canvas, paint: Paint){
        insideObject.sortedByDescending { it.z }.forEach { obj ->
            obj.draw(canvas, paint)
        }
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        width = w
        height = h
        x = xStart
        y = yStart
        widthEqObj = Math.min((w / cols).toInt(), (h / rows).toInt())
        heightEqObj = Math.min((w / cols).toInt(), (h / rows).toInt())

        calculatePositions()
        changeSizeObj()
    }

    open fun changeSizeObj() {
        val weightSizeProportions = listOf(Pair(84, 100), Pair(92, 100), Pair(1, 1))
            .subList(3 - constantSizeRange.size, 3)
        (0 until insideObject.size).forEach { i ->
            val x = positions[i][0]
            val y = positions[i][1]
            val z = positions[i][2]
            insideObject[i].setZ(z)
            var w = widthEqObj
            if (insideObject[i] is Weight && constantSizeRange.size > 0) {
                var ix = 0
                while (ix < constantSizeRange.size - 1 &&
                    insideObject[i].evaluate() > constantSizeRange[ix]) ix++
                w = widthEqObj * weightSizeProportions[ix].first / weightSizeProportions[ix].second
            }
            insideObject[i].sizeChanged(w, heightEqObj, x, y)
        }
    }

    open fun calculatePositions(){
        positions = mutableListOf()
    }

    open fun addObject(obj : EquationObject) {
        if (insideObject.size >= maxNumberOfObj){
            throw java.lang.Exception("maximum capacity of objects")
        }
        val ix = insideObject.size
        val z = positions[ix][2]
        insideObject.add(obj)
        obj.setZ(z)
        obj.sizeChanged(widthEqObj, heightEqObj, positions[ix][0], positions[ix][1])
    }

    override fun returnDraggedObject(x1: Int, y1: Int): EquationObject? {
        draggedObj = insideObject.filter { it.isIn(x1, y1) }.sortedBy { it.z }.firstOrNull()
        return draggedObj?.makeCopy()
    }

    override fun returnClickedObject(x1: Int, y1: Int): EquationObject? =
        insideObject.filter { it.isIn(x1, y1) }.sortedBy { it.z }.firstOrNull()

    fun removeDraggedObject(delete : Boolean = false){
        if (draggedObj == null) return

        if (delete){
            insideObject.remove(draggedObj)
            changeSizeObj()
        }
        draggedObj = null
    }

    fun removeObject(obj : EquationObject){
        insideObject.remove(obj)
        changeSizeObj()
    }

    fun beforeEarthquakeAnim(){
        insideObject.forEach { it.beforeEarthquakeAnim() }
    }

    fun changePosition(){
        insideObject.forEach { it.shake() }
    }

    fun afterEarthquakeAnim(){
        insideObject.forEach { it.afterEarthquakeAnim() }
    }

    fun getOverlappingScreenValue(obj : ScaleValue, original : ScaleValue) : ScaleValue? =
        insideObject.filter { it is ScaleValue && it != original }.
                filter { it.isIn(obj)}.sortedByDescending { it.overlappingArea(obj) }
            .firstOrNull() as ScaleValue?

    fun getDraggedObject() : EquationObject? = draggedObj

    open fun returnListInsideVariableTypes() : List<EquationObject> =
        insideObject.filter { it is ScaleVariable }.toList()

    fun isFull() : Boolean = insideObject.size >= maxNumberOfObj

}