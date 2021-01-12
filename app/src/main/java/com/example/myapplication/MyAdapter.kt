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
import com.example.myapplication.databinding.ListItemBinding
import com.example.myapplication.models.Property
import org.w3c.dom.Text

class MyAdapter(private val data: List<Property>, val showHideDelete: (Boolean) -> Unit) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var listData: MutableList<Property> = data as MutableList<Property>
    var selectedList = mutableListOf<Int>()

    inner class MyViewHolder(val view: ListItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(property: Property, index: Int) {
            view.constraintLayout.visibility = View.VISIBLE
            view.recyclerView.visibility = View.GONE

            if (property.selected == true) {
                view.button.visibility = View.VISIBLE
            } else {
                view.button.visibility = View.GONE
            }
            view.tvTitle.text = property.title
            view.tvDescription.text = property.description

            Glide.with(view.root.context).load(property.image).centerCrop().into(view.imageView)

            view.constraintLayout.setOnLongClickListener { markSelectedItem(index) }
            view.constraintLayout.setOnClickListener { deselectItem(index) }
        }

        fun bindRecyclerView(data: List<Property>) {

            view.constraintLayout.visibility = View.GONE
            view.recyclerView.visibility = View.VISIBLE

            val manager: RecyclerView.LayoutManager =
                LinearLayoutManager(view.root.context, LinearLayoutManager.HORIZONTAL, true)
            view.recyclerView.apply {
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
        val v = LayoutInflater.from(parent.context)
        val listItemBinding = ListItemBinding.inflate(v, parent, false)
        return MyViewHolder(listItemBinding)
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
        listData.removeAt(index)
        notifyDataSetChanged()
    }

    fun setItems(items: List<Property>) {
        listData = items as MutableList<Property>
        notifyDataSetChanged()
    }

}