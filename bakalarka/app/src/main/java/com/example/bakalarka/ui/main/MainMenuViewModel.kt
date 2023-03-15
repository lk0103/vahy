package com.example.bakalarka.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.objects.menu.RestartIcon
import com.example.bakalarka.tasks.Level
import com.example.bakalarka.tasks.Level1
import com.example.bakalarka.tasks.Level2

class MainMenuViewModel : ViewModel() {
    lateinit var mainActivity: MainActivity
    lateinit var context : Context
    lateinit var prefsLevel : SharedPreferences
    lateinit var prefsTaskInLevel: SharedPreferences
    lateinit var prefsLastUnlockedLevel: SharedPreferences
    var lastUnlockedLevel = 1

    fun initialize(mainMenuView: MainMenuView){
        context = mainActivity.applicationContext
        prefsLevel = context.getSharedPreferences("level", Context.MODE_PRIVATE)
        prefsTaskInLevel =  context.applicationContext.getSharedPreferences(
            "taskInLevel", Context.MODE_PRIVATE)

        prefsLastUnlockedLevel =  context.applicationContext.getSharedPreferences(
            "lastUnlockedLevel", Context.MODE_PRIVATE)
        lastUnlockedLevel = prefsLastUnlockedLevel.getInt("lastUnlockedLevel", 1)
        mainMenuView.changeLockedLevels(lastUnlockedLevel)
    }

    fun onTouch(mainMenu : MainMenuView, motionEvent : MotionEvent) : Boolean{
        val level = mainMenu.clickLevel(motionEvent)
        if (level > lastUnlockedLevel || level <= 0) {
            val icon = mainMenu.clickedIcon(motionEvent)
            if (icon is RestartIcon) {
                restartLevels()
                mainMenu.changeLockedLevels(lastUnlockedLevel)
                mainMenu.invalidate()
            }
            return true
        }

        val continueOnTask = storeTargetLevelAndTask(level)

        switchToChosenLevel(level, continueOnTask, mainMenu)

        return true
    }

    private fun restartLevels(){
        (1..4).forEach { level ->
            val prefsContinueOnTaskInChosenLevel = context.applicationContext.getSharedPreferences(
                "taskInLevel" + level, Context.MODE_PRIVATE
            )
            var editor = prefsContinueOnTaskInChosenLevel.edit()
            editor.putInt("taskInLevel" + level, 0)
            editor.apply()
        }

        val editor = prefsLastUnlockedLevel.edit()
        editor.putInt("lastUnlockedLevel", 1)
        editor.apply()
        lastUnlockedLevel = 1
    }

    private fun storeTargetLevelAndTask(level: Int): Int {
        val prefsContinueOnTaskInChosenLevel = context.applicationContext.getSharedPreferences(
            "taskInLevel" + level, Context.MODE_PRIVATE
        )
        var continueOnTask = prefsContinueOnTaskInChosenLevel.getInt("taskInLevel" + level, 0)

        val numTasksInLevel = getLevel(level).tasks.size
        if (continueOnTask >= numTasksInLevel)
            continueOnTask = numTasksInLevel - 1

        var editor = prefsLevel.edit()
        editor.putInt("level", level)
        editor.apply()

        editor = prefsTaskInLevel.edit()
        editor.putInt("taskInLevel", continueOnTask)
        editor.apply()
        return continueOnTask
    }

    fun getLevel(level: Int) : Level {
        when(level){
            1 -> return Level1()
            2 -> return Level2()
        }
        return Level1()
    }

    private fun switchToChosenLevel(level: Int, continueOnTask: Int,
                                    mainMenu: MainMenuView) {
        val taskType1 = getLevel(level).tasks[continueOnTask].first


        if (taskType1 == "build") {
            switchToBuildEqFragment(mainMenu)
        }

        if (taskType1 == "solve") {
            switchToSolveEqFragment(mainMenu)
        }
    }


    private fun switchToBuildEqFragment(mainMenu: MainMenuView) {
        val navController = Navigation.findNavController(mainMenu)
        if (navController.currentDestination?.id == R.id.mainMenuFragment) {
            val action = MainMenuFragmentDirections
                .actionMainMenuToBuildScaleFromEqFragment()
            Navigation.findNavController(mainMenu).navigate(action)
        }

    }

    private fun switchToSolveEqFragment(mainMenu: MainMenuView) {
        val navController = Navigation.findNavController(mainMenu)
        if (navController.currentDestination?.id == R.id.mainMenuFragment) {
            val action = MainMenuFragmentDirections
                .actionMainMenuToSolveEquationFragment()
            Navigation.findNavController(mainMenu).navigate(action)
        }
    }


}