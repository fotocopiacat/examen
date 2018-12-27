package com.example.android.examen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
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
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RegistroFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RegistroFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RegistroFragment : Fragment(),LocationListener, OnMapReadyCallback {

    private var param1: String? = null
    private var param2: String? = null

    var mapa: GoogleMap? = null
    var latitud = 0.0
    var longitud = 0.0
    var altitud = 0.0
    var lm: LocationManager? = null
    var miContexto: Context? = null

    override fun onMapReady(p0: GoogleMap?) {
        mapa = p0
        //se revisa nuevamente si los permisos fueron otorgados
        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos) {
            // granted = granted and (ActivityCompat.checkSelfPermission(miContexto!!, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted) {
            ActivityCompat.requestPermissions(miContexto as Activity, permisos, 2)
        } else {
            p0?.isMyLocationEnabled = true
        }
    }

    override fun onLocationChanged(location: Location?) {

        latitud = location?.latitude.toString().toDouble()
        longitud = location?.longitude.toString().toDouble()
        altitud = location?.altitude.toString().toDouble()

        var marcador = LatLng(latitud, longitud)
        var zoom: Float = 18f
        mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(marcador, zoom))

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {
            1 -> {
                lm = miContexto?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var granted = true
                for (permiso in permissions) {
                    granted = granted and (ActivityCompat.checkSelfPermission(
                        miContexto!!, permiso
                    ) == PackageManager.PERMISSION_GRANTED)
                }
                if (grantResults.size > 0 && granted) {
                    lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
                } else {
                    Toast.makeText(miContexto, "Permiso de gps requerido", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var miContexto : Context? = null
        // Inflate the layout for this fragment
        var v: View = inflater.inflate(R.layout.fragment_registro, container, false)
        val fragmentoMap = childFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment?
        fragmentoMap?.getMapAsync(this)


       // lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //Se inicializa el location manager

        //El fragmento mapa necesita un callback q haremos ahora

        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos) {
       //      granted = granted and (ActivityCompat.checkSelfPermission(miContexto, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted) {
            ActivityCompat.requestPermissions(miContexto as Activity, permisos, 1)
        } else {
     //       this.lm!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, this)
        }
      //  val fragmentoMapa = childFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
       // fragmentoMapa.getMapAsync(this)

//        btnAdd.setOnClickListener {
//            var nombre: String = editNombre.text.toString()
//            var descripcion: String = editDescripcion.text.toString()
//            var marcador = LatLng(latitud, longitud)
//            var zoom: Float = 20f
//            mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(marcador, zoom))
//            mapa?.addMarker(MarkerOptions().position(marcador).visible(true))
//            var customSQL = CustomSQL(miContexto!!, "Ubicaciones", null, 1)
//            customSQL.insertar(nombre, descripcion, latitud.toString(), longitud.toString())
//            editNombre.text.clear()
//            editDescripcion.text.clear()
//        }


        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    fun onButtonPressed(uri: Uri) {
     //   listener?.onFragmentInteraction(uri)
    }
}