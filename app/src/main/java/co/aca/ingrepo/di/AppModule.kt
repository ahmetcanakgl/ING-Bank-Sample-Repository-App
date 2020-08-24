package co.aca.ingrepo.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import co.aca.ingrepo.base.App
import co.aca.ingrepo.repository.ApiService
import co.aca.ingrepo.repository.Repository
import co.aca.ingrepo.repository.UserRepository
import co.aca.ingrepo.util.GmtDateTypeAdapter
import co.aca.ingrepo.util.Strings
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun providesRepository(retrofit: Retrofit): Repository {
        return Repository(retrofit.create(ApiService::class.java))
    }
    @Provides
    @Singleton
    fun providesUserCacheRepository(gson: Gson): UserRepository {
        return UserRepository(gson)
    }

    @Provides
    @Singleton
    fun providesStrings(app: App): Strings {
        return Strings(app.applicationContext)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return GsonBuilder().registerTypeAdapter(Date::class.java, GmtDateTypeAdapter()).create()
    }

}