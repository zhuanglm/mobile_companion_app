package com.esightcorp.mobile.app.companion.di

import com.esightcorp.mobile.app.bluetooth.IBleEventListener
import com.esightcorp.mobile.app.companion.repositories.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    /**
     * Prepare this singleton so that both the UI layer and the BLE layer can access to the same instance
     */
    private val appRepository = AppRepository()

    @Provides
    @Singleton
    fun provideAppRepo(): AppRepository = appRepository

    @Provides
    @Singleton
    fun provideBleEventListener(): IBleEventListener = appRepository
}
