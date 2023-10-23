package com.gws.ussd.ui.home

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import dagger.hilt.android.AndroidEntryPoint
import com.gws.common.utils.MoviesVerticalItemDecoration
import com.gws.ussd.MainActivity
import com.gws.ussd.databinding.FragmentHomeBinding
import com.gws.networking.response.ResourceResponse
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlin.math.roundToInt
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var serviceIntent: Intent


    private val ussdListAdapter: UssdListAdapter by lazy {
        UssdListAdapter()
    }
    private val phoneNumberUtil: PhoneNumberUtil by lazy {
        PhoneNumberUtil.createInstance(requireContext())
    }

    private val hintPhoneNumberLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            if (result != null && result.data != null) {
                val data = result.data
                val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
                val retrievedPhoneNumber = credential?.id
                val phoneNumberProto = phoneNumberUtil.parse(
                    retrievedPhoneNumber,
                    "MA"
                )
                val retrievedPhoneNumberFormatted = phoneNumberUtil.format(
                    phoneNumberProto,
                    PhoneNumberUtil.PhoneNumberFormat.NATIONAL
                )
                binding.inputEditText.setText(
                    retrievedPhoneNumberFormatted.replace("-", "")
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceIntent = Intent(requireContext(), UssdBackgroundService::class.java)

        val phone = getPhoneNumber(requireContext())
        Timber.e("Sync: phone $phone")
        setupMoviesList()
//        detectPhoneNumber()

        binding.valider.setOnClickListener {
            (requireActivity() as? MainActivity)?.hideSoftKeyboard()
            serviceIntent.putExtra("phoneNumber", binding.inputEditText.text.toString())
            viewModel.loadFakeUssd(binding.inputEditText.text.toString())
            requireActivity().startService(serviceIntent)
            (requireActivity() as? MainActivity)?.showLoader()
            viewModel.startBackgroundInfo()
        }
    }

    fun getPhoneNumber(context: Context): String {
        detectPhoneNumber()
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var phoneNumber = ""

        try {
            phoneNumber = telephonyManager.line1Number ?: ""
            binding.inputEditText.setText(
                phoneNumber.replace("-", "")
            )
        } catch (e: SecurityException) {
            // Permission denied; request the permission
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // You can show a rationale for needing the permission here
                // For example, display a dialog explaining why the permission is required
                // Once the user acknowledges the rationale, request the permission
                showPermissionRationaleDialog(requireContext())
            }
        }

        return phoneNumber
    }

    private fun showPermissionRationaleDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Permission Required")
        dialogBuilder.setMessage("To provide you with the best service, we need access to your phone number. Please grant the permission.")
        dialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
            // Request the permission when the user clicks "OK"

            // User has previously denied the permission without explanation, provide an option to open settings

            ActivityCompat.requestPermissions(
                requireActivity(), // Cast the context to an Activity
                arrayOf(Manifest.permission.READ_PHONE_NUMBERS),
                123
            )
//            openAppSettings(context)
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
            // Handle the case where the user clicks "Cancel" (optional)
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    private fun detectPhoneNumber() {
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val intent: PendingIntent = Credentials.getClient(
            requireActivity()
        ).getHintPickerIntent(hintRequest)
        val intentSenderRequest = IntentSenderRequest.Builder(intent.intentSender)
        hintPhoneNumberLauncher.launch(intentSenderRequest.build())
    }

    private fun subscribe() {
        viewModel.fakeUssd.observe(viewLifecycleOwner) {
            when (it) {
                is ResourceResponse.Loading -> {
                    binding.ussdInputLayout.visibility = View.VISIBLE
                    binding.ussdLayout.visibility = View.GONE
                }

                is ResourceResponse.Error -> {
                    binding.ussdInputLayout.visibility = View.VISIBLE
                    binding.ussdLayout.visibility = View.GONE
                    (requireActivity() as? MainActivity)?.hideLoader()
                }

                is ResourceResponse.Success -> {
                    binding.ussdInputLayout.visibility = View.GONE
                    binding.ussdLayout.visibility = View.VISIBLE
                    (requireActivity() as? MainActivity)?.hideLoader()
                    ussdListAdapter.setUssdList(it.data ?: emptyList())
                    Timber.e("Sync: subscribe started ${it.data}")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        subscribe()
    }

    private fun setupMoviesList() {
        val itemSpacing =
            resources.getDimension(com.gws.ussd.ui_core.R.dimen.movie_item_spacing)
        val itemDecoration = MoviesVerticalItemDecoration(1, itemSpacing.roundToInt())
        val recyclerViewLayoutManager = GridLayoutManager(
            requireContext(),
            1
        )
        binding.ussdRecycler.apply {
            clipToPadding = false
            clipChildren = false
            adapter = ussdListAdapter
            layoutManager = recyclerViewLayoutManager
            setHasFixedSize(true)
            if (itemDecorationCount == 0) {
                addItemDecoration(itemDecoration)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearList()
    }
}
