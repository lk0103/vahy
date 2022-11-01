package com.example.vahy

class OpenPackage(private var collidable : Boolean = true)
    : ScreenObject(collidable) {

    private var insideObject = mutableListOf<EquationObject>()

    init {
        //calculate x, y, z, width, height, image
    }

    fun addObjectIn(obj : EquationObject){
        if (obj is ScaleValue || obj is ScaleVariable)
            insideObject.add(obj)
    }
}