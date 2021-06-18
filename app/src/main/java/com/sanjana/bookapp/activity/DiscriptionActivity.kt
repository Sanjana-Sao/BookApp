package com.sanjana.bookapp.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sanjana.bookapp.R
import com.sanjana.bookapp.database.BookDatabase
import com.sanjana.bookapp.database.BookEntity
import com.sanjana.bookapp.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DiscriptionActivity : AppCompatActivity() {

    lateinit var imgBookImage : ImageView
    lateinit var txtBookName : TextView
    lateinit var txtBookAuthor : TextView
    lateinit var txtBookPrice : TextView
    lateinit var  txtBookRating : TextView
    lateinit var txtBookDesc : TextView
    lateinit var btnAddToFav : Button
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar : ProgressBar
    lateinit var toolbar: Toolbar

     var book_id : String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discription)

        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddTofav)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)


        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"
//            supportActionBar?.setHomeButtonEnabled(true)
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)


        progressBar.visibility = View.VISIBLE
        progressLayout.visibility = View.VISIBLE

        if (intent != null) {
            book_id = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DiscriptionActivity,
                "some unexpected error occured",
                Toast.LENGTH_LONG
            ).show()
        }

        if (book_id == "100") {
            finish()
            Toast.makeText(
                this@DiscriptionActivity,
                "some unexpected error occured",
                Toast.LENGTH_LONG
            ).show()

        }

        val queue = Volley.newRequestQueue(this@DiscriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        if (ConnectionManager() checkConnectivity (this@DiscriptionActivity)) {

        val jsonParams = JSONObject()
        jsonParams.put("book_id", book_id)

        val jsonRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonParams,
            Response.Listener {

                try {


                    val success = it.getBoolean("success")
                    if (success) {
                        progressLayout.visibility = View.GONE

                        val bookJsonObject = it.getJSONObject("book_data")
                        val bookImageUrl = bookJsonObject.getString("image")
                        Picasso.get().load(bookJsonObject.getString("image"))
                            .error(R.drawable.book_app).into(imgBookImage)
                        txtBookName.text = bookJsonObject.getString("name")
                        txtBookAuthor.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtBookRating.text = bookJsonObject.getString("rating")
                        txtBookDesc.text = bookJsonObject.getString("description")

                        val bookEntity = BookEntity(
                            book_id?.toInt() as Int,
                            txtBookName.text.toString(),
                            txtBookAuthor.text.toString(),
                            txtBookPrice.text.toString(),
                            txtBookRating.text.toString(),
                            txtBookDesc.text.toString(),
                            bookImageUrl
                        )
                        //object of class DBAsync
                        val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                        //check whether the book is favourite or not
                        val isFav = checkFav.get()

                        if(isFav){
                            btnAddToFav.text = "Remove From Favourites"
                            //object for color
                            val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                            btnAddToFav.setBackgroundColor(favColor)
                        }else{
                            btnAddToFav.text = "Add To Favourites"
                            val nofavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                            btnAddToFav.setBackgroundColor(nofavColor)
                        }

                        btnAddToFav.setOnClickListener {
                            if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()) {
                                val async = DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                val result = async.get()

                                if(result){
                                    Toast.makeText(
                                        this@DiscriptionActivity,
                                        "Book Added To Favourite",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                    btnAddToFav.text = "Remove From Favourites"
                                    val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                    btnAddToFav.setBackgroundColor(favColor)

                                }else{
                                    Toast.makeText(
                                        this@DiscriptionActivity,
                                        "Some error Occured 1 $it",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }else{
                                val async = DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                val result = async.get()

                                if(result) {
                                    Toast.makeText(
                                        this@DiscriptionActivity,
                                        "Book Removed To Favourite",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()

                                    btnAddToFav.text = "Add To Favourites"
                                    val nofavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                    btnAddToFav.setBackgroundColor(nofavColor)

                                }else{
                                    Toast.makeText(
                                        this@DiscriptionActivity,
                                        "Some error Occured 2 $it",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                        }

                    } else {
                        Toast.makeText(
                            this@DiscriptionActivity,
                            "Unaccepted error Occured",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                } catch (e: Exception) {

                    Toast.makeText(
                        this@DiscriptionActivity,
                        "JSON error Occured",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }, Response.ErrorListener {

                //  print("Response is $it")
                Toast.makeText(
                    this@DiscriptionActivity,
                    "Volley Error $it Occured",
                    Toast.LENGTH_LONG
                )
                    .show()
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "cc424f5527de95"
                return headers
            }
        }
        queue.add(jsonRequest)
    }else
        {
            val dialoge = AlertDialog.Builder(this@DiscriptionActivity)
            dialoge.setTitle("Error")
            dialoge.setMessage("Internet Connection Not Found")
            dialoge.setPositiveButton("Open settings") { text, listener ->

                val settinsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settinsIntent)
                finish()
            }
            dialoge.setNegativeButton("cancel") { text, listener ->

                ActivityCompat.finishAffinity(this@DiscriptionActivity)
            }
            dialoge.create()
            dialoge.show()
        }
    }

    //context is pass here to know that which part of the app has made a request,bookEntity for retrieving,deleting and removing and the mode is for thatt 3 values
    class DBAsyncTask(val context : Context, val bookEntity: BookEntity,val mode : Int) : AsyncTask<Void, Void, Boolean>() {

        /*

        mode1 : check the DB if the bok is favourite or not
        mode2 : save the book into DB as favourite
        mode3 : remove the favourite book
         */

        //creating a obj for database which use by all DBAsync clss
        val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode){

                1 ->{

                    // check the DB if the bok is favourite or not
                    val book:BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!= null
                }
                2 ->{

                    //save the book into DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{

                    // remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
           return false
        }
    }

}