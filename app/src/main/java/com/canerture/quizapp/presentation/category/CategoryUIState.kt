package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.presentation.base.viewmodel.State

sealed class CategoryUIState : State {
    data class Data(val data: List<Category>) : CategoryUIState()
}