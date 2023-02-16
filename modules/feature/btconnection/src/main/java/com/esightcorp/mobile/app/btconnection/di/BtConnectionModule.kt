package com.esightcorp.mobile.app.btconnection.di

import android.content.Context
import com.esightcorp.mobile.app.btconnection.repositories.BtConnectionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ActivityComponent::class)
object BtConnectionModule {

    @Provides
    @ActivityRetainedScoped
    fun provideBluetoothRepository(@ApplicationContext context: Context): BtConnectionRepository{
        return BtConnectionRepository(context)
    }
}