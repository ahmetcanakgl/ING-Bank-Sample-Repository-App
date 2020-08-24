package co.aca.ingrepo.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.aca.ingrepo.di.ViewModelFactory
import co.aca.ingrepo.di.ViewModelKey
import co.aca.ingrepo.ui.detail.FRRepoViewModel
import co.aca.ingrepo.ui.main.ACRepoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppViewModels {


    @Binds
    @IntoMap
    @ViewModelKey(ACRepoListViewModel::class)
    abstract fun bindACRepoListViewModel(repoViewModel: ACRepoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FRRepoViewModel::class)
    abstract fun bindFRRepoViewModel(repoViewModel: FRRepoViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}