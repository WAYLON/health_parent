package com.waylon.utils;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

public class JedisPoolConfigDefinition extends JedisConnectionFactory {
    private String publicKey = "DgXKJbkirHGpEnKdI8R78Vt1rgsU4";
    private String vi = "01234567";

    @Override
    public void setPassword(String password) {
        try {
            String decryptPassword = ThreeDESUtil.decrypt(password, publicKey, vi);
            super.setPassword(decryptPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
