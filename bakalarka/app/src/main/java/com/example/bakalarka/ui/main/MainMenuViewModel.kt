package com.example.bakalarka.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.bakalarka.MainActivity
import com.example.bakalarka.R
import com.example.bakalarka.objects.menu.RestartIcon
import com.example.bakalarka.tasks.*

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
        prefsTaskInLevel =  getSharedPreferences("taskInLevel")

        prefsLastUnlockedLevel =  getSharedPreferences("lastUnlockedLevel")
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
            editSharedPreferences(getSharedPreferences("taskInLevel" + level),
                "taskInLevel" + level, 0)
        }

        editSharedPreferences(prefsLastUnlockedLevel, "lastUnlockedLevel", 1)
        lastUnlockedLevel = 1

        editSharedPreferences(getSharedPreferences( "newUnlockedLevel"),
                        "newUnlockedLevel", -1)
    }

    private fun storeTargetLevelAndTask(level: Int): Int {
        val prefsContinueOnTaskInChosenLevel = getSharedPreferences("taskInLevel" + level)
        var continueOnTask = prefsContinueOnTaskInChosenLevel.getInt("taskInLevel" + level, 0)

        val numTasksInLevel = getLevel(level).tasks.size
        if (continueOnTask >= numTasksInLevel)
            continueOnTask = numTasksInLevel - 1


        editSharedPreferences(prefsLevel, "level", level)
        editSharedPreferences(prefsTaskInLevel, "taskInLevel", continueOnTask)
        return continueOnTask
    }

    fun getLevel(level: Int) : Level {
        when(level){
            1 -> return Level1()
            2 -> return Level2()
            3 -> return Level3()
            4 -> return Level4()
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


    private fun getSharedPreferences(name : String) =
        mainActivity.applicationContext.getSharedPreferences(
            name, Context.MODE_PRIVATE
        )

    private fun editSharedPreferences(prefs : SharedPreferences, name : String, value : Int){
        val editor = prefs.edit()
        editor.putInt(name, value)
        editor.apply()
    }

}