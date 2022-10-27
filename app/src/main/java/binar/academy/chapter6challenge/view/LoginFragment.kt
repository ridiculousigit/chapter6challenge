package binar.academy.chapter6challenge.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import binar.academy.chapter6challenge.R
import binar.academy.chapter6challenge.databinding.FragmentLoginBinding
import binar.academy.chapter6challenge.viewmodel.UserViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private var savedEmail: String = ""
    private var savedPassword: String = ""
    lateinit var viewModel: UserViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupViewModel()
        setupView()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    private fun setupView() {
        viewModel.callDataUser(viewLifecycleOwner)
        binding.registerHere.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_base, RegisterFragment())
                commit()
            }
        }
        binding.btnLogin.setOnClickListener {
            loginAction()
        }
        viewModel.livedataEmail.observe(viewLifecycleOwner, ({
            savedEmail = it
        }))
        viewModel.livedataPassword.observe(viewLifecycleOwner, ({
            savedPassword = it
        }))
    }

    private fun loginAction() {
        val email = binding.loginEmail.text.toString()
        val password = binding.loginPassword.text.toString()
        when {
            email == "" -> {
                binding.loginEmail.error = "Please fill out this field."
            }
            password == "" -> {
                binding.loginPassword.error = "Please fill out this field."
            }
            email != savedEmail -> {
                Toast.makeText(mContext, "Incorrect Username !", Toast.LENGTH_SHORT).show()
            }
            password != savedPassword -> {
                Toast.makeText(mContext, "Incorrect Password", Toast.LENGTH_SHORT).show()
            }
            email == savedEmail && password == savedPassword -> {
                viewModel.saveLoginStatus(true)
                startActivity(Intent(mContext, MainActivity::class.java))
                activity?.finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}