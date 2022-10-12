package com.esightcorp.mobile.app.home.di

import android.content.Context
import com.esightcorp.mobile.app.home.repositories.HomeRepository
import com.esightcorp.mobile.app.home.repositories.IHomeRepository
import com.esightcorp.mobile.app.home.viewmodels.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object HomeModule {
    @ViewModelScoped
    @Provides
    fun provideHomeRepository(@ApplicationContext context: Context): HomeRepository{
        return HomeRepository()
    }
}