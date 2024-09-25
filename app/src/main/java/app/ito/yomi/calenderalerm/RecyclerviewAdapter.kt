package app.ito.yomi.calenderalerm

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.ito.yomi.calenderalerm.Actions

class RecyclerviewAdapter(dataList: List<AlarmData>): RecyclerView.Adapter<ViewHolderItem>() {
    private lateinit var listener: OnDataCellClickListener
    private var dataList = dataList
    private val calenderManager = CalenderManager()
    private var selectedPosition = RecyclerView.NO_POSITION

    interface OnDataCellClickListener {
        fun onItemClick(data: AlarmData)
    }

    fun setOnDataCellClickListener(listener: OnDataCellClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolderItem {
        val oneXml = LayoutInflater.from(parent.context).inflate(R.layout.one_day_layout, parent, false)
        return ViewHolderItem(oneXml)
    }

    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        holder.oneDayText.text = dataList[position].date.toString()
        holder.itemView.setOnClickListener {
            listener.onItemClick(dataList[position])
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.bindingAdapterPosition
            notifyItemChanged(selectedPosition)
        }

        if (dataList[position].date == 0) {
            holder.oneDayText.text = ""
            holder.timeText.text = ""
        }else if (dataList[position].hour == 100) {
            holder.timeText.text = ""
        } else {
            holder.timeText.text = calenderManager.formatTime(dataList[position].hour, dataList[position].minute)
        }

        if (selectedPosition == position) {
            holder.itemView.findViewById<TextView>(R.id.day_text).setBackgroundResource(R.drawable.select_date_view)
            holder.itemView.findViewById<TextView>(R.id.day_text).setTextColor(Color.WHITE)
        } else {
            holder.itemView.findViewById<TextView>(R.id.day_text).setBackgroundResource(0)
            holder.itemView.findViewById<TextView>(R.id.day_text).setTextColor(Color.BLACK)
        }


    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}