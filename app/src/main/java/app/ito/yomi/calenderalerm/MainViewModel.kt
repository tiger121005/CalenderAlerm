package app.ito.yomi.calenderalerm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: AlarmDataDao
    init{
        dao = AlarmDatabase.getDatabase(application).alarmDataDao()
    }

    // DBに保管された内容を表示
    val alarmAllData = dao.getAll()

    fun insert(data: AlarmData) {
        Log.d("insert data", data.toString())
        Log.d("insert data", data.title)
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(data)
        }
    }

    // DBにデータを保存
    fun update(data: AlarmData, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateData = AlarmData(
                id = id,
                title = data.title,
                year = data.year,
                month = data.month,
                date = data.date,
                week = data.week,
                hour = data.hour,
                minute = data.minute
            )
            Log.d("update data", updateData.toString())
            dao.update(updateData)
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAlarmById(id)
        }
    }
}