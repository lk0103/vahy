package com.example.vahy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.bakalarka.objects.*
import com.example.vahy.objects.*

///-------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-------------------
//pri presuvani objektov z jednej do druhej zmizne objekt aj ked nie je miesto na
//                          druhej vahe, cize sa tam neprida
//pridat aby sa dali pridavat aj viackrat boxy toho isteho druhu - dve boxy kociek
//doubleclicky a dlhe clicky na zvysenie a znizenie hodnot
//double click na kos vrati do prvotneho obsahu vah
//parametrizovanie, aby zmizli jednotlive casti

class ScalesView(context: Context, attrs: AttributeSet)
            : View(context, attrs) {
    val screenObjects = mutableListOf<ScreenObject>()
    private var draggedObject : EquationObject? = null
    private var draggedFrom : holderOfWeights? = null
    private var maxNumberOfVariableTypes = 1
    val paint = Paint()


    val leftHolder = holderOfWeights(context, true)
    val rightHolder = holderOfWeights(context, false)
    val bin = Bin(context)
    var objectsToChooseFrom = ObjectsToChooseFrom(mutableListOf(
                        Ball(context, 1),
                        Cube(context, 1),
                        Cylinder(context, 1),
                        Ballon(context, -1),
                        Package(context),
                        Weight(context, 1)
                        ))
    val containersToDragFrom = mutableListOf(objectsToChooseFrom, leftHolder, rightHolder)

    init {
        screenObjects.add(BaseOfScale(context))
        screenObjects.add(leftHolder)
        screenObjects.add(rightHolder)
        screenObjects.add(ArmOfScale(context))
        screenObjects.add(bin)
        screenObjects.add(objectsToChooseFrom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        var heightOfBin = screenObjects.filter { it is Bin }.firstOrNull()?.height ?: 0
        screenObjects.forEach { obj ->
            if (obj is BaseOfScale || obj is ArmOfScale || obj is holderOfWeights) {
                obj.sizeChanged(w * 3 / 4, h, w / 500, h / 20)
            }
            else if (obj is Bin){
                obj.sizeChanged(w / 10, h, w * 20 / 24, h - heightOfBin)
                heightOfBin = obj.height
            }
            else if (obj is ObjectsToChooseFrom){
                obj.sizeChanged( w / 4 - w / 25 , h - heightOfBin - h/250,
                            w * 3 / 4 + w / 50,h - h / 100 - height)
            }
            else
                obj.sizeChanged(w, h, 0, 0)
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null)
            return true
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            onTouchDown(event)
        }else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP
            || action == MotionEvent.ACTION_CANCEL) {
            onTouchUp()
        } else if (action == MotionEvent.ACTION_MOVE) {
            onTouchMove(event)
        }
        return true
    }

    private fun onTouchMove(event: MotionEvent) {
        draggedObject?.move(event.x.toInt(), event.y.toInt())
        this.invalidate()
    }

    private fun onTouchUp() {
        if (draggedObject != null)
            droppedObject(draggedObject!!)
        draggedObject = null
        draggedFrom = null
        this.invalidate()
    }

    private fun onTouchDown(event: MotionEvent) {
        for (obj in containersToDragFrom) {
            draggedObject = obj.returnDraggedObject(event.x.toInt(), event.y.toInt())
            if (draggedObject != null) {
                if (obj is holderOfWeights)
                    draggedFrom = obj
                break
            }
        }
    }

    fun droppedObject(obj : EquationObject){
        if (leftHolder != draggedFrom && leftHolder.isIn(obj)){
            dropObjIntoHolder(obj, leftHolder)
        }else if (rightHolder != draggedFrom && rightHolder.isIn(obj)){
            dropObjIntoHolder(obj, rightHolder)
        }else if (bin.isIn(obj)){
            removeDraggedObjFromHolder(draggedFrom, true)
        }
        this.invalidate()
    }

    private fun dropObjIntoHolder(obj: EquationObject, holder : holderOfWeights) {
        var eqObj = obj
        if (eqObj is Package){
            val variable = eqObj.insideObject.filter { it is ScaleVariable }.firstOrNull()
            eqObj = if (variable is ScaleVariable) variable!! else eqObj
        }
        if (eqObj is ScaleVariable && !possibleToAddVariableType(eqObj)) {
            removeDraggedObjFromHolder(draggedFrom, false)
            return
        }
        holder.addEquationObjIntoHolder(obj)
        removeDraggedObjFromHolder(draggedFrom, true)
    }

    private fun possibleToAddVariableType(obj : EquationObject) : Boolean {
        val listOfVariables = leftHolder.insideVariableTypes() + rightHolder.insideVariableTypes()
        var count = 0
        if (listOfVariables.any { it is Ball }) {
            if (obj is Ball)
                return true
            count++
        }
        if (listOfVariables.any { it is Cube }) {
            if (obj is Cube)
                return true
            count++
        }
        if (listOfVariables.any { it is Cylinder }) {
            if (obj is Cylinder)
                return true
            count++
        }
        return count < maxNumberOfVariableTypes
    }

    fun removeDraggedObjFromHolder(holder : holderOfWeights?, delete: Boolean = false){
        if (holder == null)
            return
        holder.removeDraggedObject(delete)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.WHITE)
        for (obj in screenObjects) {
            obj.draw(canvas!!, paint)
        }
        draggedObject?.draw(canvas!!, paint)
    }
}