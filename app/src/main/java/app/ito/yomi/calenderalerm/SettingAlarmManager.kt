package app.ito.yomi.calenderalerm

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log


class SettingAlarmManager : Application(){

    fun settingAlarmManager(
        context: Context,
        timeDiff: Long,
        id: Int,
    ) {
//            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager




        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            context.startActivity(intent)
        }

        val intent = Intent(context, AlarmManagerReceiver::class.java)
        intent.putExtra("id", id)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        try {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeDiff, pendingIntent)
            } else {
                Log.e("SettingAlarmManager", "Cannot schedule exact alarms")
            }
        } catch (e: SecurityException) {
            Log.e("SettingAlarmManager", "SecurityException: ${e.message}")
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeDiff, pendingIntent)
    }

}