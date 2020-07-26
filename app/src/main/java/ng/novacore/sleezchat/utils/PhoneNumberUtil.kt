package ng.novacore.sleezchat.utils

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

object PhoneNumberUtility {
    fun getPhoneNumber(phoneNumber: String, defaultRegion: String?): Phonenumber.PhoneNumber?{
        return try {
            PhoneNumberUtil.getInstance().parse(phoneNumber,defaultRegion )
        }catch (ex: NumberParseException){
            ex.printStackTrace()
            null
        }
    }

    fun isValidPhoneNo(phoneNo: Phonenumber.PhoneNumber?): Boolean= phoneNo!=null && PhoneNumberUtil.getInstance().isValidNumber(phoneNo)

    fun getInternationalNumber(phoneNo: Phonenumber.PhoneNumber?):String = PhoneNumberUtil.getInstance().format(phoneNo,PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)


}