package com.alorma.rac.di

import com.alorma.rac.listening.ListeningViewModel
import com.alorma.rac.programs.ProgramViewModel
import com.alorma.rac.programs.ProgramsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ListeningViewModel() }
    viewModel { ProgramsViewModel(get()) }
    viewModel { (programId: String) -> ProgramViewModel(programId, get()) }
}