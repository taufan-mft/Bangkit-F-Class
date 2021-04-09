package com.muhammadfurqan.bangkitfclass.sqlite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadfurqan.bangkitfclass.R
import com.muhammadfurqan.bangkitfclass.sqlite.db.BookDatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 * Contact : 081375496583
 *
 * Step :
 * 1. Fork our Repository (https://github.com/fueerqan/Bangkit-F-Class)
 *
 * CHALLENGE :
 * 1. Recycler View to show all of the data, previously we only show them in toast
 * 2. Add Function to edit the books data for each item in your Recycler View Items
 * 3. Add Function to delete the books data for each item in your Recycler View Items
 * 4. Notify Data Changes for you Recycler View
 *
 * Reward : Rp20.000 Go-Pay / OVO
 * Limit : No Limit Person
 * Dateline : 23.00
 *
 * Submit to https://forms.gle/CytSQSyQDJeivpkd7
 *
 */

class SQLiteActivity : AppCompatActivity() {

    private lateinit var etBookName: AppCompatEditText
    private lateinit var btnAdd: AppCompatButton
    private lateinit var btnRead: AppCompatButton
    private lateinit var rvBooks: RecyclerView
    private lateinit var bookAdapter: BookAdapter
    lateinit var listBook: List<BookModel>
    private val bookDb: BookDatabaseManager by lazy {
        BookDatabaseManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)
        listBook = listOf()
        etBookName = findViewById(R.id.et_book_name)
        btnAdd = findViewById(R.id.btn_add)
        btnRead = findViewById(R.id.btn_read)
        btnRead.visibility = View.GONE
        rvBooks = findViewById(R.id.rv_book_list)
        bookAdapter = BookAdapter(listBook, this)
        onRead()
        rvBooks.apply {
            adapter = bookAdapter
            layoutManager = LinearLayoutManager(context)
        }
        btnAdd.setOnClickListener {
            onAdd()
        }

        btnRead.setOnClickListener {
            onRead()
        }
    }


    private fun onAdd() {
        val bookName = etBookName.text.toString()
        if (bookName.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.Main) {
                bookDb.saveData(bookName)
                bookAdapter.setData(bookDb.getData())
            }
            etBookName.setText("")
        } else {
            Toast.makeText(this, "Please fill in the book name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onRead() {
        val bookList = bookDb.getData()
        bookAdapter.setData(bookList)
        val bookListString = bookList.joinToString(separator = "\n") {
            "Book ${it.id} is ${it.name}"
        }
        Toast.makeText(this, bookListString, Toast.LENGTH_SHORT).show()
    }

}