package com.tbacademy.nextstep.presentation.screen.main.home.comment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.databinding.FragmentCommentsSheetBinding
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.home.comment.effect.CommentsEffect
import com.tbacademy.nextstep.presentation.screen.main.home.comment.event.CommentsEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.google.android.material.R as MaterialR

@AndroidEntryPoint
class CommentsSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentsSheetBinding? = null
    private val binding: FragmentCommentsSheetBinding get() = _binding!!

    private val commentsViewModel: CommentsViewModel by viewModels()

    private val commentsAdapter: CommentsAdapter by lazy {
        CommentsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsSheetBinding.inflate(inflater, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        // Find the root layout and EditText
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBottomSheetExpandedState()
        setCommentsAdapter()
        listeners()
        observers()
        handleReceivedParams()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return dialog
    }


    private fun handleReceivedParams() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val postId = requireArguments().getString(ARG_POST_ID) ?: return@repeatOnLifecycle
                val shouldTypingStart = requireArguments().getBoolean(ARG_TYPE_ACTIVE)

                commentsViewModel.onEvent(CommentsEvent.UpdatePostId(postId))

                if (shouldTypingStart) {
                    commentsViewModel.onEvent(CommentsEvent.StartTyping)
                }
            }
        }
    }

    private fun setBottomSheetExpandedState() {

        val bottomSheet = dialog?.findViewById<FrameLayout>(MaterialR.id.design_bottom_sheet)
        if (bottomSheet != null) {
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.isFitToContents = false // <<< Don't restrict height to content
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            val layoutParams = bottomSheet.layoutParams
            layoutParams.height = resources.displayMetrics.heightPixels
            bottomSheet.layoutParams = layoutParams
        }

        val behavior = BottomSheetBehavior.from(view?.parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    private fun listeners() {
        setCommentInputListener()
        setSendBtnListener()
    }

    private fun observers() {
        observeState()
        observeUiState()
        observeEffects()
    }


    private fun observeUiState() {
        collectLatest(flow = commentsViewModel.uiState) { state ->
            binding.btnSend.isVisible = state.isSendEnabled
        }
    }

    private fun observeState() {
        collectLatest(flow = commentsViewModel.state) { state ->
            Log.d("COMMENTS_STATE", "$state")
            binding.apply {
                ivNoComments.isVisible = state.comments != null && state.comments.isEmpty()
                tvNoComments.isVisible = state.comments != null && state.comments.isEmpty()
                pgComments.isVisible = state.fetchLoading
                btnSend.isVisible = !state.uploadLoading
                pbSend.isVisible = state.uploadLoading

                if (state.comments != null) {
                    commentsAdapter.submitList(state.comments) {
                        binding.rvComments.scrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun observeEffects() {
        collect(flow = commentsViewModel.effects) { effect ->
            Log.d("EFFECT_RECEIVED", "FINALLY")
            when (effect) {
                is CommentsEffect.StartTyping -> {
                    binding.etComment.requestFocus()
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(binding.etComment, InputMethodManager.SHOW_IMPLICIT)
                }

                is CommentsEffect.ShowError -> {
                    showMessage(message = effect.error.toMessageRes())
                }

                is CommentsEffect.CommentCreated -> {
                    handleCommentUpdated()
                }
            }
        }
    }

    private fun handleCommentUpdated() {
        // Clear input field
        binding.etComment.setText("")
        // Hide keyboard
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etComment.windowToken, 0)
    }

    private fun setSendBtnListener() {
        binding.btnSend.setOnClickListener {
            commentsViewModel.onEvent(CommentsEvent.CreateComment)
        }
    }

    private fun setCommentsAdapter() {
        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.adapter = commentsAdapter
    }

    private fun setCommentInputListener() {
        binding.etComment.onTextChanged { comment ->
            commentsViewModel.onEvent(CommentsEvent.CommentChanged(comment = comment))
        }
    }

    companion object {
        const val ARG_POST_ID = "postId"
        const val ARG_TYPE_ACTIVE = "typeActive"
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}