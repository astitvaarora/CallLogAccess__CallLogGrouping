package com.example.buncobricks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buncobricks.databinding.ActivityDetailedInfoBinding

class DetailedInfo : AppCompatActivity() {
    private lateinit var binding : ActivityDetailedInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var ls : List<InfoModel> = listOf()

        val receivedData: List<CallModel>? =
            intent.getSerializableExtra("data") as? List<CallModel>

        if (receivedData != null) {
            ls = convertToInfoList(receivedData)
            val switch = receivedData[0]
            val name : String
            if(switch.name.isEmpty()){
                name = switch.P_number
            }else{
                name = switch.name
            }
            binding.nametv.text = name

        } else {
            Toast.makeText(this, "No data received", Toast.LENGTH_SHORT).show()
        }

        binding.rvDetailed.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.rvDetailed.adapter = DetailedAdapter(ls)

        binding.backIcon.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



    }
    fun convertToInfoList(callModels: List<CallModel>): List<InfoModel> {
        return callModels.map { callModel ->
            val pic = when(callModel.type){
                R.drawable.incoming_call_icon -> R.drawable.incoming_call_icon_large
                R.drawable.outgoing_call_icon -> R.drawable.outgoing_call_icon_large
                R.drawable.missed_call_icon -> R.drawable.missed_call_icon_large
                else -> 1



            }// You need to replace this with the actual resource ID for the profile picture
            val type = when (callModel.type) {
                R.drawable.incoming_call_icon -> "Incoming"
                R.drawable.outgoing_call_icon -> "Outgoing"
                R.drawable.missed_call_icon-> "Missed"
                else -> "Unknown"
            }

            InfoModel(pic, type, callModel.Date, callModel.time)
        }
    }

}

data class InfoModel(
    val pic : Int,
    val type : String,
    val date : String,
    val time : String
)