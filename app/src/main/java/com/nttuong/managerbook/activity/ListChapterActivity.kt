package com.nttuong.managerbook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.nttuong.managerbook.R
import com.nttuong.managerbook.databinding.ActivityListChapterBinding

class ListChapterActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.rotate_open_anim
    ) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.rotate_close_anim
    ) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.from_bottom_anim
    ) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,
        R.anim.to_bottom_anim
    ) }
    private var clicked = false

    private lateinit var binding: ActivityListChapterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMenu.setOnClickListener {
            onShowButtonClicked()
        }
        binding.btnAdd.setOnClickListener {

        }
        binding.btnEdit.setOnClickListener {

        }
        binding.btnDelete.setOnClickListener {

        }
    }

    private fun onShowButtonClicked(){
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.visibility = View.VISIBLE
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            binding.btnAdd.visibility = View.INVISIBLE
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnDelete.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.startAnimation(fromBottom)
            binding.btnEdit.startAnimation(fromBottom)
            binding.btnDelete.startAnimation(fromBottom)
            binding.btnMenu.startAnimation(rotateOpen)
        } else {
            binding.btnAdd.startAnimation(toBottom)
            binding.btnEdit.startAnimation(toBottom)
            binding.btnDelete.startAnimation(toBottom)
            binding.btnMenu.startAnimation(rotateClose)
        }
    }

    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            binding.btnAdd.isClickable = true
            binding.btnEdit.isClickable = true
            binding.btnDelete.isClickable = true
        } else {
            binding.btnAdd.isClickable = false
            binding.btnEdit.isClickable = false
            binding.btnDelete.isClickable = false
        }
    }
}