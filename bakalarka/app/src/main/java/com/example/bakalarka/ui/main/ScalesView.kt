package com.example.vahy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.example.bakalarka.objects.*
import com.example.vahy.objects.*

///-------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-------------------
//double click na kos vrati do prvotneho obsahu vah

class ScalesView(context: Context, attrs: AttributeSet)
            : View(context, attrs), GestureDetector.OnGestureListener,
                    GestureDetector.OnDoubleTapListener{
    private val screenObjects = mutableListOf<ScreenObject>()
    private var draggedObject : EquationObject? = null
    private var draggedObjOriginalPos = Pair(0, 0)
    private var draggedFrom : HolderOfWeights? = null
    private var clickedObject : EquationObject? = null
    private var maxNumberOfVariableTypes = 1

    private val leftHolder = HolderOfWeights(context, true)
    private val rightHolder = HolderOfWeights(context, false)
    private val bin = Bin(context)
    private var objectsToChooseFrom = ObjectsToChooseFrom(mutableListOf(
                        Ball(context, 1),
                        Cube(context, 1),
                        Cylinder(context, 1),
                        Ballon(context, -1),
                        Weight(context, 1),
                        Package(context)
                        ))
    private var openPackage = OpenPackage(context)
    private val containersToDragFrom = mutableListOf(objectsToChooseFrom, leftHolder, rightHolder)

    private var scaleWidthProportion = Pair(37, 48)
    private var widthView = 0
    private var heightView = 0

    private val paint = Paint()
    private var gDetector: GestureDetectorCompat? = null


    init {
        this.gDetector = GestureDetectorCompat(context, this)
        gDetector?.setOnDoubleTapListener(this)
        gDetector?.setIsLongpressEnabled(true)
        if (objectsToChooseFrom.getInsideObjects().filter { it is Package }.count() > 0) {
            screenObjects.add(openPackage)
        }
        screenObjects.add(BaseOfScale(context))
        screenObjects.add(leftHolder)
        screenObjects.add(rightHolder)
        screenObjects.add(ArmOfScale(context))
        screenObjects.add(bin)
        screenObjects.add(objectsToChooseFrom)
    }

    fun setVisibilityObjectToChooseFrom(visible : Boolean){
        if (!visible){
            containersToDragFrom.remove(objectsToChooseFrom)
            screenObjects.remove(objectsToChooseFrom)
            scaleWidthProportion = Pair(4, 5)
            if (widthView > 0 && heightView > 0 ) changeSizeScreenObjects()
            invalidate()
            return
        }
        if (!containersToDragFrom.contains(objectsToChooseFrom) &&
            !screenObjects.contains(objectsToChooseFrom)){
            containersToDragFrom.add(objectsToChooseFrom)
            screenObjects.add(objectsToChooseFrom)
            scaleWidthProportion = Pair(37, 48)
            if (widthView > 0 && heightView > 0 ) changeSizeScreenObjects()
            invalidate()
        }
    }

    fun setObjectsToChooseFrom(objects : MutableList<EquationObject>){
        objectsToChooseFrom.setInsideObject(objects)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        var heightOfBin = screenObjects.filter { it is Bin }.firstOrNull()?.height ?: 0
        screenObjects.forEach { obj ->
            if (obj is BaseOfScale || obj is ArmOfScale || obj is HolderOfWeights) {
                obj.sizeChanged(
                    widthView * scaleWidthProportion.first / scaleWidthProportion.second, heightView,
                    widthView / 500, heightView / 20
                )
            } else if (obj is Bin) {
                if (screenObjects.contains(openPackage))
                    obj.sizeChanged(widthView / 10, heightView, widthView * 33 / 48, 10)
                else
                    obj.sizeChanged(widthView / 10, heightView,
                        widthView * 79 / 96, heightView - heightOfBin)

                heightOfBin = obj.height
            } else if (obj is OpenPackage) {
                obj.sizeChanged(widthView / 3, heightView, widthView * 37 / 48 , heightView - heightOfBin)
            } else if (obj is ObjectsToChooseFrom) {
                obj.sizeChanged(
                    widthView / 5 - widthView / 25, heightView - heightOfBin - heightOfBin / 10,
                    widthView * 4 / 5 + widthView / 50, 10
                )
            } else
                obj.sizeChanged(widthView, heightView, 0, 0)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true

        gDetector?.onTouchEvent(event!!)
        if (clickedObject != null){
            onClicked()
            return true
        }

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

    private fun onClicked() {
        draggedObject = null
        draggedFrom = null
        clickedObject = null
        invalidate()
    }

    private fun onTouchDown(event: MotionEvent) {
        for (obj in containersToDragFrom) {
            draggedObject = obj.returnDraggedObject(event.x.toInt(), event.y.toInt())
            if (draggedObject != null) {
                draggedObjOriginalPos = Pair(draggedObject?.x ?: 0, draggedObject?.y ?: 0)
                if (obj is HolderOfWeights)
                    draggedFrom = obj
                break
            }
        }
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

    private fun dropObjIntoHolder(obj: EquationObject, holder : HolderOfWeights) {
        var eqObj = obj
        if (eqObj is Package){
            val variable = eqObj.insideObject.filter { it is ScaleVariable }.firstOrNull()
            eqObj = if (variable is ScaleVariable) variable!! else eqObj
        }
        if (eqObj is ScaleVariable && !possibleToAddVariableType(eqObj)) {
            removeDraggedObjFromHolder(draggedFrom, false)
            return
        }
        try {
            holder.addEquationObjIntoHolder(obj)
            removeDraggedObjFromHolder(draggedFrom, true)
        }catch (e : java.lang.Exception){
            removeDraggedObjFromHolder(draggedFrom, false)
        }
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

    fun removeDraggedObjFromHolder(holder : HolderOfWeights?, delete: Boolean = false){
        if (holder == null)
            return
        holder.removeDraggedObject(delete)
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.WHITE)
        for (obj in screenObjects) {
            obj.draw(canvas!!, paint)
        }
        if (draggedObject != null &&
            (Math.abs(draggedObject!!.x - draggedObjOriginalPos.first) > draggedObject!!.width / 2
                    ||
             Math.abs(draggedObject!!.y  - draggedObjOriginalPos.second) > draggedObject!!.height / 2))
            draggedObject?.draw(canvas!!, paint)
    }

    private fun findClickedObject(event: MotionEvent): EquationObject? {
        var clickedObj: EquationObject? = null
        for (obj in containersToDragFrom) {
            clickedObj = obj.returnClickedObject(event.x.toInt(), event.y.toInt())
            if (clickedObj != null) {
                break
            }
        }
        return clickedObj
    }

    override fun onLongPress(event: MotionEvent?) {
        if (event == null) return

        clickedObject = findClickedObject(event)
        if (!(clickedObject is ScaleValue))
            return
        (clickedObject as ScaleValue).decrement()
        invalidate()
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        if (event == null) return true

        clickedObject = findClickedObject(event)
        if (!(clickedObject is ScaleValue))
            return true
        (clickedObject as ScaleValue).increment()
        invalidate()
        return true
    }

    override fun onDown(p0: MotionEvent?): Boolean = true

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean = true

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean
        = true

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean
        = true

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean = true

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean = true
}