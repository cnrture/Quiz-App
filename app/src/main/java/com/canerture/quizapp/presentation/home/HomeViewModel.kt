package com.canerture.quizapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.quizapp.delegation.VMDelegation
import com.canerture.quizapp.delegation.VMDelegationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() :
    ViewModel(),
    VMDelegation<HomeUIEffect, HomeEvent, HomeUIState> by VMDelegationImpl(HomeUIState(loadingState = false)) {

    init {

        initViewModel(this)

        viewModelScope.launch {
            event.collect {
                when (it) {
                    HomeEvent.PlayClicked -> {
                        setEffect(HomeUIEffect.GoToCategoryScreen)
                    }
                }
            }
        }
    }
}