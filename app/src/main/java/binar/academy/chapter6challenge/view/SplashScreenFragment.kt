package binar.academy.chapter6challenge.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import binar.academy.chapter6challenge.R
import binar.academy.chapter6challenge.databinding.FragmentSplashScreenBinding
import binar.academy.chapter6challenge.viewmodel.UserViewModel

class SplashScreenFragment : Fragment() {

    lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
}