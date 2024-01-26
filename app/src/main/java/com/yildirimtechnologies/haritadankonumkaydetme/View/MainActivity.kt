package com.yildirimtechnologies.haritadankonumkaydetme.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.yildirimtechnologies.haritadankonumkaydetme.R
import com.yildirimtechnologies.haritadankonumkaydetme.adapter.MekanAdapter
import com.yildirimtechnologies.haritadankonumkaydetme.databinding.ActivityMain2Binding
import com.yildirimtechnologies.haritadankonumkaydetme.model.Mekan
import com.yildirimtechnologies.haritadankonumkaydetme.roomdatabase.MekanDatabase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_main2)


        val db = Room.databaseBuilder(applicationContext,MekanDatabase::class.java,"Mekanlar").build()
        val mekanDao = db.mekanDao()

        compositeDisposable.add(
            mekanDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(this::handleResponse)
        )
    }
    private fun handleResponse(mekanList: List<Mekan>){
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = MekanAdapter(mekanList)
        binding.recyclerview.adapter = adapter
    }
    //menuyü aktiviteyle bağlama
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.konum_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    //item seçilince ne olacak
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.MekanEkle){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}