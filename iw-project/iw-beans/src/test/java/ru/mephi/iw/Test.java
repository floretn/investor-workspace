package ru.mephi.iw;

import ru.mephi.iw.security.PwdCoder;

public class Test {

    public static void main(String[] args) {
        PwdCoder pwdCoder = new PwdCoder();
        System.out.println(pwdCoder.encodePwd("212942"));
    }
}
