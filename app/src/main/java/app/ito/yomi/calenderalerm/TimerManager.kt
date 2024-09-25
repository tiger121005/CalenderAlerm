package app.ito.yomi.calenderalerm

import android.content.Context
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime

class TimerManager {
    fun setAlarm(context: Context, data: AlarmData) {
        val timeDiff = calculateTimeDifference(data.year, data.month, data.date, data.hour, data.minute)
        val settingAlarmManager = SettingAlarmManager()
        settingAlarmManager.createAlarm(context, data.title, data.hour, data.minute)
//        settingAlarmManager.settingAlarmManager(context, timeDiff, data.id)
    }

    private fun calculateTimeDifference(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val alarmTime = LocalDateTime.of(year, month, day, hour, minute)
        val currentTime = LocalDateTime.now()
        return Duration.between(currentTime, alarmTime).toMillis()
    }
}