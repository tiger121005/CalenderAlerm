package app.ito.yomi.calenderalerm

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderItem(v:View): RecyclerView.ViewHolder(v){
    var oneDayText:TextView = v.findViewById(R.id.day_text)
    var timeText:TextView = v.findViewById(R.id.time_text)
}
