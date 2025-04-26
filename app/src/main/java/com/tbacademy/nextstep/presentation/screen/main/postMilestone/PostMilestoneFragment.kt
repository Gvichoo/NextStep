package com.tbacademy.nextstep.presentation.screen.main.postMilestone

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentPostMilestoneBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.milestone.MilestoneFragmentDirections
import com.tbacademy.nextstep.presentation.screen.main.postMilestone.effect.PostMilestoneEffect
import com.tbacademy.nextstep.presentation.screen.main.postMilestone.event.PostMilestoneEvent
import com.tbacademy.nextstep.presentation.screen.main.user_search.event.UserSearchEvent
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PostMilestoneFragment : BaseFragment<FragmentPostMilestoneBinding>(FragmentPostMilestoneBinding::inflate) {

    private val postMilestoneViwModel: PostMilestoneViewModel by viewModels()

    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var cameraImageUri: Uri? = null

    private val args: PostMilestoneFragmentArgs by navArgs()

    override fun start() {
        val title = args.text
        postMilestoneViwModel.onEvent(PostMilestoneEvent.SetTitle(title = title))
        binding.tvMilestoneTitle.text = args.text
        initMediaPickerLauncher()
        initCameraLauncher()
    }

    override fun listeners() {
        setInputListeners()
        setDeleteImageButtonListener()
        setSelectImageButtonListener()
        setSubmitBtnListener()
        setBackBtnListener()
    }
    private fun setSubmitBtnListener() {
        binding.btnPost.setOnClickListener {
            postMilestoneViwModel.onEvent(PostMilestoneEvent.Submit(goalId = args.goalId))

        }
    }

    override fun observers() {
        observeState()
        observeEffects()
    }

    private fun observeState() {
        collect(flow = postMilestoneViwModel.state) { state ->
            binding.apply {
                loaderMilestone.loaderContainer.isVisible = state.isLoading

//                tlMilestoneTitle.error = state.titleErrorMessage?.let { getString(it) }
                tlMilestoneDescription.error = state.descriptionErrorMessage?.let { getString(it) }

                btnPost.isEnabled = state.isPostButtonEnable

                state.imageUri?.let { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(image)
                }


                tlImage.error = if (state.imageUri != null) null else state.imageErrorMessage?.let { getString(it) }
            }
        }
    }

    private fun observeEffects() {
        collectLatest(flow = postMilestoneViwModel.effects) { effects ->
            when (effects) {
                PostMilestoneEffect.LaunchMediaPicker -> launchImagePicker()
                PostMilestoneEffect.NavigateToPosts -> navigateToPosts()
                PostMilestoneEffect.NavigateBack -> findNavController().navigateUp()
            }
        }
    }

    private fun setBackBtnListener() {
        binding.btnBack.setOnClickListener {
            postMilestoneViwModel.onEvent(PostMilestoneEvent.NavigateBack)
        }
    }

    private fun navigateToPosts(){
        val action = PostMilestoneFragmentDirections.actionPostMilestoneFragmentToMainFragment()
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun setInputListeners() {
        setDescriptionInputListener()
    }

    private fun setDescriptionInputListener() {
        binding.etMilestoneDescription.onTextChanged { description ->
            postMilestoneViwModel.onEvent(PostMilestoneEvent.DescriptionChanged(description = description))
        }
    }

    private fun initCameraLauncher() {
        // Permission launcher
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                openCamera()
            }
        }
        // Camera launcher
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    cameraImageUri?.let { uri ->
                        binding.image.setImageURI(uri)
                        postMilestoneViwModel.onEvent(PostMilestoneEvent.ImageSelected(uri)) // append uri to list
                        binding.btnCancelImage.isEnabled = true
                    }
                }
            }
    }

    private fun setSelectImageButtonListener() {
        binding.btnSelectImage.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Camera", "Gallery")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndLaunch()
                    1 -> postMilestoneViwModel.onEvent(PostMilestoneEvent.PickImageClicked)
                }
            }.show()
    }


    private fun checkCameraPermissionAndLaunch() {
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun openCamera() {
        // Use internal storage instead of cache
        val imageFile = File(
            requireContext().filesDir,  // Changed from externalCacheDir
            "uploads/camera_${System.currentTimeMillis()}.jpg"
        ).apply { parentFile?.mkdirs() }  // Create directories if needed

        cameraImageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
        cameraImageUri?.let { uri ->
            cameraLauncher.launch(uri)
        }
    }

    // Add this to delete temporary camera files when done
    private fun deleteCameraFile() {
        cameraImageUri?.path?.let { path ->
            File(path).delete()
        }
    }

    private fun initMediaPickerLauncher() {
        pickMediaLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    // Add this critical permission line
                    requireContext().contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    postMilestoneViwModel.onEvent(PostMilestoneEvent.ImageSelected(it))
                    binding.image.setImageURI(it)
                    binding.btnCancelImage.isEnabled = true
                }
            }
    }

    private fun setDeleteImageButtonListener() {
        binding.btnCancelImage.setOnClickListener {
            // Clear both UI and backend
            binding.image.setImageDrawable(null)
            deleteCameraFile()  // Add this
            postMilestoneViwModel.onEvent(PostMilestoneEvent.ImageCleared)
            binding.btnCancelImage.isEnabled = false
        }
    }

    private fun launchImagePicker() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

}