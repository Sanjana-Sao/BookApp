package com.sanjana.bookapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sanjana.bookapp.R
import com.sanjana.bookapp.activity.DiscriptionActivity
import com.sanjana.bookapp.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context : Context,val itemList: ArrayList<Book>) : RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {

        // val text = itemList[position]
        // holder.textView.text = text

        val book = itemList[position]
        holder.textBookName.text = book.bookName
        holder.textBookAuthor.text = book.bookAuthor
        holder.textBookPrice.text = book.bookPrice
        holder.textBookRating.text = book.bookRating
        // holder.imgBookImage.setBackgroundResource(book.bookImage)
       // holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.book_app).into(holder.imgBookImage)

        holder.llContent.setOnClickListener {

           // Toast.makeText(context,"Clicked on ${holder.textBookName.text}",Toast.LENGTH_LONG).show()

            val intent = Intent(context,DiscriptionActivity::class.java)
            intent.putExtra("book_id",book.book_id)
            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {

       return itemList.size

    }

    class DashboardViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val textBookName :TextView = view.findViewById(R.id.txtBookName)
        val textBookAuthor : TextView = view.findViewById(R.id.txtBookAuthor)
        val textBookPrice : TextView = view.findViewById(R.id.txtBookPrice)
        val textBookRating : TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage : ImageView = view.findViewById(R.id.imgBookImage)
        val llContent :LinearLayout = view.findViewById(R.id.llContent)
    }
}