package com.example.vahy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.objects.*
import com.example.bakalarka.objects.menu.FailSuccessIcon
import com.example.vahy.equation.Addition
import com.example.vahy.equation.Constant
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
    private var isBuildEquationTask = false
    private var isSystemOf2Eq = false
    var hasPackage = false

    private var equationSystem = SystemOfEquations(mutableListOf())
    private var previousEqSys = mutableListOf<Pair<SystemOfEquations, Bracket>>()
    private var equation = Equation(Addition(mutableListOf()), Addition(mutableListOf()))
    private var screenVariableToStringVar = mutableMapOf<String, String>(
        Ball(context, 1)::class.toString() to "x",
        Cube(context, 1)::class.toString() to "y",
        Cylinder(context, 1)::class.toString() to "z",
    )

    var screenTouchDisabled = false
    private val bin = Bin(context)
    private var objectsToChooseFrom = ObjectsToChooseFrom(mutableListOf(
                        Ball(context, 1), Cube(context, 1),
                        Cylinder(context, 1), Ballon(context, -1),
                        Weight(context, 1), Package(context)))
    private var openPackage = OpenPackage(context)


    private var widthView = 0
    private var heightView = 0
    private var padding = 0

    private val paint = Paint()
    private var gDetector: GestureDetectorCompat? = null


    init {
        this.gDetector = GestureDetectorCompat(context, this)
        gDetector?.setOnDoubleTapListener(this)
        gDetector?.setIsLongpressEnabled(true)
        screenObjects.add(Scale(context))
        screenObjects.add(bin)
        screenObjects.add(objectsToChooseFrom)
        screenObjects.add(openPackage)
    }

    fun setBuildEquationTask(isBuild : Boolean, isOpenPackage : Boolean){
        isBuildEquationTask = isBuild
        hasPackage = isOpenPackage
        screenObjects.filter { it is Scale }.forEach { (it as Scale).buildEquationTask = isBuild }
        setTouchabilityOfOpenPackage(true)
        setVisibilityOfObjs()
        changeLayout()
        if (!hasPackage) setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects()
            .filter { it !is Package }.toMutableList())
        screenVariableToStringVar = mutableMapOf<String, String>(
            Ball(context, 1)::class.toString() to "x",
            Cube(context, 1)::class.toString() to "y",
            Cylinder(context, 1)::class.toString() to "z",
        )
        setEquation(SystemOfEquations(mutableListOf()), 0, screenVariableToStringVar)
    }

    fun setSystem2EqTask(isSystem : Boolean){
        if (isSystem && !isSystemOf2Eq) {
            isSystemOf2Eq = true
            val scaleIcon = Scale(context, false, false)
            scaleIcon.isIcon = true
            scaleIcon.changeLayout(false)
            screenObjects.add(scaleIcon)
            return
        }
        screenObjects.removeAll( screenObjects.filter { it is Scale && it.isIcon } )
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

    fun setVisibilityOfObjs(){
        setVisibilityObjToChooseFrom()
        setVisibilityOpenPackage()

        changeLayout()
        invalidate()
    }

    private fun changeLayout() {
        screenObjects.filter { it is Scale }
            .forEach { (it as Scale).changeLayout(hasPackage) }

        if (widthView > 0 && heightView > 0) changeSizeScreenObjects()
    }

    private fun setVisibilityObjToChooseFrom() {
        screenObjects.filter { it is ObjectsToChooseFrom }
            .forEach { it.visibility = (hasPackage || isBuildEquationTask || isSystemOf2Eq) }
    }

    private fun setVisibilityOpenPackage() {
        screenObjects.filter { it is OpenPackage }
            .forEach { it.visibility = hasPackage }
    }

    fun setEquation(sysOfEq : SystemOfEquations, indexEq : Int,
                    varScreenTypes : MutableMap<String, String>? = null,
                    bracket : Bracket? = null) : Boolean{
        if (varScreenTypes == null) defaultEquations()
        if (! sysOfEq.allBracketsSame() || indexEq >= sysOfEq.equations.size || indexEq < 0 ) {
            loadEquation()
            return false
        }
        equation = sysOfEq.equations[indexEq]
        equationSystem = sysOfEq

        if (!isBuildEquationTask && varScreenTypes == null) {
            createMappingForScreenVariablesForNewEq()
        }

        setVisibilityOfObjs()
        try {
            changeEquation(bracket)
        }catch (e : java.lang.Exception){
            defaultEquations()
            loadEquation()
            return false
        }

        if (previousEqSys.isEmpty())
            storeEquation()

        invalidate()
        return true
    }

    private fun changeEquation(bracket: Bracket?) {
        loadEquation(bracket)
        if (isSystemOf2Eq) {
            loadEquationScaleIcon()
            if (!isBuildEquationTask)
                changeObjChooseFromSystem2Eq()
        }
        if (!isBuildEquationTask && hasPackage)
            changeObjChooseFromDefaultPackage()
        checkEquality()
    }

    private fun defaultEquations() {
        equation = Equation(Addition(mutableListOf()), Addition(mutableListOf()))
        equationSystem = SystemOfEquations(mutableListOf())
        previousEqSys = mutableListOf()
        screenVariableToStringVar = mutableMapOf<String, String>(
            Ball(context, 1)::class.toString() to "x",
            Cube(context, 1)::class.toString() to "y",
            Cylinder(context, 1)::class.toString() to "z",
        )
        defaultObjectsToChoose()
    }

    private fun defaultObjectsToChoose() {
        setObjectsToChooseFrom(
            mutableListOf(
                Ball(context, 1),
                Cube(context, 1),
                Cylinder(context, 1),
                Ballon(context, -1),
                Weight(context, 1),
                Package(context)
            )
        )
        if (!hasPackage) setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects()
            .filter { it !is Package }.toMutableList())
    }

    private fun changeObjChooseFromDefaultPackage() {
        if (screenObjects.filter { it is ObjectsToChooseFrom }.all { !it.visibility }
            || previousEqSys.size > 1)
            return

        setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects()
            .filter { eqObj -> !hasPackage || openPackage.getInsideObjects().any { packageObj ->
            eqObj::class == packageObj::class } }.toMutableList())
    }

    private fun changeObjChooseFromSystem2Eq() {
        if (screenObjects.filter { it is ObjectsToChooseFrom }.all { !it.visibility }
            || previousEqSys.size > 1)
            return

        setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects().filter { objToChoose ->
            containersToManipulate().flatMap { it.getAllEquationsObjects() }
                .contains(objToChoose::class.toString())
        }.toMutableList())
    }

    private fun createMappingForScreenVariablesForNewEq(): MutableList<String> {
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

    private fun loadEquation(bracket : Bracket? = null){
        val packagePolynom = if (bracket == null) equation.findBracket() else bracket

        screenObjects.filter { it is Scale }.forEach {
            (it as Scale).loadEquation(equation, screenVariableToStringVar)
        }

        screenObjects.filter { it is OpenPackage }.forEach {
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

    private fun loadEquationScaleIcon(){
        if (equationSystem.equations.size != 2)
            return
        val equationIx = equationSystem.equations.indexOf(equation)
        screenObjects.filter { it is Scale && it.isIcon}.forEach {
            (it as Scale).loadEquation(equationSystem.equations[(equationIx + 1) % 2],
                screenVariableToStringVar)
        }
    }

    private fun switchBetweenEquations(){
        val index = (equationSystem.equations.indexOf(equation) + 1) % 2
        setEquation(equationSystem, index,
            screenVariableToStringVar)
    }

//    fun restoreOriginalEquation(){
//        if (previousEqSys.isEmpty())
//            return
//        val (previous, bracket) = previousEqSys[0]
//        setEquation(previous, equationSystem.equations.indexOf(equation),
//            screenVariableToStringVar, bracket)
//        previousEqSys = mutableListOf()
//        Log.i("storeprev", "restore original " + previousEqSys)
//    }

    fun previousEquation(){
        if (previousEqSys.isEmpty())
            return
        val (previous, bracket) = previousEqSys.removeLast()
        setEquation(previous, equationSystem.equations.indexOf(equation),
            screenVariableToStringVar, bracket)
        Log.i("storeprev", "previous eq " + previousEqSys)
    }

    private fun storeEquation(){
        val additionOfBrackets = openPackage.getBracket()
        val bracket = if (additionOfBrackets.addends.isEmpty()) Bracket(Addition(mutableListOf()))
                    else additionOfBrackets.addends[0].copy() as Bracket
        previousEqSys.add(Pair(equationSystem.copy(), bracket))
        Log.i("storeprev", "store " + previousEqSys)
    }

    private fun removeLastStoredEquation(){
        if (previousEqSys.isEmpty())
            return
        previousEqSys.removeLast()
        Log.i("storeprev", "remove last " + previousEqSys)
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
            if (obj is Scale) {
                obj.changeSizeScreenObjects(widthView, heightView, padding)
            }else if (obj is Bin) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is ObjectsToChooseFrom }.all { it.visibility },
                    screenObjects.filter { it is OpenPackage }.all { it.visibility })
            } else if (obj is OpenPackage) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is ObjectsToChooseFrom }.all { it.visibility })
            } else if (obj is ObjectsToChooseFrom) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is OpenPackage }.all { it.visibility })
            } else if (obj is FailSuccessIcon){
                obj.changeSizeInScaleView(widthView, heightView)
            }else obj.sizeChanged(widthView, heightView, 0, 0)
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
        if (screenObjects.filter { it is Scale && it.isIcon }
                .any { it.isIn(event.x.toInt(), event.y.toInt()) }){
            switchBetweenEquations()
            return
        }
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
            storeEquation()
            if (!droppedObject(draggedObject!!)){
                if (! plusMinus1ToDragged(draggedObject!!))
                    removeLastStoredEquation()
            }
        }
        checkEquality()
        draggedObject = null
        draggedFrom = null
        Log.i("build", "rovnica: " + equationSystem)
        this.invalidate()
    }

    fun checkEquality(){
        screenObjects.filter { it is Scale && !it.isIcon}
            .forEach { (it as Scale).checkEquality(equation, this) }
    }

    fun droppedObject(obj : EquationObject) : Boolean{
        if (bins().any { it.isIn(obj) }){
            removeDraggedObjFromContainer(draggedFrom, true)
        } else {
            containersToManipulate().forEach{ container ->
                if (container.isIn(obj)){
                    dropObjIntoContainer(obj, container)
                    invalidate()
                    return true
                }
            }
        }
        invalidate()
        return false
    }

    private fun dropObjIntoContainer(obj: EquationObject, container : ContainerForEquationBoxes) {
        if (container != draggedFrom && container is HolderOfWeights
            && draggedFrom is HolderOfWeights && obj is ScaleValue){
            if (container.substractValueFromOtherValueIDifferentHolder(
                    draggedFrom as ContainerForEquationBoxes, obj, equationSystem)) {
                return
            }
        }
        if (container == draggedFrom && containersToManipulate().contains(draggedFrom)
            && obj is ScaleValue){
            if (container.addValueToOtherValueInTheSameHolder(obj, equationSystem)){
                return
            }
            plusMinus1ToDragged(obj)
            return
        }
        if ((obj is Package && container is OpenPackage)) {
            removeDraggedObjFromContainer(draggedFrom, false)
            return
        }

        try {
            val equationsWithOpenPackage = equationSystem.equations +
                    Equation(openPackage.getBracket(), openPackage.getBracket())
            val eqSys = SystemOfEquations(equationsWithOpenPackage)
            container.putEquationObjIntoHolder(obj, eqSys)
            removeDraggedObjFromContainer(draggedFrom, true)
            if (container is OpenPackage)
                changeInsideOfPackages()
        }catch (e : java.lang.Exception){
            removeDraggedObjFromContainer(draggedFrom, false)
        }
    }

    private fun plusMinus1ToDragged(obj: EquationObject) : Boolean{
        val container = containersToDragFrom().filter {
            it.isIn(draggedObjOriginalPos.first, draggedObjOriginalPos.second)
        }.firstOrNull()
        if (bins().any { it.isIn(obj) } && container is ContainerForEquationBoxes)
            return true
        if (draggedObjOriginalPos == Pair(0, 0) || container == null)
            return false

        val tolerance = obj.height / 2
        if (obj.y < draggedObjOriginalPos.second - tolerance) {
            container.incrementValue(draggedObjOriginalPos.first, draggedObjOriginalPos.second)
        } else if (obj.y > draggedObjOriginalPos.second + tolerance) {
            container.decrementValue(draggedObjOriginalPos.first, draggedObjOriginalPos.second)
        }
        return container !is ObjectsToChooseFrom
    }

    private fun changeInsideOfPackages(){
        containersToDragFrom().flatMap { it.returnPackages() }
            .forEach { it.putObjectsIn(openPackage.getInsideObjects().toMutableList()) }

        equationSystem.setAllBracketInsides(openPackage.polynom)
        Log.i("build", "openPackage: " + openPackage.polynom)
    }

    fun removeDraggedObjFromContainer(holder : ContainerForEquationBoxes?, delete: Boolean = false){
        if (holder == null)
            return
        holder.removeDraggedObject(equationSystem, delete)
        if (delete && holder is OpenPackage)
            changeInsideOfPackages()
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


    fun failSuccessShow(success : Boolean){
        screenTouchDisabled = true
        val icon = FailSuccessIcon(context, success)
        screenObjects.add(icon)
        icon.changeSizeInScaleView(widthView, heightView)
        invalidate()
        object : CountDownTimer(3000, 3000){
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                screenObjects.remove(icon)
                screenTouchDisabled = false
                invalidate()
            }
        }.start()
    }


    override fun onDoubleTap(event: MotionEvent?): Boolean {
        if (event == null) return true

        for (obj in containersToDragFrom()) {
            clickedObject = obj.returnClickedObject(event.x.toInt(), event.y.toInt())
            if (clickedObject is Package && obj is ContainerForEquationBoxes){
                storeEquation()
                unpackPackage(obj)
                break
            }
            if (clickedObject is ScaleValue && obj is ContainerForEquationBoxes){
                storeEquation()
                divideScaleValue(obj)
                break
            }
        }
        invalidate()
        return true
    }

    private fun divideScaleValue(container: ContainerForEquationBoxes) {
        if (clickedObject !is ScaleValue) return

        val eqCopy = equationSystem.copy()
        val originalBracket = equationSystem.findBracket()?.copy() as Bracket?
        val value = Math.abs((clickedObject as ScaleValue).evaluate())
        val signOfValue = if ((clickedObject as ScaleValue).evaluate() >= 0) 1 else -1
        try {
            container.removeDraggedObject(equationSystem, true, clickedObject)
            container.addObjBasedOnPolynom(
                Constant(signOfValue * (value / 2)), equationSystem
            )
            container.addObjBasedOnPolynom(
                Constant(signOfValue * (value - value / 2)), equationSystem
            )
        } catch (e: Exception) {
            setEquation(
                eqCopy, equationSystem.equations.indexOf(equation),
                screenVariableToStringVar, originalBracket
            )
        }
    }

    private fun unpackPackage(container: ContainerForEquationBoxes) {
        val bracket = equationSystem.findBracket()
        if (bracket != null) {
            val eqCopy = equationSystem.copy()
            val originalBracket = bracket.copy() as Bracket?
            try {
                container.removeDraggedObject(equationSystem, true, clickedObject)
                bracket.polynom.addends.forEach { polynom ->
                    container.addObjBasedOnPolynom(polynom, equationSystem)
                }
            } catch (e: Exception) {
                setEquation(eqCopy, equationSystem.equations.indexOf(equation),
                    screenVariableToStringVar, originalBracket)
            }
        }
    }

    override fun onLongPress(event: MotionEvent?) {
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
        screenObjects.filter { it.visibility && it.dragFrom && it !is Scale } +
                screenObjects.filter { it is Scale && it.dragFrom}.flatMap {
                    (it as Scale).containersToDragFrom()
                }

    private fun containersToManipulate() : List<ContainerForEquationBoxes> =
        screenObjects.filter { it is ContainerForEquationBoxes}
            .filter {it.visibility && it.dragFrom && it.dragTo && it !is Scale}
            as List<ContainerForEquationBoxes> +
        screenObjects.filter { it is Scale && it.dragFrom && it.dragTo }.flatMap {
            (it as Scale).containersToManipulate()
        }

    private fun bins() : List<ScreenObject> =
        screenObjects.filter { it.visibility && !it.dragFrom && it.dragTo && it is Bin}

    fun getScaleVariables() : List<ScaleVariable> =
        listOf(Ball(context, 1), Cube(context, 1), Cylinder(context, 1))
            .filter { screenVariableToStringVar.keys.contains(it::class.toString()) }
}