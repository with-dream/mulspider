package com.example.core.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UUIDUtils {
    private static String uuid;

    public static String getUuid() {
        if (StringUtils.isNotEmpty(uuid))
            return uuid;
        synchronized (UUIDUtils.class) {
            if (StringUtils.isEmpty(uuid))
                uuid = String.valueOf(getLocalMac().hashCode());
        }
        return uuid;
    }

    private static String getTid() {
        return Thread.currentThread().getName();
    }

    private static String getLocalMac() {
        String uuid = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();

            byte[] mac = NetworkInterface.getByInetAddress(addr).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            if (!ArrayUtils.isEmpty(mac)) {
                for (int i = 0; i < mac.length; i++) {
                    int temp = mac[i] & 0xff;
                    String str = Integer.toHexString(temp);
                    if (str.length() == 1) {
                        sb.append("0").append(str);
                    } else {
                        sb.append(str);
                    }
                }
                uuid = sb.toString().toUpperCase();
            }

            if (StringUtils.isEmpty(uuid))
                uuid = addr.getHostName();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        return uuid;
    }

    public static String getPid() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getName().split("@")[0];
    }
}
