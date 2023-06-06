package com.vahy.bakalarka.objects

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R
import com.vahy.bakalarka.equation.Bracket
import com.vahy.bakalarka.equation.SystemOfEquations
import com.vahy.vahy.equation.*



class HolderOfWeights(context: Context,
                      val left : Boolean,
                      private var angle : Float = 0F,
                      dragFrom : Boolean = true, dragTo : Boolean = true) :
    ContainerForEquationBoxes(context, dragFrom, dragTo){
    var inMainMenu = false
    private var defaultWidthScale = 1000
    private var defaultHeightScale = 607
    private var widthOfScale = 100
    private var heightOfScale = 100
    var heightWithoutBowl = 0
    private var originalX = 0
    private var originalY = 0

    private val lMultiple = 17
    private val lDivisor = 96
    private val rMultiple = 37
    private val rDivisor = 192

    private val widthOriginalImage = 407
    private var heightOriginalImage = 607
    var heightWithoutBowlOriginalImage = 495
    var shiftX = 10


    init {
        z = 2
        defaultSizeValues()
        widthOfScale = defaultWidthScale
        heightOfScale = defaultHeightScale
        reloadImage(width, height)
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        rotateScale(angle)
    }

    private fun defaultSizeValues() {
        width = widthOriginalImage
        height = heightOriginalImage
        heightWithoutBowl = heightWithoutBowlOriginalImage
    }

    override fun reloadImage(w : Int, h : Int){
        if (inMainMenu){
            image = ContextCompat.getDrawable(context, R.drawable.left_holder_main_menu)!!.toBitmap()
            return
        }
        if (left == true) {
            image = ContextCompat.getDrawable(context, R.drawable.left_holder1)!!.toBitmap()
        }
        else {
            image = ContextCompat.getDrawable(context, R.drawable.right_holder1)!!.toBitmap()
        }
    }

    fun changeSizeInScaleView(widthView : Int, heightView : Int, padding : Int,
                              scaleWidthProportion : Pair<Int, Int>,
                              biggerNumEqBoxes : Int){
        biggerNumEquationBoxes = biggerNumEqBoxes
        sizeChanged(
            widthView * scaleWidthProportion.first / scaleWidthProportion.second - padding * 2,
            heightView - padding * 2,
            widthView / 500 + padding + shiftX,  padding
        )
    }

    fun changeSizeInMainMenu(w : Int, h : Int, xStart : Int, yStart : Int){
        val hPadding = h / 7
        heightOriginalImage += hPadding
        height += hPadding
        defaultHeightScale += hPadding


        super.sizeChanged(w, h, xStart, yStart)
        heightWithoutBowlOriginalImage = height * 305 / 400
        heightWithoutBowl = heightWithoutBowlOriginalImage
        changeSizeInsideObj()
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        if (image == null)
            return
        defaultSizeValues()
        x = xStart
        y = yStart
        widthOfScale = w
        heightOfScale = w * defaultHeightScale / defaultWidthScale
        while (heightOfScale + y >= h){
            widthOfScale -= 5
            heightOfScale -= 5
        }
        width = widthOfScale * width / defaultWidthScale
        heightWithoutBowl = heightOfScale * heightWithoutBowlOriginalImage / defaultHeightScale
        height = heightOfScale
        if (left == false) {
            x = xStart + widthOfScale - widthOfScale / 90 - width
        }
        originalX = x
        originalY = y
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        rotateScale(angle)
        changeSizeInsideObj()
    }

    override fun changeSizeInsideObj() {
        if (biggerNumEquationBoxes < 0) {
            biggerNumEquationBoxes = equationObjectBoxes.size
        }
        val blockWidth = (width * 95 / 100) / if (biggerNumEquationBoxes <= 0) 1 else biggerNumEquationBoxes
        val padding = widthOfScale / 200
        val centre = (biggerNumEquationBoxes - equationObjectBoxes.size) * blockWidth / 2

        (0 until equationObjectBoxes.size).forEach { i ->
            equationObjectBoxes[i].sizeChanged(
                blockWidth, calculateHeightBox(),
                x + i * width / equationObjectBoxes.size + padding + centre,
                y + heightWithoutBowl / 3
            )
        }
    }

    private fun calculateHeightBox() = heightWithoutBowl * 2 / 3

    override fun draw(canvas: Canvas, paint: Paint){
        if (image == null || !visibility)
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

    override fun addObjBasedOnPolynom(pol: Polynom, sysEq : SystemOfEquations?) {
        if (pol is Bracket) {
            putEquationObjIntoHolder(Package(context))
        } else {
            super.addObjBasedOnPolynom(pol, sysEq)
        }
    }

    fun getPositionTopMiddleBowl() =
        Pair(x + width / 2, y + heightWithoutBowl)

    fun getPositionLeftHolder() : Pair<Int, Int> {
        val padding = widthOfScale * lMultiple / lDivisor
        val r = widthOfScale / 2 - padding
        val radians = 2 * Math.PI * angle.toDouble() / 360
        return Pair((x + padding + (r - (r * Math.cos(radians)))).toInt(),
            (y + heightOfScale / 5 - r * Math.sin(radians)).toInt())
    }

    fun getPositionRightHolder() : Pair<Int, Int> {
        val padding = widthOfScale * rMultiple / rDivisor
        val r = widthOfScale / 2 - padding
        val radians = 2 * Math.PI * -angle.toDouble() / 360
        return Pair((x + width - padding - (r - (r * Math.cos(radians)))).toInt(),
            (y + heightOfScale / 5 - r * Math.sin(radians)).toInt())
    }

    fun setPositionLeftHolder() {
        val pos = getPositionLeftHolder()
        x = pos.first - widthOfScale * lMultiple / lDivisor
        y = pos.second - heightOfScale / 5
    }

    fun setPositionRightHolder() {
        val pos = getPositionRightHolder()
        x = pos.first + widthOfScale * rMultiple / rDivisor - width + width / 25
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