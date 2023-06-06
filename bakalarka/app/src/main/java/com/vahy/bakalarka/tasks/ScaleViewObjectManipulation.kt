package com.vahy.bakalarka.tasks

import android.content.Context
import android.view.MotionEvent
import com.vahy.bakalarka.equation.Equation
import com.vahy.bakalarka.equation.SystemOfEquations
import com.vahy.bakalarka.objects.*
import com.vahy.vahy.ScalesView
import com.vahy.vahy.objects.OpenPackage

class ScaleViewObjectManipulation(private val context: Context, private val scalesView: ScalesView) {
    var draggedObject : EquationObject? = null
    private var draggedObjOriginalPos = Pair(0, 0)
    private var draggedFrom : ContainerForEquationBoxes? = null
    var clickedObject : EquationObject? = null


    fun onClicked() {
        if (clickedObject != null && scalesView.getOpenPackage().getInsideObjects().contains(clickedObject))
            changeInsideOfPackages()
        draggedObject = null
        draggedFrom = null
        clickedObject = null
        scalesView.invalidate()
    }

    fun onTouchDown(event: MotionEvent) {
        if (draggedObject != null) return

        if (scalesView.isClickedScaleIcon(event)){
            scalesView.switchBetweenEquations()
            return
        }
        for (container in scalesView.containersToDragFrom()) {
            draggedObject = container.returnDraggedObject(event.x.toInt(), event.y.toInt())
            if (draggedObject != null) {
                draggedObjOriginalPos = Pair(draggedObject?.x ?: 0, draggedObject?.y ?: 0)
                if (container is ContainerForEquationBoxes)
                    draggedFrom = container
                break
            }
        }
    }

    fun onTouchMove(event: MotionEvent) {
        draggedObject?.move(event.x.toInt(), event.y.toInt())
        scalesView.invalidate()
    }

    fun onTouchUp() {
        if (draggedObject != null && draggedFarEnough()) {
            scalesView.storeEquation()
            if (!droppedObject(draggedObject!!)){
                if (! plusMinus1ToDragged(draggedObject!!))
                    scalesView.scaleEquations.removeLastStoredEquation()
            }
        }
        scalesView.checkEquality()
        draggedObject = null
        draggedFrom = null
        scalesView.invalidate()
    }


    fun droppedObject(obj : EquationObject) : Boolean{
        if (scalesView.isInBin(obj)){
            removeDraggedObjFromContainer(draggedFrom, true)
        } else {
            scalesView.containersToManipulate().forEach{ container ->
                if (container.isIn(obj)){
                    dropObjIntoContainer(obj, container)
                    if (container is OpenPackage || draggedFrom is OpenPackage)
                        changeInsideOfPackages()
                    scalesView.invalidate()
                    return true
                }
            }
        }
        scalesView.invalidate()
        return false
    }

    private fun dropObjIntoContainer(obj: EquationObject, draggedTo : ContainerForEquationBoxes) {
        if (draggedTo == draggedFrom &&
            scalesView.containersToManipulate().contains(draggedFrom)
            && obj is ScaleValue){
            if (draggedTo.addValueToOtherValueInTheSameHolder(obj,
                                        scalesView.scaleEquations.getEquationSystem())){
                return
            }
            plusMinus1ToDragged(obj)
            return
        }

        if ((obj is Package && draggedTo is OpenPackage)) {
            removeDraggedObjFromContainer(draggedFrom, false)
            return
        }

        try {
            val openPackage = scalesView.getOpenPackage()
            val equationsWithOpenPackage = scalesView.scaleEquations.getEquations() +
                    Equation(openPackage.getBracket(), openPackage.getBracket())
            val eqSys = SystemOfEquations(equationsWithOpenPackage)

            draggedTo.putEquationObjIntoHolder(obj, eqSys)
            removeDraggedObjFromContainer(draggedFrom, true)
        }catch (e : java.lang.Exception){
            removeDraggedObjFromContainer(draggedFrom, false)
        }
    }

    private fun plusMinus1ToDragged(obj: EquationObject) : Boolean{
        val container = scalesView.containersToDragFrom().filter {
            it.isIn(draggedObjOriginalPos.first, draggedObjOriginalPos.second)
        }.firstOrNull()

        if (container is OpenPackage &&
            !scalesView.containersToManipulate().contains(container))
            return false

        if (scalesView.isInBin(obj) && container is ContainerForEquationBoxes)
            return true
        if (draggedObjOriginalPos == Pair(0, 0) || container == null)
            return false

        val tolerance = obj.height / 2
        if (obj.y < draggedObjOriginalPos.second - tolerance) {
            container.incrementValue(draggedObjOriginalPos.first, draggedObjOriginalPos.second)
        } else if (obj.y > draggedObjOriginalPos.second + tolerance) {
            container.decrementValue(draggedObjOriginalPos.first, draggedObjOriginalPos.second)
            if (obj is Weight && obj.evaluate() <= 1)
                removeDraggedObjFromContainer(draggedFrom, true)
        }

        if (container is OpenPackage)
            changeInsideOfPackages()

        return container !is ObjectsToChooseFrom
    }

    fun changeInsideOfPackages(){
        val openPackage = scalesView.getOpenPackage()
        scalesView.containersToDragFrom().flatMap { it.returnPackages() }
            .forEach { it.putObjectsIn(openPackage.getInsideObjects().toMutableList()) }

        scalesView.scaleEquations.setInsideBrackets(openPackage.polynom)
    }

    fun removeDraggedObjFromContainer(container : ContainerForEquationBoxes?, delete: Boolean = false){
        if (container == null)
            return

        container.removeDraggedObject(scalesView.scaleEquations.getEquationSystem(),
            delete && scalesView.containersToManipulate().contains(container))
        if (delete && container is OpenPackage)
            changeInsideOfPackages()
    }

    fun onDoubleTap(event: MotionEvent?): Boolean {
        if (event == null) return true

        for (container in scalesView.containersToManipulate()) {
            clickedObject = container.returnClickedObject(event.x.toInt(), event.y.toInt())

            if (clickedObject is Package){
                scalesView.storeEquation()
                scalesView.scaleEquations.unpackPackage(container, scalesView)
                break
            }
            if (clickedObject is ScaleValue){
                scalesView.storeEquation()
                scalesView.scaleEquations.divideScaleValue(container, scalesView)

                if (container is OpenPackage)
                    changeInsideOfPackages()
                break
            }
        }

        scalesView.invalidate()
        return true
    }


    fun draggedFarEnough() =
        (Math.abs(draggedObject!!.x - draggedObjOriginalPos.first) > draggedObject!!.width / 2
                ||
                Math.abs(draggedObject!!.y - draggedObjOriginalPos.second) > draggedObject!!.height / 2)
}