package com.tbacademy.nextstep.presentation.screen.main.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.databinding.FragmentAddGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.add.adapter.MilestoneAdapter
import com.tbacademy.nextstep.presentation.screen.main.add.effect.AddGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.add.event.AddGoalEvent
import com.tbacademy.nextstep.presentation.screen.main.add.state.AddGoalState
import com.tbacademy.nextstep.presentation.screen.main.add.state.AddGoalUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class AddGoalFragment : BaseFragment<FragmentAddGoalBinding>(FragmentAddGoalBinding::inflate) {

    private val addGoalViewModel: AddGoalViewModel by viewModels()

    private lateinit var pickMediaLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var cameraImageUri: Uri? = null

    private val myAdapter: MilestoneAdapter by lazy {
        MilestoneAdapter { position, newText ->
            addGoalViewModel.onEvent(AddGoalEvent.OnMilestoneTextChanged(position, newText))
        }
    }


    @SuppressLint("DefaultLocale")
    override fun start() {
        initMediaPickerLauncher()
        setDatePicker()
        initCameraLauncher()
        setUpRecycler()
    }


    override fun listeners() {
        setInputListeners()
        setSubmitBtnListener()
        setSwitchListener()
        setDeleteImageButtonListener()
        setSelectImageButtonListener()
        setAddMilestoneButtonClickListener()
        setMinusMilestoneButtonClickListener()
        setMilestoneSwitchListener()
    }

    override fun observers() {

        observeState()
        observeEffects()
        observeUiState()
        bindWorkerResultObserver()

    }


    private fun observeState() {
        collect(flow = addGoalViewModel.state) { state ->
            binding.apply {
                loaderAddGoal.loaderContainer.isVisible = state.isLoading
                setUIElementsEnabled(!state.isLoading)

                tlGoalTitle.error = state.goalTitleErrorMessage?.let { getString(it) }
                tlGoalDescription.error = state.goalDescriptionErrorMessage?.let { getString(it) }
                tlTargetDate.error = state.goalDateErrorMessage?.let { getString(it) }
                tlMetricUnit.error = state.goalMetricUnitErrorMessage?.let { getString(it) }
                tlMetricTarget.error = state.goalMetricTargetErrorMessage?.let { getString(it) }


                btnCreateGoal.isEnabled = state.isCreateGoalEnabled

            }
        }
    }

    private fun observeUiState() {
        collect(flow = addGoalViewModel.uiState) { uiState ->
            binding.apply {
                metricInputContainer.isVisible = uiState.isMetricEnabled

                recycler.isVisible = uiState.isMileStoneEnabled
                btnForAddAndMinusMileStoneEts.isVisible = uiState.isMileStoneEnabled
                myAdapter.submitList(uiState.milestones)

                uiState.imageUri?.let { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .into(image)
                }


//                tlImage.error = uiState.goalImageErrorMessage?.let { getString(it) }
                tlImage.error = if (uiState.imageUri != null) null else uiState.goalImageErrorMessage?.let { getString(it) }
            }
        }
    }

    private fun observeEffects() {
        collectLatest(flow = addGoalViewModel.effects) { effects ->
            when (effects) {
                AddGoalEffect.NavToHomeFragment -> navToHomeFragment()
                is AddGoalEffect.ShowError -> showMessage(effects.message)
                AddGoalEffect.LaunchMediaPicker -> launchImagePicker()
            }
        }
    }


    //WorkManager
    private fun bindWorkerResultObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addGoalViewModel.uiState.collect {
                    handleWorkerStatusState(it)
                }
            }
        }
    }

    private fun handleWorkerStatusState(state: AddGoalUiState) {

        state.failedMessage?.let {
            addGoalViewModel.onEvent(AddGoalEvent.ResetFailToNull)
        }

        state.blocked?.let {
            addGoalViewModel.onEvent(AddGoalEvent.ResetBlockToNull)
        }

        state.wasCanceled?.let {
            addGoalViewModel.onEvent(AddGoalEvent.ResetCancelToNull)
        }

        state.uploadedSuccessfully?.let {
            addGoalViewModel.onEvent(AddGoalEvent.ResetSuccessToNull)
        }
    }


    //Gallery and Camera


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
                        addGoalViewModel.onEvent(AddGoalEvent.ImageSelected(uri)) // append uri to list
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
                    1 -> addGoalViewModel.onEvent(AddGoalEvent.PickImageClicked)
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
                    addGoalViewModel.onEvent(AddGoalEvent.ImageSelected(it))
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
            addGoalViewModel.onEvent(AddGoalEvent.ImageCleared)
            binding.btnCancelImage.isEnabled = false
        }
    }



    private fun launchImagePicker() {
        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private fun setUpRecycler() {
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = myAdapter
    }


    private fun setInputListeners() {
        setTitleInputListener()
        setDescriptionInputListener()
        setDateInputListener()
        setMetricTargetInputListener()
        setMetricUnitInputListener()
    }


    private fun setMilestoneSwitchListener() {
        binding.switchMileStones.setOnCheckedChangeListener { _, isChecked ->
            addGoalViewModel.onEvent(AddGoalEvent.MileStoneToggle(isChecked))
        }
    }


    private fun setMetricTargetInputListener() {
        binding.etMetricTarget.onTextChanged { metricTarget ->
            addGoalViewModel.onEvent(AddGoalEvent.GoalMetricTargetChanged(metricTarget = metricTarget))
        }
    }


    private fun setMetricUnitInputListener() {
        binding.etMetricUnit.onTextChanged { metricUnit ->
            addGoalViewModel.onEvent(AddGoalEvent.GoalMetricUnitChanged(metricUnit = metricUnit))
        }
    }


    private fun setTitleInputListener() {
        binding.etGoalTitle.onTextChanged { title ->
            addGoalViewModel.onEvent(AddGoalEvent.GoalTitleChanged(title = title))
        }
    }

    private fun setDescriptionInputListener() {
        binding.etGoalDescription.onTextChanged { description ->
            addGoalViewModel.onEvent(AddGoalEvent.GoalDescriptionChanged(description = description))
        }
    }

    private fun setDateInputListener() {
        binding.etTargetDate.onTextChanged { dateString ->
            val parsedDate = parseDate(dateString)
            if (parsedDate != null) {
                addGoalViewModel.onEvent(AddGoalEvent.GoalDateChanged(date = parsedDate))
            } else {
                Log.e("AddGoalFragment", "Invalid date format: $dateString")
            }
        }
    }


    private fun parseDate(dateString: String): Date? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    private fun setAddMilestoneButtonClickListener() {
        binding.addMileStoneEditText.setOnClickListener {
            addGoalViewModel.onEvent(AddGoalEvent.OnAddMilestoneButtonClicked)
        }
    }

    private fun setMinusMilestoneButtonClickListener() {
        binding.minusMileStoneEditText.setOnClickListener {
            addGoalViewModel.onEvent(AddGoalEvent.OnMinusMileStoneButtonClicked)
        }
    }

    private fun setSubmitBtnListener() {
        binding.btnCreateGoal.setOnClickListener {
            addGoalViewModel.onEvent(AddGoalEvent.Submit)
        }
    }

    private fun setSwitchListener() {
        binding.switchMetricBased.setOnCheckedChangeListener { _, isChecked ->
            addGoalViewModel.onEvent(AddGoalEvent.MetricToggle(isChecked))
        }
    }


    private fun navToHomeFragment() {
        findNavController().navigate(
            AddGoalFragmentDirections.actionNavAddToNavHome()
        )
    }


    @SuppressLint("DefaultLocale")
    private fun setDatePicker() {
        binding.etTargetDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                    binding.etTargetDate.setText(formattedDate)

                    calendar.set(year, month, dayOfMonth, 0, 0, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    val date = calendar.time

                    addGoalViewModel.onEvent(AddGoalEvent.GoalDateChanged(date))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePicker.datePicker.minDate = System.currentTimeMillis() + 24 * 60 * 60 * 1000

            datePicker.show()
        }
    }

    private fun setUIElementsEnabled(isEnabled: Boolean) {
        binding.apply {
            etGoalTitle.isEnabled = isEnabled
            etGoalDescription.isEnabled = isEnabled
            etTargetDate.isEnabled = isEnabled
            etMetricTarget.isEnabled = isEnabled
            etMetricUnit.isEnabled = isEnabled
            switchMetricBased.isEnabled = isEnabled
            switchMileStones.isEnabled = isEnabled
            btnSelectImage.isEnabled = isEnabled
            btnCancelImage.isEnabled = isEnabled
        }
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }
}