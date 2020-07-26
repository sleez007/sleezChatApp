package ng.novacore.sleezchat.helper

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.util.Base64
import android.util.Log
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList


class AppSignatureHelper(context: Context) : ContextWrapper(context) {
    // Get all package signatures for the current package
    val appSignatures: ArrayList<String>
        get() {
            val appCodes: ArrayList<String> = ArrayList()
            try {
                // Get all package signatures for the current package
                val packageName = packageName
                val packageManager = packageManager
                val signatures: Array<Signature> =
                    packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNATURES
                    ).signatures

                // For each signature create a compatible hash
                for (signature in signatures) {
                    val hash =
                        hash(packageName, signature.toCharsString())
                    if (hash != null) {
                        appCodes.add(String.format("%s", hash))
                    }
                    Log.d(TAG, "Hash $hash")
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, "Unable to find package to obtain hash.", e)
            }
            Timber.i(appCodes.toString())
            return appCodes
        }

    companion object {
        val TAG = AppSignatureHelper::class.java.simpleName
        private const val HASH_TYPE = "SHA-256"
        const val NUM_HASHED_BYTES = 9
        const val NUM_BASE64_CHAR = 11
        private fun hash(packageName: String, signature: String): String? {
            val appInfo:String = "$packageName $signature"
            try {
                val messageDigest: MessageDigest =
                    MessageDigest.getInstance(HASH_TYPE)

                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                var hashSignature: ByteArray = messageDigest.digest()

                // truncated into NUM_HASHED_BYTES
                hashSignature = hashSignature.copyOfRange(0, NUM_HASHED_BYTES)
                // encode into Base64
                var base64Hash: String =
                    Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
                Log.d(
                    TAG,
                    String.format("pkg: %s -- hash: %s", packageName, base64Hash)
                )
                return base64Hash
            } catch (e: NoSuchAlgorithmException) {
                Log.e(TAG, "hash:NoSuchAlgorithm", e)
            }
            return null
        }
    }

    init {
        appSignatures
    }
}