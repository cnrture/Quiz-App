package com.canerture.quizapp.presentation.category

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.*
import com.canerture.quizapp.common.Constants.MULTIPLE_CHOICE
import com.canerture.quizapp.common.Constants.TRUE_FALSE
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

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            categoryViewModel.state.collect {
                binding.progressBar.isVisible = it.loadingState

                binding.rvCategories.adapter = categoriesAdapter
                categoriesAdapter.setData(MockCategories.getCategories())
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            categoryViewModel.effect.collect {
                when (it) {
                    CategoryUIEffect.GoBack -> {
                    }
                    is CategoryUIEffect.GoToQuizScreen -> {
                        val categoryToQuiz = CategoryFragmentDirections.categoryToQuiz(
                            it.category,
                            it.type
                        )
                        findNavController().navigate(categoryToQuiz)
                    }
                    is CategoryUIEffect.ShowError -> requireContext().showPopup(
                        iconId = R.drawable.ic_error,
                        title = it.message,
                        dismissListener = {
                        }
                    )
                    is CategoryUIEffect.ShowFullScreenError -> {
                        requireContext().showFullPagePopup(
                            iconId = R.drawable.ic_error,
                            title = it.message,
                            dismissListener = {
                            }
                        )
                    }
                }
            }
        }
    }

    private fun onCategoryClick(category: Int) {
        showTypePopup(
            typeListener = {
                categoryViewModel.setEvent(CategoryEvent.CategorySelected(category, it))
            }
        )
    }

    private fun showTypePopup(
        typeListener: (String) -> Unit,
    ) {
        Dialog(requireContext()).apply {
            val binding: PopupDifficultyTypeBinding =
                PopupDifficultyTypeBinding.inflate(layoutInflater, null, false)
            setContentView(binding.root)
            setWidthPercent(75)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            setCanceledOnTouchOutside(true)

            var type = MULTIPLE_CHOICE

            with(binding) {
                btnMultipleChoice.setOnClickListener { type = MULTIPLE_CHOICE }
                btnTrueFalse.setOnClickListener { type = TRUE_FALSE }

                btnPlay.setOnClickListener {
                    typeListener(type)
                    dismiss()
                }
            }

            show()
        }
    }
}