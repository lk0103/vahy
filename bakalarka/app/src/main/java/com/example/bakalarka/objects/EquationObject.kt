package com.example.bakalarka.objects

import android.graphics.*
import android.util.Log
import com.example.vahy.objects.ScreenObject
import kotlin.random.Random

open class EquationObject(dragFrom : Boolean = false, dragTo : Boolean = false)
    : ScreenObject(dragFrom, dragTo)  {
    var widthBordingBox = 0
    var heightBordingBox = 0
    var positionBeforeEarthquake = Pair(0, 0)

    init {
        z = 2
        width = 300
        height = 300
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        if (image == null)
            return
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
        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
    }

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
    }

    fun beforeEarthquakeAnim(){
        positionBeforeEarthquake = Pair(x, y)
    }

    fun shake(){
        x = positionBeforeEarthquake.first + Random.nextInt(0, 5)
        y = positionBeforeEarthquake.second + Random.nextInt(0, 5)
    }

    fun afterEarthquakeAnim(){
        x = positionBeforeEarthquake.first
        y = positionBeforeEarthquake.second
    }

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

    override fun isIn(x1 : Int, y1: Int) : Boolean =
        (x1 >= x - widthBordingBox / 2 && x1 <= x + widthBordingBox / 2 &&
                y1 >= y - heightBordingBox / 2 && y1 <= y + heightBordingBox / 2)

}