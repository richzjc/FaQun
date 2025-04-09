package com.wallstreetcn.helper.utils.system;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.wallstreetcn.helper.R;
import com.wallstreetcn.helper.utils.ResourceUtils;
import com.wallstreetcn.helper.utils.UtilsContextManager;

import java.text.DecimalFormat;

/**
 * 作者： 巴掌 on 15/7/6 13:23
 * 邮箱： wangyuwei@wallstreetcn.com
 */

public class TDevice {

    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    private static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    private static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    private static final int NETWORK_CLASS_4_G = 3;

    private static DecimalFormat df = new DecimalFormat("#.##");

    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;

    /**
     * 判断是否以WIFI方式联网
     *
     * @param
     * @return
     */
    public static boolean isConnectWIFI() {
        //获取网络连接管理者
        ConnectivityManager connectionManager = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 获取联网的方式,移动CMNET方式是cmnet,移动CMWAP方式是cmwap,联通3gwap方式是3gwap,联通3gnet方式是3gnet,联通uniwap方式是uniwap,联通uninet方式是uninet
     *
     * @param
     * @return
     */
//    public static String getConnectionType() {
//        ConnectivityManager connectionManager = (ConnectivityManager)
//                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
//        if (networkInfo != null) {
//            return networkInfo.getExtraInfo();
//        }
//        return "error";
//    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getCurrentNetworkType() {
        int networkClass = getNetworkClass();
        String type = "CELL";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "NONE";
                break;
            case NETWORK_CLASS_WIFI:
                type = "WIFI";
                break;
            case NETWORK_CLASS_2_G:
                type = "CELL_2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "CELL_3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "CELL_4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "UNKNOWN";
                break;
        }
        return type;
    }

    private static int getNetworkClass() {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) getApplication()
                            .getSystemService(
                                    Context.TELEPHONY_SERVICE);
//                    networkType = telephonyManager.getDataNetworkType();
                    networkType = NETWORK_TYPE_UNAVAILABLE;
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }

    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    public static boolean isCUMC() {
        return TextUtils.equals(ResourceUtils.getResStringFromId(R.string.helper_china_unicom), getProvider());
    }

    /**
     * 获取运营商
     *
     * @return
     */
    public static String getProvider() {
        String provider = ResourceUtils.getResStringFromId(R.string.helper_unkown_text);
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
//            String IMSI = telephonyManager.getSubscriberId();
            String IMSI = null;
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager
                        .getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    if (operator != null) {
                        if (operator.equals("46000")
                                || operator.equals("46002")
                                || operator.equals("46007")) {
                            provider = ResourceUtils.getResStringFromId(R.string.helper_china_mobile);
                        } else if (operator.equals("46001")) {
                            provider = ResourceUtils.getResStringFromId(R.string.helper_china_unicom);
                        } else if (operator.equals("46003")) {
                            provider = ResourceUtils.getResStringFromId(R.string.helper_china_telecom);
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                        || IMSI.startsWith("46007")) {
                    provider = ResourceUtils.getResStringFromId(R.string.helper_china_mobile);
                } else if (IMSI.startsWith("46001")) {
                    provider = ResourceUtils.getResStringFromId(R.string.helper_china_unicom);
                } else if (IMSI.startsWith("46003")) {
                    provider = ResourceUtils.getResStringFromId(R.string.helper_china_telecom);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    public static boolean isNetworkConnected() {
        try {
            ConnectivityManager manager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null)
                return info.isConnected();
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private static Context getApplication() {
        return UtilsContextManager.getInstance().getApplication();
    }
}
