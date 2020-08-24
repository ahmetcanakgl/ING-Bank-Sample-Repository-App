package co.aca.ingrepo.base

import android.app.Activity
import androidx.multidex.MultiDexApplication
import co.aca.ingrepo.BuildConfig
import co.aca.ingrepo.base.AppConfig
import co.aca.ingrepo.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class App : MultiDexApplication(), HasActivityInjector {

    companion object {
        private var instance: App? = null

        fun getInstance(): App? {
            return instance
        }
    }

    @Inject
    lateinit var actInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        instance = this


        val appConfig = object : AppConfig() {
            override fun apiUrl(): String {
                return BuildConfig.SERVICE
            }
        }

        val appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .configurations(appConfig)
                .build()

        appComponent.inject(this)

    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return actInjector
    }

}