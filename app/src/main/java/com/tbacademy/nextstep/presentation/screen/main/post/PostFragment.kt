package com.tbacademy.nextstep.presentation.screen.main.post

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentPostBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.extension.showSnackbar
import com.tbacademy.nextstep.presentation.screen.main.home.extension.topReactions
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation
import com.tbacademy.nextstep.presentation.screen.main.post.effect.PostEffect
import com.tbacademy.nextstep.presentation.screen.main.post.event.PostEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFragment : BaseFragment<FragmentPostBinding>(FragmentPostBinding::inflate) {

    private val safeArgs: PostFragmentArgs by navArgs()
    private val postViewModel: PostViewModel by viewModels()

    override fun start() {
        postViewModel.onEvent(event = PostEvent.GetPost(postId = safeArgs.postId))
    }

    override fun listeners() {

    }

    override fun observers() {
        observeState()
        observeEffect()
    }

    private fun observeState() {
        collectLatest(flow = postViewModel.state) { state ->
            binding.apply {
                pbPost.isVisible = state.isLoading
            }

            binding.layoutPost.apply {
                clPost.isVisible = state.post != null

                if (state.post != null) {
                    setPost(post = state.post)
                }
            }
        }
    }

    private fun observeEffect() {
        collect(flow = postViewModel.effects) { effect ->
            when(effect) {
                is PostEffect.ShowErrorMessage -> binding.root.showSnackbar(messageRes = effect.errorMessageRes)
            }
        }
    }

    private fun setPost(post: PostPresentation) {
        binding.layoutPost.apply {
            tvTitle.text = post.title
            tvDescription.text = post.title
            post.imageUrl?.let { ivPostImage.loadImagesGlide(url = it) }
            tvCommentsCount.text = post.commentCount.toString()
            tvReactionsCount.text = post.reactionCount.toString()
            tvDate.text = post.createdAt

            // Post Reactions
            val topReactions = post.topReactions()

            // Top 3 Reactions
            ivReaction1.setImageResource(
                topReactions.getOrNull(0)?.first?.iconRes ?: R.drawable.ic_reaction_fire_24px
            )
            ivReaction1.setBackgroundResource(
                topReactions.getOrNull(0)?.first?.backgroundRes
                    ?: R.drawable.bg_reaction_fire
            )

            ivReaction2.isVisible = topReactions.size > 1
            ivReaction2.setImageResource(topReactions.getOrNull(1)?.first?.iconRes ?: 0)
            ivReaction2.setBackgroundResource(topReactions.getOrNull(1)?.first?.backgroundRes ?: 0)

            ivReaction3.isVisible = topReactions.size > 2
            ivReaction3.setImageResource(topReactions.getOrNull(2)?.first?.iconRes ?: 0)
            ivReaction3.setBackgroundResource(topReactions.getOrNull(2)?.first?.backgroundRes ?: 0)
        }
    }
}