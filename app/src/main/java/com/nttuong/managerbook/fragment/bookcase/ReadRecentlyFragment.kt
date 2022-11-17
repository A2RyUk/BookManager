package com.nttuong.managerbook.fragment.bookcase

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nttuong.managerbook.activity.DetailBookActivity
import com.nttuong.managerbook.adapter.BookManagerAdapter
import com.nttuong.managerbook.databinding.FragmentReadRecentlyBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel

class ReadRecentlyFragment : Fragment(),
    BookManagerAdapter.OnClickItemListener {

    private lateinit var binding: FragmentReadRecentlyBinding
    private val viewModel: BookManagerViewModel by activityViewModels()
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()
    private lateinit var recentBookAdapter: BookManagerAdapter
    private val userUID = fAuth.currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadRecentlyBinding.inflate(inflater, container, false)

        recentBookAdapter = BookManagerAdapter(this)
        binding.rcRecentBook.adapter = recentBookAdapter
        binding.rcRecentBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (userUID.isNotEmpty()) {
            val userDB = fStore.collection("User").document(userUID)
                .collection("RecentBook").document("ReadRecent")
            userDB.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(requireContext(), "Không thể lắng nghe dữ liệu trên firebase", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                val listRecentBook = ArrayList<String>()
                if (snapshot != null && snapshot.exists()) {
                    var book1 = snapshot.getString("Book1")
                    var book2 = snapshot.getString("Book2")
                    var book3 = snapshot.getString("Book3")
                    var book4 = snapshot.getString("Book4")
                    var book5 = snapshot.getString("Book5")
                    if (book1 != "0") {
                        listRecentBook.add(book1!!)
                    }
                    if (book2 != "0") {
                        listRecentBook.add(book2!!)
                    }
                    if (book3 != "0") {
                        listRecentBook.add(book3!!)
                    }
                    if (book4 != "0") {
                        listRecentBook.add(book4!!)
                    }
                    if (book5 != "0") {
                        listRecentBook.add(book5!!)
                    }
                    viewModel.getAllBookList.observe(viewLifecycleOwner) { list->
                        list?.let {
                            recentBookAdapter.submitList(viewModel.listRecentBookOfUser(listRecentBook, it))
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Không có dữ liệu trên firebase", Toast.LENGTH_SHORT).show()
                }
            }

        }
        return binding.root
    }

    override fun itemClick(book: Book) {
        val detailRecentBookIntent = Intent(requireContext(), DetailBookActivity::class.java)
        detailRecentBookIntent.putExtra("itemClickBookID", book.bookId.toString())
        detailRecentBookIntent.putExtra("itemClickAvatar", book.avatar)
        detailRecentBookIntent.putExtra("itemClickName", book.name)
        detailRecentBookIntent.putExtra("itemClickAuthor", book.author)
        detailRecentBookIntent.putExtra("itemClickCategory", book.category)
        detailRecentBookIntent.putExtra("itemClickStatus", book.status)
        detailRecentBookIntent.putExtra("itemClickContent", book.content)
        startActivity(detailRecentBookIntent)
    }

}