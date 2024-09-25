package app.ito.yomi.calenderalerm

import android.util.Log
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter

class CalenderManager {

    fun daysInMonthArray(date: LocalDate):ArrayList<Long> {
        val daysInMonthArray = ArrayList<Long>()
        // 年と月を取得
        val yearMonth = YearMonth.from(date)
        // 月の日数を取得
        val daysInMonth = yearMonth.lengthOfMonth()
        // 月初めの日付を取得
        val firstOfMonth = date.withDayOfMonth(1)
        // 月初めの曜日を取得
        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(0)
            } else {
                daysInMonthArray.add((i - dayOfWeek).toLong())
            }
        }
        if (daysInMonthArray[6] == 0.toLong()) {
            for (i in 0..6) {
                daysInMonthArray.removeAt(0)
            }
        } else if (daysInMonthArray[35] == 0.toLong()) {
            for (i in 0..6) {
                daysInMonthArray.removeAt(daysInMonthArray.size - 1)
            }
        }
        return daysInMonthArray
    }

    fun month(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("M月")
        return date!!.format(formatter)
    }
    fun year(date: LocalDate?): String {
        val formatter = DateTimeFormatter.ofPattern("/yyyy")
        return date!!.format(formatter)
    }

    fun getData(dates: ArrayList<Long>, selectedDate: LocalDate, alarmData: MutableList<AlarmData>): ArrayList<AlarmData> {
        val dataList = ArrayList<AlarmData>()

        Log.d("size before", dates.size.toString())
        for (i in 0..dates.size - 1) {

            var id: Int = -1
            var title = ""
            var hour = 100
            var minute = 100
            val data = filteredAlarmData(selectedDate.year, selectedDate.monthValue, dates[i].toInt(), alarmData)
            if (data.size != 0) {
                id = data[0].id
                title = data[0].title
                hour = data[0].hour
                minute = data[0].minute
            }

            val addData = AlarmData(
                id = id,
                title = title,
                year = selectedDate.year,
                month = selectedDate.monthValue,
                date = dates[i].toInt(),
                week = selectedDate.dayOfWeek.value,
                hour = hour,
                minute = minute
            )
            dataList.add(addData)

        }
        Log.d("dataList", dataList.toString())
        Log.d("dataListSize", dataList.size.toString())
//        alarmData = dataList
        return dataList
    }

    private fun filteredAlarmData(year: Int, month: Int, date: Int, alarmData: MutableList<AlarmData>): List<AlarmData> {
        val data = alarmData.filter { it.year == year && it.month == month && it.date == date }

        return data
    }

    fun formatTime(hour: Int, minute: Int): String {
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minuteStr = if (minute < 10) "0$minute" else minute.toString()
        return "$hourStr:$minuteStr"
    }

    fun formatDate(year: Int, month: Int, date: Int): String {
        val monthStr = if (month < 10) "0$month" else month.toString()
        val dateStr = if (date < 10) "0$date" else date.toString()
        return "$year/$monthStr/$dateStr"
    }
}