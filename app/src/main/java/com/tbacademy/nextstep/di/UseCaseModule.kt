package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.domain.usecase.UpdateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.UpdateGoalUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.auth.GetAuthUserIdUseCase
import com.tbacademy.nextstep.domain.usecase.auth.GetAuthUserIdUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.comment.CreateCommentUseCase
import com.tbacademy.nextstep.domain.usecase.comment.CreateCommentUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.comment.GetCommentsUseCase
import com.tbacademy.nextstep.domain.usecase.comment.GetCommentsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.goal_follow.CreateGoalFollowUseCase
import com.tbacademy.nextstep.domain.usecase.goal_follow.CreateGoalFollowUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.goal_follow.DeleteGoalFollowUseCase
import com.tbacademy.nextstep.domain.usecase.goal_follow.DeleteGoalFollowUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCase
import com.tbacademy.nextstep.domain.usecase.goal.CreateGoalUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.goal.GetMilestoneUseCase
import com.tbacademy.nextstep.domain.usecase.goal.GetMilestoneUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.goal.GetUserGoalsUseCase
import com.tbacademy.nextstep.domain.usecase.goal.GetUserGoalsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.notification.GetUserNotificationsUseCase
import com.tbacademy.nextstep.domain.usecase.notification.GetUserNotificationsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.notification.ListenForUnreadNotificationsUseCase
import com.tbacademy.nextstep.domain.usecase.notification.ListenForUnreadNotificationsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.notification.MarkAllNotificationsAsReadUseCase
import com.tbacademy.nextstep.domain.usecase.notification.MarkAllNotificationsAsReadUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.post.CreateMilestonePostUseCase
import com.tbacademy.nextstep.domain.usecase.post.CreateMilestonePostUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.post.GetFollowedPostsUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetFollowedPostsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.post.GetGoalPostsUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetGoalPostsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.reaction.CreateReactionUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.CreateReactionUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.reaction.DeleteReactionUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.DeleteReactionUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.reaction.UpdateReactionUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.UpdateReactionUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.user.GetUserInfoUseCase
import com.tbacademy.nextstep.domain.usecase.user.GetUserInfoUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.user.SearchUsersUseCase
import com.tbacademy.nextstep.domain.usecase.user.SearchUsersUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.user.UpdateUserImageUseCase
import com.tbacademy.nextstep.domain.usecase.user.UpdateUserImageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.ClearValueFromLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.ReadValueFromLocalStorageUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.userSession.SaveValueToLocalStorageUseCase
import com.tbacademy.nextstep.domain.usecase.userSession.SaveValueToLocalStorageUseCaseImpl

import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ImageValidator
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ImageValidatorImpl

import com.tbacademy.nextstep.domain.usecase.user_follow.CreateUserFollowUseCase
import com.tbacademy.nextstep.domain.usecase.user_follow.CreateUserFollowUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.user_follow.DeleteUserFollowUseCase
import com.tbacademy.nextstep.domain.usecase.user_follow.DeleteUserFollowUseCaseImpl

