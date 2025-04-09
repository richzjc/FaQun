package com.wallstreetcn.helper.utils.md5

import android.util.Base64
import com.wallstreetcn.helper.utils.TLog
import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


fun getRSAStr(data: String?): String {
    try {
        val publicKey =
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMc6OosEUzWmsoO3CocvhUkHAaXKgS74Ca2YqRUsutXMH8aq6FKreJWKkHzYBJyEjgTpGewdwzRdcfblTmlQzpECAwEAAQ=="

        val decoded = Base64.decode(publicKey, Base64.DEFAULT)


        val keySpec = X509EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        val pubKey = keyFactory.generatePublic(keySpec)


        val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
        //加密数据
        val cp = Cipher.getInstance(TRANSFORMATION)
        cp.init(Cipher.ENCRYPT_MODE, pubKey)
        val dataArr = data?.toByteArray()
        val inputLength = dataArr?.size ?: 0
        val outputStream = ByteArrayOutputStream()
        val MAX_ENCRYPT_BLOCK = 30
        var offset = 0
        var i = 0
        while (inputLength - offset > 0){
            if(inputLength - offset > MAX_ENCRYPT_BLOCK){
                val cache = cp.doFinal(dataArr, offset, MAX_ENCRYPT_BLOCK)
                outputStream.write(cache, 0, cache.size)
            }else{
                val cache = cp.doFinal(dataArr, offset, inputLength - offset)
                outputStream.write(cache, 0, cache.size)
            }
            i ++
            offset = i * MAX_ENCRYPT_BLOCK
        }

        val byteData = outputStream.toByteArray()
        outputStream.close()
        val value = Base64.encodeToString(byteData, Base64.DEFAULT)
        TLog.e("encript", value)
//        //加密数据
//        val cp = Cipher.getInstance(TRANSFORMATION)
//        cp.init(Cipher.ENCRYPT_MODE, pubKey)
//        val value =  Base64.encodeToString(cp.doFinal(data?.toByteArray()), Base64.DEFAULT)
//        TLog.e("encript", value)
        return value
    } catch (e: Exception) {
        e.printStackTrace()
        return data ?: ""
    }

}