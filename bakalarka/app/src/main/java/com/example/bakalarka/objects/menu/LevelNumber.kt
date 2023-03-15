package com.example.bakalarka.objects.menu

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R
import com.example.bakalarka.objects.ContainerForEquationBoxes
import com.example.vahy.objects.ScreenObject

class LevelNumber(private val context : Context, val number : Int)
    : ScreenObject(false, false) {

    var locked = true
    init {
        z = 2
        width = 230
        height = 295
        reloadImage(width, height)
    }

    override fun reloadImage(w : Int, h : Int){
        var drawable = R.drawable.num1
        when (number){
            1 -> drawable = if (locked) R.drawable.num1_gray else R.drawable.num1
            2 -> drawable = if (locked) R.drawable.num2_gray else R.drawable.num2
            3 -> drawable = if (locked) R.drawable.num3_gray else R.drawable.num3
            4 -> drawable = if (locked) R.drawable.num4_gray else R.drawable.num4
        }
        image = ContextCompat.getDrawable(context, drawable)!!.toBitmap()
        image = Bitmap.createScaledBitmap(image!!, w, h, true)
    }

    fun moveIntoBowl(xBowl : Int, yBowl : Int){
        x = xBowl - width / 2
        y = yBowl - height + height / 10
    }

    @JvmName("setLocked1")
    fun setLocked(isLocked : Boolean){
        locked = isLocked
        reloadImage(width, height)
    }
}