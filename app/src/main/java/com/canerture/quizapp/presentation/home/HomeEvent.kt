package com.canerture.quizapp.presentation.home

import com.canerture.quizapp.common.Event

sealed class HomeEvent : Event {
    object PlayClicked : HomeEvent()
}