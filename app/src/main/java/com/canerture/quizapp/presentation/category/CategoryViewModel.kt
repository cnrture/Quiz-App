package com.canerture.quizapp.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.delegation.VMDelegation
import com.canerture.quizapp.delegation.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryViewModel @Inject constructor() :
    ViewModel(),
    VMDelegation<CategoryUIEffect, CategoryEvent, CategoryUIState> by VMDelegationImpl(
        CategoryUIState(loadingState = false)
    ) {

    init {

        initViewModel(this)

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
}
