package com.vahy.vahy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.vahy.bakalarka.equation.Bracket
import com.vahy.bakalarka.equation.SystemOfEquations
import com.vahy.bakalarka.objects.*
import com.vahy.bakalarka.objects.menu.FailSuccessIcon
import com.vahy.bakalarka.objects.menu.LevelNumber
import com.vahy.bakalarka.tasks.ScaleEquations
import com.vahy.bakalarka.tasks.ScaleViewAnimations
import com.vahy.bakalarka.tasks.ScaleViewObjectManipulation
import com.vahy.vahy.equation.Addition
import com.vahy.vahy.objects.*


class ScalesView(context: Context, attrs: AttributeSet)
            : View(context, attrs), GestureDetector.OnGestureListener,
                    GestureDetector.OnDoubleTapListener{
    private val screenObjects = mutableListOf<ScreenObject>()

    private var isBuildEquationTask = false
    var isSystemOf2Eq = false
    var isCreateTask = false
    private var hasPackage = false
    var hasBalloon = false
    var isRotating = false
    var showNewLevelMessage = -1

    var scaleEquations = ScaleEquations(context, this)

    private var scaleAnimations = ScaleViewAnimations(context, this)

    private var objectManipulation = ScaleViewObjectManipulation(context, this)

    var screenTouchDisabled = false

    private var objectsToChooseFrom = ObjectsToChooseFrom(mutableListOf(
                        Ball(context, 1), Cube(context, 1),
                        Cylinder(context, 1), Ballon(context, -1),
                        Weight(context, 1), Package(context)))
    private var openPackage = OpenPackage(context)

    var widthView = 0
    var heightView = 0
    private var padding = 0


    private val paint = Paint()
    private var gDetector: GestureDetectorCompat? = null


    init {
        this.gDetector = GestureDetectorCompat(context, this)
        gDetector?.setOnDoubleTapListener(this)
        gDetector?.setIsLongpressEnabled(true)
        screenObjects.add(Scale(context))
        screenObjects.add(Bin(context))
        screenObjects.add(objectsToChooseFrom)
        screenObjects.add(openPackage)
    }

    fun defaultScale(){
        isBuildEquationTask = false
        isSystemOf2Eq = false
        hasPackage = false
        hasBalloon = false
        isRotating = false
        scaleEquations.setSolutionBuild()
        screenObjects.removeAll( screenObjects.filter { it is Scale && it.isIcon } )
        defaultEquations()
        setTouchabilityOfOpenPackage(false, false)
    }

    private fun defaultEquations() {
        scaleEquations.setEquation()
        scaleEquations.setEquationSystem()
        scaleEquations.setPreviousEqSys()

        if( !isBuildEquationTask) {
            scaleEquations.defaultScreenVarToString()
        }

        defaultObjectsToChoose()
    }

    private fun defaultObjectsToChoose() {
        setObjectsToChooseFrom(
            mutableListOf(
                Weight(context, 1),
                Ballon(context, -1),
                Ball(context, 1),
                Cube(context, 1),
                Cylinder(context, 1),
                Package(context)
            )
        )

        changeObjChooseFromDefault()
    }

    fun setBuildEquationTask(isBuild : Boolean, sysEq: SystemOfEquations,
                             mappingVarToString : MutableMap<String, String>? = null){
        isBuildEquationTask = isBuild
        hasPackage = sysEq.containsBracket()
        scaleEquations.setSolutionBuild(sysEq.solutions)

        screenObjects.filter { it is Scale }.forEach { (it as Scale).buildEquationTask = isBuild }

        setTouchabilityOfOpenPackage(true, true)
        setVisibilityOfObjs()
        changeLayout()

        scaleEquations.setEquationSystem(sysEq)
        scaleEquations.setEquation(sysEq.equations[0])

        if (mappingVarToString != null)
            scaleEquations.setScreenVarToString(mappingVarToString.toMutableMap())
        else
            scaleEquations.createMappingForScreenVariablesForNewEq()

        scaleEquations.setEquationSystem()

        setEquation(SystemOfEquations(mutableListOf()), 0,
                                scaleEquations.getScreenVarToString())
    }

    fun setSystem2EqTask(isSystem : Boolean){
        if (isSystem && !isSystemOf2Eq) {
            isSystemOf2Eq = true
            val scaleIcon = Scale(context, false, false)
            scaleIcon.isIcon = true
            scaleIcon.changeLayout(false)
            screenObjects.add(0, scaleIcon)
            return
        }
        else if (!isSystem && isSystemOf2Eq)
            screenObjects.removeAll( screenObjects.filter { it is Scale && it.isIcon } )
    }

    fun setHasPackage(has : Boolean){
        hasPackage = has
        setTouchabilityOfOpenPackage(true, false)
    }

    @JvmName("setHasBallon1")
    fun setHasBalloon(has : Boolean){
        hasBalloon = has
    }

    fun setSolutionBuild(newSolution : MutableMap<String, Int>){
        scaleEquations.setSolutionBuild(newSolution)
    }

    fun setTouchabilityOfOpenPackage(from : Boolean, to : Boolean){
        screenObjects.filter { it is OpenPackage }.forEach {
                it.dragFrom = from
                it.dragTo = to
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
            .forEach { it.visibility = (isBuildEquationTask || isSystemOf2Eq) }
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

        scaleEquations.setEqWithIndex(sysOfEq, indexEq)
        if (isBuildEquationTask && !sysOfEq.hasValidSolution()){
            scaleEquations.setStoredSolutions()
        }

        if (!isBuildEquationTask && varScreenTypes == null) {
            scaleEquations.createMappingForScreenVariablesForNewEq()
        }

        //added for editors
        if (varScreenTypes != null){
            scaleEquations.setScreenVarToString(varScreenTypes)
        }

        setVisibilityOfObjs()
        try {
            changeEquation(bracket)
        }catch (e : java.lang.Exception){
            defaultEquations()
            loadEquation()
            return false
        }

        scaleEquations.storeFirstEquation()

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

        checkEquality()
        if (isSystemOf2Eq){
            screenObjects.filter { it is Scale }.forEach {
                (it as Scale).changeSizeBoxesInHolders(getMaxNumBoxes()) }
        }
    }

    fun getMaxNumBoxes() = screenObjects.filter { it is Scale }
        .map { (it as Scale).getMaxNumberBoxes() }.max()


    private fun changeObjChooseFromDefault() {
        setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects()
            .filter {
                (hasPackage || it !is Package) && (hasBalloon || it !is Ballon) &&
                        (it !is ScaleVariable || scaleEquations.containsVariableMapScreenObj(it))
            }
            .toMutableList())
    }


    private fun changeObjChooseFromSystem2Eq() {
        if (screenObjects.filter { it is ObjectsToChooseFrom }.all { !it.visibility }
            || scaleEquations.getSizePreviousEq() > 1)
            return

        setObjectsToChooseFrom(objectsToChooseFrom.getInsideObjects().filter { objToChoose ->
            (containersToManipulate() + getScaleIcons()).flatMap { it.getAllEquationsObjectsTypes() }
                .contains(objToChoose::class.toString())
        }.filter { eqObj -> hasBalloon || eqObj !is Ballon }.toMutableList())
    }

    private fun loadEquation(bracket : Bracket? = null){
        val packagePolynom = if (bracket == null) scaleEquations.getEquation().findBracket()
                             else bracket

        screenObjects.filter { it is Scale }.forEach {
            (it as Scale).loadEquation(scaleEquations.getEquation(),
                scaleEquations.getScreenVarToString())
        }

        screenObjects.filter { it is OpenPackage }.forEach {
            if (packagePolynom != null) {
                (it as ContainerForEquationBoxes).setEquation(
                    packagePolynom.polynom, scaleEquations.getScreenVarToString())
            }else{
                (it as ContainerForEquationBoxes).setEquation(
                    Addition(mutableListOf()), scaleEquations.getScreenVarToString())
            }
            objectManipulation.changeInsideOfPackages()
        }

    }

    private fun loadEquationScaleIcon(){
        if (scaleEquations.getSizeEqSystem() != 2)
            return
        screenObjects.filter { it is Scale && it.isIcon}.forEach {
            (it as Scale).loadEquation(scaleEquations.getSecondEquation(),
                scaleEquations.getScreenVarToString())
        }
    }

    fun switchBetweenEquations(){
        setEquation(scaleEquations.getEquationSystem(), scaleEquations.nextEquationIndex(),
            scaleEquations.getScreenVarToString())
    }

    fun switchToEqWithIndex(ix : Int){
        if (isSystemOf2Eq && scaleEquations.getIndexEquation() != ix)
            switchBetweenEquations()
    }

    fun previousEquation(){
        scaleEquations.previousEquation()
    }

    fun getSolutions() = scaleEquations.getSolutions()

    fun getEquations() = scaleEquations.getEquations()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        widthView = w
        heightView = h
        padding = heightView / 100
        changeSizeScreenObjects()
        super.onSizeChanged(w, h, oldw, oldh)
        if (showNewLevelMessage != -1)
            scaleAnimations.unlockedLevel()
    }

    fun changeSizeScreenObjects() {
        screenObjects.forEach { obj ->
            if (obj is Scale) {
                if (isSystemOf2Eq && !obj.isIcon)
                    obj.shiftX = heightView * 19 / 168
                obj.changeSizeScreenObjects(widthView, heightView, padding)
                obj.shiftX = 0
            }else if (obj is Bin) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is ObjectsToChooseFrom }.all { it.visibility },
                    screenObjects.filter { it is OpenPackage }.all { it.visibility })
            } else if (obj is OpenPackage) {
                obj.changeSizeInScaleView(widthView, heightView, padding)
            } else if (obj is ObjectsToChooseFrom) {
                obj.changeSizeInScaleView(widthView, heightView, padding,
                    screenObjects.filter { it is OpenPackage }.all { it.visibility })
            } else if (obj is FailSuccessIcon){
                obj.changeSizeInScaleView(widthView, heightView)
            }else obj.sizeChanged(widthView, heightView, 0, 0)
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return true

        if (screenTouchDisabled) {
            if (scaleAnimations.messageResult != null)
                scaleAnimations.cancelShownResultMessage()
            else if (scaleAnimations.messageNewLevel != null)
                scaleAnimations.cancelNewLevelMessage()
            return false
        }

        gDetector?.onTouchEvent(event)
        if (objectManipulation.clickedObject != null){
            objectManipulation.onClicked()
            return true
        }

        val action = event.action
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
            objectManipulation.onTouchDown(event)
        }else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP
            || action == MotionEvent.ACTION_CANCEL) {
            objectManipulation.onTouchUp()
        } else if (action == MotionEvent.ACTION_MOVE) {
            objectManipulation.onTouchMove(event)
        }
        return true
    }

    override fun onDoubleTap(event: MotionEvent?): Boolean {
        return objectManipulation.onDoubleTap(event)
    }

    fun checkEquality(){
        if (isBuildEquationTask && isSystemOf2Eq){
            scaleEquations.exchangeSolutionsXandY()
        }
        screenObjects.filter { it is Scale && !it.isIcon}
            .forEach { (it as Scale).checkEquality(scaleEquations.getEquation(), this) }
    }


    fun storeEquation() {
        scaleEquations.storeEquation(openPackage.getBracket())
    }

    fun changeConstantSizeBorders(){
        val allCons = getAllConstantValues()
        val sizeCons = allCons.size
        val thirdSizeCons = sizeCons / 3
        var constantBorders = allCons
        if (sizeCons > 3){
            constantBorders = mutableListOf()
            constantBorders.add(allCons[thirdSizeCons - 1])
            constantBorders.add(allCons[thirdSizeCons * 2 - 1])
            constantBorders.add(allCons[sizeCons - 1])
        }

        containersToManipulate().forEach { it.setConsSizeBorders(constantBorders.toMutableList()) }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        for (obj in screenObjects.filter { it !is FailSuccessIcon && it !is LevelNumber }) {
            obj.draw(canvas!!, paint)
        }

        if (objectManipulation.draggedObject != null &&
            objectManipulation.draggedFarEnough()) {
            objectManipulation.draggedObject?.draw(canvas!!, paint)
        }

        scaleAnimations.draw(canvas, paint)
    }

    fun earthquakeAnimation(){
        scaleAnimations.earthquakeAnimation()
    }

    fun failSuccessShow(success : Boolean){
        scaleAnimations.failSuccessShow(success)
    }

    fun unlockedLevel(){
        scaleAnimations.unlockedLevel()
    }

    fun cancelShownResultMessage(){
        scaleAnimations.cancelShownResultMessage()
    }


    fun cancelShownNewLevelMessage(){
        scaleAnimations.cancelShownNewLevelMessage()
    }


    fun isInBin(obj: EquationObject) = bins().any { it.isIn(obj) }

    fun isClickedScaleIcon(event: MotionEvent) =
        screenObjects.filter { it is Scale && it.isIcon }
            .any { it.isIn(event.x.toInt(), event.y.toInt()) }

    fun getAllConstantValues() : List<Int> = containersToManipulate()
            .flatMap { it.getAllConstantValues() }.toSet()
            .toList().sorted()

    fun containersToDragFrom() : List<ScreenObject> =
        screenObjects.filter { it.visibility && it.dragFrom && it !is Scale } +
                screenObjects.filter { it is Scale && it.dragFrom}.flatMap {
                    (it as Scale).containersToDragFrom()
                }

    fun containersToManipulate() : List<ContainerForEquationBoxes> =
        screenObjects.filter { it is ContainerForEquationBoxes}
            .filter {it.visibility && it.dragFrom && it.dragTo && it !is Scale}
            as List<ContainerForEquationBoxes> +
        screenObjects.filter { it is Scale && it.dragFrom && it.dragTo }.flatMap {
            (it as Scale).containersToManipulate()
        }

    fun bins() : List<ScreenObject> =
        screenObjects.filter { it.visibility && !it.dragFrom && it.dragTo && it is Bin}

    private fun getScaleIcons() : List<ContainerForEquationBoxes> =
        screenObjects.filter { it is Scale && it.isIcon }
            .flatMap { (it as Scale).containersToManipulate() }

    fun getScales() : List<Scale> =
        screenObjects.filter { it is Scale && !it.isIcon }
            .map { it as Scale }.toList()

    fun getScaleVariables() = scaleEquations.getScaleVariables()

    fun hasSolution() = scaleEquations.hasSolution()

    fun sizeStoredPreviousEquations() = scaleEquations.getSizePreviousEq()

    fun getSystemOfEquations() = scaleEquations.getEquationSystem().copy()

    fun getScreenVarToStringObj() = scaleEquations.getScreenVarToString()

    fun getClickedObject() = objectManipulation.clickedObject

    fun getIndexEquation() = scaleEquations.getIndexEquation()

    fun getOpenPackage() = openPackage

    override fun onLongPress(event: MotionEvent?) {}

    override fun onDown(p0: MotionEvent?): Boolean = true

    override fun onShowPress(p0: MotionEvent?) {}

    override fun onSingleTapUp(p0: MotionEvent?): Boolean = true

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean = true

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean = true

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean = true

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean = true
}