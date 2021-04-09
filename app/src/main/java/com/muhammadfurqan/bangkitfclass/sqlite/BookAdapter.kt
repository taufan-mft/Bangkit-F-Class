package com.muhammadfurqan.bangkitfclass.sqlite

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.muhammadfurqan.bangkitfclass.R
import com.muhammadfurqan.bangkitfclass.sqlite.db.BookDatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class BookAdapter(private var listBook: List<BookModel>, private var context: Context) : RecyclerView.Adapter<BookAdapter.ListViewHolder>() {
    private val bookDb: BookDatabaseManager by lazy {
        BookDatabaseManager(context)
    }
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.txtNama)
        var rootLayout: LinearLayout = itemView.findViewById(R.id.rootLayout)
    }

    fun setData(list: List<BookModel>) {
        listBook = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_row_book,
            parent,
            false
        )
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val book = listBook[position]
        holder.tvName.text = book.name
        holder.rootLayout.setOnLongClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                bookDb.deleteData(book.id)
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                setData(bookDb.getData())
            }
            true
        }
        holder.rootLayout.setOnClickListener {
            alertDialog(book)
        }

    }
    fun alertDialog(book: BookModel) {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.alert_dialog, null)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(
            context
        )
        alertDialogBuilder.setView(promptsView)
        val userInput = promptsView.findViewById<View>(R.id.etUserInput) as EditText
        alertDialogBuilder
            .setCancelable(false)
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { _, _ ->
                    GlobalScope.launch(Dispatchers.Main) {
                    bookDb.editData(userInput.text.toString(), book.id)
                    Toast.makeText(context, "Edited", Toast.LENGTH_SHORT).show()
                    setData(bookDb.getData())
                }
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        // create alert dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()
    }
    override fun getItemCount(): Int {
        return listBook.size
    }

}