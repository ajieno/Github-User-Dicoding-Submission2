package com.ajieno.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FavoriteUser (
    var username: String? = "",
    var name: String? = "",
    var avatar: String? = "",
    var company: String? = "",
    var location: String? = "",
    var repository: Int = 0,
    var follower: Int = 0,
    var following: Int = 0,
    var isFav: String? = ""

) : Parcelable