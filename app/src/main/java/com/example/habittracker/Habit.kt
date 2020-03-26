package com.example.habittracker

import java.io.Serializable

enum class HabitType {
    Food,
    Drink,
    Work
}

enum class Priority {
    Low,
    Medium,
    High,
}

class Habit(
    var name: String,
    var description: String,
    var priority: Priority,
    var type: HabitType,
    var repeats: Int,
    var period: Int
): Serializable
