package com.example.materiallesson.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import coil.load
import com.example.materiallesson.R
import com.example.materiallesson.databinding.FragmentPictureNasaBinding
import com.example.materiallesson.domain.NasaRepositoryImpl
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect

const val WIKI = "https://ru.wikipedia.org/wiki/"


class NasaPictureFragment : Fragment(R.layout.fragment_picture_nasa) {

    companion object {
        fun newInstance() = NasaPictureFragment()
    }

    private val viewModel: NasaViewModel by viewModels {
        NasaViewModelFactory(NasaRepositoryImpl())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.requestPicture()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentPictureNasaBinding.bind(view)
        val fab = binding.fab
        val behavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(binding.includedView.bottomSheet)

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.loading.collect {
                binding.progress.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.image.collect { url ->
                url?.let {
                    binding.img.load(it)
                }
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
            viewModel.titleImg.collect { titleText ->
                binding.titleImg.text = titleText
            }
        }

        fab.setOnClickListener {
            with(binding) {
                includedView.web.webViewClient = WebViewClient()
                includedView.web.loadUrl(WIKI + inputEditText.text)
            }
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior.isHideable = false
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0)
                    .start();
            }

        })
    }
}

