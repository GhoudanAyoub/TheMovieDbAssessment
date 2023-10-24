package com.gws.ussd.ui.home

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.UssdResponseCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.gws.common.utils.UssdVerticalItemDecoration
import com.gws.networking.response.ResourceResponse
import com.gws.ussd.MainActivity
import com.gws.ussd.databinding.FragmentHomeBinding
import com.gws.ussd.service.UssdBackgroundService
import dagger.hilt.android.AndroidEntryPoint
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlin.math.roundToInt
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {


//    @Inject
//    lateinit var ussdApi: USSDApi

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        serviceIntent = Intent(requireContext(), UssdBackgroundService::class.java)

        // Permission denied; request the permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_NUMBERS)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            showPermissionRationaleDialog(requireContext())
        }
        setupMoviesList()

        binding.valider.setOnClickListener {
            (requireActivity() as? MainActivity)?.hideSoftKeyboard()
            requireActivity().startService(serviceIntent)
            (requireActivity() as? MainActivity)?.showLoader()
            viewModel.startBackgroundInfo()
        }
    }

    private fun showPermissionRationaleDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setTitle("Permission Required")
        dialogBuilder.setMessage("To provide you with the best service, we need access to your phone number. Please grant the permission.")
        dialogBuilder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->

            // User has previously denied the permission without explanation, provide an option to open settings

            ActivityCompat.requestPermissions(
                requireActivity(), // Cast the context to an Activity
                arrayOf(Manifest.permission.READ_PHONE_NUMBERS,Manifest.permission.CALL_PHONE),
                123
            )
            dialog.dismiss()
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
            // Handle the case where the user clicks "Cancel" (optional)
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }


    private fun subscribe() {
        viewModel.fakeUssd.observe(viewLifecycleOwner) {
            when (it) {
                is ResourceResponse.Loading -> {
                }

                is ResourceResponse.Error -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                }

                is ResourceResponse.Success -> {
                    (requireActivity() as? MainActivity)?.hideLoader()
                    ussdListAdapter.setUssdList(it.data ?: emptyList())
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
        val itemDecoration = UssdVerticalItemDecoration(1, itemSpacing.roundToInt())
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

    fun runussd2(){
//        var result = ""
//        var finish = false
//
//        val map = hashMapOf(
//            "KEY_LOGIN" to listOf("espere", "waiting", "loading", "esperando"),
//            "KEY_ERROR" to listOf("problema", "problem", "error", "null"))
//        ussdApi.callUSSDInvoke(requireContext(),  binding.inputEditText.text.toString(), map,
//            object : USSDController.CallbackInvoke {
//                override fun responseInvoke(message: String) {
//                    result += "\n-\n$message"
//                    Timber.i("UssdState onGoing: $result")
//                    ussdApi.send("2") {
//                        result += "\n-\n$it"
//                        ussdApi.send("1") {
//                            result += "\n-\n$it"
//                            finish = true
//                        }
//                    }
//                }
//
//                override fun over(message: String) {
//                    result += "\n-\n$message"
//                    when {
//                        finish -> Timber.i("Successful")
//                        else -> Timber.i("Error")
//                    }
//                    Timber.i("result")
//                }
//            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssd() {
        val telephonyManager =
            requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        try {
            telephonyManager.sendUssdRequest("#111*3*6*3*6#", object : UssdResponseCallback() {
                override fun onReceiveUssdResponse(
                    telephonyManager: TelephonyManager,
                    request: String,
                    response: CharSequence
                ) {
                    // Handle the USSD response
                    Timber.e("Sync: response $response")
                }

                override fun onReceiveUssdResponseFailed(
                    telephonyManager: TelephonyManager,
                    request: String,
                    failureCode: Int
                ) {
                    // Handle failure
                    Timber.e("Sync: failureCode $failureCode")
                }
            }, null)

        } catch (e: SecurityException) {
            // Permission denied; request the permission
        }
    }

    fun getPhoneNumber(context: Context): String {
//        detectPhoneNumber()
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var phoneNumber = ""

        try {
            phoneNumber = telephonyManager.line1Number ?: ""
        } catch (e: SecurityException) {
            // Permission denied; request the permission
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
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
}
