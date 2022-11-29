package com.canerture.quizapp.presentation.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Constants.CATEGORY
import com.canerture.quizapp.common.Constants.TYPE
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.delegation.viewmodel.VMDelegation
import com.canerture.quizapp.delegation.viewmodel.VMDelegationImpl
import com.canerture.quizapp.domain.model.question.QuestionUI
import com.canerture.quizapp.domain.usecase.GetQuestionsByCategoryUseCase
import com.canerture.quizapp.domain.usecase.GetSessionTokenUseCase
import com.canerture.quizapp.domain.usecase.GetTokenFromDataStoreUseCase
import com.canerture.quizapp.domain.usecase.ResetSessionTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getTokenFromDataStoreUseCase: GetTokenFromDataStoreUseCase,
    private val getSessionTokenUseCase: GetSessionTokenUseCase,
    private val resetSessionTokenUseCase: ResetSessionTokenUseCase,
    private val getQuestionsByCategoryUseCase: GetQuestionsByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(),
    VMDelegation<QuizUIEffect, QuizEvent, QuizUIState> by VMDelegationImpl(QuizUIState(loadingState = true)) {

    private var questionIndex = 1
    private var questionList = listOf<QuestionUI>()

    var category: Int? = null
    var type: String? = null

    init {

        initViewModel(this)

        category = savedStateHandle.get<Int>(CATEGORY)
        type = savedStateHandle.get<String>(TYPE)

        if (category != null && type != null) {
            getTokenFromDataStore(category!!, type!!)
        } else {
            setState(QuizUIState(loadingState = false))
            setEffect(QuizUIEffect.ShowError("Something went wrong!"))
        }

        event.collect(viewModelScope) { event ->
            when (event) {
                QuizEvent.CloseClicked -> {
                    setEffect(QuizUIEffect.GoBack)
                }
                is QuizEvent.Answered -> {
                    if (event.answer == questionList[questionIndex].correctAnswer) {
                        setEffect(QuizUIEffect.CorrectAnswer)
                    } else {
                        setEffect(QuizUIEffect.IncorrectAnswer(questionList[questionIndex].correctAnswer))
                    }
                }
                QuizEvent.NextQuestion -> {
                    nextQuestion()
                }
                QuizEvent.TimeIsUp -> {
                    // incorrectAnswer logic
                    nextQuestion()
                }
            }
        }
    }

    private fun nextQuestion() {
        setEffect(QuizUIEffect.NextQuestion)
        questionIndex++
        val question = questionList[questionIndex]
        question.allAnswers.shuffle()
        setState(
            QuizUIState(
                false,
                question,
                questionIndex,
                questionList.size
            )
        )
    }

    private fun getTokenFromDataStore(category: Int, type: String) {
        getTokenFromDataStoreUseCase.invoke().onEach { state ->
            when (state) {
                is GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.Success ->
                    getQuestionsByCategory(category, type, state.token)
                GetTokenFromDataStoreUseCase.GetTokenFromDataStoreState.NullToken -> {
                    getSessionToken()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getQuestionsByCategory(category: Int, type: String, token: String) {
        getQuestionsByCategoryUseCase.invoke(category, type, token).onEach { state ->
            when (state) {
                is GetQuestionsByCategoryUseCase.GetQuestionsByCategoryState.Success -> {
                    questionList = state.questionList.also { list ->
                        setState(
                            QuizUIState(
                                loadingState = false,
                                list[questionIndex],
                                questionIndex,
                                list.size
                            )
                        )
                    }
                }
                is GetQuestionsByCategoryUseCase.GetQuestionsByCategoryState.TokenEmpty -> {
                    resetSessionToken(token)
                }
                is GetQuestionsByCategoryUseCase.GetQuestionsByCategoryState.Error -> {
                    setState(QuizUIState(loadingState = false))
                    setEffect(QuizUIEffect.ShowFullScreenError(state.errorMessage))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun resetSessionToken(token: String) {
        resetSessionTokenUseCase.invoke(token).onEach {
            when (it) {
                is Resource.Success -> {
                    getTokenFromDataStore(category!!, type!!)
                }
                is Resource.Error -> {
                    setState(QuizUIState(loadingState = false))
                    setEffect(QuizUIEffect.ShowError(it.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSessionToken() {
        getSessionTokenUseCase.invoke().onEach {
            when (it) {
                is Resource.Success -> {
                    getTokenFromDataStore(category!!, type!!)
                }
                is Resource.Error -> {
                    setState(QuizUIState(loadingState = false))
                    setEffect(QuizUIEffect.ShowError(it.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}