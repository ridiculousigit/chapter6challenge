@file:Suppress("UsePropertyAccessSyntax", "MemberVisibilityCanBePrivate",
    "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate",
    "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate",
    "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused")

package binar.academy.chapter6challenge.view

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.academy.chapter6challenge.adapter.AgentAdapter
import binar.academy.chapter6challenge.databinding.FragmentHomeBinding
import binar.academy.chapter6challenge.model.AgentResponse
import binar.academy.chapter6challenge.viewmodel.HomeViewModel
import binar.academy.chapter6challenge.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@Suppress("UsePropertyAccessSyntax", "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate",
    "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate",
    "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate",
    "MemberVisibilityCanBePrivate", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused", "unused")
@AndroidEntryPoint
class HomeFragment : Fragment(), AgentAdapter.OnAgentClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    lateinit var vmUser: UserViewModel
    private lateinit var agentAdapter: AgentAdapter
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViewModel()
        setupView()
        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        vmUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    private fun setupView() {
        agentAdapter = AgentAdapter(this)
        viewModel.callListAgent()
        binding.tvEdit.setOnClickListener {
            val profileDialog = ProfileFragment()
            activity?.supportFragmentManager?.let { it1 ->
                profileDialog.show(
                    it1,
                    "PROFILE_FRAGMENT"
                )
            }
        }
        binding.rvAgent.hasFixedSize()
        binding.rvAgent.layoutManager = LinearLayoutManager(mContext)
        binding.rvAgent.adapter = agentAdapter
        viewModel.listAgent.observe(viewLifecycleOwner) {
            if (it != null) {
                agentAdapter.addData(it)
            }
        }
        vmUser.getUsername(viewLifecycleOwner)
        vmUser.livedataUsername.observe(viewLifecycleOwner) {
            binding.tvUsername.setText(it)
        }
        val image = BitmapFactory.decodeFile(requireActivity().applicationContext.filesDir.path + File.separator +"profiles"+ File.separator +"img-profile.png")
        binding.ivUser.setImageBitmap(image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAgentSelected(agentResponse: AgentResponse) {
        val bundle = Bundle()
        bundle.putSerializable("AGENT", agentResponse)
        val dialog = DetailFragment()
        dialog.arguments = bundle
        activity?.supportFragmentManager?.let { dialog.show(it, "DETAIL_FRAGMENT") }
    }

}