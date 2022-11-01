package com.example.vahy

class Weight(private val value : Int,
             private var collidable : Boolean = false,
             private var dragable : Boolean = true)
    : ScaleValue(value, collidable, dragable)  {

    init {
        //calculate x, y, z, width, height, image
    }
}