package com.ajieno.githubuser.viewModel

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajieno.githubuser.CustomOnItemClickListener
import com.ajieno.githubuser.R
import com.ajieno.githubuser.model.FavoriteUser
import com.ajieno.githubuser.view.DetailUserActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_detail_user.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import java.util.ArrayList

class FavUserAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavUserAdapter.NoteViewHolder>() {
    var listNotes = ArrayList<FavoriteUser>()
        set(listNotes) {
            if (listNotes.size > 0) {
                this.listNotes.clear()
            }
            this.listNotes.addAll(listNotes)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listNotes[position])
    }

    override fun getItemCount(): Int = this.listNotes.size

    // set view data to adapter and set on click too
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(fav: FavoriteUser) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(fav.avatar)
                    .apply(RequestOptions().override(250, 250))
                    .into(itemView.img_avatar)
                txt_name.text = fav.name
                txt_company.text = fav.company
                txt_location.text = fav.location
                itemView.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnItemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                val intent = Intent(activity, DetailUserActivity::class.java)
//                                intent.putExtra(DetailUserActivity.EXTRA_POSITION, position)
                                intent.putExtra(DetailUserActivity.EXTRA_NOTE, fav)
                                activity.startActivity(intent)
                            }
                        }
                    )
                )
            }
        }
    }
}