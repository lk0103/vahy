package com.example.bakalarka.objects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.example.vahy.objects.ScreenObject

class BallBox: EquationObjectBox() {

    init {
        maxNumberOfObj = 6
        cols = 3.5
        rows = 3.0
    }

    override fun calculatePositions() {
        val yBottom = y + height
        val yDelta = heightEqObj / 5
        val xDelta = (width - 7 * (widthEqObj) / 2) / 2


        positions = mutableListOf(
            listOf(
                x + xDelta + widthEqObj * 7 / 4 ,
                yBottom - heightEqObj / 2 + yDelta / 2,
                4
            ), //spodny rad, stredna
            listOf(
                x + xDelta + widthEqObj * 3 / 4 ,
                yBottom - heightEqObj / 2 + yDelta / 2,
                5
            ),                // spodny rad, prva
            listOf(
                x + xDelta + widthEqObj * 5 / 4  ,
                yBottom - heightEqObj * 3 / 2 + yDelta * 3 / 2,
                7
            ), //stredny rad, prva
            listOf(
                x + xDelta + widthEqObj * 11 / 4 ,
                yBottom - heightEqObj / 2 + yDelta / 2,
                3
            ),   // spodny rad, tretia
            listOf(
                x + xDelta + widthEqObj * 9 / 4,
                yBottom - heightEqObj * 3 / 2 + yDelta * 3 / 2,
                6
            ), //stredny rad, druha
            listOf(
                x + xDelta + widthEqObj * 7 / 4 ,
                yBottom - heightEqObj * 5 / 2 + yDelta * 5 / 2,
                8
            ), //najvyssi rad
        )
    }


}