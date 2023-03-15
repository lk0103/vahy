package com.example.vahy.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import com.example.bakalarka.objects.EquationObject
import com.example.bakalarka.objects.ScaleValue
import com.example.bakalarka.objects.menu.DownIcon
import com.example.bakalarka.objects.menu.UpIcon

open class ScreenObject(var dragFrom : Boolean, var dragTo : Boolean){
    var visibility = true
    var x : Int  = 0
    var y = 0
    var z = 0
    var width = 0
    var height = 0
    var image : Bitmap? = null


    open fun draw(canvas: Canvas, paint: Paint){
        if (image == null || !visibility)
            return
        canvas.drawBitmap(
            image!!,
            null,
            Rect(
                x, y, (x + width), (y + height)
            ),
            paint
        )
    }

    open fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        if (image == null)
            return
        x = xStart
        y = yStart
        height = w * height / width
        width = w
        while (height + y >= h){
            width -= 5
            height -= 5
        }
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    open fun reloadImage(w : Int, h : Int){
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
        if (!visibility)
            return false
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
               visibility && (x1 >= x && x1 <= x + width &&
                y1 >= y && y1 <= y + height)


    open fun onDoubleTap(event: MotionEvent) : EquationObject?{
        if (!visibility)
            return null
        val clickedObject = returnClickedObject(event.x.toInt(), event.y.toInt())
        if (clickedObject is ScaleValue) {
            clickedObject.increment()
            return clickedObject
        }
        if (clickedObject is com.example.bakalarka.objects.Package){
            return clickedObject
        }
        return null
    }

    open fun onLongPress(event: MotionEvent) : ScaleValue?{
        if (!visibility)
            return null
        val clickedObject = returnClickedObject(event.x.toInt(), event.y.toInt())
        if (!(clickedObject is ScaleValue))
            return null
        (clickedObject as ScaleValue).decrement()
        return clickedObject
    }
}