package com.example.vahy

import android.graphics.Bitmap

class Ball(private val value : Int,
           private var collidable : Boolean = false,
           private var dragable : Boolean = true)
    : Variable(value, collidable, dragable) {

    init {
        //calculate x, y, z, width, height, image
    }
}