package com.example.buncobricks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class MyViewHolder(itemView : View) : ViewHolder(itemView){
    private val typeIconIv : ImageView = itemView.findViewById(R.id.calltypelarge_icon)
    private val incomingTypeTV : TextView = itemView.findViewById(R.id.calltypeTV)
    private val dateTV : TextView = itemView.findViewById(R.id.date_detailed)
    private val timeTV : TextView = itemView.findViewById(R.id.time_detailed)

    fun bindData(item: InfoModel){
        typeIconIv.setImageResource(item.pic)
        incomingTypeTV.text = item.type
        dateTV.text = item.date
        timeTV.text = item.time
    }
}

class DetailedAdapter(private val info_ls: List<InfoModel>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.detailed_item,parent,false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return info_ls.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val info = info_ls[position]
        holder.bindData(info)
    }
}