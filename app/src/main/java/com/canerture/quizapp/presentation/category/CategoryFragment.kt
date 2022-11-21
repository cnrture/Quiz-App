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
import com.canerture.quizapp.common.Constants.EASY
import com.canerture.quizapp.common.Constants.HARD
import com.canerture.quizapp.common.Constants.MEDIUM
import com.canerture.quizapp.common.Constants.MULTIPLE_CHOICE
import com.canerture.quizapp.common.Constants.TRUE_FALSE
import com.canerture.quizapp.common.setWidthPercent
import com.canerture.quizapp.common.showFullPagePopup
import com.canerture.quizapp.common.showPopup
import com.canerture.quizapp.common.viewBinding
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

                it.data?.let { categoryList ->
                    binding.rvCategories.adapter = categoriesAdapter
                    categoriesAdapter.setData(categoryList)
                }
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
                            it.difficulty,
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
        showCategoryPopup(
            difficultyTypeListener = { difficulty, type ->
                categoryViewModel.setEvent(
                    CategoryEvent.CategorySelected(
                        category,
                        difficulty,
                        type
                    )
                )
            }
        )
    }

    private fun showCategoryPopup(
        difficultyTypeListener: (String, String) -> Unit,
    ) {
        Dialog(requireContext()).apply {
            val binding: PopupDifficultyTypeBinding =
                PopupDifficultyTypeBinding.inflate(layoutInflater, null, false)
            setContentView(binding.root)
            setWidthPercent(75)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            setCanceledOnTouchOutside(true)

            var difficulty = EASY
            var type = MULTIPLE_CHOICE

            with(binding) {

                btnEasy.setOnClickListener { difficulty = EASY }
                btnMedium.setOnClickListener { difficulty = MEDIUM }
                btnMedium.setOnClickListener { difficulty = HARD }
                btnMultipleChoice.setOnClickListener { type = MULTIPLE_CHOICE }
                btnTrueFalse.setOnClickListener { type = TRUE_FALSE }

                btnPlay.setOnClickListener {
                    difficultyTypeListener(difficulty, type)
                    dismiss()
                }
            }

            show()
        }
    }
}