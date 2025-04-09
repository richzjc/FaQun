package com.wallstreetcn.rpc.manager;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

/**
 * Created by Leif Zhang on 2016/12/15.
 * Email leifzhanggithub@gmail.com
 */

public class WscnDns implements Dns {
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
//        try {
//            String ips = MSDKDnsResolver.getInstance().getAddrByName(hostname);
//            List<InetAddress> list = new ArrayList<>();
//            if (ips != null) {
//                if (ips.contains(";")) {
//                    String[] ipArray = ips.split(";");
//                    for (String ip : ipArray) {
//                        list.add(InetAddress.getByName(ip));
//                    }
//                } else {
//                    list.add(InetAddress.getByName(ips));
//                }
//                return list;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return Arrays.asList(InetAddress.getAllByName(hostname));
    }
}
