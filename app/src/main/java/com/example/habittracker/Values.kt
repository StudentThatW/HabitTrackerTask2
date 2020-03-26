package com.example.habittracker

const val CREATE_CODE = 228
const val EDIT_CODE = 322

val priorities = listOf(
    "Низкий",
    "Средний",
    "Высокий"
)

val prioritiesMap = mapOf(
    Priority.Low to priorities[0],
    Priority.Medium to priorities[1],
    Priority.High to priorities[2]
)

val typesMap = mapOf(
    HabitType.Food to "Еда",
    HabitType.Drink to "Питье",
    HabitType.Work to "Работа"
)
