package app.ito.yomi.calenderalerm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun alarmDataDao(): AlarmDataDao

    companion object {
        lateinit var instance: AppDatabase

        fun getInstance(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "database"
                ).allowMainThreadQueries().build()
                return instance
            }

        }
    }
}