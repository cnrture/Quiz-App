package com.canerture.quizapp.presentation.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.delegation.ViewModelDelegation
import com.canerture.quizapp.delegation.ViewModelDelegationImpl
import com.canerture.quizapp.domain.usecase.GetQuestionsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsByCategoryUseCase: GetQuestionsByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    ViewModelDelegation<QuizUIEffect, QuizEvent, QuizUIState> by ViewModelDelegationImpl() {

    init {

        initViewModel(this, QuizUIState(loadingState = true))

        val category = savedStateHandle.get<Int>("category")
        val difficulty = savedStateHandle.get<String>("difficulty")
        val type = savedStateHandle.get<String>("type")

        if (category != null && difficulty != null && type != null) {
            getQuestionsByCategory(category, difficulty, type)
        } else {
            setState(QuizUIState(loadingState = false))
            setEffect(QuizUIEffect.ShowError("Something went wrong!"))
        }

        viewModelScope.launch {
            event.collect {
                when (it) {
                    QuizEvent.CloseClicked -> {
                        setEffect(QuizUIEffect.GoBack)
                    }
                }
            }
        }
    }

    private fun getQuestionsByCategory(category: Int, difficulty: String, type: String) {
        getQuestionsByCategoryUseCase.invoke(category, difficulty, type).onEach {
            when (it) {
                is Resource.Success -> {
                    setState(QuizUIState(loadingState = false, data = it.data))
                }
                is Resource.Error -> {
                    setState(QuizUIState(loadingState = false))
                    setEffect(QuizUIEffect.ShowFullScreenError("Question list empty!"))
                }
            }
        }.launchIn(viewModelScope)
    }
}