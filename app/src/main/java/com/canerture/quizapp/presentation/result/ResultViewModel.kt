package com.canerture.quizapp.presentation.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Constants.CORRECT_ANSWERS
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.delegation.viewmodel.VMDelegation
import com.canerture.quizapp.delegation.viewmodel.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    VMDelegation<ResultUIEffect, ResultEvent, ResultUIState> by VMDelegationImpl(
        ResultUIState(
            loadingState = true
        )
    ) {

    init {

        initViewModel(this)

        savedStateHandle.get<Int>(CORRECT_ANSWERS)?.let {
            val result = it.toFloat() / 10 * 100
            setState(ResultUIState(false, result))
        }

        event.collect(viewModelScope) { event ->
            when (event) {
                ResultEvent.ContinueClicked -> {
                    setEffect(ResultUIEffect.GoHome)
                }
            }
        }
    }
}