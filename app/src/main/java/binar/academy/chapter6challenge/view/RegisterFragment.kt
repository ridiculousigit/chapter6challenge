package binar.academy.chapter6challenge.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import binar.academy.chapter6challenge.R
import binar.academy.chapter6challenge.databinding.FragmentHomeBinding
import binar.academy.chapter6challenge.databinding.FragmentRegisterBinding
import binar.academy.chapter6challenge.viewmodel.UserViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: UserViewModel
    lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setupViewModel()
        setupView()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    private fun setupView() {
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username = binding.registerUsername.text.toString()
        val email = binding.registerEmail.text.toString()
        val password = binding.registerPassword.text.toString()
        val passwordConfirm = binding.registerConfirm.text.toString()

        when {
            username == "" -> {
                binding.registerUsername.error = "Harus Diisi"
            }
            email == "" -> {
                binding.registerEmail.error = "Harus Diisi"
            }
            password == "" -> {
                binding.registerPassword.error = "Harus Diisi"
            }
            passwordConfirm == "" -> {
                binding.registerConfirm.error = "Harus Diisi"
            }
            password != passwordConfirm -> {
                Toast.makeText(mContext, "Password tidak sama", Toast.LENGTH_SHORT).show()
            }
            else -> {
                viewModel.saveData(email, username, password)
                Toast.makeText(mContext, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragment_base, LoginFragment())
                    commit()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}