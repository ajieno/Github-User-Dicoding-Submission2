package com.ajieno.githubuser.helper

import android.database.Cursor
import com.ajieno.githubuser.db.DatabaseFavUser
import com.ajieno.githubuser.model.FavoriteUser
import java.util.*

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<FavoriteUser> {
        val favList = ArrayList<FavoriteUser>()

        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.NAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.LOCATION))
                val repository = getInt(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.REPOSITORY))
                val followers = getInt(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.FOLLOWING))
                val FavoriteUser =
                    getString(getColumnIndexOrThrow(DatabaseFavUser.FavColumns.FAVOURITE))
                favList.add(
                    FavoriteUser(
                        username,
                        name,
                        avatar,
                        company,
                        location,
                        repository,
                        followers,
                        following,
                        FavoriteUser
                    )
                )
            }
        }
        return favList
    }
}