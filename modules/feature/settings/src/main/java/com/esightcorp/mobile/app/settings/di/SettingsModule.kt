package com.esightcorp.mobile.app.settings.di

import android.content.Context
import com.esightcorp.mobile.app.settings.repositories.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SettingsModule {

    @ViewModelScoped
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context) =
        SettingsRepository(context)

}
