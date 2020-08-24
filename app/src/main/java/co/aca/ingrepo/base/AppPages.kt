package co.aca.ingrepo.base

import co.aca.ingrepo.di.ActivityScoped
import co.aca.ingrepo.di.FragmentScoped
import co.aca.ingrepo.ui.detail.FRRepo
import co.aca.ingrepo.ui.main.ACRepoList
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector

@Module(includes = [AndroidInjectionModule::class])
abstract class AppPages {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract fun bindACRepoList(): ACRepoList

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun bindFRRepo(): FRRepo

}