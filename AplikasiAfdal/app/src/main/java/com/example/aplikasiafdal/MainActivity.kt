package com.example.aplikasiafdal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var itemMl: RecyclerView
    private val list = ArrayList<Hero>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemMl = findViewById(R.id.item_ml)
        itemMl.setHasFixedSize(true)

        list.addAll(getListItem())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("Recycle")
    private fun getListItem(): ArrayList<Hero> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataAttributes = resources.getStringArray(R.array.data_attributes)

        val listItem = ArrayList<Hero>()
        for (i in dataName.indices) {
            val item = Hero(
                name = dataName[i],
                description = dataDescription[i],
                atribut = dataAttributes[i],
                photo = dataPhoto.getResourceId(i, -1)
            )
            listItem.add(item)
        }
        dataPhoto.recycle()
        return listItem
    }

    private fun showRecyclerList() {
        itemMl.layoutManager = LinearLayoutManager(this)
        val listItemAdapter = ListItemAdapter(list)
        itemMl.adapter = listItemAdapter
    }
}
