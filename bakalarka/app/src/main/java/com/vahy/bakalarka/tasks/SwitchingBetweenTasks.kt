package com.vahy.bakalarka.tasks

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.vahy.bakalarka.MainActivity
import com.vahy.bakalarka.R
import com.vahy.bakalarka.ui.main.*
import com.vahy.vahy.ScalesView
import kotlin.random.Random


class SwitchingBetweenTasks {
    lateinit var mainActivity: MainActivity

    fun changeBackground(backgroundView : BackgroundView, l : Int = -1) {
        var level = getLevelInt()
        if (l > 0)
            level = l
        val pattern = when (level) {
            1 -> R.drawable.orange_pattern
            2 -> R.drawable.violet_pattern
            3 -> R.drawable.blue_pattern
            4 -> R.drawable.green_pattern
            else -> R.drawable.blue_pattern
        }
        val bgColor = when (level) {
            1 -> R.color.level1_background
            2 -> R.color.level2_background
            3 -> R.color.level3_background
            4 -> R.color.level4_background
            else -> R.color.main_menu_background
        }
        backgroundView.changeBackground(pattern, bgColor)
    }

    fun setTaskMainMenu(taskMenuView: TaskMenuView){
        var level = getLevelInt()
        level = if (level < 1) 1 else level

        val prefsContinueOnTaskInChosenLevel = getSharedPreferences(prefsNameTaskInLevel + level)
        val continueOnTask = prefsContinueOnTaskInChosenLevel.getInt(prefsNameTaskInLevel + level, 0)

        val levelInfo = getLevel(level)
        val numTasks = levelInfo.getNumberTasks()
        taskMenuView.setProgressBarOrEditIcon(numTasks, continueOnTask)
    }

    fun getLevel(level: Int) : Level{
        when(level){
            1 -> return Level1()
            2 -> return Level2()
            3 -> return Level3()
            4 -> return Level4()
        }
        return Level1()
    }

    fun getLevelInt(): Int {
        val prefsLevel = getSharedPreferences(prefsNameLevel)
        Log.i("levels", " get level int: " + prefsLevel.getInt(prefsNameLevel, 1))
        return prefsLevel?.getInt(prefsNameLevel, -1) ?: -1
    }


    fun storeTargetLevelAndTask(): Pair<Int, Int> {
        val prefsLevel =  getSharedPreferences( prefsNameLevel)
        val level = prefsLevel.getInt(prefsNameLevel, -1)

        val prefsTaskInLevel =  getSharedPreferences(prefsNameTaskInLevel)
        var taskInLevel = prefsTaskInLevel.getInt(prefsNameTaskInLevel, 0)

        val prefsContinueOnTaskInChosenLevel = getSharedPreferences(prefsNameTaskInLevel + level)
        var continueOnTask = prefsContinueOnTaskInChosenLevel.getInt(prefsNameTaskInLevel + level, 0)

        taskInLevel++
        unlockNewLevelIfComplete(level, continueOnTask)

        val lastTaskIx = getLevel(level).getNumberTasks() - 1
        if (continueOnTask < lastTaskIx) {
            continueOnTask = taskInLevel
        }else if (continueOnTask == lastTaskIx){
            continueOnTask++
        }
        taskInLevel = generateNewTaskAfterCompletionOfLevel(level, continueOnTask)

        editSharedPreferences(prefsTaskInLevel, prefsNameTaskInLevel, taskInLevel)
        editSharedPreferences(prefsContinueOnTaskInChosenLevel, (prefsNameTaskInLevel + level), continueOnTask)
        editSharedPreferences(prefsLevel, prefsNameLevel, level)

        Log.i("levels", "store target level:   prepina sa na level: " + level +
                " prepina sa na task: " + taskInLevel +
                " na ktorom prikalde v leveli sa ma pokracovat: " + continueOnTask)

        return Pair(level, taskInLevel)
    }

    private fun generateNewTaskAfterCompletionOfLevel(level: Int, taskInLevel: Int): Int {
        var taskInLevel1 = taskInLevel
        val numTasksInLevel = getLevel(level).getNumberTasks()
        if (taskInLevel1 >= numTasksInLevel) {
            taskInLevel1 = Random.nextInt(numTasksInLevel - 3, numTasksInLevel)
        }
        return taskInLevel1
    }


    private fun unlockNewLevelIfComplete(level: Int, taskInLevel: Int) {
        val prefsLastUnlockedLevel = getSharedPreferences(prefsNameLastUnlocked)
        var lastUnlockedLevel = prefsLastUnlockedLevel.getInt(prefsNameLastUnlocked, 1)

        if (level >= lastUnlockedLevel && getLevel(level).unlockNext(taskInLevel)) {
            if (lastUnlockedLevel < NUM_LEVELS) {
                lastUnlockedLevel++
                editSharedPreferences(prefsLastUnlockedLevel, prefsNameLastUnlocked, lastUnlockedLevel)

                storeNewLevelMessage(lastUnlockedLevel)
                Log.i("levels", "lastunlocked level: " + lastUnlockedLevel)
            }
        }
    }

    private fun storeNewLevelMessage(lastUnlockedLevel: Int) {
        if (lastUnlockedLevel > NUM_LEVELS)
            return

        val prefsNewUnlockedLevel = getSharedPreferences(prefsNameNewUnlocked)
        editSharedPreferences(prefsNewUnlockedLevel, prefsNameNewUnlocked, lastUnlockedLevel)
    }

    fun readNewLevelMessage() : Int{
        val prefsNewUnlockedLevel = getSharedPreferences(prefsNameNewUnlocked)
        return prefsNewUnlockedLevel.getInt(prefsNameNewUnlocked, -1)
    }

    fun showNewLevelUnlockedMessage(scalesView: ScalesView) {
        val newLevelMessage = readNewLevelMessage()
        if (newLevelMessage >= 1 && newLevelMessage <= NUM_LEVELS &&
                scalesView.showNewLevelMessage == -1) {
            scalesView.showNewLevelMessage = newLevelMessage
            storeNewLevelMessage(-1)
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