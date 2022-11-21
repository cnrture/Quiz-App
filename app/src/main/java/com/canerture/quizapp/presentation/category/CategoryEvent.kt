package com.canerture.quizapp.presentation.category

sealed class CategoryEvent {
    class CategorySelected(category: String, difficulty: String, type: String) : CategoryEvent()
}