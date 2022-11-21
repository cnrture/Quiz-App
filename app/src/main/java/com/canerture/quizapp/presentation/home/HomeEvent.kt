package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.presentation.common.Event

sealed class HomeEvent : Event {
    object PlayClicked : HomeEvent()
    object SendTokenRequest : HomeEvent()
}