package com.esightcorp.mobile.app.eshare.di

import android.content.Context
import com.esightcorp.mobile.app.bluetooth.IBleEventListener
import com.esightcorp.mobile.app.eshare.repositories.EshareRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EshareModule {

    @Provides
    @Singleton
    fun provideEshareRepository(
        @ApplicationContext context: Context,
        bleEventListener: IBleEventListener,
    ) = EshareRepository(context, bleEventListener)
}
