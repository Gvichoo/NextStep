package com.tbacademy.nextstep.presentation.screen.main.post

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.tbacademy.nextstep.databinding.FragmentPostBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
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
        TODO("Not yet implemented")
    }

    override fun observers() {
        TODO("Not yet implemented")
    }
}