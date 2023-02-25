package com.example.bakalarka.objects

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import com.example.bakalarka.equation.SystemOfEquations
import com.example.vahy.equation.*
import com.example.vahy.objects.ScreenObject

open class ContainerForEquationBoxes(protected val context: Context,
                                     dragFrom : Boolean = true,
                                    dragTo : Boolean = true) :
                        ScreenObject(dragFrom, dragTo) {
    protected var equationObjectBoxes = mutableListOf<EquationObjectBox>()
    protected var maxNumberOfBoxes = 3
    protected var polynom = Addition(mutableListOf())
    protected var screenVariableToStringVar = mutableMapOf<String, String>(
        Ball(context, 1)::class.toString() to "x",
        Cube(context, 1)::class.toString() to "y",
        Cylinder(context, 1)::class.toString() to "z",
    )

    open fun changeSizeInsideObj() {
    }

    fun insideVariableTypes(): List<EquationObject> =
        equationObjectBoxes.flatMap { it.returnListInsideVariableTypes() }.toList()

    fun setEquation(p : Addition, screenVarToStr : MutableMap<String, String>){
        equationObjectBoxes = mutableListOf()
        polynom = p
        screenVariableToStringVar = screenVarToStr
        p.addends.forEach { pol ->
            addObjBasedOnPolynom(pol)
        }
    }

    open fun addObjBasedOnPolynom(pol: Polynom) {
        if (pol is Constant) {
            val value = pol.getValue()
            if (value < 0)
                addEquationObjIntoHolder(Ballon(context, value))
            else if (value > 0)
                addEquationObjIntoHolder(Weight(context, value))
        } else if (pol is Variable) {
            val type =
                screenVariableToStringVar.filterValues { it == pol.getVariable() }.keys.firstOrNull()!!
            if (type == Ball(context, 1)::class.toString()) {
                addEquationObjIntoHolder(Ball(context, 1))
            } else if (type == Cube(context, 1)::class.toString()) {
                addEquationObjIntoHolder(Cube(context, 1))
            } else if (type == Cylinder(context, 1)::class.toString()) {
                addEquationObjIntoHolder(Cylinder(context, 1))
            }
        } else if (pol is Multiplication){
            (0 until pol.getMultiple().evaluate(mutableMapOf())).forEach {
                addObjBasedOnPolynom(pol.getPolynom())
            }
        }
    }

    open fun addEquationObjIntoHolder(obj : EquationObject, sysEq : SystemOfEquations? = null){
        Log.i("rovnica", "container add obj into holder: " + polynom)
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
                throw java.lang.Exception("maximum capacity of boxes")
                return
            }
        }

        box.addObject(obj)
        if (sysEq != null) addObjToPolynom(obj, sysEq)
    }

    fun addValueToOtherValueInTheSameHolder(obj : EquationObject, sysEq: SystemOfEquations){
        if (obj !is ScaleValue){
            return
        }
        val originalObj = getDraggedObject()
        if (originalObj == null || originalObj !is ScaleValue ) return

        val box = equationObjectBoxes.filter { ((obj is Weight && it is WeightBox)
                || (obj is Ballon && it is BallonBox)) && it.isIn(obj) }
                                    .sortedBy { it.z }.firstOrNull()
        if (box == null) return

        val overlappingObj = box.getOverlappingScreenValue(obj, originalObj)
        if (overlappingObj == null) return
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
    }

    fun substractValueFromOtherValueIDifferentHolder(originalHolder : ContainerForEquationBoxes,
                                                     obj : EquationObject,
                                                     sysEq: SystemOfEquations){
        if (obj !is ScaleValue){
            return
        }
        val originalObj = originalHolder.getDraggedObject()
        if (originalObj == null || originalObj !is ScaleValue) return

        val box = equationObjectBoxes.filter { ((obj is Weight && it is WeightBox)
                || (obj is Ballon && it is BallonBox)) && it.isIn(obj) }
            .sortedBy { it.z }.firstOrNull()
        if (box == null) return

        val overlappingObj = box.getOverlappingScreenValue(obj, originalObj)
        if (overlappingObj == null) return
        val incValue = if(obj is Weight) -1 else 1
        addToConstant(overlappingObj.evaluate(), incValue)
        originalHolder.addToConstant(originalObj.evaluate(), incValue)
        overlappingObj.add(incValue)
        originalObj.add(incValue)
        if (originalObj.isNotValidValue()){
            originalHolder.removeDraggedObject(sysEq, true)
        }
        if (overlappingObj.isNotValidValue()){
            removeDraggedObject(sysEq, true, overlappingObj)
        }
    }


    private fun addObjToPolynom(obj : EquationObject, sysEq: SystemOfEquations){
        if (obj is ScaleValue){
            polynom.addConstant(obj.evaluate())
        }else if (obj is ScaleVariable) {
            polynom.addVariable(screenVariableToStringVar[obj::class.toString()]!!)
        } else if (obj is Package){
            val bracket = sysEq.findBracket()
            if (bracket != null) polynom.addBracket(bracket)
        }
        Log.i("rovnica", "container addObject to polynom: " + polynom)
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
       Log.i("rovnica", "container removeObj: " + polynom)
    }

    private fun addToConstant(toValue : Int, value : Int){
        polynom.addToConstant(toValue, value)
        Log.i("rovnica", "container addToConstant: tovalue: " + toValue +
                " value: " + value + " rovnica: "+ polynom)
    }

    private fun removeBox(box: EquationObjectBox?) {
        equationObjectBoxes.remove(box)
        changeSizeInsideObj()
    }

    private fun addEquationObjectBox(box : EquationObjectBox){
        if (equationObjectBoxes.size >= maxNumberOfBoxes){
            throw java.lang.Exception("maximum capacity of boxes")
        }
        equationObjectBoxes.add(box)
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
            Log.i("rovnica", "remove dragged: " + polynom + " dragged value: " + deletedObj?.evaluate())
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

    override fun onDoubleTap(event: MotionEvent): ScaleValue? {
        val clicked =  super.onDoubleTap(event)
        if (clicked != null) addToConstant(clicked.evaluate() - 1, 1)
        return clicked
    }

    override fun onLongPress(event: MotionEvent): ScaleValue? {
        val clicked =  super.onLongPress(event)
        if (clicked != null) addToConstant(clicked.evaluate() + 1, -1)
        return clicked
    }

    override fun returnDraggedObject(x1: Int, y1: Int): EquationObject? {
        val draggedObj = mutableListOf<EquationObject?>()
        for (box in equationObjectBoxes){
            draggedObj.add(box.returnDraggedObject(x1, y1))
        }
        return draggedObj.filter { it != null }.sortedBy { it?.z }.firstOrNull()
    }

    override fun returnClickedObject(x1: Int, y1: Int): EquationObject? {
        val clickedObj = mutableListOf<EquationObject?>()
        for (box in equationObjectBoxes){
            clickedObj.add(box.returnClickedObject(x1, y1))
        }
        return clickedObj.filter { it != null }.sortedBy { it?.z }.firstOrNull()
    }

    fun getDraggedObject() : EquationObject? =
        equationObjectBoxes.map { it.getDraggedObject() }.filter { it != null }.firstOrNull()

    override fun returnPackages() : MutableList<Package> =
        equationObjectBoxes.flatMap { it.returnPackages()}.toMutableList()


}