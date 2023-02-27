package com.example.bakalarka.objects

import android.content.Context
import android.graphics.*
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.equation.Bracket
import com.example.vahy.equation.*

class HolderOfWeights(context: Context,
                      private val left : Boolean,
                      private var angle : Float = 0F,
                      dragFrom : Boolean = true, dragTo : Boolean = true) :
    ContainerForEquationBoxes(context, dragFrom, dragTo){
    private var defaultWidthScale = 100
    private var defaultHeightScale = 100
    private var widthOfScale = 100
    private var heightOfScale = 100
    private var heightWithoutBowl = 0
    private var originalX = 0
    private var originalY = 0

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
        widthOfScale = defaultWidthScale
        heightOfScale = defaultHeightScale
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        rotateScale(angle)
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        if (image == null)
            return
        x = xStart
        y = yStart
        widthOfScale = w
        heightOfScale = w * defaultHeightScale / defaultWidthScale
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
        originalX = x
        originalY = y
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        rotateScale(angle)
        changeSizeInsideObj()
    }

    override fun changeSizeInsideObj() {
        (0 until equationObjectBoxes.size).forEach { i ->
            equationObjectBoxes[i].sizeChanged(
                width / equationObjectBoxes.size, calculateHeightBox(),
                x + i * width / equationObjectBoxes.size,
                y + heightWithoutBowl / 3
            )
        }
    }

    private fun calculateHeightBox() = heightWithoutBowl * 2 / 3

    override fun draw(canvas: Canvas, paint: Paint){
        if (image == null)
            return
        equationObjectBoxes.forEach { box ->
            box.draw(canvas, paint)
        }
        canvas.drawBitmap(
            image!!,
            null,
            Rect(
                x, y, (x + width), (y + height)
            ),
            paint
        )
    }

    override fun addObjBasedOnPolynom(pol: Polynom) {
        if (pol is Bracket) {
            addEquationObjIntoHolder(Package(context))
        } else {
            super.addObjBasedOnPolynom(pol)
        }
    }

    fun getPositionLeftHolder() : Pair<Int, Int> {
        val padding = widthOfScale * 17 / 96
        val r = widthOfScale / 2 - padding
        val radians = 2 * Math.PI * angle.toDouble() / 360
        return Pair((x + padding + (r - (r * Math.cos(radians)))).toInt(),
            (y + heightOfScale / 5 - r * Math.sin(radians)).toInt())
    }

    fun getPositionRightHolder() : Pair<Int, Int> {
        val padding = widthOfScale * 37 / 192
        val r = widthOfScale / 2 - padding
        val radians = 2 * Math.PI * -angle.toDouble() / 360
        return Pair((x + width - padding - (r - (r * Math.cos(radians)))).toInt(),
            (y + heightOfScale / 5 - r * Math.sin(radians)).toInt())
    }

    fun setPositionLeftHolder() {
        val pos = getPositionLeftHolder()
        x = pos.first - widthOfScale * 17 / 96
        y = pos.second - heightOfScale / 5
    }

    fun setPositionRightHolder() {
        val pos = getPositionRightHolder()
        x = pos.first + widthOfScale * 37 / 192 - width
        y = pos.second - heightOfScale / 5
    }

    fun rotateScale(a : Float){
        x = originalX
        y = originalY
        angle = a
        if (left){
            setPositionLeftHolder()
        }else{
            setPositionRightHolder()
        }
        changeSizeInsideObj()
    }
}