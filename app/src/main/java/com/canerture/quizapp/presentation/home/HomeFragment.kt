package com.canerture.quizapp.presentation.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.canerture.quizapp.R
import com.canerture.quizapp.common.extension.collect
import com.canerture.quizapp.common.extension.gone
import com.canerture.quizapp.common.extension.showErrorPopup
import com.canerture.quizapp.common.extension.visible
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

        collectState()
        collectEffect()

        binding.btnPlay.setOnClickListener {
            homeViewModel.setEvent(HomeEvent.PlayClicked)
        }
    }

    private fun collectState() = with(binding) {
        homeViewModel.state.collect(viewLifecycleOwner) { state ->
            when (state) {
                HomeUIState.Loading -> progressBar.visible()
                HomeUIState.TokenSuccess -> progressBar.gone()
            }
        }
    }

    private fun collectEffect() = homeViewModel.effect.collect(viewLifecycleOwner) { effect ->
        when (effect) {
            HomeUIEffect.GoToCategoryScreen -> findNavController().navigate(R.id.homeToCategory)
            is HomeUIEffect.ShowError -> {
                binding.progressBar.gone()
                requireContext().showErrorPopup(R.drawable.ic_error, effect.message) {
                    (activity as MainActivity).finish()
                }
            }
        }
    }
}