package com.example.android.examen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.layout_lista_lugares.view.*

class ListaFragment : Fragment() {

    var miContexto: Context? = null
    var customSQL = CustomSQL(miContexto, "Ubicaciones", null, 1)
    var listaLugares: ArrayList<UbicacionClase> = ArrayList<UbicacionClase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        miContexto = activity
        var customSQL : CustomSQL = CustomSQL(miContexto, "Ubicaciones", null, 1)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        // Inflate the layout for this fragment
            var v: View = inflater.inflate(R.layout.fragment_lista, container, false)

            var customSQL = CustomSQL(miContexto, "Ubicaciones", null, 1)

            var fragmentManager = fragmentManager

            var listaLugares : ArrayList<UbicacionClase> = customSQL.sendUbicaciones("Ubicaciones")

            var adaptador = CustomAdapter(miContexto!!,listaLugares,fragmentManager!!)
            var recyclerLista = v.findViewById<RecyclerView>(R.id.rvLugares)
            recyclerLista.layoutManager = LinearLayoutManager(miContexto, LinearLayout.VERTICAL,false)
            recyclerLista.adapter = adaptador

        return v
    }

    class CustomAdapter(var miContexto: Context, var listaLugares : ArrayList<UbicacionClase>, var fragmentManager: FragmentManager) :
        RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

        class CustomViewHolder(val miVista: View) : RecyclerView.ViewHolder(miVista) {

            fun binData(site : UbicacionClase) {
                miVista.lblNombreLugar.text = site.nombre
                miVista.lblDescripcion.text = site.descripcion
                miVista.lblLatitud.text = site.latitud.toString()
                miVista.lbllongitud.text=site.longitud.toString()
            }
        }

        override fun onCreateViewHolder(p0: ViewGroup, position: Int): CustomAdapter.CustomViewHolder {
            val v: View = LayoutInflater.from(miContexto).inflate(R.layout.layout_lista_lugares, p0, false)
            return CustomViewHolder(v)
        }

        override fun getItemCount(): Int {
            return listaLugares.size
        }

        override fun getItemId (position : Int) : Long {
            return position.toLong()
            Log.d("tamano de locaciones",listaLugares.size.toString())
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.binData(listaLugares[position])

        }




    }




}

