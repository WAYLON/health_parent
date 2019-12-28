package com.waylon.utils;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author wangl
 * 用户名加密
 * privateKey:MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEA1YdmmfKkBXNZWH9OtagrANoac9HfEEIlD0q9RTFbBISJ4JPWzEOa0r1OnlcOrMWUehVOjhULj5g7aPJDVh72TwIDAQABAkBNQ3tKxVO4bS6tKROwA7Pb0Q+1TQGr/mMuj1rBSMSocyvC2TgTjtzhhyBIYrdj6kUOMFilnUtb4a/HX7tg+tXhAiEA7E3wqmB6DnPWjdL+kIebFLY4xjdv8Y9sMY+uKUE+PmkCIQDnU37vOY6wAzo+/yyJYxdyF+CEj35hCu84qtRy2sDn9wIhAN+6V07ocwd180Bp8S0jxteQQyagXDNn6DbwYyOaKvVpAiEAo04knEmr28JLilGmZU8ZLCiDDdInS+bmPsEKvhKEgL0CIQCb2mskGDQ0US71WMKkdQsjCnG0YbHABTx3040BXfv2oQ==
 * publicKey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANWHZpnypAVzWVh/TrWoKwDaGnPR3xBCJQ9KvUUxWwSEieCT1sxDmtK9Tp5XDqzFlHoVTo4VC4+YO2jyQ1Ye9k8CAwEAAQ==
 * password:X1c2Pxgd/QNTPmWw6Bgg0WEiOjbj8JMI2VJQzoXZNvC0v4B3AtmSUe0+0/4/CGX9xNpJBWDTHyVP+5tsQf1Jzg==
 */
@SuppressWarnings("all")
public class DecryptDruidSource extends DruidDataSource {
    private static String usernamePublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANWHZpnypAVzWVh/TrWoKwDaGnPR3xBCJQ9KvUUxWwSEieCT1sxDmtK9Tp5XDqzFlHoVTo4VC4+YO2jyQ1Ye9k8CAwEAAQ==";

    @Override
    public void setUsername(String username) {
        try {
            username = ConfigTools.decrypt(usernamePublicKey, username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.setUsername(username);
    }
}
