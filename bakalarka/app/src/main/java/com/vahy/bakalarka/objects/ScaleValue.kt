package com.vahy.bakalarka.objects

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

open class ScaleValue(protected var value : Int,
                      dragFrom : Boolean = false,
                      dragTo : Boolean = false)
    : EquationObject(dragFrom, dragTo)  {

    override fun draw(canvas: Canvas, paint: Paint) {
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

        drawValue(paint, canvas)
    }

    open fun drawValue(paint: Paint, canvas: Canvas) {
    }

    override fun evaluate() : Int = value

    open fun increment(){
        value++
    }

    open fun decrement(){
        value--
    }

    open fun add(v : Int){
        value += v
    }

    open fun isNotValidValue() = false
}