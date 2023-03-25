package com.example.vahy.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.bakalarka.objects.EquationObject
import com.example.bakalarka.objects.ScaleValue

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


    open fun isIn(obj : ScreenObject) : Boolean {
        val x1 = if (this is EquationObject) x - width / 2 else x
        val y1 = if (this is EquationObject) y - height / 2 else y
        val objX1 = if (obj is EquationObject) obj.x - obj.width / 2 else obj.x
        val objY1 = if (obj is EquationObject) obj.y - obj.height / 2 else obj.y
        if (!visibility)
            return false
        // if rectangle has area 0, no overlap
        if (x1 == x1 + width || y1 == y1 + height || objX1 + obj.width == objX1 || objY1 == objY1 + obj.height) {
            return false
        }

        // If one rectangle is on left side of other
        if (x1 > objX1 + obj.width || objX1 > x1 + width) {
            return false
        }

        // If one rectangle is above other
        if ( objY1 > y1 + height || y1 > objY1 + obj.height) {
            return false
        }
        return true
    }

    fun overlappingArea(obj : ScreenObject): Int {
        val x1 = if (this is EquationObject) x - width / 2 else x
        val y1 = if (this is EquationObject) y - height / 2 else y
        val objX1 = if (obj is EquationObject) obj.x - obj.width / 2 else obj.x
        val objY1 = if (obj is EquationObject) obj.y - obj.height / 2 else obj.y
        val xOverlap = maxOf(0, minOf(x1 + width, objX1 + obj.width) - maxOf(x1, objX1))
        val yOverlap = maxOf(0, minOf(y1 + height, objY1 + obj.height) - maxOf(y1, objY1))
        return xOverlap * yOverlap
    }

    open fun isIn(x1 : Int, y1 : Int) : Boolean =
               visibility && (x1 >= x && x1 <= x + width &&
                y1 >= y && y1 <= y + height)

    open fun incrementValue(x1 : Int, y1 : Int) : EquationObject?{
        if (!visibility)
            return null
        val clickedObject = returnClickedObject(x1, y1)
        if (clickedObject is ScaleValue) {
            clickedObject.increment()
            return clickedObject
        }
        if (clickedObject is com.example.bakalarka.objects.Package){
            return clickedObject
        }
        return null
    }

    open fun decrementValue(x1 : Int, y1 : Int) : ScaleValue?{
        if (!visibility)
            return null
        val clickedObject = returnClickedObject(x1, y1)
        if (!(clickedObject is ScaleValue))
            return null
        (clickedObject as ScaleValue).decrement()
        return clickedObject
    }
}