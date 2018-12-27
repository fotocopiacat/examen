package com.example.android.examen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.sql.SQLException

class CustomSQL (
    val miContexto: Context?,
    var nombreDb: String,
    val factory: SQLiteDatabase.CursorFactory?,
    var version: Int) : SQLiteOpenHelper(miContexto,
    nombreDb,
    factory,
    version) {

    override fun onCreate(db: SQLiteDatabase?) {
        var query = "CREATE TABLE Ubicaciones(id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre TEXT, Descripcion TEXT, Latitud TEXT, Longitud TEXT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    //Se crea una funcion que intentara escribir en la base de datos
    fun insertar(nombre : String , descripcion : String, latitud : String, longitud : String) {
        try {
            val db = this.writableDatabase
            var cv = ContentValues()
            cv.put("nombre", nombre)
            cv.put("descripcion", descripcion)
            cv.put("latitud", latitud)
            cv.put("longitud", longitud)
            val resultado = db.insert("Ubicaciones", null, cv)
            db.close()
            if (resultado == 1L) {
                System.out.println("No agregado")
                Toast.makeText(miContexto, "No agregados", Toast.LENGTH_SHORT).show()
            }
            else {
                System.out.println("mensaje agregado")
                Toast.makeText(miContexto, "Si agregados", Toast.LENGTH_SHORT).show()

            }
        } catch (e: SQLException)
        {
            System.out.println("ERROR Al insertar locacion en DB")
            Toast.makeText(miContexto, "Error al insertar locación. Revise log.", Toast.LENGTH_LONG).show()
        }
    }

    fun getUbicaciones(nombre: String, descripcion: String,latitud: String, longitud: String): ArrayList<UbicacionClase> {
        val db = this.writableDatabase
        var ubicacionesList = ArrayList<UbicacionClase>()
        val query = "SELECT * FROM Ubicaciones"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                var nombre= cursor.getString(1)
                var descripcion = cursor.getString(2)
                var latitud = cursor.getDouble(3)
                var longitud = cursor.getDouble(4)
                ubicacionesList.add(UbicacionClase(nombre,descripcion,latitud,longitud))
            } while (cursor.moveToNext())
        }
        return ubicacionesList
    }

    fun sendUbicaciones(nombreDb: String): ArrayList<UbicacionClase> {
        val db = this.writableDatabase
        var ubicacionesRecibidas = ArrayList<UbicacionClase>()
        val query = "SELECT * FROM Ubicaciones"
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                var nombre= cursor.getString(1)
                var descripcion = cursor.getString(2)
                var latitud = cursor.getDouble(3)
                var longitud = cursor.getDouble(4)
                ubicacionesRecibidas.add(UbicacionClase(nombre,descripcion,latitud,longitud))
            } while (cursor.moveToNext())
        }
        return ubicacionesRecibidas
    }
}