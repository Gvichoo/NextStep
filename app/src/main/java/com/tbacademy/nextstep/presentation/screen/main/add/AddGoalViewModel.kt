package com.tbacademy.nextstep.presentation.screen.main.add

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import com.tbacademy.nextstep.data.worker.UploadGoalWorker
import com.tbacademy.core.model.error.InputValidationResult
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateImageUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDateUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalTitleUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricTargetUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricUnitUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMilestoneUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.extension.getErrorMessageResId
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import com.tbacademy.nextstep.presentation.screen.main.add.effect.AddGoalEffect
import com.tbacademy.nextstep.presentation.screen.main.add.event.AddGoalEvent
import com.tbacademy.nextstep.presentation.screen.main.add.state.AddGoalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val validateTitleUseCase: ValidateAddGoalTitleUseCase,
    private val validateDescriptionUseCase: ValidateAddGoalDescriptionUseCase,
    private val validateDateUseCase: ValidateAddGoalDateUseCase,
    private val validateMetricTargetUseCase: ValidateMetricTargetUseCase,
    private val validateMetricUnitUseCase: ValidateMetricUnitUseCase,
    private val validateMilestoneUseCase: ValidateMilestoneUseCase,
    private val validateImage : ValidateImageUseCase,
    private val application: Application

) : BaseViewModel<AddGoalState, AddGoalEvent, AddGoalEffect>(
    initialState = AddGoalState()
) {


    override fun onEvent(event: AddGoalEvent) {
        when (event) {
            is AddGoalEvent.CreateGoal -> {
                createGoal(
                    title = event.title,
                    description = event.description,
                    targetDate = event.goalDate,
                    metricTarget = event.metricTarget,
                    metricUnit = event.metricUnit,
                    isMetricEnabled = event.isMetricEnabled,
                    imageUri = event.imageUrl,
                    isMilestoneEnable = event.isMilestoneEnabled,
                    milestone = event.milestone
                )
            }

            is AddGoalEvent.GoalDescriptionChanged -> onDescriptionChanged(description = event.description)
            is AddGoalEvent.GoalTitleChanged -> onTitleChanged(title = event.title)

            AddGoalEvent.OnCreateGoalBtnClicked -> viewModelScope.launch { emitEffect(AddGoalEffect.NavToHomeFragment) }
            AddGoalEvent.Submit -> submitAddGoalForm()
            is AddGoalEvent.GoalDateChanged -> onDateChanged(date = event.date)
            is AddGoalEvent.MetricToggle -> updateState { this.copy(isMetricEnabled = event.enabled) }
            is AddGoalEvent.GoalMetricTargetChanged -> onMetricTargetChanged(metricTarget = event.metricTarget)
            is AddGoalEvent.GoalMetricUnitChanged -> onMetricUnitChanged(metricUnit = event.metricUnit)

            is AddGoalEvent.ImageSelected -> updateState { this.copy(imageUri = event.imageUri) }
            AddGoalEvent.PickImageClicked -> viewModelScope.launch { emitEffect(AddGoalEffect.LaunchMediaPicker) }
            AddGoalEvent.ImageCleared -> updateState { this.copy(imageUri = null) }


            is AddGoalEvent.MileStoneToggle -> updateState { this.copy(isMileStoneEnabled = event.enabled) }
            AddGoalEvent.OnAddMilestoneButtonClicked -> onAddMilestone()
            AddGoalEvent.OnMinusMileStoneButtonClicked -> onRemoveMilestone()

            is AddGoalEvent.OnMilestoneTextChanged -> onMilestoneTextChanged(event.id, event.text)
            AddGoalEvent.ResetBlockToNull -> resetBlocked()
            AddGoalEvent.ResetCancelToNull -> resetCancelled()
            AddGoalEvent.ResetFailToNull -> resetFailed()
            AddGoalEvent.ResetSuccessToNull -> resetUploadedSuccessfully()
        }
    }


    private fun writeUserData(goal: Goal) {

        val milestonesJson = Gson().toJson(goal.milestone)

        val data =
            workDataOf(
                "title" to goal.title,
                "description" to goal.description,
                "metricTarget" to goal.metricTarget,
                "metricUnit" to goal.metricUnit,
                "targetDate" to goal.targetDate.toString(),
                "createdAt" to goal.createdAt.toString(),
                "imageUri" to goal.imageUri.toString(),
                "milestone" to milestonesJson
            )
        Log.d("UploadGoalWorker", "Enqueueing with input data: $")

        val worker = OneTimeWorkRequestBuilder<UploadGoalWorker>()
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()


        WorkManager.getInstance(application)
            .enqueueUniqueWork("writeUser", ExistingWorkPolicy.KEEP, worker)


        bindReportUploadWorkObserver()

    }

    private fun bindReportUploadWorkObserver() {
        viewModelScope.launch {
            WorkManager.getInstance(application).getWorkInfosForUniqueWorkFlow("writeUser")
                .collect { workInfoList ->
                    workInfoList.forEach { workInfo ->
                        when(workInfo.state){
                            WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING -> {
                                updateState { copy(isLoading = true) }

                            }
                            WorkInfo.State.SUCCEEDED -> {
                                updateState { copy(isLoading = false) }
                                updateState { copy(uploadedSuccessfully = Unit) }
                                emitEffect(AddGoalEffect.NavToHomeFragment)
                            }
                            WorkInfo.State.FAILED -> {
                                val errorMessage = workInfo.outputData.getString("error_message") ?: "Unknown error"
                                updateState { copy(isLoading = false) }
                                updateState { copy(failedMessage = errorMessage) }
                            }
                            WorkInfo.State.BLOCKED -> {
                                updateState { copy(isLoading = false) }
                                updateState { copy(blocked = Unit) }
                            }
                            WorkInfo.State.CANCELLED -> {
                                updateState { copy(isLoading = false) }
                                updateState { copy(wasCanceled = Unit) }
                            }
                        }
                    }
                }
        }
    }

    private fun createGoal(
        title: String,
        description: String,
        targetDate: Long,
        metricUnit: String,
        metricTarget: String,
        isMetricEnabled: Boolean,
        imageUri: Uri?,
        isMilestoneEnable: Boolean,
        milestone: List<MilestoneItem>
    ) {
        viewModelScope.launch {


            val newGoal = Goal(
                title = title,
                description = description,
                targetDate = targetDate,
                metricUnit = if (isMetricEnabled) metricUnit else null,
                metricTarget = if (isMetricEnabled) metricTarget else null,
                imageUri = imageUri,
                milestone = if (isMilestoneEnable) milestone else null
            )

            writeUserData(newGoal)
            Log.d("CREATE_GOAL", "GOAL: $newGoal")
        }
    }

    private fun resetUploadedSuccessfully() {
        updateState { copy(uploadedSuccessfully = null) }
    }

    private fun resetFailed() {
        updateState { copy(failedMessage = null) }
    }

    private fun resetCancelled() {
        updateState { copy(wasCanceled = null) }
    }

    private fun resetBlocked() {
        updateState { copy(blocked = null) }
    }


    private fun onMilestoneTextChanged(position: Int, text: String) {
        val currentMilestones = state.value.milestones.toMutableList()

        val updatedMilestone = currentMilestones[position].copy(text = text)

        val milestoneError = validateInputOnChange {
            validateMilestoneUseCase(text)
        }?.getErrorMessageResId()

        val milestoneWithError = updatedMilestone.copy(errorMessage = milestoneError)

        currentMilestones[position] = milestoneWithError

        updateState { copy(milestones = currentMilestones) }
    }


    private fun onAddMilestone() {
        updateState {
            val updatedList = milestones + MilestoneItem(id = milestoneIdCounter, text = "")
            copy(
                milestones = updatedList,
                milestoneIdCounter = milestoneIdCounter + 1
            )
        }
    }

    private fun onRemoveMilestone() {
        updateState {
            if (milestones.size > 1) {
                copy(milestones = milestones.dropLast(1))
            } else this
        }
    }


    //On Metric Target Update
    private fun onMetricTargetChanged(metricTarget: String) {
        updateState { this.copy(metricTarget = metricTarget) }

        val metricTargetValidationResult =
            validateInputOnChange { validateMetricTargetUseCase(metricTarget = metricTarget) }
        val metricTargetErrorMessage: Int? = metricTargetValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalMetricTargetErrorMessage = metricTargetErrorMessage) }
    }

    //On Metric Unit Update
    private fun onMetricUnitChanged(metricUnit: String) {
        updateState { this.copy(metricUnit = metricUnit) }

        val metricUnitValidationResult =
            validateInputOnChange { validateMetricUnitUseCase(metricUnit = metricUnit) }
        val metricUnitErrorMessage: Int? = metricUnitValidationResult?.getErrorMessageResId()

        updateState { this.copy(goalMetricUnitErrorMessage = metricUnitErrorMessage) }
    }

    // On Title Update
    private fun onTitleChanged(title: String) {
        updateState { this.copy(title = title) }

        val titleValidationResult = validateInputOnChange { validateTitleUseCase(title = title) }
        val titleErrorMessage: Int? = titleValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalTitleErrorMessage = titleErrorMessage) }
    }

    // On Description Update
    private fun onDescriptionChanged(description: String) {
        updateState { this.copy(description = description) }

        val descriptionValidationResult =
            validateInputOnChange { validateDescriptionUseCase(description = description) }
        val descriptionErrorMessage: Int? = descriptionValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalDescriptionErrorMessage = descriptionErrorMessage) }
    }

    private fun onDateChanged(date: Date) {
        updateState { this.copy(goalDate = date) }

        val dateValidationResult = validateInputOnChange { validateDateUseCase(date = date) }
        val dateErrorMessage: Int? = dateValidationResult?.getErrorMessageResId()
        updateState { this.copy(goalDateErrorMessage = dateErrorMessage) }
    }

    // On Submit
    private fun submitAddGoalForm() {

        val formIsValid = validateForm(
            title = state.value.title,
            description = state.value.description,
            goalDate = state.value.goalDate,
            metricUnit = state.value.metricUnit,
            metricTarget = state.value.metricTarget,
            isMetricEnabled = state.value.isMetricEnabled,
            imageUri = state.value.imageUri,
            isMilestoneEnable = state.value.isMileStoneEnabled,
            milestone = state.value.milestones

        )

        if (formIsValid) {
            state.value.goalDate?.let {
                createGoal(
                    title = state.value.title,
                    description = state.value.description,
                    targetDate = it.time,
                    metricUnit = state.value.metricUnit,
                    metricTarget = state.value.metricTarget,
                    isMetricEnabled = state.value.isMetricEnabled,
                    imageUri = state.value.imageUri,
                    isMilestoneEnable = state.value.isMileStoneEnabled,
                    milestone = state.value.milestones
                )
            }
        } else {
            updateState { this.copy(formBeenSubmitted = true) }
        }
        Log.d("SUBMIT_FORM", "Validation failed, checking errors...")
        // Optionally, log errors for each field
    }


    private fun validateForm(
        title: String,
        description: String,
        goalDate: Date?,
        metricUnit: String,
        metricTarget: String,
        isMetricEnabled: Boolean,
        imageUri: Uri?,
        isMilestoneEnable: Boolean,
        milestone: List<MilestoneItem>


    ): Boolean {

        // Validate Inputs
        val titleValidationError = validateTitleUseCase(title = title).getErrorMessageResId()
        val descriptionValidationForm =
            validateDescriptionUseCase(description = description).getErrorMessageResId()
        val dateValidationError = validateDateUseCase(date = goalDate).getErrorMessageResId()

        var metricUnitError: Int? = null
        var metricTargetError: Int? = null

        if (isMetricEnabled) {
            metricUnitError =
                validateMetricUnitUseCase(metricUnit = metricUnit).getErrorMessageResId()
            metricTargetError =
                validateMetricTargetUseCase(metricTarget = metricTarget).getErrorMessageResId()

        }

        val imageValidationError = validateImage(imageUri?.toString()).getErrorMessageResId()
        // Update states of errors
        updateState {
            copy(
                goalTitleErrorMessage = titleValidationError,
                goalDescriptionErrorMessage = descriptionValidationForm,
                goalDateErrorMessage = dateValidationError,
                goalMetricUnitErrorMessage = metricUnitError,
                goalMetricTargetErrorMessage = metricTargetError,
            )
        }
        updateState {
            copy(
                goalImageErrorMessage = imageValidationError
            )
        }
        if (isMilestoneEnable) {
            val updatedMilestones = milestone.map {
                val error = validateMilestoneUseCase(it.text).getErrorMessageResId()
                it.copy(errorMessage = error)
            }

            updateState { copy(milestones = updatedMilestones) }

            if (updatedMilestones.any { it.errorMessage != null }) {
                return false
            }
        }


        // Check if form if valid
        val errors: List<Int?> = listOf(
            titleValidationError,
            descriptionValidationForm,
            dateValidationError,
            if (isMetricEnabled) metricUnitError else null,
            if (isMetricEnabled) metricTargetError else null,
            imageValidationError
        )

        return errors.all { it == null }
    }


    // Helpers
    private fun validateInputOnChange(validator: () -> InputValidationResult): InputValidationResult? {
        return if (state.value.formBeenSubmitted)
            validator()
        else null
    }
}