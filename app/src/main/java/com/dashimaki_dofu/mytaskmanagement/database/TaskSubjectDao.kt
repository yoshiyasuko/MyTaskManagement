package com.dashimaki_dofu.mytaskmanagement.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dashimaki_dofu.mytaskmanagement.model.SubTask
import com.dashimaki_dofu.mytaskmanagement.model.Task
import com.dashimaki_dofu.mytaskmanagement.model.TaskSubject


/**
 * TaskSubjectDao
 *
 * Created by Yoshiyasu on 2024/02/10
 */

@Dao
interface TaskSubjectDao {
    @Transaction
    @Query("select * from tasks")
    suspend fun getAllTaskSubjects(): List<TaskSubject>

    @Transaction
    @Query("select * from tasks where id=:taskId limit 1")
    suspend fun getTaskSubject(taskId: Int): List<TaskSubject>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task): Int

    @Query("select count(*) from tasks where id=:taskId")
    suspend fun countTask(taskId: Int): Int

    @Transaction
    suspend fun insertOrUpdateTask(task: Task): Int {
        return if (countTask(task.id) > 0) {
            // MEMO: @Updateの戻り値はidではなく「レコードが存在した行数」のため注意
            //  そのため明示的にtask.idを戻している
            updateTask(task)
            task.id
        } else {
            insertTask(task).toInt()
        }
    }

    @Transaction
    @Query("delete from tasks where id=:taskId")
    suspend fun deleteTask(taskId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubTask(subTask: SubTask): Long

    @Update
    suspend fun updateSubTask(subTask: SubTask): Int

    @Query("delete from subTasks where id=:subTaskId")
    suspend fun deleteSubTask(subTaskId: Int): Int
}
