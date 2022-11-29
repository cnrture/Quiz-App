package com.canerture.quizapp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.extension.showFullPagePopup
import com.canerture.quizapp.common.extension.showPopup
import com.canerture.quizapp.databinding.FragmentHomeBinding
import com.canerture.quizapp.delegation.viewBinding
import com.canerture.quizapp.presentation.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            with(homeViewModel) {
                btnPlay.setOnClickListener {
                    setEvent(HomeEvent.PlayClicked)
                }

                state.collect(viewLifecycleOwner) {
                    progressBar.isVisible = it.loadingState
                }

                effect.collect(viewLifecycleOwner) { effect ->
                    when (effect) {
                        HomeUIEffect.GoToCategoryScreen -> {
                            findNavController().navigate(R.id.homeToCategory)
                        }
                        is HomeUIEffect.ShowError ->
                            requireContext().showPopup(R.drawable.ic_error, effect.message) {
                                (activity as MainActivity).finish()
                            }
                        is HomeUIEffect.ShowFullScreenError -> {
                            requireContext().showFullPagePopup(
                                R.drawable.ic_error,
                                effect.message
                            ) {
                                (activity as MainActivity).finish()
                            }
                        }
                    }
                }
            }
        }
    }
}