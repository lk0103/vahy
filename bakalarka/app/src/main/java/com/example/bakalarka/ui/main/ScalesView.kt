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
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import com.example.bakalarka.R
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.objects.*
import com.example.vahy.equation.Addition
import com.example.vahy.objects.*
import java.lang.Exception


class ScalesView(context: Context, attrs: AttributeSet)
            : View(context, attrs), GestureDetector.OnGestureListener,
                    GestureDetector.OnDoubleTapListener{
    private val screenObjects = mutableListOf<ScreenObject>()
    private var draggedObject : EquationObject? = null
    private var draggedObjOriginalPos = Pair(0, 0)
    private var draggedFrom : ContainerForEquationBoxes? = null
    private var clickedObject : EquationObject? = null
    private var maxNumberOfVariableTypes = 1
    private var buildEquationTask = false

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

    private val scaleWidthSmaller = Pair(37, 48)
    private val scaleWidthBigger = Pair(4, 5)
    private var scaleWidthProportion = scaleWidthSmaller

    private var widthView = 0
    private var heightView = 0
    private var padding = 0

    private val paint = Paint()
    private var gDetector: GestureDetectorCompat? = null


    init {
        this.gDetector = GestureDetectorCompat(context, this)
        gDetector?.setOnDoubleTapListener(this)
        gDetector?.setIsLongpressEnabled(true)
        screenObjects.add(BaseOfScale(context))
        screenObjects.add(leftHolder)
        screenObjects.add(rightHolder)
        screenObjects.add(ArmOfScale(context, angleOfScale))
        screenObjects.add(bin)
        screenObjects.add(objectsToChooseFrom)
        screenObjects.add(openPackage)
    }

    fun setBuildEquationTask(isBuild : Boolean, isOpenPackage : Boolean){
        buildEquationTask = isBuild
        setTouchabilityOfOpenPackage(true)
        setVisibilityOfObjsDependingOnBracket(true)
        setVisibilityOpenPackage(isOpenPackage)
        changeLayout(isOpenPackage)
        if (!isOpenPackage) setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects()
            .filter { it !is Package }.toMutableList())
    }

    fun setTouchabilityOfOpenPackage(touchable : Boolean){
        screenObjects.filter { it is OpenPackage }.forEach {
                it.dragFrom = touchable
                it.dragTo = touchable
            }
    }

    fun setObjectsToChooseFrom(objects : MutableList<EquationObject>){
        screenObjects.filter { it is ObjectsToChooseFrom }
            .forEach { (it as ObjectsToChooseFrom).setInsideObject(objects) }
        invalidate()
    }

    fun setVisibilityOfObjsDependingOnBracket(containsBrackets : Boolean){
        setVisibilityObjToChooseFrom(containsBrackets)
        setVisibilityOpenPackage(containsBrackets)

        changeLayout(containsBrackets)
        invalidate()
    }

    private fun changeLayout(containsBrackets: Boolean) {
        if (containsBrackets) scaleWidthProportion = scaleWidthSmaller
        else scaleWidthProportion = scaleWidthBigger

        if (widthView > 0 && heightView > 0) changeSizeScreenObjects()
    }

    private fun setVisibilityObjToChooseFrom(containsBrackets: Boolean) {
        screenObjects.filter { it is ObjectsToChooseFrom }
            .forEach { it.visibility = containsBrackets }
    }

    private fun setVisibilityOpenPackage(containsBrackets: Boolean) {
        screenObjects.filter { it is OpenPackage }
            .forEach { it.visibility = containsBrackets }
    }


    fun setEquation(sysOfEq : SystemOfEquations, indexEq : Int,
                    varScreenTypes : MutableMap<String, String>? = null) : Boolean{
        if (varScreenTypes == null) defaultEquations()
        if (! sysOfEq.allBracketsSame() || indexEq >= sysOfEq.equations.size
            || indexEq < 0 ) {
            loadEquation()
            return false
        }
        equation = sysOfEq.equations[indexEq]
        equationSystem = sysOfEq
        originalEqSys = sysOfEq.copy()
        val variableScreenTypes: MutableList<String> =
            varScreenTypes?.keys?.toMutableList() ?: createMapingForScreenVariablesForNewEq()

        if (!buildEquationTask)
            setVisibilityOfObjsDependingOnBracket(equationSystem.containsBracket())

        try {
            loadEquation()
            if (!buildEquationTask)
                changeObjToChooseFromForNewEq(variableScreenTypes)
            checkEquality()
        }catch (e : java.lang.Exception){
            defaultEquations()
            loadEquation()
            return false
        }
        invalidate()
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
        setObjectsToChooseFrom(mutableListOf(
            Ball(context, 1),
            Cube(context, 1),
            Cylinder(context, 1),
            Ballon(context, -1),
            Weight(context, 1),
            Package(context)
        ))
    }

    private fun changeObjToChooseFromForNewEq(variableScreenTypes: MutableList<String>) {
        if (screenObjects.filter { it is ObjectsToChooseFrom }.all { !it.visibility })
            return

        val containsPackages = equationSystem.containsBracket()

        setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects().filter {
            it is ScaleValue || (containsPackages && it is Package) ||
                    variableScreenTypes.contains(it::class.toString())
        }.filter { eqObj -> !containsPackages || openPackage.getInsideObjects().any { packageObj ->
            eqObj::class == packageObj::class } }.toMutableList())
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
                            packagePolynom.polynom, screenVariableToStringVar)
                    }else{
                        (it as ContainerForEquationBoxes).setEquation(
                            Addition(mutableListOf()), screenVariableToStringVar)
                    }
                    changeInsideOfPackages()
                }
            }
        }
    }

    fun restoreOriginalEquation(){
        setEquation(originalEqSys, equationSystem.equations.indexOf(equation),
            screenVariableToStringVar)
    }

    fun getSolutions() : Map<String, Int> {
        val solutions = equationSystem.solutions
        return screenVariableToStringVar.mapValues { solutions[it.value] ?: 0 }
    }

    fun getEquations() : List<Equation> = equationSystem.equations

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        padding = heightView / 100
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun changeSizeScreenObjects() {
        screenObjects.forEach { obj ->
            if (obj is BaseOfScale) {
                obj.changeSizeInScaleView(widthView, heightView, padding, scaleWidthProportion)
            } else if (obj is ArmOfScale) {
                obj.changeSizeInScaleView(widthView, heightView, padding, scaleWidthProportion)
            } else if (obj is HolderOfWeights) {
                obj.changeSizeInScaleView(widthView, heightView, padding, scaleWidthProportion)
            } else if (obj is Bin) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is ObjectsToChooseFrom }.all { it.visibility },
                    screenObjects.filter { it is OpenPackage }.all { it.visibility })
            } else if (obj is OpenPackage) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is ObjectsToChooseFrom }.all { it.visibility })
            } else if (obj is ObjectsToChooseFrom) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is OpenPackage }.all { it.visibility })
            } else
                obj.sizeChanged(widthView, heightView, 0, 0)
        }
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
        Log.i("build", "rovnica: " + equationSystem)
        this.invalidate()
    }

    fun checkEquality(){
        if (buildEquationTask)
            return
        val comparison = equation.compareLeftRight()
        val newAngle = maxAngleScaleAnim * comparison
        if (angleOfScale != newAngle){
            rotationScaleAnimation(newAngle)
        }
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
            && draggedFrom is HolderOfWeights && obj is ScaleValue){
            container.substractValueFromOtherValueIDifferentHolder(draggedFrom as ContainerForEquationBoxes, obj, equationSystem)
            return
        }
        if (container == draggedFrom && containersToManipulate().contains(draggedFrom)
            && obj is ScaleValue){
            container.addValueToOtherValueInTheSameHolder(obj, equationSystem)
            return
        }
