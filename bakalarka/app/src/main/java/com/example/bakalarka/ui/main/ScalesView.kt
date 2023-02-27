package com.example.vahy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.objects.*
import com.example.vahy.equation.Addition
import com.example.vahy.equation.Polynom
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
    private var equationSystem = SystemOfEquations(mutableListOf())
    private var originalEqSys = SystemOfEquations(mutableListOf())
    private var equation = Equation(Addition(mutableListOf()), Addition(mutableListOf()))
    private var screenVariableToStringVar = mutableMapOf<String, String>(
        Ball(context, 1)::class.toString() to "x",
        Cube(context, 1)::class.toString() to "y",
        Cylinder(context, 1)::class.toString() to "z",
    )

    private var screenTouchDisabled = false
    private var angleOfScale = 0F
    private var maxAngleScaleAnim = 5F

    private val leftHolder = HolderOfWeights(context, true, angleOfScale)
    private val rightHolder = HolderOfWeights(context, false, angleOfScale)
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

    private var scaleWidthProportion = Pair(37, 48)
    private var widthView = 0
    private var heightView = 0
    private var padding = 0

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
        screenObjects.add(ArmOfScale(context, angleOfScale))
        screenObjects.add(bin)
        screenObjects.add(objectsToChooseFrom)
    }

    private fun manageOpenPackage() {
        if (objectsToChooseFrom.getInsideObjects().filter { it is Package }.count() > 0) {
            if (! screenObjects.contains(openPackage)) screenObjects.add(openPackage)
        }else{
            if (screenObjects.contains(openPackage)) screenObjects.remove(openPackage)
        }
    }

    fun setTouchabilityOfOpenPackage(touchable : Boolean){
        openPackage.dragFrom = touchable
        openPackage.dragTo = touchable
        manageOpenPackage()
    }

    fun setVisibilityObjectToChooseFrom(visible : Boolean){
        if (!visible){
            screenObjects.remove(objectsToChooseFrom)
            scaleWidthProportion = Pair(4, 5)
            if (widthView > 0 && heightView > 0 ) changeSizeScreenObjects()
            invalidate()
            return
        }
        if (!screenObjects.contains(objectsToChooseFrom)){
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

    fun setEquation(sysOfEq : SystemOfEquations, indexEq : Int) : Boolean{
        if (! sysOfEq.allBracketsSame() || indexEq >= sysOfEq.equations.size)
            return false
        equation = sysOfEq.equations[indexEq]
        equationSystem = sysOfEq
        originalEqSys = sysOfEq
        val variableScreenTypes: MutableList<String> = createMapingForScreenVariablesForNewEq()

        changeObjToChooseFromForNewEq(variableScreenTypes)

        try {
            loadEquation()
        }catch (e : java.lang.Exception){
            defaultEquations()
            loadEquation()
            return false
        }
        return true
    }

    private fun defaultEquations() {
        equation = Equation(Addition(mutableListOf()), Addition(mutableListOf()))
        equationSystem = SystemOfEquations(mutableListOf())
        originalEqSys = SystemOfEquations(mutableListOf())
        screenVariableToStringVar = mutableMapOf<String, String>(
            Ball(context, 1)::class.toString() to "x",
            Cube(context, 1)::class.toString() to "y",
            Cylinder(context, 1)::class.toString() to "z",
        )
    }

    private fun changeObjToChooseFromForNewEq(variableScreenTypes: MutableList<String>) {
        val containsPackages = equationSystem.containsBracket()
        setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects().filter {
            it is ScaleValue || (containsPackages && it is Package) ||
                    variableScreenTypes.contains(it::class.toString())
        }.toMutableList())
    }

    private fun createMapingForScreenVariablesForNewEq(): MutableList<String> {
        val variables = equation.findVariablesStrings()
        maxNumberOfVariableTypes = Math.min(variables.size, 3)
        screenVariableToStringVar = mutableMapOf()
        val variableScreenTypes: MutableList<String> = mutableListOf(
            Ball(context, 1)::class.toString(),
            Cube(context, 1)::class.toString(),
            Cylinder(context, 1)::class.toString()
        ).shuffled()
            .subList(0, maxNumberOfVariableTypes).toMutableList()
        (0 until maxNumberOfVariableTypes).forEach { i ->
            screenVariableToStringVar[variableScreenTypes[i]] = variables[i]
        }
        return variableScreenTypes
    }

    private fun loadEquation(){
        val packagePolynom = equation.findBracket()
        screenObjects.filter { it is ContainerForEquationBoxes }.forEach {
            when (it){
                leftHolder -> (it as ContainerForEquationBoxes).setEquation(equation.left, screenVariableToStringVar)
                rightHolder -> (it as ContainerForEquationBoxes).setEquation(equation.right, screenVariableToStringVar)
                openPackage -> {
                    if (packagePolynom != null) {
                        (it as ContainerForEquationBoxes).setEquation(
                            packagePolynom.polynom,
                            screenVariableToStringVar
                        )
                    }else{
                        (it as ContainerForEquationBoxes).setEquation(
                            Addition(mutableListOf()),
                            screenVariableToStringVar
                        )
                    }
                    changeInsideOfPackages()
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        padding = heightView / 100
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
            widthView * scaleWidthProportion.first / scaleWidthProportion.second - padding * 2,
            heightView - padding * 2,
            widthView / 500 + padding, heightView / 20 + padding
        )
    }

    private fun changeSizeBin(obj: ScreenObject, heightOfBin: Int) : Int{
        if (screenObjects.contains(openPackage) && screenObjects.contains(objectsToChooseFrom))
            obj.sizeChanged(widthView / 10 - padding * 2, heightView - padding * 2,
                widthView * 33 / 48 + padding, padding)
        else
            obj.sizeChanged(
                widthView / 10 - padding * 2, heightView - padding * 2,
                widthView * 81 / 96 + padding, heightView - heightOfBin + padding
            )

        return obj.height
    }

    private fun changeSizeOpenPackage(obj: ScreenObject, heightOfBin: Int) {
        if (screenObjects.contains(objectsToChooseFrom))
            obj.sizeChanged(
                widthView * 1 / 4 - padding * 2, heightView - 2 * padding,
                widthView * 7 / 8  + padding,heightView - heightOfBin * 7 / 9  + padding
            )
        else
            obj.sizeChanged(
                widthView * 1 / 4 - padding * 2, heightView - heightOfBin - 10 - padding * 2,
                widthView * 7 / 8 + padding, heightView / 4 + padding
            )
    }

    private fun changeSizeObjectsToChooseFrom(obj: ScreenObject) {
        if (screenObjects.contains(openPackage))
            obj.sizeChanged(
                widthView / 5 - widthView / 25 - padding * 2, heightView / 2 - padding * 2,
                widthView * 4 / 5 + widthView / 50, 10
            )
        else
            obj.sizeChanged(
                widthView / 5 - widthView / 25 - padding * 2, heightView * 3 / 5 - padding * 2,
                widthView * 4 / 5 + widthView / 50, 10
            )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || screenTouchDisabled) return true

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
        for (obj in containersToDragFrom()) {
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
        if (draggedObject != null && draggedFarEnough()) {
            droppedObject(draggedObject!!)
        }
        checkEquality()
        draggedObject = null
        draggedFrom = null
        this.invalidate()
    }

    fun checkEquality(){
        val comparison = equation.compareLeftRight()
        val newAngle = maxAngleScaleAnim * comparison
        if (angleOfScale != newAngle){
            rotationScaleAnimation(newAngle)
        }
        Log.i("equality", "check equality")
    }

    fun droppedObject(obj : EquationObject){
        if (bins().any { it.isIn(obj) }){
            removeDraggedObjFromContainer(draggedFrom, true)
        } else {
            containersToManipulate().forEach{ container ->
                if (container.isIn(obj)){
                    dropObjIntoContainer(obj, container)
                    invalidate()
                    return
                }
            }
        }
        this.invalidate()
    }

    private fun dropObjIntoContainer(obj: EquationObject, container : ContainerForEquationBoxes) {
        if (container != draggedFrom && container is HolderOfWeights
            && draggedFrom is HolderOfWeights && !(obj is ScaleValue)){
            return
        }
        if (container != draggedFrom && container is HolderOfWeights
            && draggedFrom is HolderOfWeights && obj is ScaleValue){
            container.substractValueFromOtherValueIDifferentHolder(draggedFrom as ContainerForEquationBoxes, obj, equationSystem)
            return
        }
        if (container == draggedFrom && containersToManipulate().contains(draggedFrom)
            && obj is ScaleValue){
            container.addValueToOtherValueInTheSameHolder(obj, equationSystem)
            return
        }
        var eqObj = obj
        if (eqObj is Package){
            val variable = eqObj.insideObject.filter { it is ScaleVariable }.firstOrNull()
            eqObj = if (variable is ScaleVariable) variable!! else eqObj
        }
        if ((eqObj is ScaleVariable && !possibleToAddVariableType(eqObj)) ||
            (obj is Package && container is OpenPackage)) {
            removeDraggedObjFromContainer(draggedFrom, false)
            return
        }
        try {
            val equationsWithOpenPackage = equationSystem.equations +
                    Equation(openPackage.getBracket(), openPackage.getBracket())
            val eqSys = SystemOfEquations(equationsWithOpenPackage)
            container.addEquationObjIntoHolder(obj, eqSys)
            removeDraggedObjFromContainer(draggedFrom, true)
            if (container is OpenPackage)
                changeInsideOfPackages()
            Log.i("rovnica", "addObj: " + equation)
        }catch (e : java.lang.Exception){
            removeDraggedObjFromContainer(draggedFrom, false)
        }
    }

    private fun changeInsideOfPackages(){
        containersToDragFrom().flatMap { it.returnPackages() }
            .forEach { it.putObjectsIn(openPackage.getInsideObjects().toMutableList()) }
    }

    private fun possibleToAddVariableType(obj : EquationObject) : Boolean {
        val listOfVariables = containersToManipulate().flatMap { it.insideVariableTypes() }
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
        holder.removeDraggedObject(equationSystem, delete)
        if (delete && holder is OpenPackage)
            changeInsideOfPackages()
        Log.i("rovnica", "removeDraggedObj: " + equation)
    }

    fun rotationScaleAnimation(targetAngle : Float){
        screenTouchDisabled = true
        var deltaAngle = 0.07F
        val countDownInterval = 20L
        val countDownTime = ((Math.max(targetAngle, angleOfScale) -
                Math.min(targetAngle, angleOfScale)) / deltaAngle * countDownInterval ).toLong()
        deltaAngle *= if (targetAngle > angleOfScale) 1 else -1
        object : CountDownTimer(countDownTime, countDownInterval){
            override fun onTick(p0: Long) {
                angleOfScale += deltaAngle
                rotateScale(angleOfScale)
            }

            override fun onFinish() {
                angleOfScale = targetAngle
                rotateScale(angleOfScale)
                screenTouchDisabled = false
                Log.i("equality", "angle: " + angleOfScale + " targetAngle: " + targetAngle)
            }
        }.start()

    }

    fun rotateScale(angle : Float){
        angleOfScale= angle
        val arm : ArmOfScale? = screenObjects.filter { it is ArmOfScale }.firstOrNull() as ArmOfScale?
        if (arm == null)
            return
        arm.rotate(angle)
        screenObjects.filter { it is HolderOfWeights }.forEach { (it as HolderOfWeights).rotateScale(angle) }
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.WHITE)
        for (obj in screenObjects) {
            obj.draw(canvas!!, paint)
        }
        if (draggedObject != null &&
            draggedFarEnough()
        )
            draggedObject?.draw(canvas!!, paint)
    }

    private fun draggedFarEnough() =
        (Math.abs(draggedObject!!.x - draggedObjOriginalPos.first) > draggedObject!!.width / 2
                ||
                Math.abs(draggedObject!!.y - draggedObjOriginalPos.second) > draggedObject!!.height / 2)

    override fun onLongPress(event: MotionEvent?) {
        if (event == null) return

        for (obj in containersToDragFrom()) {
            clickedObject = obj.onLongPress(event)
            if (clickedObject != null) {
                break
            }
        }
        invalidate()
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        if (event == null) return true

        for (obj in containersToDragFrom()) {
            clickedObject = obj.onDoubleTap(event)
            if (clickedObject != null) {
                break
            }
        }
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

    private fun containersToDragFrom() : List<ScreenObject> = screenObjects.filter { it.dragFrom }

    private fun containersToManipulate() : List<ContainerForEquationBoxes> = screenObjects.filter {
        it is ContainerForEquationBoxes}.filter {it.dragFrom && it.dragTo} as List<ContainerForEquationBoxes>

    private fun bins() : List<ScreenObject> = screenObjects.filter { !it.dragFrom && it.dragTo }
}