package com.example.submission

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ApiConfig
import android.util.Log
import com.example.submission.Response.Response
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_event)
        recyclerView.layoutManager = LinearLayoutManager(this)

        ApiConfig.getApiService().getEvents(0).enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()?.listEvents
                    if (events != null) {
                        val adapter = ReviewAdapter(events)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@MainActivity, "No events found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Response error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }


            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error fetching data: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("APIError", t.message, t)
            }

        })
    }
}
