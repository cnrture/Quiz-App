package com.canerture.quizapp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.showFullPagePopup
import com.canerture.quizapp.common.showPopup
import com.canerture.quizapp.common.viewBinding
import com.canerture.quizapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlay.setOnClickListener {
            homeViewModel.setEvent(HomeEvent.PlayClicked)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            homeViewModel.state.collect {
                binding.progressBar.isVisible = it.loadingState
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            homeViewModel.effect.collect { effect ->
                when (effect) {
                    HomeUIEffect.GoToCategoryScreen -> {
                        findNavController().navigate(R.id.homeToCategory)
                    }
                    is HomeUIEffect.ShowError -> requireContext().showPopup(
                        iconId = R.drawable.ic_error,
                        title = effect.message,
                        dismissListener = {
                        }
                    )
                    is HomeUIEffect.ShowFullScreenError -> {
                        requireContext().showFullPagePopup(
                            iconId = R.drawable.ic_error,
                            title = effect.message,
                            dismissListener = {
                                homeViewModel.setEvent(HomeEvent.SendTokenRequest)
                            }
                        )
                    }
                }
            }
        }
    }
}