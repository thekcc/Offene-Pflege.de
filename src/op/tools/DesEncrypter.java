package op.tools;

import entity.system.SYSPropsTools;
import op.OPDE;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

@Deprecated
public class DesEncrypter {
    Cipher ecipher;
    Cipher dcipher;
    Logger logger;

    // Iteration count
    int iterationCount = 19;

    private static final char[] PASSWORD = LocalMachine.getSerialNumber().toCharArray();
    private static final byte[] SALT = {
            (byte) 0x1f, (byte) 0xac, (byte) 0xea, (byte) 0xff,
            (byte) 0xcf, (byte) 0x98, (byte) 0x1a, (byte) 0x01
    };

    public DesEncrypter() {

        logger = Logger.getLogger(getClass());

        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(PASSWORD, SALT, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        } catch (Exception e) {
            OPDE.fatal(logger, e);
        }
    }


    /**
     * this constructor is the old one. will be removed in future.
     * @param passphrase
     */
    public DesEncrypter(String passphrase) {
           try {

   //            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
   //            if (ni == null) { // Das ist nötig, weil ein Linux in einer VMWare hier ein NULL liefert.
   //                ni = NetworkInterface.getNetworkInterfaces().nextElement();
   //            }


                // i am doing this to reproduce same keys on the same machine. especially a mac tends to shuffle the nic list from time to time
               NetworkInterface ni = NetworkInterface.getByName(OPDE.getLocalProps().getProperty(SYSPropsTools.LOCAL_KEY_CIPHER_NIC, ""));
               if (ni == null) {
                   ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
                   if (ni == null) { // Das ist nötig, weil ein Linux in einer VMWare hier ein NULL liefert.
                       ni = NetworkInterface.getNetworkInterfaces().nextElement();
                   }
               }

               // Die 6-Bytes MAC Adresse muss noch um zwei weitere, beliebige Bytes aufgefüllt werden. Das verlangt der Algorithmus
               byte[] salt = ArrayUtils.addAll(ni.getHardwareAddress(), new byte[]{(byte) 0x9B, (byte) 0xC8});
               // sometimes the ni has no hardware address (had this when using vmware). if that happens, everythins is messed up anyways
               // so this class will never work and OPDE asks for a new password which will be encoded with the new class anyways.
               if (salt.length != 8){ // this prevents nasty exceptions.
                   salt = SALT;
               }

               // Create the key
               KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, iterationCount);
               SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
               ecipher = Cipher.getInstance(key.getAlgorithm());
               dcipher = Cipher.getInstance(key.getAlgorithm());

               // Prepare the parameter to the ciphers
               AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

               // Create the ciphers
               ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
               dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
           } catch (Exception e) {
               OPDE.fatal(logger,e);
           }
       }

    public String encrypt(String str) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");

        // Encrypt
        byte[] enc = ecipher.doFinal(utf8);

        // Encode bytes to base64 to get a string
        return new sun.misc.BASE64Encoder().encode(enc);
    }

    public String decrypt(String str) throws IOException, BadPaddingException, IllegalBlockSizeException {
        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        // Decrypt
        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    }


}