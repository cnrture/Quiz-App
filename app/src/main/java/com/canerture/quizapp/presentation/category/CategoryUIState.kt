package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.presentation.common.State

data class CategoryUIState(
    val loadingState: Boolean = false,
    val data: List<Category>? = null
) : State