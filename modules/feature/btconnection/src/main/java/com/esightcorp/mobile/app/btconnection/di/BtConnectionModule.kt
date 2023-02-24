package com.esightcorp.mobile.app.btconnection.di

import android.content.Context
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object BtConnectionModule {

    @Provides
    @Singleton
    fun provideBluetoothRepository(@ApplicationContext context: Context): BtConnectionRepository{
        return BtConnectionRepository(context)
    }
}