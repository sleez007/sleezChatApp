package ng.novacore.sleezchat.utils

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import ng.novacore.sleezchat.db.entity.UsersEntity
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

object ContactsUtil {
    /**
     * Retrieve all contacts from phone
     */
    fun retrieveContacts(context: Context): ArrayList<UsersEntity>{

        val contactList = ArrayList<UsersEntity>()

        val contentResolver = context.contentResolver
        val projection = listOf<String>(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.LABEL,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
        )
        val cur : Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,projection.toTypedArray(),null,null,null)
        cur?.let {
            if(it.count > 0){
                while(it.moveToNext()){
                    var fullName: String? = null

                    val name = it.getStringOrNull(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber = it.getStringOrNull(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val contactId = it.getInt(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
                    val image_uri = it.getStringOrNull(it.getColumnIndex( ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                    name?.let {
                        fullName = if (it.contains("\\s+")) {
                            val nameSplit = it.split("\\s+")
                            nameSplit[0] + nameSplit[1]
                        }else{
                            name
                        }
                    }

                    phoneNumber?.let {tel->
                        val regExp = Regex("[^\\d]")
                        val phoneDigits = phoneNumber.replace(regExp, "")
                        val isValid = !(phoneDigits.length < 6 || phoneDigits.length > 13)
                        val phNumberProto: String = phoneDigits.replace(Regex("-"), "")

                        var phoneNo : String
                        if(phoneDigits.length != 10){
                            phoneNo = "+".plus(phNumberProto)
                        }else{
                            phoneNo =phNumberProto
                        }

                        var phoneNumTemp: String
                        val intTelNum : Phonenumber.PhoneNumber? = formatNumber(phoneNumber)
                        intTelNum?.let {standardTel ->
                            phoneNumTemp = standardTel.nationalNumber.toString()
                            Timber.i("STANDARD NUM $phoneNumTemp")
                            if(isValid){
                                val contactInfo =
                                    UsersEntity(
                                        contactID = contactId,
                                        contactName = fullName,
                                        phoneNumber = phoneNo,
                                        phoneQuery = phoneNumTemp,
                                        image = image_uri,
                                        _id = contactId.toString()
                                    )

                                var isInList : Boolean = true
                                val listSize : Int = contactList.size
                                if(listSize == 0) contactList.add(contactInfo)
                                //remove duplicate numbers
                                for (i in 0 until listSize) {
                                    isInList = contactList[i].phoneNumber?.trim().equals(phoneNo.trim())
                                    if(isInList) break
                                }

                                if(!isInList) contactList.add(contactInfo)
                            }

                        }
                    }


                }
            }
            cur.close()
        }
        return contactList
    }

    /**
     * @param phoneNumber : A valid phone number of type string
     * to be formatted.
     * @return A nullable Phonenumber.PhoneNumber?
     */
     fun formatNumber(phoneNumber : String): Phonenumber.PhoneNumber?{
        val defaultCountry = Locale.getDefault().country
         var parsedNumber: Phonenumber.PhoneNumber? = null
        try{
            val phoneUtil = PhoneNumberUtil.getInstance()
            parsedNumber = phoneUtil.parse(phoneNumber, defaultCountry)
        }catch(ex: NumberParseException){
            ex.printStackTrace()
        }finally {
            return parsedNumber
        }
    }
}