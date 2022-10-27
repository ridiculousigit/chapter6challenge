package binar.academy.chapter6challenge.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import binar.academy.chapter6challenge.R
import binar.academy.chapter6challenge.database.AgentModel
import binar.academy.chapter6challenge.databinding.FragmentDetailBinding
import binar.academy.chapter6challenge.model.AgentResponse
import binar.academy.chapter6challenge.viewmodel.HomeViewModel
import com.bumptech.glide.Glide

class DetailFragment : DialogFragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var vmHome: HomeViewModel
    lateinit var agent: AgentResponse

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
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setupViewModel()
        setupView()
        observe()
        return binding.root
    }

    private fun setupViewModel() {
        vmHome = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    private fun setupView() {
        agent = arguments?.getSerializable("AGENT") as AgentResponse
        vmHome.checkIfFavoriteAgent(agent.name)
        binding.apply {
            detailName.text = agent.name
            detailRole.text = agent.role
            detailBiography.text = agent.desc
            setImageToView(detailImage, agent.image)
            setImageToView(detailIcon, agent.roleIcon)
            setImageToView(cAbility, agent.skillC)
            setImageToView(qAbility, agent.skillQ)
            setImageToView(eAbility, agent.skillE)
            setImageToView(xAbility, agent.skillX)
        }
    }

    private fun observe() {
        vmHome.isFavoriteAgent.observe(viewLifecycleOwner) {
            if (it) {
                setFilledFavoriteIcon(true)
                binding.btnFavorite.setOnClickListener {
                    vmHome.deleteFavoriteAgent(agent.name)
                    setFilledFavoriteIcon(false)
                    Toast.makeText(
                        mContext,
                        "Successfully added to favorites !",
                        Toast.LENGTH_SHORT
                    ).show()
                    vmHome.checkIfFavoriteAgent(agent.name)
                }
            } else {
                setFilledFavoriteIcon(false)
                binding.btnFavorite.setOnClickListener {
                    val agentModel = AgentModel(
                        agent.id.toInt(),
                        agent.image,
                        agent.name,
                        agent.role,
                        agent.roleIcon,
                        agent.skillQ,
                        agent.skillE,
                        agent.skillC,
                        agent.skillX,
                        agent.desc
                    )
                    vmHome.addFavoriteAgent(agentModel)
                    setFilledFavoriteIcon(true)
                    Toast.makeText(
                        mContext,
                        "Successfully added to favorites !",
                        Toast.LENGTH_SHORT
                    ).show()
                    vmHome.checkIfFavoriteAgent(agent.name)
                }
            }
        }
    }

    private fun setFilledFavoriteIcon(isFilled: Boolean) {
        if (isFilled) {
            binding.btnFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_favorite_filled
                )
            )
        } else {
            binding.btnFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_favorite
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setImageToView(view: ImageView, url: String) {
        Glide.with(mContext)
            .load(url)
            .into(view)
    }
}