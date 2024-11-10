package com.example.mobileairportapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "airPort.db"

        const val TABLE_AIRPORT = "airports"
        const val TABLE_FLIGHT = "flights"
        const val TABLE_FAVORITE = "favorites"

        const val COLUMN_AIRPORT_ID = "id"
        const val COLUMN_IATA_CODE = "iata_code"
        const val COLUMN_NAME = "name"
        const val COLUMN_PASSAGERS = "passagers"

        const val COLUMN_FAVORITE_ID = "favorite_id"
        const val COLUMN_DEPARTURE_CODE = "departure_code"
        const val COLUMN_DESTINATION_CODE = "destination_code"

        const val COLUMN_FLIGHT_ID = "flight_id"
        const val COLUMN_DEPARTURE_FLIGHT_IATA = "departure_flight_iata"
        const val COLUMN_DESTINATION_FLIGHT_IATA = "destination_flight_iata"
        const val COLUMN_DEPARTURE_FLIGHT_NAME = "departure_flight_name"
        const val COLUMN_DESTINATION_FLIGHT_NAME = "destination_flight_name"
        const val COLUMN_USER_FAVORITE = "isFavorite"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createAirportTable = ("CREATE TABLE $TABLE_AIRPORT ($COLUMN_AIRPORT_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_IATA_CODE TEXT, $COLUMN_NAME TEXT)")
        db.execSQL(createAirportTable)

        val createFavoriteTable = ("CREATE TABLE $TABLE_FAVORITE ($COLUMN_FAVORITE_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_DEPARTURE_CODE TEXT, $COLUMN_DESTINATION_CODE TEXT)")
        db.execSQL(createFavoriteTable)

        val createFlightTable = ("CREATE TABLE $TABLE_FLIGHT ($COLUMN_FLIGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_DEPARTURE_FLIGHT_IATA TEXT, $COLUMN_DESTINATION_FLIGHT_IATA TEXT, $COLUMN_DEPARTURE_FLIGHT_NAME TEXT, $COLUMN_DESTINATION_FLIGHT_NAME TEXT, $COLUMN_USER_FAVORITE BOOLEAN)")
        db.execSQL(createFlightTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AIRPORT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FLIGHT")
        onCreate(db)
    }
}