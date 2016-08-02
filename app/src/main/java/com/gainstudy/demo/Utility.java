package com.gainstudy.demo;

import com.google.firebase.auth.FirebaseAuth;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by soura on 30-07-2016.
 */

public class Utility {

    private static FirebaseAuth auth;

    public String nextSessionId(String name) {
        int random = (int)(Math.random() * 5000 + 1000);
        return (name+""+random);
    }
}

