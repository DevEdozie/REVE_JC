package com.edozie.reve_jc.di

import com.edozie.reve_jc.remote.AuthRepository
import com.edozie.reve_jc.remote.FirebaseAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepo(repo: FirebaseAuthRepository): AuthRepository
}