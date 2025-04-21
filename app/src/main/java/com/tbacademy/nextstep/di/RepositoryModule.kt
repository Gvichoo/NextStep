package com.tbacademy.nextstep.di

import com.tbacademy.nextstep.data.repository.auth.AuthRepositoryImpl
import com.tbacademy.nextstep.data.repository.comment.CommentRepositoryImpl
import com.tbacademy.nextstep.data.repository.follow.GoalFollowRepositoryImpl
import com.tbacademy.nextstep.data.repository.goal.GoalRepositoryImpl
import com.tbacademy.nextstep.data.repository.login.LoginRepositoryImpl
import com.tbacademy.nextstep.data.repository.post.PostRepositoryImpl
import com.tbacademy.nextstep.data.repository.reaction.ReactionRepositoryImpl
import com.tbacademy.nextstep.data.repository.register.RegisterRepositoryImpl
import com.tbacademy.nextstep.data.repository.user.UserRepositoryImpl
import com.tbacademy.nextstep.data.repository.userSession.UserSessionManagerRepositoryImpl
import com.tbacademy.nextstep.domain.repository.auth.AuthRepository
import com.tbacademy.nextstep.domain.repository.comment.CommentRepository
import com.tbacademy.nextstep.domain.repository.follow.GoalFollowRepository
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import com.tbacademy.nextstep.domain.repository.register.RegisterRepository
import com.tbacademy.nextstep.domain.repository.user.UserRepository
import com.tbacademy.nextstep.domain.repository.userSession.UserSessionManagerRepository
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindFollowRepository(impl: GoalFollowRepositoryImpl): GoalFollowRepository
}