package com.bogdan801.rememberit.di

import android.content.Context
import androidx.room.Room
import com.bogdan801.rememberit.BaseApplication
import com.bogdan801.rememberit.data.localdb.Database
import com.bogdan801.rememberit.data.repository.RepositoryImpl
import com.bogdan801.rememberit.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication{
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, Database::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDao(db :Database) = db.dbDao

    @Provides
    @Singleton
    fun provideNoteRepository(db: Database): Repository {
        return RepositoryImpl(db.dbDao)
    }
}