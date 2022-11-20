package com.canerture.quizapp.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.data.model.category.TriviaCategory
import com.canerture.quizapp.delegation.ViewModelDelegation
import com.canerture.quizapp.delegation.ViewModelDelegationImpl
import com.canerture.quizapp.domain.usecase.GetCategoriesUseCase
import com.canerture.quizapp.domain.usecase.GetTokenFromDataStoreUseCase
import com.canerture.quizapp.presentation.UIState
import com.canerture.quizapp.presentation.home.HomeEvent
import com.canerture.quizapp.presentation.home.HomeUIEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getTokenFromDataStoreUseCase: GetTokenFromDataStoreUseCase
) : ViewModel(),
    ViewModelDelegation<CategoryUIEffect, CategoryEvent, List<TriviaCategory>> by ViewModelDelegationImpl() {

    init {
        getCategories()
        getSessionTokenFromDataStore()

        viewModelScope.launch {
            event.collect {
                when(it) {
                    is CategoryEvent.CategorySelected -> {

                    }
                }
            }
        }
    }

    private fun getCategories() {
        getCategoriesUseCase.invoke().onEach {
            when (it) {
                Resource.Loading -> setState(UIState(loadingState = true))
                is Resource.Success -> setState(UIState(loadingState = false, data = it.data))
                is Resource.Error -> {
                    setState(UIState(loadingState = false))
                    setEffect(CategoryUIEffect.ShowError(it.message))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSessionTokenFromDataStore() {
        getTokenFromDataStoreUseCase.invoke().onEach { token ->
        }.launchIn(viewModelScope)
    }
}