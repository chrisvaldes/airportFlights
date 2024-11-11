package com.example.mobileairportapp.database
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.mutableStateOf
import com.example.mobileairportapp.database.DatabaseHelper.Companion.COLUMN_AIRPORT_ID
import com.example.mobileairportapp.database.DatabaseHelper.Companion.COLUMN_FLIGHT_ID
import com.example.mobileairportapp.database.DatabaseHelper.Companion.COLUMN_IATA_CODE
import com.example.mobileairportapp.database.DatabaseHelper.Companion.COLUMN_NAME
import com.example.mobileairportapp.database.DatabaseHelper.Companion.COLUMN_USER_FAVORITE
import com.example.mobileairportapp.database.DatabaseHelper.Companion.TABLE_FLIGHT

//penser a ajouter les messages de succès et d'erreur.

class Repository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // Airport
    fun addAirport(iata_code : String, name: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IATA_CODE, iata_code)
            put(COLUMN_NAME, name)
        }
        return db.insert(DatabaseHelper.TABLE_AIRPORT, null, values)
    }

    fun getAirport(): List<Airport> {
        val airports = mutableListOf<Airport>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_AIRPORT, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(COLUMN_AIRPORT_ID))
                val iataCode = getString(getColumnIndexOrThrow(COLUMN_IATA_CODE))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
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
            "$COLUMN_IATA_CODE = ?", // Clause WHERE
            arrayOf(iata_code), // Argument pour la clause WHERE
            null, // Pas de GROUP BY
            null, // Pas de HAVING
            null  // Pas de ORDER BY
        )

        with(cursor) {
            if (moveToFirst()) { // Vérifier s'il y a au moins un enregistrement
                val id = getLong(getColumnIndexOrThrow(COLUMN_AIRPORT_ID))
                val iata = getString(getColumnIndexOrThrow(COLUMN_IATA_CODE))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
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
                val id = getInt(getColumnIndexOrThrow(COLUMN_FLIGHT_ID))
                val departure_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA))
                val departure_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_NAME))
                val destination_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_IATA))
                val destination_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_NAME))
                val isFavoriteString = getString(getColumnIndexOrThrow(COLUMN_USER_FAVORITE))
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
                val id = getInt(getColumnIndexOrThrow(COLUMN_FLIGHT_ID))
                val departure_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA))
                val departure_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_NAME))
                val destination_iata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_IATA))
                val destination_name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_NAME))
                val isFavoriteInt = getInt(getColumnIndexOrThrow(COLUMN_USER_FAVORITE))
                val isFavorite = isFavoriteInt == 1
                flights.add(Flight(id, departure_iata, departure_name, destination_iata, destination_name,
                    mutableStateOf(isFavorite)
                ))
            }
        }
        cursor.close()
        return flights
    }

    fun searchAirports(query: String): List<Airport> {
        val db: SQLiteDatabase = dbHelper.readableDatabase// Obtenez votre instance de base de données
        val airports = mutableListOf<Airport>()

        val cursor = db.query(
            DatabaseHelper.TABLE_AIRPORT,
            arrayOf(COLUMN_AIRPORT_ID, COLUMN_IATA_CODE, COLUMN_NAME),
            "$COLUMN_NAME LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_AIRPORT_ID))
                val iataCode = getString(getColumnIndexOrThrow(COLUMN_IATA_CODE))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                airports.add(Airport(id.toLong(), iataCode, name))
            }
        }
        return airports
    }

    fun updateFavoriteStatus(flightId: Long, isFavorite: Boolean) {
        val db: SQLiteDatabase = dbHelper.writableDatabase // Obtenez l'instance de la base de données
        val contentValues = ContentValues().apply {
            put(COLUMN_USER_FAVORITE, isFavorite)
        }

        // Mettre à jour la ligne correspondante
        db.update(
            TABLE_FLIGHT,
            contentValues,
            "$COLUMN_FLIGHT_ID = ?",
            arrayOf(flightId.toString())
        )
    }

    fun getFavoriteFlights(): List<Flight> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val flights = mutableListOf<Flight>()

        // Requête pour récupérer tous les vols où isFavorite est vrai
        val cursor = db.query(
            DatabaseHelper.TABLE_FLIGHT,
            arrayOf(
                COLUMN_FLIGHT_ID,
                DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA,
                DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_NAME,
                DatabaseHelper.COLUMN_DESTINATION_FLIGHT_IATA,
                DatabaseHelper.COLUMN_DESTINATION_FLIGHT_NAME,
                COLUMN_USER_FAVORITE
            ),
            "$COLUMN_USER_FAVORITE = ?",
            arrayOf("1"), // Filtrer pour isFavorite = true
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_FLIGHT_ID))
                val departureIata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_IATA))
                val departureName = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEPARTURE_FLIGHT_NAME))
                val destinationIata = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_IATA))
                val destinationName = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESTINATION_FLIGHT_NAME))
                val isFavorite = getInt(getColumnIndexOrThrow(COLUMN_USER_FAVORITE)) == 1 // Convertir en Boolean

                flights.add(Flight(
                    id,
                    departureIata,
                    departureName,
                    destinationIata,
                    destinationName,
                    mutableStateOf(isFavorite)
                ))
            }
        }

        cursor.close() // Assurez-vous de fermer le curseur
        return flights
    }
}