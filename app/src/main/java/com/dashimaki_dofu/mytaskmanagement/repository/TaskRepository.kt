package com.dashimaki_dofu.mytaskmanagement.repository

import com.dashimaki_dofu.mytaskmanagement.database.TaskDao
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.TaskAndSubTasks


/**
 * TaskRepository
 *
 * Created by Yoshiyasu on 2024/02/10
 */

interface TaskRepository {
    suspend fun getAllTaskAndSubTasks(): List<TaskAndSubTasks>
    suspend fun createTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun createSubTask(subTask: SubTask)
    suspend fun updateSubTask(subTask: SubTask)
    suspend fun deleteSubTask(subTask: SubTask)
}

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun getAllTaskAndSubTasks(): List<TaskAndSubTasks> {
        return taskDao.getAllTaskAndSubTasks()
    }

    override suspend fun createTask(task: Task) {
        taskDao.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    override suspend fun createSubTask(subTask: SubTask) {
        taskDao.insertSubTask(subTask)
    }

    override suspend fun updateSubTask(subTask: SubTask) {
        taskDao.updateSubTask(subTask)
    }

    override suspend fun deleteSubTask(subTask: SubTask) {
        taskDao.deleteSubTask(subTask)
    }
}
