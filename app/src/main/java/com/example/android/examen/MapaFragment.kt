package com.example.android.examen

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MapaFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    var miContexto : Context? = null
    var customSQL = CustomSQL(miContexto, "Ubicaciones", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        miContexto = activity
        var customSQL : CustomSQL = CustomSQL(miContexto, "Ubicaciones", null, 1)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v : View = inflater.inflate(R.layout.fragment_mapa, container, false)




        return v
    }








}
