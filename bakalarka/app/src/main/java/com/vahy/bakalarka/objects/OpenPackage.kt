package com.vahy.vahy.objects

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.vahy.bakalarka.R
import com.vahy.bakalarka.equation.Bracket
import com.vahy.bakalarka.equation.SystemOfEquations
import com.vahy.bakalarka.objects.EquationObject
import com.vahy.bakalarka.objects.ContainerForEquationBoxes
import com.vahy.vahy.equation.Addition

class OpenPackage(context : Context, dragFrom : Boolean = true, dragTo : Boolean = true)
    : ContainerForEquationBoxes(context, dragFrom, dragTo) {


    init {
        image = ContextCompat.getDrawable(context, R.drawable.open_package1)!!.toBitmap()
        z = 2
        width = 600
        height = 500
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        maxNumberOfBoxes = 2
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, R.drawable.open_package1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    fun changeSizeInScaleView(widthView : Int, heightView : Int, padding : Int){
        val w = widthView * 11 / 40 - padding * 2
        sizeChanged(
            w, heightView * 8 / 16 - 2 * padding,
            widthView * 6 / 7  + padding,heightView - heightView * 58 / 240
        )
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        if (image == null)
            return
        x = xStart
        y = yStart
        height = h
        width = w
        while (height > h){
            width -= 5
            height -= 5
        }
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        changeSizeInsideObj()
    }

    override fun changeSizeInsideObj() {
        val xStart = x - width / 2 + width / 10
        val yStart = y - height / 2 - height / 20
        val w = width - 2 * width / 10
        val h = height - height / 4
        (0 until equationObjectBoxes.size).forEach { i ->
            equationObjectBoxes[i].sizeChanged(
                w / equationObjectBoxes.size, h,
                xStart + i * w / equationObjectBoxes.size,
                yStart
            )
        }
    }

    override fun draw(canvas: Canvas, paint: Paint){
        if (image == null || !visibility)
            return

        canvas.drawBitmap(
            image!!,
            null,
            Rect(
                x - width / 2, y - height / 2,
                x + width / 2, y + height / 2
            ),
            paint
        )

        equationObjectBoxes.forEach { box ->
            box.draw(canvas, paint)
        }
    }


    override fun putEquationObjIntoHolder(obj : EquationObject, sysEq : SystemOfEquations?) {
        if (obj is com.vahy.bakalarka.objects.Package)
            return
        super.putEquationObjIntoHolder(obj, sysEq)
    }

    override fun isIn(x1 : Int, y1 : Int) : Boolean {
        return visibility && (x1 >= x - width / 2 && x1 <= x + width / 2 &&
                y1 >= y - height / 2 && y1 <= y + height / 2)
    }

    fun getInsideObjects() : List<EquationObject> =
        equationObjectBoxes.flatMap { it.insideObject }.toList()


    fun getBracket() : Addition =
        if (polynom.size() > 0) Addition(mutableListOf(Bracket(polynom)))
        else Addition(mutableListOf())
}