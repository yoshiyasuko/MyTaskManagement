package com.dashimaki_dofu.mytaskmanagement.repository

import com.dashimaki_dofu.mytaskmanagement.database.TaskSubjectDao
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject


/**
 * TaskSubjectRepository
 *
 * Created by Yoshiyasu on 2024/02/10
 */

interface TaskSubjectRepository {
    fun getAllTaskSubjects(): Flow<List<TaskSubject>>
    fun getTaskSubject(taskId: Int): Flow<TaskSubject>
    suspend fun createTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun createSubTask(subTask: SubTask)
    suspend fun updateSubTask(subTask: SubTask)
    suspend fun deleteSubTask(subTask: SubTask)
}

class TaskSubjectRepositoryImpl @Inject constructor(
    private val taskSubjectDao: TaskSubjectDao
) : TaskSubjectRepository {
    override fun getAllTaskSubjects(): Flow<List<TaskSubject>> {
//        return taskSubjectDao.getAllTaskSubjects()
        return flowOf(makeDummyTaskSubjects())
    }

    override fun getTaskSubject(taskId: Int): Flow<TaskSubject> {
//        return taskSubjectDao.getTaskSubject(taskId)
        return flowOf(makeDummyTaskSubjects().first { it.task.id == taskId })
    }

    override suspend fun createTask(task: Task) {
        taskSubjectDao.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskSubjectDao.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskSubjectDao.deleteTask(task)
    }

    override suspend fun createSubTask(subTask: SubTask) {
        taskSubjectDao.insertSubTask(subTask)
    }

    override suspend fun updateSubTask(subTask: SubTask) {
        taskSubjectDao.updateSubTask(subTask)
    }

    override suspend fun deleteSubTask(subTask: SubTask) {
        taskSubjectDao.deleteSubTask(subTask)
    }
}

class TaskSubjectRepositoryMock : TaskSubjectRepository {
    override fun getAllTaskSubjects(): Flow<List<TaskSubject>> {
        return flowOf(makeDummyTaskSubjects())
    }

    override fun getTaskSubject(taskId: Int): Flow<TaskSubject> {
        return flowOf(makeDummyTaskSubjects().first())
    }

    override suspend fun createTask(task: Task) = Unit
    override suspend fun updateTask(task: Task) = Unit
    override suspend fun deleteTask(task: Task) = Unit
    override suspend fun createSubTask(subTask: SubTask) = Unit
    override suspend fun updateSubTask(subTask: SubTask) = Unit
    override suspend fun deleteSubTask(subTask: SubTask) = Unit
}
