package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.models.Property
import com.example.myapplication.network.Api
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var data: MutableList<Property>

    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: MyAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var shrimmerView: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = LinearLayoutManager(this)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        shrimmerView = findViewById(R.id.shimmer_view_container)

        swipeRefresh.setOnRefreshListener {
            getAllData()
        }
        
        getAllData()

    }

    fun getAllData(){
        Api.retrofitService.getAllData().enqueue(object: Callback<List<Property>>{
            override fun onResponse(
                call: Call<List<Property>>,
                response: Response<List<Property>>
            ) {
                shrimmerView.stopShimmer()
                shrimmerView.visibility = View.GONE
                if(swipeRefresh.isRefreshing){
                    swipeRefresh.isRefreshing = false
                }
                if(response.isSuccessful){
                    recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply{
                        data = response.body() as MutableList<Property>
                        myAdapter = MyAdapter(data){index -> deleteItem(index)}
                        layoutManager = manager
                        adapter = myAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<Property>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun deleteItem(index: Int){
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this item ?")
        alertBuilder.setPositiveButton("Delete"){_,_ ->
            if(::data.isInitialized && ::myAdapter.isInitialized){
                data.removeAt(index)
                myAdapter.setItems(data)
                Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
            }
        }

        alertBuilder.setNegativeButton("No"){_,_ ->

        }

        alertBuilder.setNeutralButton("Cancel"){_,_ ->

        }
       alertBuilder.show()
    }
}