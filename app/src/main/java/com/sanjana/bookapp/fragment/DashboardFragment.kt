package com.sanjana.bookapp.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sanjana.bookapp.R
import com.sanjana.bookapp.adapter.DashboardRecyclerAdapter
import com.sanjana.bookapp.model.Book
import com.sanjana.bookapp.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var btnCheckInternet: Button

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar


    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1,book2->
        if( book1.bookRating.compareTo(book2.bookRating,true)==0){
            //sort according to name if rating is same
            book1.bookName.compareTo(book2.bookName,true)
            }else
        book1.bookRating.compareTo(book2.bookRating,true)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        //this methos is to add menu for the fragment for the activity compiler automatically adds it by onCreateOptionsMenu(
        setHasOptionsMenu(true)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)

        layoutManager = LinearLayoutManager(activity)

        recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)

        btnCheckInternet = view.findViewById(R.id.btnCheckInternet)

        btnCheckInternet.setOnClickListener {

            Toast.makeText(this.context, "button clicked ", Toast.LENGTH_LONG).show()

            if (ConnectionManager() checkConnectivity (activity as Context)) {

                val dialoge = AlertDialog.Builder(activity as Context)
                dialoge.setTitle("success")
                dialoge.setMessage("Internet Connection Found")
                dialoge.setPositiveButton("ok") { text, listener ->

                }
                dialoge.setNegativeButton("cancel") { text, listener ->

                }
                dialoge.create()
                dialoge.show()
            } else {

                val dialoge = AlertDialog.Builder(activity as Context)
                dialoge.setTitle("Error")
                dialoge.setMessage("Internet Connection Not Found")
                dialoge.setPositiveButton("ok") { text, listener ->

                }
                dialoge.setNegativeButton("cancel") { text, listener ->

                }
                dialoge.create()
                dialoge.show()
            }
        }

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager() checkConnectivity (activity as Context)) {

            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener {

                    try {

                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")
                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {

                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")

                                )
                                bookInfoList.add(bookObject)
                                if(activity!= null) {
                                    recyclerAdapter =
                                        DashboardRecyclerAdapter(activity as Context, bookInfoList)

                                    recyclerDashboard.adapter = recyclerAdapter

                                    recyclerDashboard.layoutManager = layoutManager

                                }

                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                " Some error occured!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(activity as Context, "JSON error Occured", Toast.LENGTH_LONG)
                            .show()
                    }
                }, Response.ErrorListener {

                    //  print("Response is $it")
                    if(activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occured",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "cc424f5527de95"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)
        } else {
            val dialoge = AlertDialog.Builder(activity as Context)
            dialoge.setTitle("Error")
            dialoge.setMessage("Internet Connection Not Found")
            dialoge.setPositiveButton("Open settings") { text, listener ->

                val settinsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settinsIntent)
                activity?.finish()
            }
            dialoge.setNegativeButton("cancel") { text, listener ->

                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialoge.create()
            dialoge.show()

        }
            return view
    }

    //this used to be add an item in the toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    //method for sorting while clicking on the icon
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item?.itemId
        if(id == R.id.actoin_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }
}
