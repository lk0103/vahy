package com.example.vahy.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.equation.Bracket
import com.example.bakalarka.equation.Equation
import com.example.bakalarka.equation.SystemOfEquations
import com.example.bakalarka.objects.EquationObject
import com.example.bakalarka.objects.ContainerForEquationBoxes
import com.example.vahy.equation.Addition
import com.example.vahy.equation.Polynom

class OpenPackage(context : Context, dragFrom : Boolean = true, dragTo : Boolean = true)
    : ContainerForEquationBoxes(context, dragFrom, dragTo) {


    init {
        image = ContextCompat.getDrawable(context, R.drawable.open_package1)!!.toBitmap()
        z = 2
        width = 600
        height = 600
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        maxNumberOfBoxes = 2
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        if (image == null)
            return
        x = xStart
        y = yStart
        height = w
        width = w
        while (height > h){
            width -= 5
            height -= 5
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
        changeSizeInsideObj()
    }

    override fun changeSizeInsideObj() {
        val xStart = x - width / 2 + width / 10
        val yStart = y - height / 2
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
        if (image == null)
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

    override fun addEquationObjIntoHolder(obj : EquationObject, sysEq : SystemOfEquations?) {
        if (obj is com.example.bakalarka.objects.Package)
            return
        super.addEquationObjIntoHolder(obj, sysEq)
    }

    override fun isIn(x1 : Int, y1 : Int) : Boolean =
        (x1 >= x - width / 2 && x1 <= x + width / 2 &&
                y1 >= y - height / 2 && y1 <= y + height / 2)

    fun getInsideObjects() : List<EquationObject> =
        equationObjectBoxes.flatMap { it.insideObject }.toList()


    fun getBracket() : Addition =
        if (polynom.size() > 0) Addition(mutableListOf(Bracket(polynom)))
        else Addition(mutableListOf())
}