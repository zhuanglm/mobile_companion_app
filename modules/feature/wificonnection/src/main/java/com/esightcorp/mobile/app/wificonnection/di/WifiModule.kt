package com.esightcorp.mobile.app.wificonnection.di

import android.content.Context
import com.esightcorp.mobile.app.wificonnection.repositories.WifiConnectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ActivityComponent::class)
object WifiModule {
    @ViewModelScoped
    @Provides
    fun provideWifiRepository(@ApplicationContext context: Context):WifiConnectionRepository{
        return WifiConnectionRepository(context)
    }
}