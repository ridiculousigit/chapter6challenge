package binar.academy.chapter6challenge.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import binar.academy.chapter6challenge.api.APIService
import binar.academy.chapter6challenge.database.DataStoreUser
import binar.academy.chapter6challenge.database.FavoriteDao
import binar.academy.chapter6challenge.database.FavoriteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val USER_PREFERENCES = "user_preferences"

@InstallIn(SingletonComponent::class)
@Module
object HiltModule {

    @Singleton
    @Provides
    fun apiService(): APIService {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://6356a4db9243cf412f8a1a48.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(APIService::class.java)
    }

    @Provides
    fun getUserManager(@ApplicationContext context: Context): DataStoreUser = DataStoreUser(context)

    @Provides
    fun provideFavoriteDAO(db: FavoriteDatabase): FavoriteDao = db.favoriteDao()

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context): FavoriteDatabase {
        return Room.databaseBuilder(context, FavoriteDatabase::class.java, "favorite.db").build()
    }

}