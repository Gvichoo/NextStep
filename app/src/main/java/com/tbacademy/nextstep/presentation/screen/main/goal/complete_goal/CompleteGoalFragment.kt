package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentCompleteGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.extension.showSnackbar
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.effect.CompleteGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal.event.CompleteGoalEvent
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CompleteGoalFragment :
    BaseFragment<FragmentCompleteGoalBinding>(FragmentCompleteGoalBinding::inflate) {

    private val completeGoalViewModel: CompleteGoalViewModel by viewModels()

    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var cameraImageUri: Uri? = null

    private val args: CompleteGoalFragmentArgs by navArgs()

    override fun start() {
        initCameraLauncher()
        initMediaPickerLauncher()
        setGoalInfo()
    }

    override fun listeners() {
        setBackBtnListener()
        setSelectImageListener()
        setSubmitBtnListener()
        setDescriptionInputListener()
    }

    override fun observers() {
        observeEffect()
        observeState()
    }

    private fun observeState() {
        collectLatest(flow = completeGoalViewModel.state) { state ->
            binding.apply {
                loaderMilestone.loaderContainer.isVisible = state.isLoading

                tvGoalTitle.text = state.goalTitle

                tlDescription.error = state.descriptionErrorMessage?.let { getString(it) }

                tvImageError.isVisible = state.imageErrorMessage != null
                state.imageErrorMessage?.let {
                    tvImageError.text = getString(it)
                }

                state.imageUri?.let { uri ->
                    ivImagePreview.loadImagesGlide(uri = uri)
                }
                btnSubmit.isEnabled = state.isSubmitEnabled
            }
        }
    }

    private fun observeEffect() {
        collect(flow = completeGoalViewModel.effects) { effect ->
            when (effect) {
                is CompleteGoalEffect.NavigateBack -> findNavController().navigateUp()
                is CompleteGoalEffect.ShowErrorMessage -> binding.root.showSnackbar(messageRes = effect.errorRes)
                is CompleteGoalEffect.LaunchMediaPicker -> launchImagePicker()
                is CompleteGoalEffect.LaunchCameraPicker -> checkCameraPermissionAndLaunch()
                is CompleteGoalEffect.ShowUpdateImageDialog -> showImagePickerDialog()
                is CompleteGoalEffect.NavigateToGoalCompleted -> navigateToGoalCompleted()
            }
        }
    }

    private fun setGoalInfo() {
        completeGoalViewModel.onEvent(
            event = CompleteGoalEvent.SetGoalInfo(
                goalId = args.goalId,
                goalTitle = args.goalTitle
            )
        )
    }

    private fun setBackBtnListener() {
        binding.btnBack.setOnClickListener {
            completeGoalViewModel.onEvent(event = CompleteGoalEvent.Return)
        }
    }

    private fun setSubmitBtnListener() {
        binding.btnSubmit.setOnClickListener {
            completeGoalViewModel.onEvent(event = CompleteGoalEvent.Submit(goalId = args.goalId))
        }
    }

    private fun setDescriptionInputListener() {
        binding.etDescription.onTextChanged { description ->
            completeGoalViewModel.onEvent(CompleteGoalEvent.DescriptionChanged(description = description))
        }
    }

    private fun navigateToGoalCompleted() {
        val action = CompleteGoalFragmentDirections.actionCompleteGoalFragmentToGoalCompletedFragment()
        findNavController().navigate(action)
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
                    completeGoalViewModel.onEvent(CompleteGoalEvent.ImageSelected(imageUri = uri))
                }
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
                        completeGoalViewModel.onEvent(CompleteGoalEvent.ImageSelected(imageUri = uri))
                    }
                }
            }
    }

    private fun openCamera() {
        val imageFile = File(
            requireContext().filesDir,
            "uploads/camera_${System.currentTimeMillis()}.jpg"
        ).apply { parentFile?.mkdirs() }

        cameraImageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
        cameraImageUri?.let { uri ->
            cameraLauncher.launch(uri)
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        permissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun launchImagePicker() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setSelectImageListener() {
        binding.btnSelectImage.setOnClickListener {
            completeGoalViewModel.onEvent(event = CompleteGoalEvent.PickImage)
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf(getString(R.string.camera), getString(R.string.gallery))
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_image))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> completeGoalViewModel.onEvent(CompleteGoalEvent.CameraSelected)
                    1 -> completeGoalViewModel.onEvent(CompleteGoalEvent.GallerySelected)
                }
            }.show()
    }

}