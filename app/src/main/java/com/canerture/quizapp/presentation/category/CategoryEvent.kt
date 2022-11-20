package com.canerture.quizapp.presentation.category

sealed class CategoryEvent {
    class CategorySelected(category: String): CategoryEvent()
}