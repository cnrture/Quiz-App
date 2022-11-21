package com.canerture.quizapp.presentation.category

sealed class CategoryEvent {
    class CategorySelected(val category: Int, val difficulty: String, val type: String) :
        CategoryEvent()
}