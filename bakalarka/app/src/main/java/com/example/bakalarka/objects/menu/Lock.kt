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

class Lock(context: Context)
    : Icon(context) {
    private val locksImages = mutableListOf<Bitmap?>()
    private var lockIndex = 0

    init {
        val locks = mutableListOf(R.drawable.lock_1, R.drawable.lock_2, R.drawable.lock_3)
        (0 until 3).forEach { i ->
            locksImages.add(ContextCompat.getDrawable(context, locks[i])!!.toBitmap())
            width = locksImages[i]?.width ?: 10
            height = locksImages[i]?.height ?: 10
            locksImages[i] = Bitmap.createScaledBitmap(locksImages[i]!!, width, height, true)
        }
    }

    override fun sizeChanged(w : Int, h : Int, xStart : Int, yStart : Int){
        (0 until 3).forEach { i ->
            image = locksImages[i]
            super.sizeChanged(w, h, xStart, yStart)
        }
    }

    fun move(xStart: Int, yStart: Int){
        x = xStart
        y = yStart
    }

    override fun draw(canvas: Canvas, paint: Paint){
        if (locksImages[lockIndex] == null || !visibility)
            return
        canvas.drawBitmap(
            locksImages[lockIndex]!!,
            null,
            Rect(
                x, y, (x + width), (y + height)
            ),
            paint
        )
    }

    fun changeLockIndex(ix : Int){
        lockIndex = Math.max(Math.min(ix, locksImages.size - 1), 0)
    }
}
