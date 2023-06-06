package com.vahy.bakalarka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vahy.bakalarka.R.*
import com.vahy.bakalarka.equation.SystemOfEquations

class MainActivity : AppCompatActivity() {

    private var createdEquation : SystemOfEquations? = null
    private var screenVariablesToString : MutableMap<String, String>? = null
    private var typeCreateTask = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
    }

    fun deleteCreatedEquation(){
        createdEquation = null
        screenVariablesToString = null
        typeCreateTask = -1
    }

    fun getCreatedEquation() : SystemOfEquations? = createdEquation?.copy()

    fun getTypeCreateTask() = typeCreateTask

    fun getVariableToScreenObjects() = screenVariablesToString

    fun setCreatedEquation(sysEq : SystemOfEquations?,
                           screenToStringObj : MutableMap<String, String>){
        createdEquation = sysEq
        screenVariablesToString = screenToStringObj
    }

    fun setTypeCreateTask(type : Int){
        typeCreateTask = type
    }

}