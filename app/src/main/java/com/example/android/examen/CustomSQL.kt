package com.example.android.examen

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import java.io.File
import java.sql.SQLException

class CustomSQL (val miContexto: Context,
                 var nombreDb : String,
                 val factory: SQLiteDatabase.CursorFactory?,
                 var version : Int) : SQLiteOpenHelper(miContexto,
    nombreDb,
    factory,
    version) {

    override fun onCreate(db: SQLiteDatabase?) {
        var query = "CREATE TABLE Ubicaciones(id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre TEXT, Descripcion TEXT, Latitud STRING, Longitud STRING)"
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
            cv.put("descripcion", longitud)

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

/*    //Función para eliminar DB
    fun eliminar(nombreDb: String) {
        val database = miContexto.getDatabasePath(nombreDb)
        //chequeo si la DB existe y si existe la elimina, si no existe, no la elimina.
        //ojo que aqui el archivo DB sigue apareciendo en data/data/paquete/databases,
        //pero si intento copiarla en mi escritorio, me dice que no pudo porque "No such file or directory"
        if (database.exists()) {
            miContexto.deleteDatabase(nombreDb)
            Toast.makeText(miContexto, "DB eliminada", Toast.LENGTH_LONG).show()
        } else {
            //Luego de borrarla, al apretar de nuevo el boton BORRAR, sale esto.
            Toast.makeText(miContexto, "Error al eliminar BD", Toast.LENGTH_LONG).show()
        }
    }*/

/*    fun eliminarMarcadores (latitudD: Double, longitudD: Double): ArrayList<LatLng> {
        var ubicacionesList = ArrayList<LatLng>()
        // Selecciona lo que hay en la tabla Ubicaciones con un query
        val query = "SELECT * FROM Ubicaciones"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        // Se hace cursor q viaja por todos los datos de la tabla Ubicaciones
        // y guarda latitud y longiud
        if (cursor.moveToFirst()) {
            do {
                val lat= cursor.getDouble(0)
                val lng = cursor.getDouble(1)
                ubicacionesList.add(LatLng(latitudD,longitudD))
            } while (cursor.moveToNext())
        }
        if (ubicacionesList.size == 0) {
            Toast.makeText(miContexto, "No se elimina markers porque DB no existe", Toast.LENGTH_LONG).show()
        }
        return ubicacionesList
    }*/

/*    fun getUbicaciones(latitudD: Double, longitudD: Double): ArrayList<LatLng> {
        var ubicacionesList = ArrayList<LatLng>()
        // Selecciona lo que hay en la tabla Ubicaciones con un query
        val query = "SELECT * FROM Ubicaciones"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        // Se hace cursor q viaja por todos los datos de la tabla Ubicaciones
        // y guarda latitud y longiud
        if (cursor.moveToFirst()) {
            do {
                val lat= cursor.getDouble(0)
                val lng = cursor.getDouble(1)
                ubicacionesList.add(LatLng(latitudD,longitudD))
            } while (cursor.moveToNext())
        }
        return ubicacionesList
    }*/
}