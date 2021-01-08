package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.models.Property
import com.example.myapplication.network.Api
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var data: MutableList<Property>

    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: MyAdapter
    private var mainMenu: Menu? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        manager = LinearLayoutManager(this)

        binding.swipeRefresh.setOnRefreshListener {
            getAllData()
        }
        
        getAllData()

    }

    private fun showHideDelete(show: Boolean){
        mainMenu?.findItem(R.id.menu_delete)?.isVisible = show
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        mainMenu = menu
        menuInflater.inflate(R.menu.main_menu, menu)
        showHideDelete(false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    fun getAllData(){
        Api.retrofitService.getAllData().enqueue(object: Callback<List<Property>>{
            override fun onResponse(
                call: Call<List<Property>>,
                response: Response<List<Property>>
            ) {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerViewContainer.visibility = View.GONE
                if(binding.swipeRefresh.isRefreshing){
                    binding.swipeRefresh.isRefreshing = false
                }
                if(response.isSuccessful){
                    binding.recyclerView.apply{
                        data = response.body() as MutableList<Property>
                        myAdapter = MyAdapter(data){show -> showHideDelete(show)}
                        layoutManager = manager
                        adapter = myAdapter
                        val swipeDelete = object : SwipeToDeleteCallback(this@MainActivity){
                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                myAdapter.deleteItem(viewHolder.adapterPosition)
                            }
                        }

                        val touchHelper  = ItemTouchHelper(swipeDelete)
                        touchHelper.attachToRecyclerView(this)
                    }
                }
            }

            override fun onFailure(call: Call<List<Property>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun deleteItem(){
        val alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Delete")
        alertBuilder.setMessage("Do you want to delete this item ?")
        alertBuilder.setPositiveButton("Delete"){_,_ ->
            if(::myAdapter.isInitialized){
                myAdapter.deleteSelectedItem()
                showHideDelete(false)
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