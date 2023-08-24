package com.example.a7minutesworkout.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.databinding.ItemRowBinding

class HistoryAdapter(
    private val items:ArrayList<HistoryEntity>,
    private val deleteListener: (id: Int) -> Unit
): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvIbm = binding.tvIBM
        val tvCategory = binding.tvCategory
        val tvDate = binding.tvDate
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.tvIbm.text = item.ibm
        holder.tvCategory.text = item.category
        holder.tvDate.text = item.date

        if(position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                holder.itemView.context, R.color.lightGray))
        }
        else {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                holder.itemView.context, R.color.white))
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }

    }


    override fun getItemCount(): Int {
        return items.size
    }
}