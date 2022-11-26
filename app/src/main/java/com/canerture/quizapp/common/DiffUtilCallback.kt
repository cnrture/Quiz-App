package com.canerture.quizapp.common

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val itemsTheSame: (Int, Int) -> Boolean = { _, _ -> false },
    private val contentsTheSame: (Int, Int) -> Boolean = { _, _ -> false }
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return itemsTheSame(oldItemPosition, newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return contentsTheSame(oldItemPosition, newItemPosition)
    }
}