package binar.academy.chapter6challenge.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import binar.academy.chapter6challenge.adapter.AgentAdapterFavorite
import binar.academy.chapter6challenge.database.AgentModel
import binar.academy.chapter6challenge.databinding.FragmentFavoriteBinding
import binar.academy.chapter6challenge.model.AgentResponse
import binar.academy.chapter6challenge.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), AgentAdapterFavorite.OnAgentFavoriteClick {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var agentAdapter: AgentAdapterFavorite
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
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        setupViewModel()
        setupView()
        observe()
        return binding.root
    }

    private fun observe() {
        viewModel.listAgentFavorite.observe(viewLifecycleOwner) {
            if (it != null) {
                agentAdapter.clearList()
                agentAdapter.addData(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllFavoriteAgent()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]
    }

    private fun setupView() {
        agentAdapter = AgentAdapterFavorite(this)
        binding.rvFav.hasFixedSize()
        binding.rvFav.layoutManager = LinearLayoutManager(mContext)
        binding.rvFav.adapter = agentAdapter
        binding.swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
                val intent = Intent(mContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }, 3000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAgentSelected(agentResponse: AgentModel) {
        val bundle = Bundle()
        val data = AgentResponse(
            agentResponse.id.toString(),
            agentResponse.image,
            agentResponse.name,
            agentResponse.role,
            agentResponse.roleIcon,
            agentResponse.skillQ,
            agentResponse.skillE,
            agentResponse.skillC,
            agentResponse.skillX,
            agentResponse.desc
        )
        bundle.putSerializable("AGENT", data)
        val dialog = DetailFragment()
        dialog.arguments = bundle
        activity?.supportFragmentManager?.let { dialog.show(it, "DETAIL_FRAGMENT") }
    }

    override fun onAgentDeleted(name: String, position: Int) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Remove from favorites")
        builder.setMessage("Are you sure you want to remove this data ?")
        builder.setPositiveButton("Yes") { dialog, which ->
            viewModel.deleteFavoriteAgent(name)
            agentAdapter.delete(position)
            viewModel.getAllFavoriteAgent()
            Toast.makeText(mContext, "Successfully removed from favorites !", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

}