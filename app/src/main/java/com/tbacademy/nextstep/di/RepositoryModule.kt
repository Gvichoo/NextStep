package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.data.repository.auth.AuthManagerImpl
import com.tbacademy.nextstep.data.repository.comment.CommentRepositoryImpl
import com.tbacademy.nextstep.data.repository.goal_follow.GoalFollowRepositoryImpl
import com.tbacademy.nextstep.data.repository.goal.GoalRepositoryImpl
import com.tbacademy.nextstep.data.repository.login.LoginRepositoryImpl
import com.tbacademy.nextstep.data.repository.post.PostRepositoryImpl
import com.tbacademy.nextstep.data.repository.reaction.ReactionRepositoryImpl
import com.tbacademy.nextstep.data.repository.register.RegisterRepositoryImpl
import com.tbacademy.nextstep.data.repository.user.UserRepositoryImpl
import com.tbacademy.nextstep.data.repository.userSession.UserSessionManagerRepositoryImpl
import com.tbacademy.nextstep.data.repository.user_follow.UserFollowRepositoryImpl
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import com.tbacademy.nextstep.domain.repository.comment.CommentRepository
import com.tbacademy.nextstep.domain.repository.goal_follow.GoalFollowRepository
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import com.tbacademy.nextstep.domain.repository.user.UserRepository
import com.tbacademy.nextstep.domain.repository.userSession.UserSessionManagerRepository
import com.tbacademy.nextstep.domain.repository.user_follow.UserFollowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(impl: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(impl: RegisterRepositoryImpl): RegisterRepository

    @Binds
    @Singleton
    abstract fun bindUserSessionManagerRepository(impl: UserSessionManagerRepositoryImpl): UserSessionManagerRepository

    @Binds
    @Singleton
    abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository

    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindReactionRepository(impl: ReactionRepositoryImpl): ReactionRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(impl: CommentRepositoryImpl): CommentRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthManagerImpl): AuthManager

    @Binds
    @Singleton
    abstract fun bindGoalFollowRepository(impl: GoalFollowRepositoryImpl): GoalFollowRepository

    @Binds
    @Singleton
    abstract fun bindUserFollowRepository(impl: UserFollowRepositoryImpl): UserFollowRepository
}