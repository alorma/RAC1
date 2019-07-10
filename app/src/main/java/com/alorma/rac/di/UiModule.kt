package com.alorma.rac.di

import com.alorma.rac.listening.ListeningViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ListeningViewModel() }
}