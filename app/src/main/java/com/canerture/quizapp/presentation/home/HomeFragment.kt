package com.canerture.quizapp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.showFullPagePopup
import com.canerture.quizapp.common.showPopup
import com.canerture.quizapp.common.viewBinding
import com.canerture.quizapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlay.setOnClickListener {
            findNavController().navigate(R.id.homeToCategory)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.state.collect { homeState ->
                    binding.progressBar.isVisible = homeState.loadingState
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.effect.collect { effect ->
                    when (effect) {
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
}