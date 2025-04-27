package com.tbacademy.nextstep.domain.usecase.user

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchUsersUseCase {
    suspend operator fun invoke(query: String): Flow<Resource<List<User>>>
}

class SearchUsersUseCaseImpl @Inject constructor(
    private val usersRepository: UserRepository
): SearchUsersUseCase {
    override suspend fun invoke(query: String): Flow<Resource<List<User>>> {
        return usersRepository.searchUsers(query = query)
    }
}