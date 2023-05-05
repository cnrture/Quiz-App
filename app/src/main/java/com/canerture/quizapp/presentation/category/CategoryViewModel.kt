package com.canerture.quizapp.presentation.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.data.source.local.MockCategories
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegation
import com.canerture.quizapp.presentation.base.viewmodel.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CategoryViewModel : ViewModel(),
    VMDelegation<CategoryUIEffect, CategoryEvent, CategoryUIState> by VMDelegationImpl(
        CategoryUIState.Data(MockCategories.getCategories())
    ) {

    init {
        initViewModel(this)
        collectEvent()
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
}
