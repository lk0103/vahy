package com.example.vahy

class Ballon(private val value : Int,
             private var collidable : Boolean = false,
             private var dragable : Boolean = true)
    : Value(value, collidable, dragable)  {

    init {
        //calculate x, y, z, width, height, image
    }
}