package com.canerture.quizapp.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.delegation.ViewModelDelegation
import com.canerture.quizapp.delegation.ViewModelDelegationImpl
import com.canerture.quizapp.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel(),
    ViewModelDelegation<CategoryUIEffect, CategoryEvent, CategoryUIState> by ViewModelDelegationImpl() {

    init {

        initViewModel(this, CategoryUIState(loadingState = true))

        getCategories()

        viewModelScope.launch {
            event.collect {
                when (it) {
                    is CategoryEvent.CategorySelected -> {
                        setEffect(
                            CategoryUIEffect.GoToQuizScreen(
                                it.category,
                                it.difficulty,
                                it.type
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getCategories() {
        getCategoriesUseCase.invoke().onEach {
            when (it) {
                is Resource.Success -> setState(
                    CategoryUIState(
                        loadingState = false,
                        data = it.data
                    )
                )
                is Resource.Error -> {
                    setState(CategoryUIState(loadingState = false))
                    setEffect(CategoryUIEffect.ShowError(it.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}