package com.example.bakalarka.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R


class Package(private val context: Context, touchable : Boolean = false,
              draggable : Boolean = true)
    : EquationObject(touchable, draggable)  {
    var insideObject = mutableListOf<EquationObject>()
    private var maxNumberOfVariableTypes = 1

    init {
        image = ContextCompat.getDrawable(context, R.drawable.package1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, width, height, true);
    }

    override fun reloadImage(w : Int, h : Int){
        image = ContextCompat.getDrawable(context, R.drawable.package1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Package(context, touchable, draggable))

    override fun setParametersOfCopy(copy : EquationObject) : EquationObject {
        (copy as Package).insideObject = mutableListOf<EquationObject>()
        insideObject.forEach { obj ->
            (copy as Package).insideObject.add(obj)
        }
        return super.setParametersOfCopy(copy)
    }

    fun putObjectsIn(inside : MutableList<EquationObject>){
        insideObject = inside.toMutableList()
    }

    fun addObjectIn(obj : EquationObject){
        if (!possibleToAddVariable(obj) ||
            ! (obj is ScaleValue || obj is ScaleVariable))
            throw java.lang.Exception("maximum capacity of variables in the package")

        insideObject.add(obj)
    }

    fun possibleToAddVariable(obj : EquationObject) : Boolean{
        var count = 0
        if (insideObject.any { it is Ball }) {
            if (obj is Ball)
                return true
            count++
        }
        if (insideObject.any { it is Cube }) {
            if (obj is Cube)
                return true
            count++
        }
        if (insideObject.any { it is Cylinder }) {
            if (obj is Cylinder)
                return true
            count++
        }
        return count < maxNumberOfVariableTypes
    }

    override fun evaluate(): Int =
        insideObject.sumOf { it.evaluate() }
}