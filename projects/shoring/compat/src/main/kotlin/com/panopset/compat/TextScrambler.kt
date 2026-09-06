package com.panopset.compat

import com.panopset.compat.Stringop.isPopulated
import com.panopset.compat.Stringop.wrapFixedWidth
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 *
 *
 * If you do not want to use the defaults, the values must be set prior to calling encrypt or
 * decrypt. For example, to change the key obtention iterations:
 *
 *
 * <pre>
 * Stringop.setEol(&quot;&#92;n&quot;); // If you don't want the operating system to decide
 * // what return character to use for encrypted text linewrapping.
 * new TextScrambler().withKeyObtentionIterations(5000).encrypt(pwd, msg);
</pre> *
 */
class TextScrambler {

    private var wrapWidth = DEFAULT_WRAP_WIDTH
    private var keygenAlgorithm = DEFAULT_KEYGEN_ALGORITHM
    private var cipherAlgorithm = DEFAULT_CIPHER_ALGORITHM
    private var keyAlgorithm = DEFAULT_KEY_ALGORITHM
    private var iters = DEFAULT_KEY_OBTENTION_ITERATIONS
    private fun createCipher(): Cipher {
        return try {
            Cipher.getInstance(cipherAlgorithm)
        } catch (e: Exception) {
            Logz.errorEx(e)
            throw RuntimeException(e)
        }
    }

    private var secretKeyFactory: SecretKeyFactory? = null
        get() {
            if (field == null) {
                field = try {
                    SecretKeyFactory.getInstance(keyAlgorithm)
                } catch (e: NoSuchAlgorithmException) {
                    Logz.errorEx(e)
                    throw RuntimeException(e)
                }
            }
            return field
        }

    /**
     * Encrypt a message.
     *
     * @param password User password, pass phrase.
     * @param msg Message to be encrypted.
     * @return Base64 encoded encrypted message.
     * @throws Exception Exception.
     */
    fun encrypt(password: String, msg: String): String {
        return if (isPopulated(password)) {
            val pkgStr = String(Base64.getUrlEncoder().encode(encrypt2bytes(password, msg)))
            wrapFixedWidth(pkgStr, wrapWidth)
        } else {
            val errorMessage = Nls.xlate("Please specify a password.")
            Logz.warn(errorMessage)
            errorMessage
        }
    }

    /**
     * Use this method if just want the bytes.
     *
     * @param password Password or passphrase.
     * @param msg Message to encrypt.
     * @return byte array for easy transmission and storage.
     */
    private fun encrypt2bytes(password: String, msg: String): ByteArray {
        val salt = ByteArray(8)
        nextBytes(salt)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, iters, 128)
        val secretKey = SecretKeySpec(secretKeyFactory!!.generateSecret(spec).encoded, keygenAlgorithm)
        val cipher = createCipher()
        val iv = ByteArray(16)
        nextBytes(iv)
        val ivspec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)
        val encryptedData = cipher.doFinal(msg.toByteArray(StandardCharsets.UTF_8))
        val rtn = ByteArray(salt.size + iv.size + encryptedData.size)
        System.arraycopy(salt, 0, rtn, 0, salt.size)
        System.arraycopy(iv, 0, rtn, salt.size, iv.size)
        System.arraycopy(encryptedData, 0, rtn, salt.size + iv.size, encryptedData.size)
        return rtn
    }

    /**
     * Decrypt a message.
     *
     * @param password User password, pass phrase.
     * @param msg Message to be decrypted.
     * @return Decrypted message.
     * @throws Exception Exception.
     */
    fun decrypt(password: String, msg: String): String {
        return try {
            val msgTrim = msg.trim { it <= ' ' }
                .replace(Stringop.CARRIAGE_RETURN, "").replace(Stringop.LINE_FEED, "")
            val pkgBytes = Base64.getUrlDecoder().decode(msgTrim)
            decryptFromBytes(password, pkgBytes)
        } catch (ex: IllegalArgumentException) {
            val errorMsg =
                "${Nls.xlate("Likely wrong password, or not an encrypted message, because")}: ${ex.message}"
            Logz.errorMsg(errorMsg, ex)
            return errorMsg
        }
    }

    /**
     * Decrypt a message that was encrypted with encrypt2bytes.
     *
     * @param password User password, pass phrase.
     * @param bytes Message to be decrypted.
     * @return Decrypted message.
     * @throws Exception Exception.
     */
    fun decryptFromBytes(password: String, bytes: ByteArray): String {
        if (bytes.size < 25) {
            throw Exception(Nls.xlate("Not an encrypted message."))
        }
        val bb = ByteBuffer.wrap(bytes)
        val msgLength = bytes.size - 24
        val salt = ByteArray(8)
        val iv = ByteArray(16)
        val encr = ByteArray(msgLength)
        bb[salt, 0, 8]
        bb[iv, 0, 16]
        bb[encr, 0, msgLength]
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, iters, 128)
        val secretKey = SecretKeySpec(secretKeyFactory!!.generateSecret(spec).encoded, keygenAlgorithm)
        val cipher = createCipher()
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val data = cipher.doFinal(encr)
        return String(data)
    }

    /**
     * @param value Default is **80**. Set to 0 or -1 for no wrapping.
     * @return this.
     */
    fun withWrapWidth(value: Int): TextScrambler {
        wrapWidth = value
        return this
    }

    /**
     *
     * @param value Default is **10000**.
     * @return this.
     */
    fun withKeyObtentionIters(value: Int): TextScrambler {
        iters = value
        return this
    }

    /**
     *
     * @param value Default is **AES**.
     * @return this.
     */
    fun withKeygenAlgorithm(value: String): TextScrambler {
        keygenAlgorithm = value
        return this
    }

    /**
     *
     * @param value Default is **AES/CBC/PKCS5PADDING**.
     * @return this.
     */
    fun withCipherAlgorithm(value: String): TextScrambler {
        cipherAlgorithm = value
        return this
    }

    /**
     *
     * @param value Default is **PBKDF2WithHmacSHA512**.
     * @return this.
     */
    fun withKeyAlgorithm(value: String): TextScrambler {
        keyAlgorithm = value
        return this
    }

    companion object {
        init {
            Security.setProperty("crypto.policy", "unlimited")
        }

        const val DEFAULT_WRAP_WIDTH = 80
        const val DEFAULT_KEYGEN_ALGORITHM = "AES"
        const val DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING"
        const val DEFAULT_KEY_ALGORITHM = "PBKDF2WithHmacSHA512"
        const val DEFAULT_KEY_OBTENTION_ITERATIONS = 10000
    }
}
