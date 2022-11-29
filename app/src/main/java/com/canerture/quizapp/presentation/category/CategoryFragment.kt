package com.canerture.quizapp.presentation.category

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.Constants.MULTIPLE_CHOICE
import com.canerture.quizapp.common.Constants.TRUE_FALSE
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.extension.setWidthPercent
import com.canerture.quizapp.common.extension.showFullPagePopup
import com.canerture.quizapp.data.source.local.MockCategories
import com.canerture.quizapp.databinding.FragmentCategoryBinding
import com.canerture.quizapp.databinding.PopupDifficultyTypeBinding
import com.canerture.quizapp.delegation.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)

    private val categoryViewModel: CategoryViewModel by viewModels()

    private val categoriesAdapter by lazy { CategoriesAdapter(onCategoryClick = ::onCategoryClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(categoryViewModel) {
                state.collect(viewLifecycleOwner) {
                    progressBar.isVisible = it.loadingState

                    rvCategories.adapter = categoriesAdapter
                    categoriesAdapter.setData(MockCategories.getCategories())
                }

                effect.collect(viewLifecycleOwner) {
                    when (it) {
                        is CategoryUIEffect.GoToQuizScreen -> {
                            val categoryToQuiz = CategoryFragmentDirections.categoryToQuiz(
                                it.category,
                                it.type
                            )
                            findNavController().navigate(categoryToQuiz)
                        }
                        is CategoryUIEffect.ShowFullScreenError -> {
                            requireContext().showFullPagePopup(R.drawable.ic_error, it.message) {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onCategoryClick(category: Int) {
        showTypePopup {
            categoryViewModel.setEvent(CategoryEvent.CategorySelected(category, it))
        }
    }

    private fun showTypePopup(
        typeListener: (String) -> Unit,
    ) {
        Dialog(requireContext()).apply {
            val binding = PopupDifficultyTypeBinding.inflate(layoutInflater, null, false)
            setContentView(binding.root)
            setWidthPercent(75)
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
    }
}