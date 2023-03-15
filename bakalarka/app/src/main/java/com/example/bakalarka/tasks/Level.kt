package com.example.bakalarka.tasks

const val NUM_LEVELS = 4
abstract class Level {
    var tasks : List<Pair<String, EquationsGenerator>> = listOf()
}