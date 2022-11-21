package com.canerture.quizapp.infrastructure.provider

interface StringResourceProvider {
    fun getString(stringResId: Int): String
}