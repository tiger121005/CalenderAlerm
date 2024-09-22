package app.ito.yomi.calenderalerm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
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
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(data)
        }
    }

    // DBにデータを保存
    fun update(data: AlarmData) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(data)
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