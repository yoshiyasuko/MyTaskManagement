package com.dashimaki_dofu.mytaskmanagement.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject
import kotlinx.coroutines.flow.Flow


/**
 * TaskSubjectDao
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@Dao
interface TaskSubjectDao {
    @Transaction
    @Query("select * from tasks")
    fun getAllTaskSubjects(): Flow<List<TaskSubject>>

    @Transaction
    @Query("select * from tasks where id=:taskId limit 1")
    fun getTaskSubject(taskId: Int): Flow<List<TaskSubject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SubTask)

    @Update
    suspend fun updateSubTask(subTask: SubTask)

    @Delete
    suspend fun deleteSubTask(subTask: SubTask)
}
