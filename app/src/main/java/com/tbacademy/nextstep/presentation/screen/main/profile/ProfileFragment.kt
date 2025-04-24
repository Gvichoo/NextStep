package com.tbacademy.nextstep.presentation.screen.main.profile

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentProfileBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.screen.main.add.event.AddGoalEvent
import com.tbacademy.nextstep.presentation.screen.main.profile.effect.ProfileEffect
import com.tbacademy.nextstep.presentation.screen.main.profile.event.ProfileEvent
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val goalAdapter by lazy {
        GoalAdapter()
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var cameraImageUri: Uri? = null

    override fun start() {
        checkProfileOwner()
        setGoalAdapter()
        initCameraLauncher()
        initMediaPickerLauncher()
    }

    override fun listeners() {
        setOnBackBtnListener()
        setFollowBtnListener()
        setUpdateImageListener()
    }

    override fun observers() {
        observeState()
        observeEffect()
    }

    private fun observeEffect() {
        collectLatest(flow = profileViewModel.effects) { effect ->
            when (effect) {
                is ProfileEffect.NavigateBack -> findNavController().navigateUp()
                is ProfileEffect.ShowErrorMessage -> {}
                is ProfileEffect.ShowUpdateImageDialog -> showImagePickerDialog()
                is ProfileEffect.LaunchCameraPicker -> checkCameraPermissionAndLaunch()
                is ProfileEffect.LaunchMediaPicker -> launchImagePicker()
            }
        }
    }

    private fun observeState() {
        collectLatest(flow = profileViewModel.state) { state ->
            binding.apply {
                pbProfile.isVisible = state.isLoading
                pbUploadImage.isVisible = state.isImageLoading
                pbGoals.isVisible = state.goalsLoading

                groupProfileContent.isVisible = !state.isLoading

                if (state.user != null) {
                    goalAdapter.submitList(state.userGoals)

                    state.user.profilePictureUrl?.let {
                        ivProfile.loadImagesGlide(url = it)
                    }

                    ivProfile.isInvisible = state.isImageLoading

                    tvUsername.text = state.user.username
                    btnFollow.isVisible = !state.user.isOwnUser
                    btnUpdateImage.isVisible = state.user.isOwnUser
                    btnBack.isVisible = !state.withBottomNav

                    if (!state.user.isUserFollowed) {
                        btnFollow.text = requireContext().getString(R.string.follow)
                    } else {
                        btnFollow.text = requireContext().getString(R.string.followed)
                    }
                }
            }
            Log.d("PROFILE_STATE", "$state")
        }
    }

    private fun setOnBackBtnListener() {
        binding.btnBack.setOnClickListener {
            profileViewModel.onEvent(ProfileEvent.BackRequest)
        }
    }

    private fun setFollowBtnListener() {
        binding.btnFollow.setOnClickListener {
            profileViewModel.onEvent(ProfileEvent.ToggleFollowUser)
        }
    }

    private fun checkProfileOwner() {
        val usedId = arguments?.getString("userId")
        profileViewModel.onEvent(ProfileEvent.SetProfileState(userId = usedId))
    }

    private fun setUpdateImageListener() {
        binding.btnUpdateImage.setOnClickListener {
            profileViewModel.onEvent(event = ProfileEvent.UpdateImage)
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
                    profileViewModel.onEvent(ProfileEvent.ImageSelected(imageUri = uri))
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
                        profileViewModel.onEvent(ProfileEvent.ImageSelected(imageUri = uri))
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

    private fun setGoalAdapter() {
        binding.rvGoals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGoals.adapter = goalAdapter
    }

    private fun showImagePickerDialog() {
        val options = arrayOf(getString(R.string.camera), getString(R.string.gallery))
        android.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_image))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> profileViewModel.onEvent(ProfileEvent.CameraSelected)
                    1 -> profileViewModel.onEvent(ProfileEvent.GallerySelected)
                }
            }.show()
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }
}