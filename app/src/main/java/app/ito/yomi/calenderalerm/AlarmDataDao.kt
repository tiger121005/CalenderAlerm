package app.ito.yomi.calenderalerm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AlarmDataDao {
    @Insert
    fun insert(alerm: AlarmData)

    @Query("select * from alarm_data")
    fun getAll(): List<AlarmData>

    @Query("select * from alarm_data where year = :year and month = :month and day = :day order by hour, minute")
    fun getAlarmByDate(year: Int, month: Int, day: Int): List<AlarmData>

    @Query("select * from alarm_data where id = :id")
    fun deleteAlarmById(id: Int): AlarmData


}