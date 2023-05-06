package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.presentation.base.viewmodel.State

data class CategoryUIState(
    val data: List<Category>? = null
) : State