package com.canerture.quizapp.presentation.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.canerture.quizapp.data.model.category.TriviaCategory
import com.canerture.quizapp.databinding.ItemCategoryBinding
import com.canerture.quizapp.presentation.common.DiffUtilCallback

class CategoriesAdapter(private val onCategoryClick: (Int) -> Unit) :
    RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var list = emptyList<TriviaCategory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(list[position])

    inner class CategoryViewHolder(private var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TriviaCategory) {

            binding.btnCategory.text = item.name

            binding.btnCategory.setOnClickListener {
                item.id?.let { onCategoryClick(it) }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun setData(newList: List<TriviaCategory>) {
        val diffUtil = DiffUtilCallback(
            list, newList,
            itemsTheSame = { oldPosition, newPosition ->
                list[oldPosition].id == newList[newPosition].id
            },
            contentsTheSame = { oldPosition, newPosition ->
                list[oldPosition] == newList[newPosition]
            }
        )
        val diffUtilResults = DiffUtil.calculateDiff(diffUtil)
        list = newList
        diffUtilResults.dispatchUpdatesTo(this)
    }
}