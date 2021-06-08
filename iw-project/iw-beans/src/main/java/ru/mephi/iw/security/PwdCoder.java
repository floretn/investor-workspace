package ru.mephi.iw.security;

import ru.mephi.iw.exceptions.IwRuntimeException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PwdCoder {

    public String encodePwd(String pwd) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IwRuntimeException("Не может найти алгоритм SHA-256", e);
        }

        m.reset();
        m.update(pwd.getBytes(StandardCharsets.UTF_8));
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        return bigInt.toString(36);
    }
}
