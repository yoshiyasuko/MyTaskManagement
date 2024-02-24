package com.dashimaki_dofu.mytaskmanagement.di

import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepository
import com.dashimaki_dofu.mytaskmanagement.repository.TaskSubjectRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * RepositoryModule
 *
 * Created by Yoshiyasu on 2024/02/14
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindToTaskSubjectRepository(
        impl: TaskSubjectRepositoryImpl
    ): TaskSubjectRepository
}
