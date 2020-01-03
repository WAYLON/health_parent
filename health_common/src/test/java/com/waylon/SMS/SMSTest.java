package com.waylon.SMS;

import com.aliyuncs.exceptions.ClientException;
import com.waylon.utils.SMSUtils;

public class SMSTest {
    public static void main(String[] args) throws ClientException {
        SMSUtils.sendShortMessage("SMS_176926166","15776544***","1234");
    }
}
