package com.example.bakalarka.objects

import com.example.vahy.objects.ScreenObject

open class ContainerForEquationBoxes(touchable : Boolean = true) :
                        ScreenObject(touchable) {
    protected var equationObjectBoxes = mutableListOf<EquationObjectBox>()
    protected var maxNumberOfBoxes = 3

    open fun changeSizeInsideObj() {
    }

    fun insideVariableTypes(): List<EquationObject> =
        equationObjectBoxes.flatMap { it.returnListInsideVariableTypes() }.toList()


    open fun addEquationObjIntoHolder(obj : EquationObject){
        var box = findBoxByObjType(obj)
        if (box?.isFull() ?: false) box = null

        if (box == null){
            try {
                box = createBoxByObjType(obj)
                addEquationObjectBox(box)
                box.addObject(obj)
                return
            }catch (e : java.lang.Exception){
                removeBox(box)
                throw java.lang.Exception("maximum capacity of boxes")
            }
        }

        box.addObject(obj)
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

    fun removeDraggedObject(delete : Boolean = false){
        for (box in equationObjectBoxes){
            box.removeDraggedObject(delete)
        }
        equationObjectBoxes = equationObjectBoxes.filter { it.insideObject.size > 0 }.toMutableList()
        changeSizeInsideObj()
    }

    override fun returnPackages() : MutableList<Package> =
        equationObjectBoxes.flatMap { it.returnPackages()}.toMutableList()
}