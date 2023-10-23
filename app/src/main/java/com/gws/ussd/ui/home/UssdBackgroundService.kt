package com.gws.ussd.ui.home

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import com.gws.networking.repository.Synchronizer
import javax.inject.Inject
import timber.log.Timber

@AndroidEntryPoint
class UssdBackgroundService : Service() {

    @Inject
     lateinit var synchronizer: Synchronizer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("phoneNumber")?.let {
            Timber.e("Sync: phone Number $it")
            synchronizer.createFakeUssdDataList(it)

            Thread {
                var counter = 1
                synchronizer?.fakeUssdList?.size?.let {
                    Timber.e("Sync: Sync started $it")
                    while (counter < 99) {
                        // Log the current number

                        // Increment the counter
                        counter++
                        synchronizer?.fakeUssdList?.get(counter)?.let {

                            synchronizer?.updateList(it)
                            Timber.e("Sync: Sync success ${it.id} ${it.etat}")
                        }

                        // Sleep for a moment (optional)
                        try {
                            Thread.sleep(2000) // Sleep for 1 second
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
            }.start()
        }
        return START_STICKY
    }

}
