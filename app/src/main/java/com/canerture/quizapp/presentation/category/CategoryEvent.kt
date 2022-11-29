package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.presentation.base.Event

sealed class CategoryEvent : Event {
    class CategorySelected(val category: Int, val type: String) : CategoryEvent()
}