package app.ito.yomi.calenderalerm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AlarmData::class], version = 2, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun alarmDataDao(): AlarmDataDao
    companion object {
        @Volatile
        private var instance: AlarmDatabase? = null

        fun getDatabase(context: Context): AlarmDatabase{
            return instance ?: synchronized(this){
                Room.databaseBuilder(context, AlarmDatabase::class.java, "database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}