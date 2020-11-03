package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.models.Property
import org.w3c.dom.Text

class MyAdapter(private val data: List<Property>, val onClickDelete: (Int) -> Unit) : RecyclerView.Adapter<MyAdapter.MyViewHolder>()  {

    private var listData: MutableList<Property> = data as MutableList<Property>
    inner class MyViewHolder(val view: View): RecyclerView.ViewHolder(view){

        fun bind(property: Property, index: Int){
//            val tv = view.findViewById<TextView>(R.id.list_tv)
//            tv.text = text
            val title = view.findViewById<TextView>(R.id.tvTitle)
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            val description = view.findViewById<TextView>(R.id.tvDescription)
            val button = view.findViewById<Button>(R.id.button)

            title.text = property.title
            description.text = property.description

            Glide.with(view.context).load(property.image).centerCrop().into(imageView)

            button.setOnClickListener{deleteItem(index)}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listData[position], position)
    }

    fun deleteItem(index: Int){
        listData.removeAt(index)
        notifyDataSetChanged()
    }

    fun setItems(items: List<Property>){
//        listData = items
//        notifyDataSetChanged()
    }

}