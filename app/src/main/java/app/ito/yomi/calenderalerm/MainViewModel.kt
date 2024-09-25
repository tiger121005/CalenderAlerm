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

    // 現在表示するアラームのLiveData
//    private val _alarmData = MutableLiveData<LiveData<AlarmData>>()
//    val alarmData: LiveData<AlarmData> = _alarmData.switchMap { alarm ->
//        alarm
//    }

    // idを基にアラームを取得し、LiveDataを更新する
//    fun getAlarm(year: Int, month: Int, date: Int) {
//        _alarmData.value = dao.getAlarmByDate(year, month, date)
//    }

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

//    fun update(alarm: AlarmData) {
//        viewModelScope.launch(Dispatchers.IO) {
//            dao.update(alarm)
//        }
//    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAlarmById(id)
        }
    }
}