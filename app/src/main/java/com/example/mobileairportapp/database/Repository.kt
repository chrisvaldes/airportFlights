package com.example.mobileairportapp.database
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.mutableStateOf

//penser a ajouter les messages de succès et d'erreur.

class Repository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // Airport
    fun addAirport(iata_code : String, name: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_IATA_CODE, iata_code)
            put(DatabaseHelper.COLUMN_NAME, name)
        }
        return db.insert(DatabaseHelper.TABLE_AIRPORT, null, values)
    }

    fun getAirport(): List<Airport> {
        val airports = mutableListOf<Airport>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_AIRPORT, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_AIRPORT_ID))
                val iataCode = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_IATA_CODE))
                val name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                airports.add(Airport(id.toLong(), iataCode, name))
            }
        }
        cursor.close()
        return airports
    }

    //Flight
    fun addFlight(departure_iata : String, departure_name: String, destination_iata : String, destination_name: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA,departure_iata)
            put(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_NAME, departure_name)
            put(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_IATA, destination_iata)
            put(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_NAME, destination_name)
        }
        return db.insert(DatabaseHelper.TABLE_FLIGHT, null, values)
    }

    fun getFlightByIata(iata_code: String): Airport? {

        var airport: Airport? = null
        val db = dbHelper.writableDatabase
        // Sélectionner les aéroports en fonction du code IATA
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_AIRPORT,
            null, // Sélectionner toutes les colonnes
            "${DatabaseHelper.COLUMN_IATA_CODE} = ?", // Clause WHERE
            arrayOf(iata_code), // Argument pour la clause WHERE
            null, // Pas de GROUP BY
            null, // Pas de HAVING
            null  // Pas de ORDER BY
        )

        with(cursor) {
            if (moveToFirst()) { // Vérifier s'il y a au moins un enregistrement
                val id = getLong(getColumnIndexOrThrow(DatabaseHelper.COLUMN_AIRPORT_ID))
                val iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_IATA_CODE))
                val name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                airport = Airport(id, iata, name)
            }
        }
        cursor.close()
        return airport // Retourner l'objet Airport ou null si non trouvé
    }

    fun getFlight(): List<Flight> {
        val flights = mutableListOf<Flight>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_FLIGHT, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_FLIGHT_ID))
                val departure_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA))
                val departure_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_NAME))
                val destination_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_IATA))
                val destination_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_NAME))
                val isFavoriteString = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FAVORITE))
                val isFavorite = isFavoriteString.toBoolean()
                flights.add(Flight(id, departure_iata, departure_name, destination_iata, destination_name,
                    mutableStateOf(isFavorite)
                ))
            }
        }
        cursor.close()
        return flights
    }


    fun getAirportsFlighDesservetByIataCode(iataCode: String): List<Flight> {
        val flights = mutableListOf<Flight>()
        val db: SQLiteDatabase = dbHelper.readableDatabase

        // Sélectionner les aéroports en fonction du code IATA
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_FLIGHT,
            null, // Sélectionner toutes les colonnes
            "${DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA} = ?", // Clause WHERE
            arrayOf(iataCode), // Argument pour la clause WHERE
            null, // Pas de GROUP BY
            null, // Pas de HAVING
            null  // Pas de ORDER BY
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_FLIGHT_ID))
                val departure_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA))
                val departure_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_NAME))
                val destination_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_IATA))
                val destination_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_NAME))
                val isFavoriteString = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_FAVORITE))
                val isFavorite = isFavoriteString.toBoolean()
                flights.add(Flight(id, departure_iata, departure_name, destination_iata, destination_name,
                    mutableStateOf(isFavorite)
                ))
            }
        }
        cursor.close()
        return flights
    }
}