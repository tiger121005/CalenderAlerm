package app.ito.yomi.calenderalerm

class Actions {
    fun formatTime(hour: Int, minute: Int): String {
        val hourStr = if (hour < 10) "0$hour" else hour.toString()
        val minuteStr = if (minute < 10) "0$minute" else minute.toString()
        return "$hourStr:$minuteStr"
    }
}