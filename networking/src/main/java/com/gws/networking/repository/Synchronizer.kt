package com.gws.networking.repository

import android.content.Context
import androidx.work.WorkManager
import chari.groupewib.com.networking.handler.UssdHandler
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import com.gws.local_models.models.Ussd
import java.util.Date
import java.util.Random
import javax.inject.Inject
import timber.log.Timber

@Reusable
class Synchronizer @Inject constructor(
    @ApplicationContext val context: Context,
    val ussdHandler: UssdHandler
) {

    val fakeUssdList = mutableListOf<Ussd>()

    fun updateList(ussd: Ussd) {
        ussdHandler.addOrUpdateUssd(ussd, resultFunc = {
            Timber.e("Ussd: Updated success")
        })
    }

    // Function to generate a random string of a specified length
    fun generateRandomString(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val result = StringBuilder(length)
        repeat(length) {
            result.append(characters[random.nextInt(characters.length)])
        }
        return result.toString()
    }

    /** Function to generate a random date within a given range **/
    fun generateRandomDate(startDate: Date, endDate: Date): Date {
        val random = Random().nextLong()
        val diff = endDate.time - startDate.time
        return Date(startDate.time + (random % diff))
    }

    fun createFakeUssdDataList(): List<Ussd> {

        val startDate = Date(120, 0, 1) // Start date: January 1, 2020
        val endDate = Date(123, 11, 31) // End date: December 31, 2023

        for (i in 1..101) {
            val fakeUssd = Ussd(
                id = i.toString(),
                idaccount = i.toString(),
                num = "0639603352",
                ussd = "#111*2*1#",
                sumstep = (Random().nextInt(10) + 1).toString(),
                step1 = "1",
                step2 = "07${Random().nextInt(1000000000)}",
                step3 = "1",
                step4 = Random().nextInt(10000).toString(),
                step5 = generateRandomString(6),
                step6 = generateRandomString(6),
                step7 = "",
                step8 = "",
                step9 = "",
                step10 = "",
                iduser = "0",
                sim = "",
                date = generateRandomDate(startDate, endDate).toString(),
                heure = "${Random().nextInt(24)}:${Random().nextInt(60)}:${Random().nextInt(60)}",
                etat = "0",
                reponceussd = "",
                idlogin = "0",
                datesaisie = generateRandomDate(startDate, endDate).toString()
            )
            fakeUssdList.add(fakeUssd)
        }

        return fakeUssdList
    }

}
