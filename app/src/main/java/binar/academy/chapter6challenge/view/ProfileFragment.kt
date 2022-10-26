package binar.academy.chapter6challenge.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import binar.academy.chapter6challenge.R
import binar.academy.chapter6challenge.databinding.FragmentProfileBinding
import binar.academy.chapter6challenge.viewmodel.UserViewModel

class ProfileFragment : DialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    lateinit var viewModel: UserViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupViewModel()
        setupView()
        observe()
        return binding.root
    }

    private fun observe() {
        viewModel.dataUser.observe(viewLifecycleOwner) {
            val data = it.split("|")
            binding.profileUsername.setText(data[0])
            binding.profileName.setText(data[1])
            binding.password.setText(data[2])
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    private fun setupView() {
        viewModel.getUserData(viewLifecycleOwner)
        updateUser()
    }

    private fun updateUser() {
        val username = binding.profileUsername.text.toString()
        val email = binding.profileName.text.toString()
        val password = binding.password.text.toString()

        when {
            username == "" -> {
                binding.profileUsername.error = "Harus Diisi"
            }
            email == "" -> {
                binding.profileName.error = "Harus Diisi"
            }
            password == "" -> {
                binding.password.error = "Harus Diisi"
            }
            else -> {
                viewModel.saveData(email, username, password)
                Toast.makeText(mContext, "Update Berhasil", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}