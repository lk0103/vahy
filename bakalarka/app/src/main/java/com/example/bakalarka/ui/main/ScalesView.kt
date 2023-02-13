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
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.objects.*
import com.example.vahy.equation.Addition
import com.example.vahy.equation.Constant
import com.example.vahy.equation.Multiplication
import com.example.vahy.equation.Variable
import com.example.vahy.objects.*

///-------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!-------------------
//double click na kos vrati do prvotneho obsahu vah
//aby sa dalo z daneho obsahu vah urobit instancia rovnice alebo nacitat rovnica do vahy

class ScalesView(context: Context, attrs: AttributeSet)
            : View(context, attrs), GestureDetector.OnGestureListener,
                    GestureDetector.OnDoubleTapListener{
    private val screenObjects = mutableListOf<ScreenObject>()
    private var draggedObject : EquationObject? = null
    private var draggedObjOriginalPos = Pair(0, 0)
    private var draggedFrom : ContainerForEquationBoxes? = null
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
    private val containersToManipulate = mutableListOf<ContainerForEquationBoxes>(leftHolder, rightHolder)

    private var scaleWidthProportion = Pair(37, 48)
    private var widthView = 0
    private var heightView = 0

    private val paint = Paint()
    private var gDetector: GestureDetectorCompat? = null


    init {
        this.gDetector = GestureDetectorCompat(context, this)
        gDetector?.setOnDoubleTapListener(this)
        gDetector?.setIsLongpressEnabled(true)
        manageOpenPackage()
        screenObjects.add(BaseOfScale(context))
        screenObjects.add(leftHolder)
        screenObjects.add(rightHolder)
        screenObjects.add(ArmOfScale(context))
        screenObjects.add(bin)
        screenObjects.add(objectsToChooseFrom)

        ////TEST ROVNIC
        val eq = Addition(mutableListOf(
            Multiplication(Variable("x", 2.0),
                            Constant(4.0)
            ) ,
            Multiplication(Constant(2.0),
                Constant(5.0)
            ),
            Constant(3.0),
            Multiplication(Bracket(Addition(mutableListOf(Constant(4.0),
                                                Variable("x", 2.0)))),
                Constant(6.0)
            )
        ))
        ///otestovat ci nebude 0 * 2 alebo 0 * x
        Log.i("rovnica", "povodna " + eq.toString())
//        eq.addConstant(3.0)
//        Log.i("rovnica", "add const 3: " + eq.toString())
//        eq.addConstant(3.0)
//        Log.i("rovnica", "add const 3: " + eq.toString())
//        eq.addConstant(2.0)
//        Log.i("rovnica", "add const 2: " + eq.toString())
//        eq.addConstant(4.0)
//        Log.i("rovnica", "add const 4: " + eq.toString())
//        eq.removeConstant(4.0)
//        Log.i("rovnica", "remove const 4: " + eq.toString())
//        eq.removeConstant(4.0)
//        Log.i("rovnica", "remove const 4: " + eq.toString())
//        eq.removeConstant(2.0)
//        Log.i("rovnica", "remove const 2: " + eq.toString())
//        eq.removeConstant(3.0)
//        Log.i("rovnica", "remove const 3: " + eq.toString())
//        eq.removeConstant(3.0)
//        Log.i("rovnica", "remove const 3: " + eq.toString())
//        eq.removeConstant(3.0)
//        Log.i("rovnica", "remove const 3: " + eq.toString())
//        eq.removeConstant(3.0)
//        Log.i("rovnica", "remove const 3: " + eq.toString())

//        eq.addToConstant(3.0, 1.0)
//        Log.i("rovnica", "inc const 3: " + eq.toString())
//        eq.addToConstant(4.0, 2.0)
//        Log.i("rovnica", "inc const 4: " + eq.toString())
//        eq.addToConstant(3.0, 1.0)
//        Log.i("rovnica", "inc const 3: " + eq.toString())
//        eq.addToConstant(6.0, -7.0)
//        Log.i("rovnica", "dec const 5: " + eq.toString())
//        eq.addToConstant(-1.0, 1.0)
//        Log.i("rovnica", "dec const 5: " + eq.toString())
//        eq.addToConstant(2.0, 10.0)
//        Log.i("rovnica", "dec const 2: " + eq.toString())

        eq.addVariable("x", 2.0)
        Log.i("rovnica", "add x = 2: " + eq.toString())
        eq.addVariable("x", 3.0)
        Log.i("rovnica", "add x = 3: " + eq.toString())
        eq.removeVariable("x", 2.0)
        Log.i("rovnica", "remove x = 2: " + eq.toString())
        eq.removeVariable("x", 2.0)
        Log.i("rovnica", "remove x = 2: " + eq.toString())
        eq.removeVariable("x", 2.0)
        Log.i("rovnica", "remove x = 2: " + eq.toString())
        eq.removeVariable("x", 2.0)
        Log.i("rovnica", "remove x = 2: " + eq.toString())
        eq.removeVariable("x", 2.0)
        Log.i("rovnica", "remove x = 2: " + eq.toString())
        eq.removeVariable("x", 2.0)
        Log.i("rovnica", "remove x = 2: " + eq.toString())
        eq.removeVariable("x", 2.0)
        Log.i("rovnica", "remove x = 2: " + eq.toString())
        eq.removeVariable("y", 2.0)
        Log.i("rovnica", "remove y = 2: " + eq.toString())
        eq.addVariable("y", 3.0)
        Log.i("rovnica", "add y = 3: " + eq.toString())
        eq.addVariable("y", 3.0)
        Log.i("rovnica", "add y = 3: " + eq.toString())
    }

    private fun manageOpenPackage() {
        if (objectsToChooseFrom.getInsideObjects().filter { it is Package }.count() > 0) {
            if (! screenObjects.contains(openPackage)) screenObjects.add(openPackage)
            if (! containersToDragFrom.contains(openPackage)) {
                if (openPackage.touchable) containersToDragFrom.add(openPackage)
                else containersToDragFrom.remove(openPackage)
            }
            if (! containersToManipulate.contains(openPackage)) {
                if (openPackage.touchable) containersToManipulate.add(openPackage)
                else containersToManipulate.remove(openPackage)
            }
        }else{
            if (screenObjects.contains(openPackage)) screenObjects.remove(openPackage)
            if (containersToDragFrom.contains(openPackage)) containersToDragFrom.remove(openPackage)
            if (containersToManipulate.contains(openPackage)) containersToManipulate.remove(openPackage)
        }
    }

    fun setTouchabilityOfOpenPackage(touchable : Boolean){
        openPackage.touchable = touchable
        manageOpenPackage()
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
        manageOpenPackage()
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
                changeSizeScale(obj)
            } else if (obj is Bin) {
                heightOfBin = changeSizeBin(obj, heightOfBin)
            } else if (obj is OpenPackage) {
                changeSizeOpenPackage(obj, heightOfBin)
            } else if (obj is ObjectsToChooseFrom) {
                changeSizeObjectsToChooseFrom(obj)

            } else
                obj.sizeChanged(widthView, heightView, 0, 0)
        }
    }

    private fun changeSizeScale(obj: ScreenObject) {
        obj.sizeChanged(
            widthView * scaleWidthProportion.first / scaleWidthProportion.second, heightView,
            widthView / 500, heightView / 20
        )
    }

    private fun changeSizeBin(obj: ScreenObject, heightOfBin: Int) : Int{
        if (screenObjects.contains(openPackage) && screenObjects.contains(objectsToChooseFrom))
            obj.sizeChanged(widthView / 10, heightView, widthView * 33 / 48, 10)
        else
            obj.sizeChanged(
                widthView / 10, heightView,
                widthView * 81 / 96, heightView - heightOfBin
            )

        return obj.height
    }

    private fun changeSizeOpenPackage(obj: ScreenObject, heightOfBin: Int) {
        if (screenObjects.contains(objectsToChooseFrom))
            obj.sizeChanged(
                widthView * 1 / 4, heightView, widthView * 7 / 8,
                heightView - heightOfBin * 7 / 9
            )
        else
            obj.sizeChanged(
                widthView * 1 / 4, heightView - heightOfBin - 10, widthView * 7 / 8,
                heightView / 4
            )
    }

    private fun changeSizeObjectsToChooseFrom(obj: ScreenObject) {
        if (screenObjects.contains(openPackage))
            obj.sizeChanged(
                widthView / 5 - widthView / 25, heightView / 2,
                widthView * 4 / 5 + widthView / 50, 10
            )
        else
            obj.sizeChanged(
                widthView / 5 - widthView / 25, heightView * 3 / 5,
                widthView * 4 / 5 + widthView / 50, 10
            )
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true

        gDetector?.onTouchEvent(event)
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
        if (clickedObject != null && openPackage.getInsideObjects().contains(clickedObject))
            changeInsideOfPackages()
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
                if (obj is ContainerForEquationBoxes)
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
        if (bin.isIn(obj)){
            removeDraggedObjFromContainer(draggedFrom, true)
        } else {
            containersToManipulate.forEach{ container ->
                if (container != draggedFrom && container.isIn(obj)){
                    dropObjIntoContainer(obj, container)
                    invalidate()
                    return
                }
            }
        }
        this.invalidate()
    }

    private fun dropObjIntoContainer(obj: EquationObject, container : ContainerForEquationBoxes) {
        var eqObj = obj
        if (eqObj is Package){
            val variable = eqObj.insideObject.filter { it is ScaleVariable }.firstOrNull()
            eqObj = if (variable is ScaleVariable) variable!! else eqObj
        }
        if (eqObj is ScaleVariable && !possibleToAddVariableType(eqObj)) {
            removeDraggedObjFromContainer(draggedFrom, false)
            return
        }
        try {
            container.addEquationObjIntoHolder(obj)
            removeDraggedObjFromContainer(draggedFrom, true)
            if (container is OpenPackage)
                changeInsideOfPackages()
        }catch (e : java.lang.Exception){
            removeDraggedObjFromContainer(draggedFrom, false)
        }
    }

    private fun changeInsideOfPackages(){
        containersToDragFrom.flatMap { it.returnPackages() }
            .forEach { it.putObjectsIn(openPackage.getInsideObjects().toMutableList()) }
    }

    private fun possibleToAddVariableType(obj : EquationObject) : Boolean {
        val listOfVariables = containersToManipulate.flatMap { it.insideVariableTypes() }
        var count = 0
        val typeVariableClasses = listOf(Ball(context, 0), Cube(context, 0), Cylinder(context, 0))
        typeVariableClasses.forEach { type ->
            if (listOfVariables.any { it::class == type::class }) {
                if (obj::class == type::class )
                    return true
                count++
            }
        }
        return count < maxNumberOfVariableTypes
    }

    fun removeDraggedObjFromContainer(holder : ContainerForEquationBoxes?, delete: Boolean = false){
        if (holder == null)
            return
        holder.removeDraggedObject(delete)
        if (delete && holder is OpenPackage)
            changeInsideOfPackages()
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