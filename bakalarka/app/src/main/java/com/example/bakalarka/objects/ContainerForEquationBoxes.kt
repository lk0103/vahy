package com.example.bakalarka.objects

import android.content.Context
import android.util.Log
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.SystemOfEquations
import com.example.vahy.equation.*
import com.example.vahy.objects.ScreenObject

open class ContainerForEquationBoxes(protected val context: Context,
                                     dragFrom : Boolean = true,
                                    dragTo : Boolean = true) :
                        ScreenObject(dragFrom, dragTo) {
    protected var equationObjectBoxes = mutableListOf<EquationObjectBox>()
    protected var maxNumberOfBoxes = 3
    var polynom = Addition(mutableListOf())
    protected var screenVariableToStringVar = defaultScreenObjsToString()
    var biggerNumEquationBoxes = -1

    private fun defaultScreenObjsToString() = mutableMapOf<String, String>(
            Ball(context, 1)::class.toString() to "x",
            Cube(context, 1)::class.toString() to "y",
            Cylinder(context, 1)::class.toString() to "z",
        )

    open fun changeSizeInsideObj() {
    }

    fun setEquation(p : Addition, screenVarToStr : MutableMap<String, String>){
        equationObjectBoxes = mutableListOf()
        polynom = p
        screenVariableToStringVar = screenVarToStr
        p.addends.forEach { pol ->
            addObjBasedOnPolynom(pol)
        }
        if (polynom.addends.size <= 0 && screenVarToStr.size <= 0 ){
            screenVariableToStringVar = defaultScreenObjsToString()
        }
    }

    open fun addObjBasedOnPolynom(pol: Polynom, sysEq : SystemOfEquations? = null) {

        if (pol is Constant) {
            val value = pol.getValue()
            if (value < 0)
                putEquationObjIntoHolder(Ballon(context, value), sysEq)
            else if (value > 0)
                putEquationObjIntoHolder(Weight(context, value), sysEq)
        } else if (pol is Variable) {
            val type =
                screenVariableToStringVar.filterValues { it == pol.getVariable() }.keys.firstOrNull()
            if (type == Ball(context, 1)::class.toString()) {
                putEquationObjIntoHolder(Ball(context, 1), sysEq)
            } else if (type == Cube(context, 1)::class.toString()) {
                putEquationObjIntoHolder(Cube(context, 1), sysEq)
            } else if (type == Cylinder(context, 1)::class.toString()) {
                putEquationObjIntoHolder(Cylinder(context, 1), sysEq)
            }
        } else if (pol is Multiplication){
            (0 until pol.getMultiple().evaluate(mutableMapOf())).forEach {
                addObjBasedOnPolynom(pol.getPolynom(), sysEq)
            }
        }
    }

    open fun putEquationObjIntoHolder(obj : EquationObject, sysEq : SystemOfEquations? = null){
        var box = findBoxByObjType(obj)
        if (box?.isFull() ?: false) box = null

        if (box == null){
            try {
                box = createBoxByObjType(obj)
                addEquationObjectBox(box)
                box.addObject(obj)
                if (sysEq != null) addObjToPolynom(obj, sysEq)
                return
            }catch (e : java.lang.Exception){
                removeBox(box)
                throw java.lang.Exception(e.message)
                return
            }
        }

        box.addObject(obj)
        if (sysEq != null) addObjToPolynom(obj, sysEq)
    }

    fun addValueToOtherValueInTheSameHolder(obj : EquationObject, sysEq: SystemOfEquations): Boolean{
        if (obj !is ScaleValue){
            return false
        }
        val originalObj = getDraggedObject()
        if (originalObj == null || originalObj !is ScaleValue ) return false

        val box = equationObjectBoxes.filter { (it is WeightBox
                || it is BallonBox) && it.isIn(obj) }
                                    .sortedBy { it.z }.firstOrNull()
        if (box == null) return false

        val overlappingObj = box.getOverlappingScreenValue(obj, originalObj)
        if (overlappingObj == null) return false

        addToConstant(overlappingObj.evaluate(), 1)
        addToConstant(originalObj.evaluate(), -1)
        overlappingObj.add(1)
        originalObj.add(-1)
        if (originalObj.isNotValidValue()){
            removeDraggedObject(sysEq, true)
        }
        if (overlappingObj.isNotValidValue()){
            removeDraggedObject(sysEq, true, overlappingObj)
        }
        return true
    }


    private fun addObjToPolynom(obj : EquationObject, sysEq: SystemOfEquations){
        if (obj is ScaleValue){
            polynom.addConstant(obj.evaluate())
        }else if (obj is ScaleVariable) {
            polynom.addVariable(screenVariableToStringVar[obj::class.toString()]!!)
        } else if (obj is Package){
            val bracket = sysEq.findBracket()
            if (bracket != null) polynom.addBracket(bracket)
            else polynom.addBracket(Bracket(Addition(mutableListOf())))
        }
    }

    private fun removeObjFromPolynom(obj : EquationObject?, sysEq: SystemOfEquations){
        if (obj == null)
            return
        if (obj is ScaleValue){
            polynom.removeConstant(obj.evaluate())
        }else if (obj is ScaleVariable){
            polynom.removeVariable(screenVariableToStringVar[obj::class.toString()]!!)
        }else if (obj is Package){
            val bracket = sysEq.findBracket()
            if (bracket != null) polynom.removeBracket(bracket)
        }
    }

    private fun addToConstant(toValue : Int, value : Int){
        polynom.addToConstant(toValue, value)
    }

    private fun removeBox(box: EquationObjectBox?) {
        equationObjectBoxes.remove(box)
        changeSizeInsideObj()
    }

    fun addEquationObjectBox(box : EquationObjectBox){
        if (equationObjectBoxes.size >= maxNumberOfBoxes){
            throw java.lang.Exception("maximum capacity of boxes")
        }
        equationObjectBoxes.add(box)
        if (biggerNumEquationBoxes < equationObjectBoxes.size) {
            setBiggerNumberBoxes(equationObjectBoxes.size)
            return
        }
        changeSizeInsideObj()
    }

    private fun findBoxByObjType(obj: EquationObject) : EquationObjectBox? =
        if (obj is Ball) equationObjectBoxes.filter { it is BallBox }
            .sortedBy { it.insideObject.size }.firstOrNull()
        else if (obj is Cube) equationObjectBoxes.filter { it is CubeBox }
            .sortedBy { it.insideObject.size }.firstOrNull()
        else if (obj is Cylinder) equationObjectBoxes.filter { it is CylinderBox }
            .sortedBy { it.insideObject.size }.firstOrNull()
        else if (obj is Weight) equationObjectBoxes.filter { it is WeightBox }
            .sortedBy { it.insideObject.size }.firstOrNull()
        else if (obj is Ballon) equationObjectBoxes.filter { it is BallonBox }
            .sortedBy { it.insideObject.size }.firstOrNull()
        else if (obj is Package) equationObjectBoxes.filter { it is PackageBox }
            .sortedBy { it.insideObject.size }.firstOrNull()
        else null

    private fun createBoxByObjType(obj: EquationObject) : EquationObjectBox =
        if (obj is Ball) BallBox()
        else if (obj is Cube) CubeBox()
        else if (obj is Cylinder) CylinderBox()
        else if (obj is Weight) WeightBox()
        else if (obj is Ballon) BallonBox()
        else if (obj is Package) PackageBox()
        else EquationObjectBox()

    fun removeDraggedObject(sysEq : SystemOfEquations, delete : Boolean = false,
                            deletedObj : EquationObject? = getDraggedObject()){
        if (delete){
            removeObjFromPolynom(deletedObj, sysEq)
        }
        for (box in equationObjectBoxes){
            if (deletedObj == getDraggedObject()) {
                box.removeDraggedObject(delete)
            }else if (deletedObj != null){
                box.removeObject(deletedObj)
            }
        }
        equationObjectBoxes = equationObjectBoxes.filter { it.insideObject.size > 0 }.toMutableList()
        changeSizeInsideObj()
    }

    override fun incrementValue(x1 : Int, y1 : Int): EquationObject? {
        val clicked = super.incrementValue(x1, y1)
        if (clicked is ScaleValue)
            addToConstant(clicked.evaluate() - 1, 1)

        return clicked
    }

    override fun decrementValue(x1 : Int, y1 : Int): ScaleValue? {
        val clicked =  super.decrementValue(x1, y1)
        if (clicked != null) addToConstant(clicked.evaluate() + 1, -1)
        return clicked
    }

    fun beforeEarthquakeAnim(){
        equationObjectBoxes.forEach { it.beforeEarthquakeAnim() }
    }

    fun changePosition(){
        equationObjectBoxes.forEach { it.changePosition() }
    }

    fun afterEarthquakeAnim(){
        equationObjectBoxes.forEach { it.afterEarthquakeAnim() }
    }


    override fun returnDraggedObject(x1: Int, y1: Int): EquationObject? {
        val draggedObjs = mutableListOf<EquationObject?>()
        for (box in equationObjectBoxes){
            draggedObjs.add(box.returnDraggedObject(x1, y1))
        }

        val draggedObj = draggedObjs.filter { it != null }.sortedBy { it?.z }.firstOrNull()

        if (dragFrom && !dragTo)
            return draggedObj?.makeCopy()

        return draggedObj
    }

    override fun returnClickedObject(x1: Int, y1: Int): EquationObject? {
        val clickedObj = mutableListOf<EquationObject?>()
        for (box in equationObjectBoxes){
            clickedObj.add(box.returnClickedObject(x1, y1))
        }
        return clickedObj.filter { it != null }.sortedBy { it?.z }.firstOrNull()
    }

    private fun getDraggedObject() : EquationObject? =
        equationObjectBoxes.map { it.getDraggedObject() }.filter { it != null }.firstOrNull()

    override fun returnPackages() : MutableList<Package> =
        equationObjectBoxes.flatMap { it.returnPackages()}.toMutableList()

    fun getAllEquationsObjectsTypes() : Set<String> =
        equationObjectBoxes.flatMap { it.insideObject }
            .map { it::class.toString()  }.toSet()

    fun getAllConstantValues() =
        equationObjectBoxes.flatMap { it.insideObject }.filter { it is Weight }
            .map { (it as Weight).evaluate() }

    fun isFull() = equationObjectBoxes.size >= maxNumberOfBoxes

    fun getNumberBoxes() = equationObjectBoxes.size

    fun setBiggerNumberBoxes(number : Int){
        biggerNumEquationBoxes = number
        changeSizeInsideObj()
    }

    fun setConsSizeBorders(borders : MutableList<Int>){
        equationObjectBoxes.forEach { it.constantSizeRange = borders }
    }
}