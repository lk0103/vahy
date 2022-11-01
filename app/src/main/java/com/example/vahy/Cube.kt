package com.example.vahy

import android.graphics.Bitmap

class Cube(private val value : Int,
           private var collidable : Boolean = false,
           private var dragable : Boolean = true)
    : ScaleVariable(value, collidable, dragable)  {

    init {
        //calculate x, y, z, width, height, image
    }
}