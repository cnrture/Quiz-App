package com.canerture.quizapp.presentation.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Constants.CATEGORY
import com.canerture.quizapp.common.Constants.TYPE
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.delegation.VMDelegation
import com.canerture.quizapp.delegation.VMDelegationImpl
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.domain.usecase.GetQuestionsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsByCategoryUseCase: GetQuestionsByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    VMDelegation<QuizUIEffect, QuizEvent, QuizUIState> by VMDelegationImpl(QuizUIState(loadingState = true)) {

    private var questionIndex = 1
    private var questionList = listOf<QuestionUI>()

    init {

        initViewModel(this)

        val category = savedStateHandle.get<Int>(CATEGORY)
        val type = savedStateHandle.get<String>(TYPE)

        if (category != null && type != null) {
            getQuestionsByCategory(category, type)
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
                    is QuizEvent.AnswerClicked -> {
                        if (it.isCorrect) {
                        } else {
                        }
                        setEffect(QuizUIEffect.ResetUI)
                        questionIndex++
                        setState(
                            QuizUIState(
                                false,
                                questionList[questionIndex],
                                questionIndex,
                                questionList.size
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getQuestionsByCategory(category: Int, type: String) {
        getQuestionsByCategoryUseCase.invoke(category, type).onEach {
            when (it) {
                is Resource.Success -> {
                    questionList = it.data
                    setState(
                        QuizUIState(
                            false,
                            questionList[questionIndex],
                            questionIndex,
                            questionList.size
                        )
                    )
                }
                is Resource.Error -> {
                    setState(QuizUIState(loadingState = false))
                    setEffect(QuizUIEffect.ShowFullScreenError("Question list empty!"))
                }
            }
        }.launchIn(viewModelScope)
    }
}