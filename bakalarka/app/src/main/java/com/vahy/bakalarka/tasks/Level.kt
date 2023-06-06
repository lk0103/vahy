package com.vahy.bakalarka.tasks

const val NUM_LEVELS = 4
abstract class Level {
    var tasks : List<Pair<String, Generator>> = listOf()

    fun unlockNext(continueOnTask : Int) = continueOnTask >= tasks.size - 3

    fun isFinished(continueOnTask: Int) = continueOnTask >= tasks.size

    fun getNumberTasks() = tasks.size

    fun getTaskGenerator(index : Int) = tasks[index].first

    fun getTaskType(index : Int) = tasks[index].second
}