package com.canerture.quizapp.presentation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegation
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    VMDelegation<ResultUIEffect, ResultEvent, ResultUIState> by VMDelegationImpl(ResultUIState()) {

    init {

        initViewModel(this)

        collectEvent()

        savedStateHandle.get<Int>(CORRECT_ANSWERS)?.let {
            val isLowerThanFive = it < NUMBER_FIVE
            setState(ResultUIState(it, isLowerThanFive))
        }
    }

    private fun collectEvent() = event.collect(viewModelScope) { event ->
        when (event) {
            ResultEvent.ContinueClicked -> {
                setEffect(ResultUIEffect.GoHome)
            }
        }
    }

    companion object {
        private const val NUMBER_TEN = 10
        private const val NUMBER_FIVE = 5
        private const val NUMBER_HUNDRED = 100
        private const val CORRECT_ANSWERS = "correctAnswers"
    }
}