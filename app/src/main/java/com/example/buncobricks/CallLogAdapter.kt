package com.example.buncobricks

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView

class CallLogViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    private val number : TextView = itemView.findViewById(R.id.numberTV);
    private val callTypeIcon : ImageView = itemView.findViewById(R.id.call_type_icon);
    private val time : TextView = itemView.findViewById(R.id.time);
    private val date : TextView = itemView.findViewById(R.id.date);
    private val type : ImageView = itemView.findViewById(R.id.call_type_icon)

    private fun switch(name:String,pHno:String):String{
        return if(name.isEmpty()){
            pHno
        }else{
            name
        }
    }


    fun bindData(user:CallModel ){
        number.text = switch(user.name,user.P_number)
        callTypeIcon.setImageResource(user.type);
        time.text = user.time;
        date.text = user.Date;
        type.setImageResource(user.type)
    }

}

class CallLogAdapter(private val callLogs: List<CallModel>, private val listner: MainActivity,private val cellClickListener: CellClickListener):
    RecyclerView.Adapter<CallLogViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CallLogViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.call_log_item,parent,false)

        return CallLogViewHolder(view);
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val cal_log = callLogs[position];
        holder.bindData(cal_log)
        holder.itemView.setOnClickListener {
            cellClickListener.onCellClickListener(cal_log,position)
        }

    }

    override fun getItemCount(): Int {
        return callLogs.size
    }

}

interface CellClickListener  {
    fun onCellClickListener(data:CallModel,position: Int)

}