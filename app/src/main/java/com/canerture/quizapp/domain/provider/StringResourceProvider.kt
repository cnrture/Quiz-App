package com.canerture.quizapp.domain.provider

interface StringResourceProvider {
    fun getString(stringResId: Int): String
}