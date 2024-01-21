package com.example.buncobricks

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buncobricks.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.provider.CallLog
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.io.Serializable


class MainActivity : AppCompatActivity(), CellClickListener{
    private lateinit var binding : ActivityMainBinding
    private val groupedCallLogs = mutableMapOf<String, MutableList<CallModel>>()
    companion object {
        private const val PERMISSION_REQUEST_READ_CALL_LOG = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CALL_LOG),
                PERMISSION_REQUEST_READ_CALL_LOG
            )
        } else {
            // Permission already granted, load call logs
            loadCallLogs()
        }
//        for(entry in groupedCallLogs){
//            Log.d("1sd","${entry.key}   ${entry.value}")
//        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadCallLogs() {
        val callLogs = getCallLogs()
        val adapter = CallLogAdapter(callLogs,this,this)
        binding.recyclerView.adapter = adapter

    }
    fun groupCallLogsByNumber(callLog: List<CallModel>): Map<String, List<CallModel>> {
//        val groupedCallLogs = mutableMapOf<String, MutableList<CallModel>>()

        for (call_log in callLog) {
            val phoneNumber = call_log.P_number

            if (groupedCallLogs.containsKey(phoneNumber)) {
                // Add to an existing group
                groupedCallLogs[phoneNumber]?.add(call_log)
            } else {
                // Create a new group
                groupedCallLogs[phoneNumber] = mutableListOf(call_log)
            }
        }
        return groupedCallLogs
    }
    @SuppressLint("SimpleDateFormat")
    private fun getCallLogs(): List<CallModel> {
        val callLogs = mutableListOf<CallModel>()

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            val numberColumn = it.getColumnIndex(CallLog.Calls.NUMBER)
            val typeColumn = it.getColumnIndex(CallLog.Calls.TYPE)
            val dateColumn = it.getColumnIndex(CallLog.Calls.DATE)
            val durationColumn = it.getColumnIndex(CallLog.Calls.DURATION)
            val nameColumn = it.getColumnIndex(CallLog.Calls.CACHED_NAME)

            while (it.moveToNext()) {
                val number = it.getString(numberColumn)
                val name = it.getString(nameColumn)
                val type = when (it.getInt(typeColumn)) {
                    CallLog.Calls.INCOMING_TYPE -> R.drawable.incoming_call_icon
                    CallLog.Calls.OUTGOING_TYPE -> R.drawable.outgoing_call_icon
                    CallLog.Calls.MISSED_TYPE -> R.drawable.missed_call_icon
                    else -> R.drawable.incoming_call_icon
                }

                val date = it.getLong(dateColumn)

                val sdf = java.text.SimpleDateFormat("dd-MM-yyyy")
                val date_formatted = java.util.Date(date)
                val formattedDate = sdf.format(date_formatted)
                val duration = it.getInt(durationColumn)
                val minutes = duration / 60
                val hours = minutes / 60
                val formatted_duration = "$hours:$minutes:$duration ago";

                val callLog = CallModel(
                    name,
                    number,
                    formattedDate.toString(),
                    type,
                    formatted_duration,
                    )
                groupCallLogsByNumber(callLogs);
                callLogs.add(callLog)
            }
        }
        return callLogs
    }

    override fun onCellClickListener(data: CallModel, position: Int) {
        val intent = Intent(this, DetailedInfo::class.java)
        val groupedData = groupedCallLogs[data.P_number]

        if (groupedData != null) {
            val subsetSize = 25
            val subset = if (groupedData.size > subsetSize) {
                groupedData.subList(0, subsetSize)
            } else {
                ArrayList(groupedData)
            }

            intent.putExtra("data", ArrayList(subset))
            startActivity(intent)
        } else {
            Toast.makeText(this, "No data available for ${data.P_number}", Toast.LENGTH_SHORT).show()
        }
    }
}

data class CallModel(
    val name : String,
    val P_number : String,
    val Date : String,
    val type : Int,
    val time : String
    ): Serializable


