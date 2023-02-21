package com.example.vahy.objects

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.bakalarka.R

//mozno ked dvojklik na kos - tak sa vsetko vyberie
//a vrati sa rovnica do povodneho stavu
class Bin(context : Context) :
    ScreenObject(false, true){

    init {
        image = ContextCompat.getDrawable(context, R.drawable.bin1)!!.toBitmap()
        z = 2
        width = 200
        height = 300
        image = Bitmap.createScaledBitmap(image, width, height, true)
    }
}