//        var eqObj = obj
//        if (eqObj is Package){
//            Log.i("build", "obj is package")
//            val variable = eqObj.insideObject.filter { it is ScaleVariable }.firstOrNull()
//            eqObj = if (variable is ScaleVariable) variable!! else eqObj
//        }
//        if ((eqObj is ScaleVariable && !possibleToAddVariableType(eqObj)) ||
//            (obj is Package && container is OpenPackage)) {
//            Log.i("build", "not possible to add variable type")
//            removeDraggedObjFromContainer(draggedFrom, false)
//            return
//        }
        //nahradene
        if ((obj is Package && container is OpenPackage)) {
            Log.i("build", "not possible to add variable type")
            removeDraggedObjFromContainer(draggedFrom, false)
            return
        }

        try {
            Log.i("build", "dropObj")
            val equationsWithOpenPackage = equationSystem.equations +
                    Equation(openPackage.getBracket(), openPackage.getBracket())
            val eqSys = SystemOfEquations(equationsWithOpenPackage)
            container.putEquationObjIntoHolder(obj, eqSys)
            removeDraggedObjFromContainer(draggedFrom, true)
            if (container is OpenPackage)
                changeInsideOfPackages()
        }catch (e : java.lang.Exception){
            Log.i("build", " " + e.message)
            removeDraggedObjFromContainer(draggedFrom, false)
        }
    }

    private fun changeInsideOfPackages(){
        containersToDragFrom().flatMap { it.returnPackages() }
            .forEach { it.putObjectsIn(openPackage.getInsideObjects().toMutableList()) }

        equationSystem.setAllBracketInsides(openPackage.polynom)
        Log.i("build", "openPackage: " + openPackage.polynom)
    }

    ///TOTO ASI NETREBA STACI AK ZMENIM OBSAH ObjectsToChooseFrom
