package com.example.mindtick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecordAdapter(

    private val records: List<RecordItem>

) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    class RecordViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvTime =
            itemView.findViewById<TextView>(R.id.tvTime)

        val tvScore =
            itemView.findViewById<TextView>(R.id.tvScore)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecordViewHolder {

        val view = LayoutInflater.from(
            parent.context
        ).inflate(
            R.layout.item_record,
            parent,
            false
        )

        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecordViewHolder,
        position: Int
    ) {

        val record =
            records[position]

        holder.tvTime.text =
            record.date

        holder.tvScore.text =
            "${record.score}점"
    }

    override fun getItemCount(): Int {

        return records.size
    }
}