import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDateUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDateUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalDescriptionUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateEmailUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateEmailUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateNecessaryFieldUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateNecessaryFieldUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidatePasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidatePasswordUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateRepeatedPasswordUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateRepeatedPasswordUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateUsernameUseCase
import com.tbacademy.nextstep.domain.usecase.validation.authorization.ValidateUsernameUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalTitleUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateAddGoalTitleUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricTargetUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricTargetUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricUnitUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMetricUnitUseCaseImpl
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMilestoneUseCase
import com.tbacademy.nextstep.domain.usecase.validation.addGoal.ValidateMilestoneUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Singleton
    @Binds
    fun bindValidateEmailUseCase(validateEmailUseCase: ValidateEmailUseCaseImpl): ValidateEmailUseCase

    @Singleton
    @Binds
    fun bindValidatePasswordUseCase(validatePasswordUseCase: ValidatePasswordUseCaseImpl): ValidatePasswordUseCase

    @Singleton
    @Binds
    fun bindValidateRepeatedPasswordUseCase(validateRepeatedPasswordUseCase: ValidateRepeatedPasswordUseCaseImpl): ValidateRepeatedPasswordUseCase

    @Singleton
    @Binds
    fun bindValidateUsernameUseCase(validateUsernameUseCase: ValidateUsernameUseCaseImpl): ValidateUsernameUseCase

    @Singleton
    @Binds
    fun bindNecessaryFieldUseCase(validateNecessaryFieldUseCase: ValidateNecessaryFieldUseCaseImpl): ValidateNecessaryFieldUseCase

    @Singleton
    @Binds
    fun bindGetValueFromLocalStorageUseCase(impl: ReadValueFromLocalStorageUseCaseImpl): ReadValueFromLocalStorageUseCase

    @Singleton
    @Binds
    fun bindSaveValueToLocalStorageUseCase(impl: SaveValueToLocalStorageUseCaseImpl): SaveValueToLocalStorageUseCase

    @Singleton
    @Binds
    fun bindClearValueFromLocalStorageUseCase(impl: ClearValueFromLocalStorageUseCaseImpl): ClearValueFromLocalStorageUseCase

    @Singleton
    @Binds
    fun bindCreateGoalUseCase(impl: CreateGoalUseCaseImpl): CreateGoalUseCase

    @Singleton
    @Binds
    fun bindGetUserGoalsUseCase(impl: GetUserGoalsUseCaseImpl): GetUserGoalsUseCase

    @Singleton
    @Binds
    fun bindValidateAddGoalTitleUseCase(impl: ValidateAddGoalTitleUseCaseImpl): ValidateAddGoalTitleUseCase

    @Singleton
    @Binds
    fun bindValidateAddGoalDescriptionUseCase(impl: ValidateAddGoalDescriptionUseCaseImpl): ValidateAddGoalDescriptionUseCase

    @Singleton
    @Binds
    fun bindValidateAddGoalDateUseCase(impl: ValidateAddGoalDateUseCaseImpl): ValidateAddGoalDateUseCase

    @Singleton
    @Binds
    fun bindGetPostsUseCase(impl: GetPostsUseCaseImpl): GetPostsUseCase

    @Singleton
    @Binds
    fun bindGetGoalPostsUseCase(impl: GetGoalPostsUseCaseImpl): GetGoalPostsUseCase

    @Singleton
    @Binds
    fun bindGetFollowedPostsUseCase(impl: GetFollowedPostsUseCaseImpl): GetFollowedPostsUseCase

    @Singleton
    @Binds
    fun bindValidateMetricTargetUseCase(impl: ValidateMetricTargetUseCaseImpl): ValidateMetricTargetUseCase

    @Singleton
    @Binds
    fun bindValidateMetricUnitUseCase(impl: ValidateMetricUnitUseCaseImpl): ValidateMetricUnitUseCase

    @Singleton
    @Binds
    fun bindCreateReactionUseCale(impl: CreateReactionUseCaseImpl): CreateReactionUseCase

    @Singleton
    @Binds
    fun bindValidateMilestoneUseCase(impl: ValidateMilestoneUseCaseImpl): ValidateMilestoneUseCase

    @Singleton
    @Binds
    fun bindDeleteReactionUseCase(impl: DeleteReactionUseCaseImpl): DeleteReactionUseCase

    @Singleton
    @Binds
    fun bindUpdateReactionUseCase(impl: UpdateReactionUseCaseImpl): UpdateReactionUseCase

    @Singleton
    @Binds
    fun bindCreateCommentUseCase(impl: CreateCommentUseCaseImpl): CreateCommentUseCase

    @Singleton
    @Binds
    fun bindGetCommentsUseCase(impl: GetCommentsUseCaseImpl): GetCommentsUseCase

    @Singleton
    @Binds
    fun bindGetUserInfoUseCase(impl: GetUserInfoUseCaseImpl): GetUserInfoUseCase

    @Singleton
    @Binds
    fun bindSearchUsersUseCase(impl: SearchUsersUseCaseImpl): SearchUsersUseCase

    @Singleton
    @Binds
    fun bindUpdateUserImageUseCase(impl: UpdateUserImageUseCaseImpl): UpdateUserImageUseCase

    @Singleton
    @Binds
    fun binGetAuthUserIdUseCase(impl: GetAuthUserIdUseCaseImpl): GetAuthUserIdUseCase

    @Singleton
    @Binds
    fun bindCreateGoalFollowUseCase(impl: CreateGoalFollowUseCaseImpl): CreateGoalFollowUseCase

    @Singleton
    @Binds
    fun bindDeleteGoalFollowUseCase(impl: DeleteGoalFollowUseCaseImpl): DeleteGoalFollowUseCase

    @Singleton
    @Binds
    fun bindCreateUserFollowUseCase(impl: CreateUserFollowUseCaseImpl): CreateUserFollowUseCase

    @Singleton
    @Binds
    fun bindDeleteUserFollowUseCase(impl: DeleteUserFollowUseCaseImpl): DeleteUserFollowUseCase

//    @Singleton
//    @Binds
//    fun checkIsUserFollowedUseCase(impl: CheckIsUerFollowedUseCaseImpl): CheckIsUserFollowedUseCase

    @Singleton
    @Binds
    fun bindImageValidator(impl: ImageValidatorImpl): ImageValidator

    @Singleton
    @Binds
    fun bindGetMilestoneUseCase(impl: GetMilestoneUseCaseImpl): GetMilestoneUseCase

    @Singleton
    @Binds
    fun bindUpdateGoalUseCase(impl: UpdateGoalUseCaseImpl): UpdateGoalUseCase

    @Singleton
    @Binds
    fun bindGetUserNotificationsUseCase(impl: GetUserNotificationsUseCaseImpl): GetUserNotificationsUseCase

    @Singleton
    @Binds
    fun bindListenForUnreadNotificationsUseCase(impl: ListenForUnreadNotificationsUseCaseImpl): ListenForUnreadNotificationsUseCase

    @Singleton
    @Binds
    fun bindMarkAllNotificationsAsReadUseCase(impl: MarkAllNotificationsAsReadUseCaseImpl): MarkAllNotificationsAsReadUseCase
  
    @Singleton
    @Binds
    fun bindCreateMilestonePostUseCase(impl: CreateMilestonePostUseCaseImpl): CreateMilestonePostUseCase
}