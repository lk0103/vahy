package com.example.vahy

import android.graphics.Bitmap

open class EquationObject(private var collidable : Boolean = false,
                     private var dragable : Boolean = true)
    : ScreenObject(collidable)  {

    init {
        //calculate x, y, z, width, height, image
    }

    open fun evaluate() : Int = 0

    fun move(deltaX : Int, deltaY : Int){

    }

    fun onDrag(){

    }
}