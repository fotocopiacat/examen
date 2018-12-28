package com.example.android.examen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MapaFragment : Fragment(), OnMapReadyCallback {
    private var param1: String? = null
    private var param2: String? = null
    var data_id: Int? = null
    var miContexto : Context? = null
    var customSQL = CustomSQL(miContexto, "Ubicaciones", null, 1)
    var mapa: GoogleMap? = null
    var lm: LocationManager? = null
    var latitud = 0.0
    var longitud = 0.0
    var altitud = 0.0

    override fun onMapReady(p0: GoogleMap?) {

        mapa = p0

        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos)

        {
            miContexto = this.activity
            granted = granted and (ActivityCompat.checkSelfPermission(miContexto!!, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted)
        {
            ActivityCompat.requestPermissions(miContexto as Activity,permisos,2)
        }
        else {
            p0?.isMyLocationEnabled = true
        }
        var customSQL : CustomSQL = CustomSQL(miContexto, "Ubicaciones", null, 1)


        var latLangff = LatLng(latitud,longitud)
        var locaciones : ArrayList<LatLng> = customSQL.sendUbicacionToMap()
        var zoom : Float = 15f

        //Se lee de la db y dibuja marcador pero no todos los q deberia
        for (i in locaciones) {
            this.mapa?.addMarker(MarkerOptions().position(i).visible(true))
            this.mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(i, zoom))
        }
        System.out.println(data_id);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        miContexto = activity
        lm = this.miContexto?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_mapa, container, false)

        val fragmentoMapa = childFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
        fragmentoMapa.getMapAsync(this)
        return v
    }
}
