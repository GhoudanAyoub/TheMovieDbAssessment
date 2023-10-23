package ghoudan.ayoub.local_models.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Ussd(

    var id: String? = null,
    var idaccount: String? = null,
    var num: String? = null,
    var ussd: String? = null,
    var sumstep: String? = null,
    var step1: String? = null,
    var step2: String? = null,
    var step3: String? = null,
    var step4: String? = null,
    var step5: String? = null,
    var step6: String? = null,
    var step7: String? = null,
    var step8: String? = null,
    var step9: String? = null,
    var step10: String? = null,
    var iduser: String? = null,
    var sim: String? = null,
    var date: String? = null,
    var heure: String? = null,
    var etat: String? = null,
    var reponceussd: String? = null,
    var idlogin: String? = null,
    var datesaisie: String? = null

) : Parcelable
