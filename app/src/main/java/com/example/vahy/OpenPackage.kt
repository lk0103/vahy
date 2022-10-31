package com.example.vahy

import android.graphics.Bitmap

class OpenPackage(private var collidable : Boolean = true)
    : ScreenObject(collidable) {

    private var insideObject = mutableListOf<EquationObject>()

    init {
        //calculate x, y, z, width, height, image
    }

    fun addObjectIn(obj : EquationObject){
        if (obj is Value || obj is Variable)
            insideObject.add(obj)
    }
}