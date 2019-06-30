package com.example.rvplayground

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.example.rvplayground.logic.MajongLogic
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var counter = 0
    var score = 100

    val width = 4
    val height = 4

    val stoneDrawables = arrayOf(R.drawable.majong_icon_0,
            R.drawable.majong_icon_1,
            R.drawable.majong_icon_2,
            R.drawable.majong_icon_3,
            R.drawable.majong_icon_4,
            R.drawable.majong_icon_5,
            R.drawable.majong_icon_6,
            R.drawable.majong_icon_7)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        recyclerView.layoutManager = GridLayoutManager(this, width)
        recyclerView.adapter = FlipFlotAdapter(MajongLogic(width, height))

        tvCounter.text = "Count of mistakes: $counter"
        tvScore.text = "Your score: $score"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.reset -> {
                val logic = MajongLogic(width, height)
                logic.reset()
                recyclerView.layoutManager = GridLayoutManager(this, width)
                recyclerView.adapter = FlipFlotAdapter(MajongLogic(width, height))
                counter = 0
                score = 100
                tvCounter.text = "Count of mistakes: $counter"
                tvScore.text = "Your score: $score"
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class FlipFlotAdapter(val logic: MajongLogic) : RecyclerView.Adapter<DataListViewHolder>() {

        override fun getItemCount(): Int = logic.width * logic.height
        var ignoreInput = false

        override fun onBindViewHolder(holder: DataListViewHolder, index: Int) {
            val x = index % logic.width
            val y = index / logic.width
//            val itemData = if (logic.isChecked(x, y)) "|" else "-";

//            holder.setTitle(itemData)
            val stone = logic.getStone(x, y)
            val open = logic.isStoneOpen(x, y)
            holder.setBackgroundRes(if (open) stoneDrawables[stone] else R.drawable.ic_launcher_background)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.cell_view, parent, false)
            val holder = DataListViewHolder(v)

            v.setOnClickListener { v -> onItemClicked(holder) }
            return holder
        }

        private fun onItemClicked(holder: DataListViewHolder) {
            if (ignoreInput)
                return

            val index = holder.adapterPosition
            val x = index % logic.width
            val y = index / logic.width

            if (logic.isStoneOpen(x, y))
                return

            val undoPair = logic.action(x, y)
            if (undoPair != null) {
                ignoreInput = true
                Handler(Looper.getMainLooper()).postDelayed({
                    ignoreInput = false
//                    logic.reset()
                    logic.performUndo(undoPair)
                    notifyDataSetChanged()
                }, 1000)
                counter++
                score -= counter
                tvCounter.text = "Count of mistakes: $counter"
                tvScore.text = "Your score: $score"
            }

            if (logic.haveWon()) {
                Toast.makeText(holder.itemView.context, "You have won!", Toast.LENGTH_SHORT).show()
            }
            notifyDataSetChanged()

//            val index = holder.adapterPosition
//            val x = index % logic.width
//            val y = index / logic.width
//            logic.action(x, y)
//
//            if (logic.haveWon()) {
//                Toast.makeText(holder.itemView.context, "You have won!", Toast.LENGTH_SHORT).show()
//                logic.shuffle()
//            }
//
//            notifyDataSetChanged()
        }
    }

    class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.cell_title)

        fun setTitle(title: String) {
            tv.setText(title)
        }

        fun setBackgroundRes(resId: Int) {
            itemView.setBackgroundResource(resId)
        }
    }
}
