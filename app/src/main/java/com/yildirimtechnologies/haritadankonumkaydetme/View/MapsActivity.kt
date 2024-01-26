package com.yildirimtechnologies.haritadankonumkaydetme.View

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.yildirimtechnologies.haritadankonumkaydetme.R
import com.yildirimtechnologies.haritadankonumkaydetme.databinding.ActivityMapsBinding
import com.yildirimtechnologies.haritadankonumkaydetme.model.Mekan
import com.yildirimtechnologies.haritadankonumkaydetme.roomdatabase.MekanDao
import com.yildirimtechnologies.haritadankonumkaydetme.roomdatabase.MekanDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences
    private var takipBoolean : Boolean? = null
    private var secililatitude : Double? = null
    private var secililongtitude : Double? = null
    private lateinit var db : MekanDatabase
    private lateinit var mekanDao: MekanDao
    val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()
        sharedPreferences = this.getSharedPreferences("com.yildirimtechnologies.haritadankonumkaydetme",MODE_PRIVATE)
        takipBoolean = false
        secililatitude = 0.0
        secililongtitude = 0.0
        db = Room.databaseBuilder(applicationContext,MekanDatabase::class.java,"Mekanlar")
            //.allowMainThreadQueries()
            .build()
        mekanDao = db.mekanDao()
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)
        //casting
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(p0: Location) {
                takipBoolean = sharedPreferences.getBoolean("takipBoolean",false)
                if (takipBoolean == false){
                    val userLocation = LatLng(p0.latitude,p0.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                    sharedPreferences.edit().putBoolean("takipBoolean",true).apply()
                }
            }
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            //kullancıya uyarı mesajı göstreilip gösterilmeyeceğini androide sorma
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.root,"Konum erişimi için izniniz gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver"){
                    //izin istenilecek
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
            }else {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            //eşit değildir permission granted dediğimiz için izin verilmediyse bunu yap.(acces fine location izin verid<l,iye eşit değil)
        }else{
            //izin verildi ise (permission granted)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
            val lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastlocation != null){
                val lastuserlocation = LatLng(lastlocation.latitude,lastlocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastuserlocation,15f))
            }
            mMap.isMyLocationEnabled = true
        }
       /*        val ayasofya = LatLng(41.0091330760635,28.980089117230712)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ayasofya,15f))
        mMap.addMarker(MarkerOptions().position(ayasofya).title("Ayasofya"))        */
    }
    private fun  registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if (result){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                    val lastlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastlocation != null){
                        val lastuserlocation = LatLng(lastlocation.latitude,lastlocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastuserlocation,15f))
                    }
                    mMap.isMyLocationEnabled = true
                }
                //permission granted
            }else{
                //permission denied
                Toast.makeText(this@MapsActivity,"İzine ihtiyacımız var",Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))
        secililatitude = p0.latitude
        secililongtitude = p0.longitude
    }
    fun kayıt(view: View) {
        try {
            if (secililatitude != null && secililongtitude != null) {
                val mekan = Mekan(binding.yerText.text.toString(), secililatitude!!, secililongtitude!!)
                compositeDisposable.add(
                    mekanDao.insert(mekan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                // Başarıyla eklendiğinde
                                println("Mekan başarıyla kaydedildi: ${mekan.isim}, ${mekan.latitude}, ${mekan.longtitude}")
                                handleresponse()
                            },
                            { throwable ->
                                // Hata durumunda
                                throwable.printStackTrace()
                                println("Mekan kaydı sırasında bir hata oluştu: ${throwable.message}")
                                // Hata durumunu loglamak veya kullanıcıya bildirim göstermek gibi bir işlem yapabilirsiniz
                            }
                        )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Bir hata oluştu: ${e.message}")
            // Genel hata durumunu loglamak veya kullanıcıya bildirim göstermek gibi bir işlem yapabilirsiniz
        }
    }

    fun sil(view : View){
    }
    private fun handleresponse(){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
//güncel izin kontrolü yap