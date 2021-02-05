package com.example.mtmstask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mtmstask.adapters.LocationsAdapter
import com.example.mtmstask.viewmodels.HomeActivityVM
import com.example.mtmstask.viewmodels.HomeActivityVMFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class VIewmodelModule {

    @Binds
    @IntoMap
    @HomeActivityVMFactory.ViewModelKey(HomeActivityVM::class)
    internal abstract fun bindScoreViewModel(homeActivityVM: HomeActivityVM): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: HomeActivityVMFactory): ViewModelProvider.Factory


}