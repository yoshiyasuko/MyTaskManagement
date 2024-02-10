package com.dashimaki_dofu.mytaskmanagement

import android.app.Application
import androidx.room.Room
import com.dashimaki_dofu.mytaskmanagement.database.TaskDatabase


/**
 * RoomApplication
 *
 * Created by Yoshiyasu on 2024/02/10
 */

class RoomApplication: Application() {
    companion object {
        lateinit var database: TaskDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room
            .databaseBuilder(
                context = applicationContext,
                klass = TaskDatabase::class.java,
                TaskDatabase.DATABASE_NAME
            )
            .build()
    }
}
