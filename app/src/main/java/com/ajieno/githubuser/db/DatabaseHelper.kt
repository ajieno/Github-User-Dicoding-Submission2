package com.ajieno.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.TABLE_NAME

internal class DatabaseHelper (context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ){
    
    companion object{
        private const val DATABASE_NAME = "dbGithubUser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseFavUser.FavColumns.USERNAME} TEXT PRIMARY KEY  NOT NULL," +
                " ${DatabaseFavUser.FavColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseFavUser.FavColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseFavUser.FavColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseFavUser.FavColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseFavUser.FavColumns.REPOSITORY} INTEGER NOT NULL," +
                " ${DatabaseFavUser.FavColumns.FOLLOWERS} INTEGER NOT NULL," +
                " ${DatabaseFavUser.FavColumns.FOLLOWING} INTEGER NOT NULL," +
                " ${DatabaseFavUser.FavColumns.FAVOURITE} TEXT NOT NULL)"
        
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE  IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}