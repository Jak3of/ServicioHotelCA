package com.mycompany.avanceproyecto.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase utilitaria para encriptar y desencriptar contraseñas usando AES
 */
public class PasswordEncryption {
    private static final Logger logger = LoggerFactory.getLogger(PasswordEncryption.class);
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY = "hotelca";
    
    /**
     * Genera una clave AES de 128 bits a partir de la clave maestra
     */
    private static SecretKeySpec generateKey() { 
        try {
            // Crear hash SHA-256 de la clave y tomar los primeros 16 bytes para AES-128
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] key = sha.digest(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
            
            // Tomar solo los primeros 16 bytes para AES-128
            byte[] aesKey = new byte[16];
            System.arraycopy(key, 0, aesKey, 0, 16);
            
            return new SecretKeySpec(aesKey, ALGORITHM);
        } catch (Exception e) {
            logger.error("Error generando clave de encriptación", e);
            throw new RuntimeException("Error en la generación de clave", e);
        }
    }
    
    /**
     * Encripta una contraseña en texto plano
     * @param plainPassword Contraseña en texto plano
     * @return Contraseña encriptada en Base64
     */
    public static String encrypt(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        
        try {
            SecretKeySpec secretKey = generateKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes(StandardCharsets.UTF_8));
            String encryptedPassword = Base64.getEncoder().encodeToString(encryptedBytes);
            
            logger.debug("Contraseña encriptada exitosamente");
            return encryptedPassword;
            
        } catch (Exception e) {
            logger.error("Error encriptando contraseña", e);
            throw new RuntimeException("Error en la encriptación", e);
        }
    }
    
    /**
     * Desencripta una contraseña encriptada
     * @param encryptedPassword Contraseña encriptada en Base64
     * @return Contraseña en texto plano
     */
    public static String decrypt(String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña encriptada no puede estar vacía");
        }
        
        try {
            SecretKeySpec secretKey = generateKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            
            String plainPassword = new String(decryptedBytes, StandardCharsets.UTF_8);
            logger.debug("Contraseña desencriptada exitosamente");
            return plainPassword;
            
        } catch (Exception e) {
            logger.error("Error desencriptando contraseña", e);
            throw new RuntimeException("Error en la desencriptación", e);
        }
    }
    
    /**
     * Verifica si una contraseña en texto plano coincide con una encriptada
     * @param plainPassword Contraseña en texto plano
     * @param encryptedPassword Contraseña encriptada
     * @return true si coinciden, false en caso contrario
     */
    public static boolean verifyPassword(String plainPassword, String encryptedPassword) {
        try {
            if (plainPassword == null || encryptedPassword == null) {
                return false;
            }
            
            String decryptedPassword = decrypt(encryptedPassword);
            boolean matches = plainPassword.equals(decryptedPassword);
            
            logger.debug("Verificación de contraseña: {}", matches ? "exitosa" : "fallida");
            return matches;
            
        } catch (Exception e) {
            logger.error("Error verificando contraseña", e);
            return false;
        }
    }
    
    /**
     * Método utilitario para verificar si una cadena está encriptada
     * @param password Cadena a verificar
     * @return true si parece estar encriptada (Base64 válido)
     */
    public static boolean isEncrypted(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        
        try {
            Base64.getDecoder().decode(password);
            // Si llegamos aquí, es Base64 válido
            return password.length() > 10 && password.matches("^[A-Za-z0-9+/]*={0,2}$");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
