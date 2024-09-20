package app.ito.yomi.calenderalerm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_data")
data class AlarmData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "year")
    var year: Int,

    @ColumnInfo(name = "month")
    var month: Int,

    @ColumnInfo(name = "day")
    var day: Int,

    @ColumnInfo(name = "week")
    var week: Int,

    @ColumnInfo(name = "hour")
    var hour: Int,

    @ColumnInfo(name = "minute")
    var minute: Int
)
