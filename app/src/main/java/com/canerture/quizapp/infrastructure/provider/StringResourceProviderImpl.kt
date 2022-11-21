package com.canerture.quizapp.infrastructure.provider

import android.content.Context
import androidx.annotation.StringRes

class StringResourceProviderImpl(private val context: Context) : StringResourceProvider {

    override fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
}