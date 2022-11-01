package com.example.vahy


class Package(private var collidable : Boolean = false,
              private var dragable : Boolean = true)
    : EquationObject(collidable, dragable)  {
    private var insideObject = mutableListOf<EquationObject>()

    init {
        //calculate x, y, z, width, height, image
    }

    fun putObjectsIn(inside : MutableList<EquationObject>){
        insideObject = inside.toMutableList()
    }

    fun addObjectIn(obj : EquationObject){
        if (obj is ScaleValue || obj is ScaleVariable)
            insideObject.add(obj)
    }

    override fun evaluate(): Int =
        insideObject.sumOf { it.evaluate() }
}