package com.example.android.examen

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_lista_lugares.view.*

class ListaFragment : Fragment() {

    var miContexto: Context? = null
    var customSQL = CustomSQL(miContexto,"Ubicaciones", null,1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        var v: View = inflater.inflate(R.layout.fragment_lista, container, false)

        var fragmentManager = fragmentManager

        var listaLugares = ArrayList<UbicacionClase>()
        miContexto = activity
        var customSQL = CustomSQL(miContexto,"Ubicaciones", null,1)
        listaLugares = customSQL.sendUbicaciones()


        var adaptador = customAdapter(miContexto!!, listaLugares, fragmentManager!!)
        var lista = v.findViewById<RecyclerView>(R.id.rvLugares)
        lista.adapter = adaptador

        return v
    }

    class customAdapter(
        var miContexto: Context,
        var listaLugares: ArrayList<UbicacionClase>,
        var fragmentManager: FragmentManager) : RecyclerView.Adapter<customAdapter.CustomViewHolder>() {

        class CustomViewHolder(val v: View) : RecyclerView.ViewHolder(v) {
            fun binData(site: UbicacionClase) {
                v.lblNombreLugar
                v.lblLatitud
                v.lbllongitud
            }
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): customAdapter.CustomViewHolder {
            val v: View = LayoutInflater.from(miContexto).inflate(R.layout.layout_lista_lugares, p0, false)
            return CustomViewHolder(v)
        }

        override fun getItemCount(): Int {
            return listaLugares.size
        }
        override fun onBindViewHolder(p0: customAdapter.CustomViewHolder, p1: Int) {

            p0.binData(listaLugares[p1])
        }
    }
}
