package binar.academy.chapter6challenge.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import binar.academy.chapter6challenge.R
import binar.academy.chapter6challenge.databinding.FragmentProfileBinding
import binar.academy.chapter6challenge.viewmodel.BlurViewModel
import binar.academy.chapter6challenge.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ProfileFragment : DialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    lateinit var vmUser: UserViewModel
    private val vmBlur: BlurViewModel by lazy {
        BlurViewModel(requireActivity().application)
    }
    private var image_uri: Uri? = null
    private val resultGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            Log.d("URI_IMG", result.toString())
            binding.ivProfile.setImageURI(result)
            image_uri = result!!
            vmBlur.setImageUri(result)
        }

    private val REQUEST_CODE_PERMISSION = 100
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

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
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.btnivProfile.setOnClickListener {
            checkingPermissions()
        }

        binding.ivProfile.setOnClickListener {
            checkingPermissions()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso)

        setupViewModel()
        setupView()
        observe()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setbgImage()
    }

    private fun observe() {
        vmUser.dataUser.observe(viewLifecycleOwner) {
            val data = it.split("|")
            binding.profileUsername.setText(data[0])
            binding.profileEmail.setText(data[1])
            binding.profilePassword.setText(data[2])
        }
        var image =
            BitmapFactory.decodeFile(requireActivity().applicationContext.filesDir.path + File.separator + "profiles" + File.separator + "img-profile.png")
        binding.ivProfile.setImageBitmap(image)
    }

    private fun setupViewModel() {
        vmUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]
    }

    private fun setupView() {
        vmUser.getUserData(viewLifecycleOwner)

        binding.btnUpdate.setOnClickListener {
            updateUser()
        }

        binding.btnLogout.setOnClickListener {
            if (GoogleSignIn.getLastSignedInAccount(mContext) != null) {
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    vmUser.saveData("", "", "")
                    vmUser.saveLoginStatus(false)
                    val intent = Intent(mContext, BaseActivity::class.java)
                    startActivity(intent)
                }
            } else {
                vmUser.saveData("", "", "")
                vmUser.saveLoginStatus(false)
                startActivity(Intent(mContext, BaseActivity::class.java))
            }
        }
    }

    private fun updateUser() {
        val username = binding.profileUsername.text.toString()
        val email = binding.profileEmail.text.toString()
        val password = binding.profilePassword.text.toString()

        if (image_uri != null) {
            saveImage()
        }

        when {
            username == "" -> {
                binding.profileUsername.error = "Please fill out this field."
            }
            email == "" -> {
                binding.profileEmail.error = "Please fill out this field."
            }
            password == "" -> {
                binding.profilePassword.error = "Please fill out this field."
            }
            else -> {
                vmUser.saveData(username, email, password)
                Toast.makeText(mContext,
                    "Your profile has been updated successfully !",
                    Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }

    private fun setbgImage() {
        var image =
            BitmapFactory.decodeFile(requireActivity().applicationContext.filesDir.path + File.separator + "blur_outputs" + File.separator + "IMG-BLURRED.png")
        binding.bgProfile.setImageBitmap(image)
    }

    private fun saveImage() {
        val resolver = requireActivity().applicationContext.contentResolver
        val picture = BitmapFactory.decodeStream(
            resolver.openInputStream(Uri.parse(image_uri.toString())))
        saveImageProfile(requireContext(), picture)
        vmBlur.applyBlur()
    }

    private fun accessGallery() {
        requireActivity().intent.type = "image/*"
        resultGallery.launch("image/*")
    }

    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            accessGallery()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun saveImageProfile(applicationContext: Context, bitmap: Bitmap): Uri {
        val name = "img-profile.png"
        val outputDir = File(applicationContext.filesDir, "profiles")
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } finally {
            out?.let {
                try {
                    it.close()
                } catch (ignore: IOException) {
                }

            }
        }
        Log.d("URI_IMG", Uri.fromFile(outputFile).toString())
        return Uri.fromFile(outputFile)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}