package com.mycompany.avanceproyecto.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PasswordEncryptionTest {

    @Test
    void testEncryptDecrypt() {
        String originalPassword = "admin123";
        
        // Encriptar
        String encrypted = PasswordEncryption.encrypt(originalPassword);
        assertNotNull(encrypted);
        assertNotEquals(originalPassword, encrypted);
        
        // Desencriptar
        String decrypted = PasswordEncryption.decrypt(encrypted);
        assertEquals(originalPassword, decrypted);
    }

    @Test
    void testVerifyPassword() {
        String password = "admin123";
        String encrypted = PasswordEncryption.encrypt(password);
        
        assertTrue(PasswordEncryption.verifyPassword(password, encrypted));
        assertFalse(PasswordEncryption.verifyPassword("wrongPassword", encrypted));
    }

    @Test
    void testIsEncrypted() {
        String plainText = "admin123";
        String encrypted = PasswordEncryption.encrypt("admin123");
        
        assertFalse(PasswordEncryption.isEncrypted(plainText));
        assertTrue(PasswordEncryption.isEncrypted(encrypted));
    }
}
