@file:Suppress("RedundantNullableReturnType", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused", "unused")

package binar.academy.chapter6challenge.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import binar.academy.chapter6challenge.databinding.FragmentSplashScreenBinding

@Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused")
class SplashScreenFragment : Fragment() {

    lateinit var binding: FragmentSplashScreenBinding

    @Suppress("RedundantNullableReturnType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
}