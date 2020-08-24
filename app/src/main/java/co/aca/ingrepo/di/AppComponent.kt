package co.aca.ingrepo.di

import co.aca.ingrepo.base.App
import co.aca.ingrepo.base.AppConfig
import co.aca.ingrepo.base.AppPages
import co.aca.ingrepo.base.AppViewModels
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    AppModule::class,
    AppPages::class,
    AppViewModels::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun configurations(configs: AppConfig): Builder

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}