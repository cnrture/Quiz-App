package com.canerture.quizapp.presentation.category

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.canerture.quizapp.R
import com.canerture.quizapp.common.Constants.EASY
import com.canerture.quizapp.common.Constants.HARD
import com.canerture.quizapp.common.Constants.MEDIUM
import com.canerture.quizapp.common.setWidthPercent
import com.canerture.quizapp.common.showFullPagePopup
import com.canerture.quizapp.common.showPopup
import com.canerture.quizapp.common.viewBinding
import com.canerture.quizapp.databinding.FragmentCategoryBinding
import com.canerture.quizapp.databinding.PopupDifficultyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)

    private val categoryViewModel: CategoryViewModel by viewModels()

    private val categoriesAdapter by lazy { CategoriesAdapter(onCategoryClick = ::onCategoryClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.state.collect { homeState ->
                    binding.progressBar.isVisible = homeState.loadingState

                    homeState.data?.let {
                        binding.rvCategories.adapter = categoriesAdapter
                        categoriesAdapter.setData(it)
                    }
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.effect.collect { effect ->
                    when (effect) {
                        is CategoryUIEffect.ShowError -> requireContext().showPopup(
                            iconId = R.drawable.ic_error,
                            title = effect.message,
                            dismissListener = {
                            }
                        )
                        is CategoryUIEffect.ShowFullScreenError -> {
                            requireContext().showFullPagePopup(
                                iconId = R.drawable.ic_error,
                                title = effect.message,
                                dismissListener = {
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onCategoryClick(categoryName: String) {
        showCategoryPopup(
            description = categoryName,
            easyListener = {
                categoryViewModel.setEvent(CategoryEvent.CategorySelected(EASY))
            },
            mediumListener = {
                categoryViewModel.setEvent(CategoryEvent.CategorySelected(MEDIUM))
            },
            hardListener = {
                categoryViewModel.setEvent(CategoryEvent.CategorySelected(HARD))
            }
        )
    }

    private fun showCategoryPopup(
        description: String,
        easyListener: () -> Unit,
        mediumListener: () -> Unit,
        hardListener: () -> Unit
    ) {
        Dialog(requireContext()).apply {
            val binding: PopupDifficultyBinding =
                PopupDifficultyBinding.inflate(layoutInflater, null, false)
            setContentView(binding.root)
            setWidthPercent(75)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            setCanceledOnTouchOutside(true)

            with(binding) {
                tvDescription.text = description
                btnEasy.setOnClickListener {
                    dismiss()
                    easyListener()
                }
                btnMedium.setOnClickListener {
                    dismiss()
                    mediumListener()
                }
                btnMedium.setOnClickListener {
                    dismiss()
                    hardListener()
                }
            }

            show()
        }
    }
}