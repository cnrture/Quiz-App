package com.canerture.quizapp.presentation.category

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.extension.setWidthPercent
import com.canerture.quizapp.common.extension.showErrorPopup
import com.canerture.quizapp.common.viewBinding
import com.canerture.quizapp.data.source.local.MockCategories
import com.canerture.quizapp.databinding.FragmentCategoryBinding
import com.canerture.quizapp.databinding.PopupDifficultyTypeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)

    private val categoryViewModel: CategoryViewModel by viewModels()

    private val categoriesAdapter by lazy { CategoriesAdapter(onCategoryClick = ::onCategoryClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectState()
        collectEffect()
    }

    private fun collectState() = categoryViewModel.state.collect(viewLifecycleOwner) { state ->
        with(binding) {
            when (state) {
                is CategoryUIState.Data -> {
                    rvCategories.adapter = categoriesAdapter
                    categoriesAdapter.submitList(MockCategories.getCategories())
                }
            }
        }
    }

    private fun collectEffect() = categoryViewModel.effect.collect(viewLifecycleOwner) { effect ->
        when (effect) {
            is CategoryUIEffect.GoToQuizScreen -> {
                val categoryToQuiz = CategoryFragmentDirections.categoryToQuiz(
                    effect.category,
                    effect.type
                )
                findNavController().navigate(categoryToQuiz)
            }

            is CategoryUIEffect.ShowError -> {
                requireContext().showErrorPopup(R.drawable.ic_error, effect.message)
            }
        }
    }

    private fun onCategoryClick(category: Int) = showTypePopup {
        categoryViewModel.setEvent(CategoryEvent.CategorySelected(category, it))
    }

    private fun showTypePopup(typeListener: (String) -> Unit) = Dialog(requireContext()).apply {
        val binding = PopupDifficultyTypeBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        setWidthPercent(WIDTH_PERCENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        setCanceledOnTouchOutside(true)

        with(binding) {
            imgMultipleChoice.setOnClickListener {
                typeListener(MULTIPLE_CHOICE)
                dismiss()
            }
            imgTrueFalse.setOnClickListener {
                typeListener(TRUE_FALSE)
                dismiss()
            }
        }

        show()
    }

    companion object {
        private const val WIDTH_PERCENT = 75
        private const val MULTIPLE_CHOICE = "multiple"
        private const val TRUE_FALSE = "boolean"
    }
}