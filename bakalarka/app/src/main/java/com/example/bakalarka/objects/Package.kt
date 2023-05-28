package com.example.bakalarka.objects

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R


class Package(private val context: Context, dragFrom : Boolean = false,
              dragTo : Boolean = false)
    : EquationObject(dragFrom, dragTo)  {
    var insideObject = mutableListOf<EquationObject>()

    init {
        image = ContextCompat.getDrawable(context, R.drawable.package1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, width, height, true);
    }

    override fun reloadImage(w : Int, h : Int){
        if (image == null)
            return
        image = ContextCompat.getDrawable(context, R.drawable.package1)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    override fun makeCopy(): EquationObject =
        setParametersOfCopy(Package(context, dragFrom, dragTo))

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

    override fun returnPackages() : MutableList<Package> = mutableListOf(this)


    override fun evaluate(): Int =
        insideObject.sumOf { it.evaluate() }
}