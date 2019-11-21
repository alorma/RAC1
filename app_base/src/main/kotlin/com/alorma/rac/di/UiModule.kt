package com.alorma.rac.di

import androidx.appcompat.app.AppCompatActivity
import com.alorma.rac.core.AppThemeSwitcher
import com.alorma.rac.listening.ListeningViewModel
import com.alorma.rac.now.NowViewModel
import com.alorma.rac.programs.ProgramViewModel
import com.alorma.rac.programs.ProgramsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ListeningViewModel() }
    viewModel { NowViewModel(get()) }
    viewModel { ProgramsViewModel(get()) }
    viewModel { ProgramViewModel() }
    factory { (activity: AppCompatActivity) -> AppThemeSwitcher(activity, get()) }
}