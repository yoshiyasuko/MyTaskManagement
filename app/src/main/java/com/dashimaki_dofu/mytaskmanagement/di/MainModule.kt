package com.dashimaki_dofu.mytaskmanagement.di

import android.content.Context
import androidx.room.Room
import com.dashimaki_dofu.mytaskmanagement.database.TaskDatabase
import com.dashimaki_dofu.mytaskmanagement.database.TaskSubjectDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * MainModule
 *
 * Created by Yoshiyasu on 2024/02/14
 */

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(
        @ApplicationContext context: Context
    ): TaskDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = TaskDatabase::class.java,
                TaskDatabase.DATABASE_NAME
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskSubjectDao(
        db: TaskDatabase
    ): TaskSubjectDao {
        return db.taskSubjectDao()
    }
}
