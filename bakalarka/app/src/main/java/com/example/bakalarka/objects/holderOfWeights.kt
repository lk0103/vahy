package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.vahy.objects.ScreenObject

class HolderOfWeights(context: Context,
                      private val left : Boolean,
                      touchable : Boolean = true) :
    ScreenObject(touchable){
    private var equationObjectBoxes = mutableListOf<EquationObjectBox>()
    private var defaultWidthScale = 100
    private var defaultHeightScale = 100
    private var heightWithoutBowl = 0
    private val maxNumberOfBoxes = 3

    init {
        if (left == true) {
            image = ContextCompat.getDrawable(context, R.drawable.left_holder1)!!.toBitmap()
        }
        else {
            image = ContextCompat.getDrawable(context, R.drawable.right_holder1)!!.toBitmap()
        }
        z = 2
        width = 407
        height = 607
        heightWithoutBowl = 492
        defaultWidthScale = 1000
        defaultHeightScale = 607
        image = Bitmap.createScaledBitmap(image, width, height, true);
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        x = xStart
        y = yStart
        var widthOfScale = w
        var heightOfScale = w * defaultHeightScale / defaultWidthScale
        while (heightOfScale + y >= h){
            widthOfScale -= 5
            heightOfScale -= 5
        }
        width = widthOfScale * width / defaultWidthScale
        heightWithoutBowl = heightOfScale * heightWithoutBowl / height
        height = heightOfScale
        if (left == false) {
            x = xStart + widthOfScale - x - width
        }
        image = Bitmap.createScaledBitmap(image, width, height, true)
        changeSizeInsideObj()
    }

    override fun draw(canvas: Canvas, paint: Paint){
        equationObjectBoxes.forEach { box ->
            box.draw(canvas, paint)
        }
        canvas.drawBitmap(
            image,
            null,
            Rect(
                x, y, (x + width), (y + height)
            ),
            paint
        )
    }

    fun insideVariableTypes(): List<EquationObject> =
        equationObjectBoxes.flatMap { it.returnListInsideVariableTypes() }.toList()


    private fun changeSizeInsideObj() {
        (0 until equationObjectBoxes.size).forEach { i ->
            equationObjectBoxes[i].sizeChanged(
                width / equationObjectBoxes.size, calculateHeightBox(),
                x + i * width / equationObjectBoxes.size,
                y + heightWithoutBowl / 3
            )
        }
    }

    private fun calculateHeightBox() = heightWithoutBowl * 2 / 3

    fun addEquationObjIntoHolder(obj : EquationObject){
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
        Log.i("gesture", "clickedObjects holder " + clickedObj.toString())
        return clickedObj.filter { it != null }.sortedBy { it?.z }.firstOrNull()
    }

    fun removeDraggedObject(delete : Boolean = false){
        for (box in equationObjectBoxes){
            box.removeDraggedObject(delete)
        }
        equationObjectBoxes = equationObjectBoxes.filter { it.insideObject.size > 0 }.toMutableList()
        changeSizeInsideObj()
    }

}