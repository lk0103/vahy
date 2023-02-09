package com.example.bakalarka.objects

import android.graphics.*
import com.example.vahy.objects.ScreenObject

open class EquationObject(collidable : Boolean = false,
                          protected var draggable : Boolean = true)
    : ScreenObject(collidable)  {
    var widthBordingBox = 0
    var heightBordingBox = 0

    init {
        z = 2
        width = 300
        height = 300
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        x = xStart
        y = yStart
        height = w
        width = w
        widthBordingBox = w
        heightBordingBox = h
        while (height > h){
            width -= 5
            height -= 5
        }
        if (image.width * 3 / 2 < width || image.height * 3 / 2 < height){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }

    open fun reloadImage(w : Int, h : Int){
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.drawBitmap(
            image,
            null,
            Rect(
                x - width / 2, y - height / 2,
                x + width / 2, y + height / 2
            ),
            paint
        )
    }

    override fun isIn(x1 : Int, y1 : Int) : Boolean =
        (x1 >= x - widthBordingBox / 2 && x1 <= x + widthBordingBox / 2 &&
                y1 >= y - heightBordingBox / 2 && y1 <= y + heightBordingBox / 2)

    open fun makeCopy() : EquationObject {
        return EquationObject()
    }

    open fun setParametersOfCopy(copy : EquationObject) : EquationObject {
        copy.x = x
        copy.y = y
        copy.z = z
        copy.width = width
        copy.height = height
        copy.image = image
        copy.widthBordingBox = widthBordingBox
        copy.heightBordingBox = heightBordingBox
        return copy
    }

    open fun evaluate() : Int = 0

    fun move(newX : Int, newY : Int){
        x = newX
        y = newY
    }

    fun onTouch(x1 : Int, y1: Int) : Boolean =
        if (! draggable) false
        else isIn(x1, y1)
}