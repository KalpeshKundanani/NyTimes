package com.kalpeshkundanani.nytimes.data.enums

/**
 * Created by Kalpesh Kundanani on 12/11/20.
 */
enum class NewsPeriod {
    DAY, WEEK, MONTH;

    fun getPeriod(): Int {
        return when (this) {
            DAY -> 1
            WEEK -> 7
            MONTH -> 30
            else -> -1
        }
    }

    fun getName(): String? {
        return when (this) {
            DAY -> "One Day"
            WEEK -> "Seven Days"
            MONTH -> "Thirty Days"
            else -> null
        }
    }
}