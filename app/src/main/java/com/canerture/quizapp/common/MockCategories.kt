package com.canerture.quizapp.common

import com.canerture.quizapp.R
import com.canerture.quizapp.data.model.category.Category

object MockCategories {
    fun getCategories() = listOf(
        Category(9, "General Knowledge", R.drawable.trivia_logo),
        Category(11, "Film", R.drawable.trivia_logo),
        Category(12, "Music", R.drawable.trivia_logo),
        Category(15, "Video Games", R.drawable.trivia_logo),
        Category(21, "Sports", R.drawable.trivia_logo),
        Category(22, "Geography", R.drawable.trivia_logo),
        Category(23, "History", R.drawable.trivia_logo),
        Category(24, "Politics", R.drawable.trivia_logo),
        Category(25, "Art", R.drawable.trivia_logo),
        Category(26, "Celebrities", R.drawable.trivia_logo),
        Category(27, "Animals", R.drawable.trivia_logo),
    )
}