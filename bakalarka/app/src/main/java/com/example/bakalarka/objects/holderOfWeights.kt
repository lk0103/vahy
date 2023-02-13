package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

class HolderOfWeights(context: Context,
                      private val left : Boolean,
                      touchable : Boolean = true) :
    ContainerForEquationBoxes(touchable){
    private var defaultWidthScale = 100
    private var defaultHeightScale = 100
    private var heightWithoutBowl = 0

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
}