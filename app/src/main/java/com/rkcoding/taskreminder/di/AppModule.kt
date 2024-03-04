package com.rkcoding.taskreminder.di

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.rkcoding.taskreminder.todo_features.data.repository.AlarmSchedulerImpl
import com.rkcoding.taskreminder.todo_features.data.repository.AuthRepositoryImpl
import com.rkcoding.taskreminder.todo_features.data.repository.FirebaseTaskRepositoryImpl
import com.rkcoding.taskreminder.todo_features.domain.repository.AlarmScheduler
import com.rkcoding.taskreminder.todo_features.domain.repository.AuthRepository
import com.rkcoding.taskreminder.todo_features.domain.repository.FirebaseTaskRepository
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
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideFireStoreInstance(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseRepository(
         firebaseAuth: FirebaseAuth,
         fireStore: FirebaseFirestore
    ): FirebaseTaskRepository{
        return FirebaseTaskRepositoryImpl(firebaseAuth, fireStore)
    }

    @Provides
    @Singleton
    fun providesAlarmScheduler(application: Application): AlarmScheduler{
        return AlarmSchedulerImpl(application)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository{
        return AuthRepositoryImpl(firebaseAuth)
    }

}