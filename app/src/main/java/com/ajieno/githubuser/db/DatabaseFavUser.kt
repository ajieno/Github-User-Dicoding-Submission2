package com.ajieno.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseFavUser {
    const val AUTHORITY = "com.ajieno.githubuser"
    const val SCHEME = "content"

    class FavColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "favuser"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val FAVOURITE = "isFav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}