package com.canerture.quizapp.presentation.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.canerture.quizapp.data.model.category.Category
import com.canerture.quizapp.databinding.ItemCategoryBinding
import com.canerture.quizapp.presentation.base.DiffUtilCallback

class CategoriesAdapter(private val onCategoryClick: (Int) -> Unit) :
    ListAdapter<Category, CategoriesAdapter.CategoryViewHolder>(
        DiffUtilCallback<Category>(
            itemsTheSame = { oldItem, newItem ->
                oldItem == newItem
            },
            contentsTheSame = { oldItem, newItem ->
                oldItem.id == newItem.id
            }
        )
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
        ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(currentList[position])

    inner class CategoryViewHolder(private var binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category) = with(binding) {
            tvCategory.setText(item.name)
            imgCategory.setImageResource(item.image)
            root.setOnClickListener {
                onCategoryClick(item.id)
            }
        }
    }
}