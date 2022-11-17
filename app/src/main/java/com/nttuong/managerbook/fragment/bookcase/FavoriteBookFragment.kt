package com.nttuong.managerbook.fragment.bookcase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nttuong.managerbook.R
import com.nttuong.managerbook.activity.DetailBookActivity
import com.nttuong.managerbook.adapter.BookManagerAdapter
import com.nttuong.managerbook.databinding.FragmentFavoriteBookBinding
import com.nttuong.managerbook.db.entities.Book
import com.nttuong.managerbook.viewmodel.BookManagerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class FavoriteBookFragment : Fragment(),
    BookManagerAdapter.OnClickItemListener {

    private lateinit var binding: FragmentFavoriteBookBinding
    private val viewModel: BookManagerViewModel by activityViewModels()
    private val fAuth = FirebaseAuth.getInstance()
    private val fStore = FirebaseFirestore.getInstance()
    private lateinit var favoriteBookAdapter: BookManagerAdapter
    private val userUID = fAuth.currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBookBinding.inflate(inflater, container, false)

        favoriteBookAdapter = BookManagerAdapter(this)
        binding.rcFavoriteBook.adapter = favoriteBookAdapter
        binding.rcFavoriteBook.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        if (userUID.isNotEmpty()) {
            val userDB = fStore.collection("User").document(userUID)
                .collection("ListBook").whereEqualTo("Favorite", "yes")
            userDB.addSnapshotListener { value, e ->
                if (e != null) {
                    Toast.makeText(requireContext(), "Không thể lắng nghe dữ liệu trên firebase", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                var listBookFavoriteName = ArrayList<String>()
                for (doc in value!!) {
                    listBookFavoriteName.add(doc.id)
                    Log.d("Log de test", "${doc.id}")
                }
                //get list book from room and post to adapter
                viewModel.getAllBookList.observe(viewLifecycleOwner) { list->
                    list?.let {
                        favoriteBookAdapter.submitList(viewModel.getAllFavoriteBook(it, listBookFavoriteName))
                        Log.d("Log de test", "${viewModel.getAllFavoriteBook(it, listBookFavoriteName)}")
                    }
                }
            }
        }
        return binding.root
    }

    override fun itemClick(book: Book) {
        val detailFavoriteBookIntent = Intent(requireContext(), DetailBookActivity::class.java)
        detailFavoriteBookIntent.putExtra("itemClickBookID", book.bookId.toString())
        detailFavoriteBookIntent.putExtra("itemClickAvatar", book.avatar)
        detailFavoriteBookIntent.putExtra("itemClickName", book.name)
        detailFavoriteBookIntent.putExtra("itemClickAuthor", book.author)
        detailFavoriteBookIntent.putExtra("itemClickCategory", book.category)
        detailFavoriteBookIntent.putExtra("itemClickStatus", book.status)
        detailFavoriteBookIntent.putExtra("itemClickContent", book.content)
        startActivity(detailFavoriteBookIntent)
    }
}