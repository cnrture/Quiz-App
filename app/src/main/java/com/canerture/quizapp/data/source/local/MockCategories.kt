package com.canerture.quizapp.data.source.local

import com.canerture.quizapp.R
import com.canerture.quizapp.data.model.category.Category

object MockCategories {
    fun getCategories() = listOf(
        Category(11, "Film", R.drawable.ic_film),
        Category(12, "Music", R.drawable.ic_music),
        Category(15, "Video Games", R.drawable.ic_videogames),
        Category(21, "Sports", R.drawable.ic_sports),
        Category(22, "Geography", R.drawable.ic_geography),
        Category(23, "History", R.drawable.ic_history),
        Category(24, "Politics", R.drawable.ic_politics),
        Category(25, "Art", R.drawable.ic_art),
        Category(26, "Celebrities", R.drawable.ic_celebrities),
        Category(27, "Animals", R.drawable.ic_animals),
    )
}