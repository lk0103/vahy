package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.example.vahy.objects.ScreenObject

class CustomNumberPicker(private val context: Context, private var value : Int = 1)
    : ScreenObject(false, false) {
    private var min = 1
    private var max = 30
    private var upButton = UpIcon(context)
    private var downButton = DownIcon(context)

    init {
        width = 100
        height = 100
    }

    override fun sizeChanged(w: Int, h: Int, xStart: Int, yStart: Int) {
        width = w
        height = h
        x = xStart
        y = yStart
        upButton.sizeChanged(width / 2, height * 5 / 8 ,
            xStart + width / 2 + width / 10, yStart - height / 6)
        downButton.sizeChanged(width / 2, height * 5 / 8,
            xStart + width / 2 + width / 10, yStart + height / 2 )
    }

    override fun draw(canvas: Canvas, paint: Paint){
        val startX = x + width / 4F
        val startY = y + height * 2 / 3F
        paint.color = Color.BLACK
        paint.textSize = height * 2 / 3F
        paint.textAlign = Paint.Align.CENTER

        canvas.drawText(value.toString(), startX, startY, paint)

        upButton.draw(canvas, paint)
        downButton.draw(canvas, paint)
    }

    fun touch(x1 : Int, y1: Int){
        if (upButton.isIn(x1, y1)){
            if (value == max){
                value = min
            }else{
                value++
            }
        }else if (downButton.isIn(x1, y1)){
            if (value == min){
                value = max
            }else{
                value--
            }
        }
    }

}