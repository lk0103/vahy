package com.example.vahy.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import com.example.bakalarka.objects.EquationObject
import com.example.bakalarka.objects.ScaleValue

open class ScreenObject(var touchable : Boolean){
    var x : Int  = 0
    var y = 0
    var z = 0
    var width = 0
    var height = 0
    lateinit var image : Bitmap


    open fun draw(canvas: Canvas, paint: Paint){
        canvas.drawBitmap(
            image,
            null,
            Rect(
                x, y, (x + width), (y + height)
            ),
            paint
        )
    }

    open fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        x = xStart
        y = yStart
        height = w * height / width
        width = w
        while (height + y >= h){
            width -= 5
            height -= 5
        }
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }

    open fun returnDraggedObject(x1 : Int, y1 : Int) : EquationObject?{
        return null
    }

    open fun returnClickedObject(x1 : Int, y1 : Int) : EquationObject?{
        return null
    }

    open fun returnPackages() : MutableList<com.example.bakalarka.objects.Package> = mutableListOf()

    @JvmName("setZ1")
    fun setZ(newZ : Int) {
        z = newZ
    }


    fun isIn(obj : ScreenObject) : Boolean {
        // if rectangle has area 0, no overlap
        if (x == x + width || y == y + height || obj.x + obj.width == obj.x || obj.y == obj.y + obj.height) {
            return false
        }

        // If one rectangle is on left side of other
        if (x > obj.x + obj.width || obj.x > x + width) {
            return false
        }

        // If one rectangle is above other
        if ( obj.y > y + height || y > obj.y + obj.height) {
            return false
        }
        return true
    }

    open fun isIn(x1 : Int, y1 : Int) : Boolean =
                (x1 >= x && x1 <= x + width &&
                y1 >= y && y1 <= y + height)


    open fun onDoubleTap(event: MotionEvent) : ScaleValue?{
        val clickedObject = returnClickedObject(event.x.toInt(), event.y.toInt())
        if (!(clickedObject is ScaleValue))
            return null
        (clickedObject as ScaleValue).increment()
        return clickedObject
    }

    open fun onLongPress(event: MotionEvent) : ScaleValue?{
        val clickedObject = returnClickedObject(event.x.toInt(), event.y.toInt())
        if (!(clickedObject is ScaleValue))
            return null
        (clickedObject as ScaleValue).decrement()
        return clickedObject
    }
}