package com.example.rvplayground

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Arrays.asList
import java.util.zip.Inflater

class MainActivityBackup : AppCompatActivity() {

    val dataList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0..50)
            dataList.add("data_" + i)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = DataListAdapter(dataList)
    }

    class DataListAdapter(val data:MutableList<String>) : RecyclerView.Adapter<DataListViewHolder>() {

        override fun getItemCount(): Int  = data.size

        override fun onBindViewHolder(holder: DataListViewHolder, index: Int) {
            val itemData = data[index]
            println("onBindViewHolder, index = " + index + ", data=" + itemData)
            holder.setTitle(itemData)
            holder.setBackgroundColor(if (index % 2 == 0) Color.YELLOW else Color.WHITE)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListViewHolder {
            println("onCreateViewHolder, viewType = " + viewType )
            val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_view, parent, false)
            val holder = DataListViewHolder(v)

            v.setOnClickListener { v -> onItemClicked(holder) }

            return holder
        }

        private fun onItemClicked(holder: DataListViewHolder) {
            val index = holder.adapterPosition
            data.removeAt(index)

            //notifyItemRemoved(index)
            notifyDataSetChanged()

//            val itemData = data[index]
//            Toast.makeText(holder.itemView.context, "$itemData clicked", Toast.LENGTH_SHORT).show()
        }
    }

    class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.cell_title)

        fun setTitle(title: String) {
            tv.setText(title)
        }

        fun setBackgroundColor(color: Int) {
            itemView.setBackgroundColor(color)
        }
    }

}
