package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityHistoryBinding
import com.example.a7minutesworkout.room.HistoryDao
import com.example.a7minutesworkout.room.HistoryEntity
import com.example.a7minutesworkout.room.HistoryAdapter
import com.example.a7minutesworkout.room.HistoryApp
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private var binding: ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)

        setContentView(binding?.root)


        setSupportActionBar(binding?.toolbarHistory)

        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarHistory?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val historyDao = (application as HistoryApp).db.historyDao()
        showListHistories(historyDao)
    }

    private fun showListHistories(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchAllHistories().collect{
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, historyDao)
            }
        }
    }

    private fun setupListOfDataIntoRecyclerView(
        historiesList: ArrayList<HistoryEntity>,
        historyDao: HistoryDao
    ) {
        if(historiesList.isNotEmpty()) {
            val historyAdapter = HistoryAdapter(historiesList,
            ) { deleteId ->
                deleteRecordDialog(deleteId, historyDao)
            }
            binding?.rvHistories?.layoutManager = LinearLayoutManager(this)
            binding?.rvHistories?.adapter = historyAdapter
            binding?.rvHistories?.visibility = View.VISIBLE
            binding?.tvNoHistories?.visibility = View.GONE
        }
        else {
            binding?.rvHistories?.visibility = View.GONE
            binding?.tvNoHistories?.visibility = View.VISIBLE
        }
    }

    private fun deleteRecordDialog(id:Int, historyDao: HistoryDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete History")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                historyDao.delete(HistoryEntity(id = id))
                Toast.makeText(
                    applicationContext,
                    "History deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}