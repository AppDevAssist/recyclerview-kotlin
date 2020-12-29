package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.models.Property
import org.w3c.dom.Text

class MyAdapter(private val data: List<Property>, val showHideDelete: (Boolean) -> Unit) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var listData: MutableList<Property> = data as MutableList<Property>
    var selectedList = mutableListOf<Int>()

    inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(property: Property, index: Int) {
            val title = view.findViewById<TextView>(R.id.tvTitle)
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            val description = view.findViewById<TextView>(R.id.tvDescription)
            val button = view.findViewById<Button>(R.id.button)
            val constraintLayout = view.findViewById<ConstraintLayout>(R.id.constraintLayout)
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

            constraintLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE

            if (property.selected == true) {
                button.visibility = View.VISIBLE
            } else {
                button.visibility = View.GONE
            }
            title.text = property.title
            description.text = property.description

            Glide.with(view.context).load(property.image).centerCrop().into(imageView)

            constraintLayout.setOnLongClickListener { markSelectedItem(index) }
            constraintLayout.setOnClickListener { deselectItem(index) }
        }

        fun bindRecyclerView(data: List<Property>) {
            val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
            val constraintLayout = view.findViewById<ConstraintLayout>(R.id.constraintLayout)

            constraintLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            val manager: RecyclerView.LayoutManager =
                LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, true)
            recyclerView.apply {
                val data = data as MutableList<Property>
                var myAdapter = MyAdapter(data) { show -> showHideDelete(show) }
                layoutManager = manager
                adapter = myAdapter
            }
        }
    }

    fun deselectItem(index: Int) {
        if (selectedList.contains(index)) {
            selectedList.remove(index)
            listData[index].selected = false
            notifyDataSetChanged()
            showHideDelete(selectedList.isNotEmpty())
        }
    }

    fun markSelectedItem(index: Int): Boolean {
        for (item in listData) {
            item.selected = false
        }

        if(!selectedList.contains(index)){
            selectedList.add(index)
        }

        selectedList.forEach {
            listData[it].selected = true
        }

        notifyDataSetChanged()
        showHideDelete(true)
        return true
    }

    fun deleteSelectedItem() {
        if(selectedList.isNotEmpty()){
            listData.removeAll{item -> item.selected == true}
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (listData[position].horizontal) {
            listData[position].data?.let { holder.bindRecyclerView(it) }
        } else {
            holder.bind(listData[position], position)
        }
    }

    fun deleteItem(index: Int) {
//        listData.removeAt(index)
//        notifyDataSetChanged()
    }

    fun setItems(items: List<Property>) {
        listData = items as MutableList<Property>
        notifyDataSetChanged()
    }

}