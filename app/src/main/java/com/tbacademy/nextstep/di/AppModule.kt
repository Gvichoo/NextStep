package com.tbacademy.nextstep.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireBaseStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideHandleResponse(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        authManager: AuthManager
    ): HandleResponse {
        return HandleResponse(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
            authManager = authManager
        )
    }
}