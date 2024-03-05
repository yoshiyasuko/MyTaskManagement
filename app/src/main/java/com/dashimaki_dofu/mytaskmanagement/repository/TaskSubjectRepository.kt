package com.dashimaki_dofu.mytaskmanagement.repository

import com.dashimaki_dofu.mytaskmanagement.database.TaskSubjectDao
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import com.dashimaki_dofu.mytaskmanagement.model.makeDummyTaskSubjects
import javax.inject.Inject


/**
 * TaskSubjectRepository
 *
 * Created by Yoshiyasu on 2024/02/10
 */

//region Interface
interface TaskSubjectRepository {
    suspend fun getAllTaskSubjects(): List<TaskSubject>
    suspend fun getTaskSubject(taskId: Int): TaskSubject
    suspend fun saveTask(task: Task): Int
    suspend fun deleteTask(taskId: Int): Int
    suspend fun saveSubTask(subTask: SubTask): Int
    suspend fun updateSubTask(subTask: SubTask): Int
    suspend fun deleteSubTask(subTaskId: Int): Int
}
//endregion

//region Impl
class TaskSubjectRepositoryImpl @Inject constructor(
    private val taskSubjectDao: TaskSubjectDao
) : TaskSubjectRepository {
    override suspend fun getAllTaskSubjects(): List<TaskSubject> {
        return taskSubjectDao.getAllTaskSubjects()
    }

    override suspend fun getTaskSubject(taskId: Int): TaskSubject {
        return taskSubjectDao.getTaskSubject(taskId).firstOrNull() ?: TaskSubject.initialize()
    }

    override suspend fun saveTask(task: Task): Int {
        return taskSubjectDao.insertOrUpdateTask(task)
    }

    override suspend fun deleteTask(taskId: Int): Int {
        return taskSubjectDao.deleteTask(taskId)
    }

    override suspend fun saveSubTask(subTask: SubTask): Int {
        return taskSubjectDao.insertSubTask(subTask).toInt()
    }

    override suspend fun updateSubTask(subTask: SubTask): Int {
        return taskSubjectDao.updateSubTask(subTask)
    }

    override suspend fun deleteSubTask(subTaskId: Int): Int {
        return taskSubjectDao.deleteSubTask(subTaskId)
    }
}
//endregion

//region Mock
class TaskSubjectRepositoryMock : TaskSubjectRepository {
    override suspend fun getAllTaskSubjects(): List<TaskSubject> {
        return makeDummyTaskSubjects()
    }

    override suspend fun getTaskSubject(taskId: Int): TaskSubject {
        return makeDummyTaskSubjects().firstOrNull() ?: TaskSubject.initialize()
    }

    override suspend fun saveTask(task: Task): Int = -1
    override suspend fun deleteTask(taskId: Int) = -1
    override suspend fun saveSubTask(subTask: SubTask) = -1
    override suspend fun updateSubTask(subTask: SubTask) = -1
    override suspend fun deleteSubTask(subTaskId: Int) = -1
}
//endregion
