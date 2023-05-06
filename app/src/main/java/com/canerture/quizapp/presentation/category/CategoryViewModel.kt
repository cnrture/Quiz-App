package com.canerture.quizapp.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.data.source.local.MockCategories
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegation
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CategoryViewModel @Inject constructor() :
    ViewModel(),
    VMDelegation<CategoryUIEffect, CategoryEvent, CategoryUIState> by VMDelegationImpl(
        CategoryUIState(MockCategories.getCategories())
    ) {

    init {
        initViewModel(this)
        collectEvent()
        getCategories()
    }

    private fun collectEvent() = event.collect(viewModelScope) {
        when (it) {
            is CategoryEvent.CategorySelected -> setEffect(
                CategoryUIEffect.GoToQuizScreen(
                    it.category,
                    it.type
                )
            )
        }
    }

    private fun getCategories() = viewModelScope.launch {
        setState(CategoryUIState(MockCategories.getCategories()))
    }
}
