package com.ajieno.githubuser.view

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ajieno.githubuser.R
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.AVATAR
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.COMPANY
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.CONTENT_URI
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.FAVOURITE
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.FOLLOWERS
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.FOLLOWING
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.LOCATION
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.NAME
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.REPOSITORY
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.USERNAME
import com.ajieno.githubuser.db.FavUserHelper
import com.ajieno.githubuser.model.FavoriteUser
import com.ajieno.githubuser.model.User
import com.ajieno.githubuser.viewModel.ViewPagerDetailAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val Extra = "Extra"
        const val EXTRA_NOTE = "extra_note"
    }

    private var isFavourite = false
    private lateinit var gitHelper: FavUserHelper
    private var favorite: FavoriteUser? = null
    private lateinit var imageAvatar: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        supportActionBar?.hide()

        gitHelper = FavUserHelper.getInstance(applicationContext)
        gitHelper.open()

        favorite = intent.getParcelableExtra(EXTRA_NOTE)
        if (favorite != null){
            setDataObject()
            isFavourite = true
            val checked: Int = R.drawable.ic_favorite_black_24dp
            btn_fav.setImageResource(checked)
        }else{
            setData()
        }
        viewPagerConfig()

        btn_fav.setOnClickListener(this)

    }

    private fun setDataObject(){
        val favUser = intent.getParcelableExtra(EXTRA_NOTE) as FavoriteUser
//        setActionBarTitle("Detail of " + favUser.name.toString())
        txt_detail_name.text = favUser.name.toString()
        txt_username.text = favUser.username.toString()
        txt_detail_company.text = favUser.company.toString()
        txt_detail_location.text = favUser.location.toString()
        txt_detail_repository.text = favUser.repository.toString()
        txt_detail_follower.text = favUser.follower.toString()
        txt_following.text = favUser.following.toString()
        Glide.with(this)
            .load(favUser.avatar.toString())
            .into(img_detail_avatar)
        imageAvatar = favUser.avatar.toString()
    }

    private fun setData(){
        val user : User = intent.getParcelableExtra(Extra)
        txt_detail_name.setText(user.name)
        txt_username.setText(user.username)
        txt_detail_company.setText("Company : "+user.company)
        txt_detail_location.setText("Location : "+user.location)
        txt_detail_repository.setText("Repository : "+user.repository)
        txt_detail_follower.setText("Follower : "+user.follower)
        txt_following.setText("Following : "+user.following)

        Glide.with(this)
                .load(user.avatar)
                .into(img_detail_avatar)
        imageAvatar = user.avatar.toString()
    }

    private fun viewPagerConfig() {
        val viewPagerDetailAdapter = ViewPagerDetailAdapter(this, supportFragmentManager)
        view_pager.adapter = viewPagerDetailAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }

    override fun onClick(view: View?) {
        val checked: Int = R.drawable.ic_favorite_black_24dp
        val unChecked: Int = R.drawable.ic_favorite_border_black_24dp
        if (view?.id == R.id.btn_fav) {
            if (isFavourite) {
                gitHelper.deleteById(favorite?.username.toString())
                Toast.makeText(this, "Deleted from favourite list", Toast.LENGTH_SHORT).show()
                btn_fav.setImageResource(unChecked)
                isFavourite = false
            } else {
                val dataUsername = txt_username.text.toString()
                val dataName = txt_detail_name.text.toString()
                val dataAvatar = imageAvatar
                val datacompany = txt_detail_company.text.toString()
                val dataLocation = txt_detail_location.text.toString()
                val dataRepository = txt_detail_repository.text.toString()
                val dataFollowers = txt_detail_follower.text.toString()
                val dataFollowing = txt_following.text.toString()
                val dataFavourite = "1"

                val values = ContentValues()
                values.put(USERNAME, dataUsername)
                values.put(NAME, dataName)
                values.put(AVATAR, dataAvatar)
                values.put(COMPANY, datacompany)
                values.put(LOCATION, dataLocation)
                values.put(REPOSITORY, dataRepository)
                values.put(FOLLOWERS, dataFollowers)
                values.put(FOLLOWING, dataFollowing)
                values.put(FAVOURITE, dataFavourite)

                isFavourite = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, "Added to favourite list", Toast.LENGTH_SHORT).show()
                btn_fav.setImageResource(checked)
            }
        }
    }


}