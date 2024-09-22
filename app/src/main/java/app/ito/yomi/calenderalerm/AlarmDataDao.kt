package app.ito.yomi.calenderalerm

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface AlarmDataDao {
    @Insert
    fun insert(alarm: AlarmData)

    @Update()
    fun update(alarm: AlarmData)

    @Query("select * from alarm_data")
    fun getAll(): LiveData<List<AlarmData>>

    @Query("select * from alarm_data where year = :year and month = :month and date = :date order by hour, minute")
    fun getAlarmByDate(year: Int, month: Int, date: Int): LiveData<List<AlarmData>>

    @Query("delete from alarm_data where id = :id")
    fun deleteAlarmById(id: Int)

}