//    private fun possibleToAddVariableType(obj : EquationObject) : Boolean {
//        val listOfVariables = containersToManipulate().flatMap { it.insideVariableTypes() }
//        var count = 0
//        val typeVariableClasses = listOf(Ball(context, 0), Cube(context, 0), Cylinder(context, 0))
//        typeVariableClasses.forEach { type ->
//            if (listOfVariables.any { it::class == type::class }) {
//                if (obj::class == type::class )
//                    return true
//                count++
//            }
//        }
//        return count < maxNumberOfVariableTypes
//    }

    fun removeDraggedObjFromContainer(holder : ContainerForEquationBoxes?, delete: Boolean = false){
        if (holder == null)
            return
        holder.removeDraggedObject(equationSystem, delete)
        if (delete && holder is OpenPackage)
            changeInsideOfPackages()
    }

    fun rotationScaleAnimation(targetAngle : Float){
        screenTouchDisabled = true
        var deltaAngle = 0.08F
        val countDownInterval = 18L
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
            if (clickedObject is Package && obj is ContainerForEquationBoxes){
                unpackPackage(obj)
            }
            if (clickedObject != null) {
                break
            }
        }
        invalidate()
        return true
    }

    private fun unpackPackage(obj: ContainerForEquationBoxes) {
        val bracket = equationSystem.findBracket()
        if (bracket != null) {
            val eqCopy = equationSystem.copy()
            try {
                obj.removeDraggedObject(equationSystem, true, clickedObject)
                bracket.polynom.addends.forEach { polynom ->
                    obj.addObjBasedOnPolynom(polynom, equationSystem)
                }
            } catch (e: Exception) {
                setEquation(
                    eqCopy, equationSystem.equations.indexOf(equation),
                    screenVariableToStringVar
                )
            }
        }
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

    private fun containersToDragFrom() : List<ScreenObject> =
        screenObjects.filter { it.visibility && it.dragFrom }

    private fun containersToManipulate() : List<ContainerForEquationBoxes> = screenObjects.filter {
        it is ContainerForEquationBoxes}.filter {it.visibility && it.dragFrom && it.dragTo}
            as List<ContainerForEquationBoxes>

    private fun bins() : List<ScreenObject> =
        screenObjects.filter { it.visibility && !it.dragFrom && it.dragTo && it is Bin}

    fun getScaleVariables() : List<ScaleVariable> =
        listOf(Ball(context, 1), Cube(context, 1), Cylinder(context, 1))
            .filter { screenVariableToStringVar.keys.contains(it::class.toString()) }
}