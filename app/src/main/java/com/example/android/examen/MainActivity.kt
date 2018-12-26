package com.example.android.examen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),LocationListener, OnMapReadyCallback {

    //se crea variables para latitud, longitud y mapa
    var mapa: GoogleMap? = null
    var latitud = 0.0
    var longitud = 0.0
    var altitud = 0.0
    var lm : LocationManager? = null
    //Este boolean sirve para saber si la DB está guardando datos o no.
    //lo uso para abrir o cerrar la conexión con la DB
    var isSaving : Boolean = false
    //Este boolean lo uso para saber si se están mostrando los marcadores o no,
    //así puedo limpiarlos o leerlos de la DB.
    var isShowing : Boolean = true


    override fun onMapReady(p0: GoogleMap?) {
        mapa = p0
        //se revisa nuevamente si los permisos fueron otorgados
        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos)
        {
            granted = granted and (ActivityCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted)
        {
            //aqui se le pone codigo 2 en vez del anterior (el de mas abajo) que dice 1
            ActivityCompat.requestPermissions(this,permisos,2)
        }
        else {
            p0?.isMyLocationEnabled = true
        }
    }

    //Se declara un Location Manager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Se inicializa el location manager
        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //El fragmento mapa necesita un callback q haremos ahora
        val fragmentoMapaCB = supportFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
        fragmentoMapaCB.getMapAsync(this)

        //El gps es un permiso importante entonces debe salir un popup que le indique al usuario
        //sobre el uso del GPS
        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos)
        {
            granted = granted and (ActivityCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted)
        {
            ActivityCompat.requestPermissions(this,permisos,1)
        }
        else {
            lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,this)
        }

        //fragmento mapa
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
        //Antes decia THIS con rojo, pero al anadir el listner de mapa (ultimo metodo de Main Activity) y
        //sobreescribir, deja de suceder
        fragmentoMapa.getMapAsync(this)

        //Detiene la inserción de datos en la DB
        btnDetener.setOnClickListener{
            isSaving = false
            Toast.makeText(this, "Base de datos cerrada", Toast.LENGTH_SHORT).show()
        }

        //Abre la conexión con la DB en caso de estar cerrada o detenida
        btnIniciar.setOnClickListener {
            isSaving = true
            System.out.println("listo para guardar locaciones en la DB")
            Toast.makeText(this, "Base de datos abierta", Toast.LENGTH_SHORT).show()
        }

        //Borra la base de datos
        btnAdd.setOnClickListener{
            this.mapa?.clear()
            var customSQL = CustomSQL(this,"myDB", null, 1)
            customSQL.eliminar("myDB")
            isShowing = false

        }

        //Dibuja y desdibuja marcadores
        btnDibujar.setOnClickListener{
            var customSQL = CustomSQL(this,"myDB", null, 1)
            var ubicaciondb = customSQL.getUbicaciones(latitud,longitud)
            //si isShowing (seteado en creacion de marcadores) es verdadero, limpia los markers
            if (this.isShowing) {
                mapa?.clear()
                Toast.makeText(this, "Eliminando marcadores", Toast.LENGTH_LONG).show()
            }
            else if (!this.isShowing && ubicaciondb.size==0){
                Toast.makeText(this, "No se borra marcadores porque DB no existe", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "Leyendo database", Toast.LENGTH_LONG).show()
                for (i in ubicaciondb) {
                    //Esto deberia crear los marcadores leyendolos desde la DB, ya que itera sobre todos los datos,
                    //pero solo crea el ultimo
                    mapa?.addMarker(MarkerOptions().position(i).visible(true))
                    //Imprime en consola todas las ubicaciones. Lo hice para saber si estaba iterando correctamente
                    System.out.println(i)
                }
            }
            this.isShowing = !this.isShowing
        }
    }

    override fun onLocationChanged(location: Location?) {
        //Se indica que las variables latitd y longitud obtienen sus valores de la locacion
        //obtenida por el servicio de ubicacion
        latitud = location?.latitude.toString().toDouble()
        longitud = location?.longitude.toString().toDouble()
        altitud = location?.altitude.toString().toDouble()

        var lat = latitud.toString()
        var long = longitud.toString()

        if (isSaving == true) {
            var marcador = LatLng(latitud,longitud)
            mapa?.addMarker(MarkerOptions().position(marcador))
            var zoom : Float = 1500f
            mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(marcador,zoom));
            var customSQL = CustomSQL(this,"myDB", null, 1)
            customSQL.insertar(lat,long)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode)
        {
            1->
            {
                lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var granted = true
                for (permiso in permissions) {
                    granted = granted and (ActivityCompat.checkSelfPermission(
                        this, permiso)== PackageManager.PERMISSION_GRANTED)
                }
                if (grantResults.size > 0 && granted)
                {
                    lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,this)
                } else
                {
                    Toast.makeText(this, "Permiso de gps requerido", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
        //se deja vacio
    }

    override fun onProviderDisabled(provider: String?) {
        //se deja vacio
    }
}