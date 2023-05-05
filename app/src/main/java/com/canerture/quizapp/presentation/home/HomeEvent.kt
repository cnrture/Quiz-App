package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.base.viewmodel.Event

sealed class HomeEvent : Event {
    object PlayClicked : HomeEvent()
}