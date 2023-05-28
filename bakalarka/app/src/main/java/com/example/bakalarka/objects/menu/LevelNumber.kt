package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.objects.ContainerForEquationBoxes
import com.example.vahy.objects.ScreenObject

class LevelNumber(private val context : Context, val number : Int)
    : ScreenObject(false, false) {

    private var lock = Lock(context)
    private var showLock = false
    private var locked = true
    init {
        z = 2
        width = 230
        height = 295
        reloadImage(width, height)
    }

    fun changeSizeInScaleView(widthView : Int, heightView : Int){
        val w = widthView * 1 / 5
        val h = heightView
        sizeChanged(w, h, widthView / 4,  - heightView / 5)
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        if (image == null)
            return
        x = xStart
        y = yStart
        height = w * height / width
        width = w
        while (height >= h){
            width -= 5
            height -= 5
        }

        resizeLock()

        if (image!!.width < width || image!!.height < height){
            reloadImage(width, height)
            return
        }
        image = Bitmap.createScaledBitmap(image!!, width, height, true)
    }

    override fun reloadImage(w : Int, h : Int){
        var drawable = R.drawable.num1
        when (number){
            1 -> drawable = if (locked) R.drawable.num1_gray else R.drawable.num1
            2 -> drawable = if (locked) R.drawable.num2_gray else R.drawable.num2
            3 -> drawable = if (locked) R.drawable.num3_gray else R.drawable.num3
            4 -> drawable = if (locked) R.drawable.num4_gray else R.drawable.num4
        }
        image = ContextCompat.getDrawable(context, drawable)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    override fun draw(canvas: Canvas, paint: Paint){
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

        if (showLock){
            lock.draw(canvas, paint)
        }
    }

    fun moveIntoBowl(xBowl : Int, yBowl : Int){
        x = xBowl - width / 2
        y = yBowl - height + height / 10
        resizeLock()
    }

    fun move(xStart: Int, yStart: Int){
        x = xStart
        y = yStart
        lock.move(x + width / 7, y + height / 4)
    }

    private fun resizeLock() {
        lock.sizeChanged(width * 16 / 20, height, x + width / 7, y + height / 4)
    }

    @JvmName("setLocked1")
    fun setLocked(isLocked : Boolean){
        locked = isLocked
        if (locked){
            lock.changeLockIndex(0)
        }
        reloadImage(width, height)
    }

    fun showLock(show : Boolean, ix : Int = 0){
        showLock = show
        lock.changeLockIndex(ix)
    }
}