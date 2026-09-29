package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AdConfigEntity
import com.example.data.model.SavedVideoEntity
import com.example.data.model.UserEntity
import com.example.data.model.VideoEntity
import com.example.data.model.WatchHistoryEntity

@Database(
    entities = [
        VideoEntity::class,
        UserEntity::class,
        AdConfigEntity::class,
        WatchHistoryEntity::class,
        SavedVideoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VairalDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun userDao(): UserDao
    abstract fun adConfigDao(): AdConfigDao
    abstract fun userActivityDao(): UserActivityDao

    companion object {
        @Volatile
        private var INSTANCE: VairalDatabase? = null

        fun getDatabase(context: Context): VairalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VairalDatabase::class.java,
                    "vairal6t9_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
