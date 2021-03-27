package com.ajieno.githubuser.view

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajieno.githubuser.R
import com.ajieno.githubuser.db.DatabaseFavUser.FavColumns.Companion.CONTENT_URI
import com.ajieno.githubuser.helper.MappingHelper
import com.ajieno.githubuser.model.FavoriteUser
import com.ajieno.githubuser.viewModel.FavUserAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavUserAdapter

    companion object{
        private const val  EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        rv_fav_user.layoutManager = LinearLayoutManager(this)
        rv_fav_user.setHasFixedSize(true)
        adapter = FavUserAdapter(this)
        rv_fav_user.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object  : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listNotes = list
            }
        }

    }

    // get data and set it to adapter from SQLite database
    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar_fav.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferredNotes.await()
            progressbar_fav.visibility = View.INVISIBLE
            if (favData.size > 0) {
                adapter.listNotes = favData
            } else {
                adapter.listNotes = ArrayList()
                showSnackbarMessage()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listNotes)
    }

    private fun showSnackbarMessage() {
        Snackbar.make(rv_fav_user, "Tidak ada data saat ini", Snackbar.LENGTH_SHORT).show()
    }

    // run this func when open again for refresh data
    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }
}