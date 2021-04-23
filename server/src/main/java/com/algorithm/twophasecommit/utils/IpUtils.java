package com.algorithm.twophasecommit.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;

/**
 * 获取IP方法
 * 
 * @author ruoyi
 */
public class IpUtils
{
    private static final Logger log = LoggerFactory.getLogger(IpUtils.class);

    //获取ip
    public static String getLocalIP() {
        String localIP = "127.0.0.1";
        try {
            OK:
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address instanceof Inet4Address) {
                        localIP = address.getHostAddress();
                        break OK;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return localIP;
    }

    //通过request获取ip
    public static String getIp() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServerName();
    }

    //通过request获取端口
    public static String getLocalPort() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServerPort() + "";
    }


    public static String getIpAndPort() {
        return  getLocalIP() + ":" + getLocalPort();
    }
}