package com.dk.safepackage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dk.safepackage.dao.PackageDao
import com.dk.safepackage.entities.PackageEntity

@Database(entities = [PackageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun packageDao(): PackageDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "SafePackageDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
