package com.ajieno.githubuser.view.fragment

import android.nfc.NfcAdapter.EXTRA_DATA
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajieno.githubuser.R
import com.ajieno.githubuser.model.FavoriteUser
import com.ajieno.githubuser.model.User
import com.ajieno.githubuser.view.DetailUserActivity
import com.ajieno.githubuser.view.DetailUserActivity.Companion.EXTRA_NOTE
import com.ajieno.githubuser.viewModel.ListFollowerAdapter
import com.ajieno.githubuser.viewModel.ListFollowingAdapter
import com.ajieno.githubuser.viewModel.followerFilterList
import com.ajieno.githubuser.viewModel.userFilterList
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_followers.*
import org.json.JSONArray
import org.json.JSONObject


class FollowersFragment : Fragment() {

    companion object{
        private val TAG = FollowersFragment::class.java.simpleName
        const val Extra = "Extra"
    }

    private var listData: ArrayList<User> = ArrayList()
    private lateinit var adapter: ListFollowerAdapter
    private var favorite: FavoriteUser? = null
    private lateinit var dataUser: FavoriteUser
    private lateinit var dataUser2: User


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListFollowerAdapter(listData)
        listData.clear()

        favorite = activity!!.intent.getParcelableExtra(DetailUserActivity.EXTRA_NOTE)
        if (favorite != null) {
            dataUser = activity!!.intent.getParcelableExtra(EXTRA_NOTE) as FavoriteUser
            getDataGit(dataUser.username.toString())
        } else {
            dataUser2 = activity!!.intent.getParcelableExtra(Extra) as User
            getDataGit(dataUser2.username.toString())
        }
    }

    private fun getDataGit(id: String){
        progressbar_follower?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token <yourtoken>")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/followers"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressbar_follower?.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        getDataGitDetail(username)
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressbar_follower?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun getDataGitDetail(id: String) {
        progressbar_follower?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token <yourtoken>")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                progressbar_follower?.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    val username: String? = jsonObject.getString("login").toString()
                    val name: String? = jsonObject.getString("name").toString()
                    val avatar: String? = jsonObject.getString("avatar_url").toString()
                    val company: String? = jsonObject.getString("company").toString()
                    val location: String? = jsonObject.getString("location").toString()
                    val repository: Int = jsonObject.getInt("public_repos")
                    val followers: Int = jsonObject.getInt("followers")
                    val following: Int = jsonObject.getInt("following")
                    listData.add(
                            User(
                                    username,
                                    name,
                                    avatar,
                                    company,
                                    location,
                                    repository,
                                    followers,
                                    following
                            )
                    )
                    showRecyclerList()
                } catch (e: Exception) {
                    if (context != null) {
                        Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
                    }
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray,
                    error: Throwable
            ) {
                progressbar_follower?.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                        .show()
            }
        })
    }

    private fun showRecyclerList() {
        rv_followers.layoutManager = LinearLayoutManager(activity)
        val listDataAdapter = ListFollowerAdapter(followerFilterList)
        rv_followers.adapter = listDataAdapter

    }

}