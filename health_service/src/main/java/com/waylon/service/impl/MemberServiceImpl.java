package com.waylon.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.waylon.constant.RedisMessageConstant;
import com.waylon.dao.MemberDao;
import com.waylon.pojo.Member;
import com.waylon.service.MemberService;
import com.waylon.utils.MD5Utils;
import com.waylon.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.MapMessage;
import java.util.concurrent.TimeUnit;

/**
 * 会员服务
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination smsDestination;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void sendLoginSmsCode(String telephone) {
        //生成6位随机数
        String code = ValidateCodeUtils.generateValidateCode(6);
        System.out.println("登录验证码：" + code);
        //存入缓存
        redisTemplate.boundValueOps(telephone + RedisMessageConstant.SENDTYPE_LOGIN).set(code);
        //发送到activeMQ	....
        jmsSend(telephone, code, jmsTemplate, smsDestination);
    }

    static void jmsSend(String telephone, String code, JmsTemplate jmsTemplate, Destination smsDestination) {
        jmsTemplate.send(smsDestination, session -> {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setObject("mobile", telephone);//手机号
            mapMessage.setObject("params", code);//参数
            mapMessage.setObject("templateCode", "SMS_176926166");//模板code
            mapMessage.setObject("signName", "WAYLON");//签名
            return mapMessage;
        });
    }

    //保存会员信息
    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null) {
            //使用md5将明文密码进行加密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }
}