package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.common.State
import com.canerture.quizapp.data.model.category.Category

data class CategoryUIState(
    val loadingState: Boolean = false,
    val data: List<Category>? = null
) : State