package com.canerture.quizapp.presentation.category

import com.canerture.quizapp.data.model.category.TriviaCategory
import com.canerture.quizapp.presentation.common.State

data class CategoryUIState(
    val loadingState: Boolean = false,
    val data: List<TriviaCategory>? = null
) : State