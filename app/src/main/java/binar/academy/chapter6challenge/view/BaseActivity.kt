package binar.academy.chapter6challenge.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import binar.academy.chapter6challenge.R
import binar.academy.chapter6challenge.databinding.ActivityBaseBinding
import binar.academy.chapter6challenge.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding
    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        setFragmentTo(SplashScreenFragment())

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.checkIsLogin(this)
        }, 3000)

        viewModel.livedataIsLogin.observe(this) {
            if (it) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                setFragmentTo(LoginFragment())
            }
        }

    }

    private fun setFragmentTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_base, fragment)
            commit()
        }
    }

}