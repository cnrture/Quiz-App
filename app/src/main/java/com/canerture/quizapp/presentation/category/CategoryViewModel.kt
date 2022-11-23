package com.canerture.quizapp.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.Resource
import com.canerture.quizapp.delegation.VMDelegation
import com.canerture.quizapp.delegation.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel(),
    VMDelegation<CategoryUIEffect, CategoryEvent, CategoryUIState> by VMDelegationImpl(
        CategoryUIState(loadingState = false)
    ) {

    init {

        initViewModel(this)

        getCategories()

        viewModelScope.launch {
            event.collect {
                when (it) {
                    is CategoryEvent.CategorySelected -> {
                        setEffect(
                            CategoryUIEffect.GoToQuizScreen(
                                it.category,
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