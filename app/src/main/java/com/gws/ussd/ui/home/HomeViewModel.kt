package com.gws.ussd.ui.home

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chari.groupewib.com.networking.handler.UssdHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import com.gws.local_models.models.Ussd
import com.gws.networking.repository.Synchronizer
import com.gws.networking.response.ResourceResponse
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
    val synchronizer: Synchronizer,
    val ussdHandler: UssdHandler
) : ViewModel() {

    private var UssdLiveData: MutableLiveData<ResourceResponse<List<Ussd>>> = MutableLiveData()
    val fakeUssd: LiveData<ResourceResponse<List<Ussd>>> = UssdLiveData

    val handler = Handler()
    private val delayMillis = 2500L  // 2.5 seconds

    private val backgroundInfoRunnable = object : Runnable {
        override fun run() {
            getBackgroundInfo()
            handler.postDelayed(this, delayMillis)
        }
    }

    fun startBackgroundInfo() {
        handler.postDelayed(backgroundInfoRunnable, delayMillis)
    }

    fun clearList(){
        ussdHandler.clearUssd()
    }

    /** loadFakeUssd  **/
    fun loadFakeUssd() {
        synchronizer.createFakeUssdDataList()

        Timber.e("Sync: HomeViewModel started ${synchronizer.fakeUssdList.size}")
    }

    /** run background thread function**/
    fun getBackgroundInfo() {
        viewModelScope.launch {
            UssdLiveData.value = ResourceResponse.Loading()
            ussdHandler.getOrCreateUssd().apply {
                if (this.ussds?.isEmpty() == true)
                    UssdLiveData.value = ResourceResponse.Error(Throwable("Error"))
                else this.ussds?.let {
                    Timber.e("Sync: getBackgroundInfo started ${it.size}" +
                            " ${it.filter { it.etat == "1" }.size} "
                    )
                    UssdLiveData.value = ResourceResponse.Success(it)

                }
            }
        }
    }

}
