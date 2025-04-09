package com.wallstreetcn.rpc;

public class ServerAPI {
    static {
        update();
    }

    public static final int SIT = 0;
    public static final int STAGE = 1;
    public static final int PROD = 2;

    public static final String DOMAIN = "awtmt.com";
    public static final String ROUTER_HOST = "https://wallstreetcn.com";
    public static final String API_V1 = "apiv1/";
    public static final String LAZYCAT_API_V1 = "lazycatapiv1/";

    private static final String SIT_API = "https://api-one-sit-wscn.";
    private static final String STAGE_API = "https://api-stage-wscn.";
    private static final String PROD_API = "https://api-one-wscn.";

    // webSocket
    private static final String WSS_SIT = "wss://realtime-sit-wscn." + DOMAIN;
    private static final String WSS_STAGE = "wss://realtime-stage-wscn." + DOMAIN;
    private static final String WSS_PROD = "wss://realtime-wscn." + DOMAIN;
    private static final String STREAMER_WSS_SIT = "wss://streamer-sit-wscn." + DOMAIN;
    private static final String STREAMER_WSS_PROD = "wss://streamer-prod-wscn." + DOMAIN;

    // market
    private static final String DDC_SIT = "https://api-ddc-sit-wscn.awtmt.com/";
    private static final String DDC_RELEASE = "https://api-ddc-wscn.awtmt.com/";

    // market
    private static final String DDC_SIT_XUANGUBAO = "https://api-ddc-wscn-sit.xuangubao.cn/";
    private static final String DDC_RELEASE_XUANGUBAO = "https://api-ddc-wscn.xuangubao.cn/";

    private static final String WSS_DDC_REAL_TIME_RELEASE = "wss://ddcrealtime-ws-api-wscn.awtmt.com/";
    private static final String WSS_DDC_REAL_TIME_SIT = "wss://ddcrealtime-ws-api-sit-wscn.awtmt.com/";
    private static final String WOWS_RELEASE = "https://wows-api-wscn.awtmt.com";
    private static final String WOWS_SIT = "https://wows-api-sit-wscn.awtmt.com";
    private static final String FLASH_RELEASE = "https://flash-api-wscn.awtmt.com/";
    private static final String FLASH_SIT = "https://flash-api-sit-wscn.awtmt.com/";
    private static final String F10_RELEASE = "https://wallstreetcn.com/f10/";
    // web
    private static final String FRONTEND_WEB_RELEASE = "https://wallstreetcn.com/";
    private static final String FRONTEND_WEB_SIT = "https://frontend-sit.wallstreetcn.com/";




    private static int sEnvironment = PROD;
    public static String sBase = PROD_API + DOMAIN;
    public static String sStreamerWssProd = STREAMER_WSS_PROD;
    public static String sWssBase = WSS_PROD;
    public static String sDdcBase = DDC_RELEASE;
    public static String sDdcBaseXuanGuBao = DDC_RELEASE_XUANGUBAO;
    public static String sDdcWssBase = WSS_DDC_REAL_TIME_RELEASE;
    public static String sFlashBase = FLASH_RELEASE;
    public static String sWowsBase = WOWS_RELEASE;
    public static String sF10Base = F10_RELEASE;
    public static String sFrontendWebBase = FRONTEND_WEB_RELEASE;


    public static void setEnvironment(int environment) {
        sEnvironment = environment;
        update();
    }

    public static boolean isRelease() {
        switch (ServerAPI.getEnvironment()) {
            case ServerAPI.SIT:
                return false;
            case ServerAPI.STAGE:
            case ServerAPI.PROD:
        }
        return true;
    }

    public static int getEnvironment() {
        return sEnvironment;
    }

    public static String getEnvironmentString() {
        switch (sEnvironment) {
            case SIT:
                return "test";
            case PROD:
                return "prod";
            case STAGE:
                return "stage";
        }
        return "";
    }

    private static void update() {
        switch (sEnvironment) {
            case SIT:
                sBase = SIT_API + DOMAIN;       //用老的测试环境
                sWssBase = WSS_SIT;
                sStreamerWssProd = STREAMER_WSS_SIT;
                sDdcBase = DDC_SIT;
                sDdcBaseXuanGuBao = DDC_SIT_XUANGUBAO;
                sDdcWssBase = WSS_DDC_REAL_TIME_SIT;
                sFlashBase = FLASH_SIT;
                sWowsBase = WOWS_SIT;
                sFrontendWebBase = FRONTEND_WEB_SIT;
                break;
            case STAGE:
                sBase = STAGE_API + DOMAIN;
                sWssBase = WSS_STAGE;
                sStreamerWssProd = STREAMER_WSS_PROD;
                sDdcBase = DDC_RELEASE;
                sDdcBaseXuanGuBao = DDC_RELEASE_XUANGUBAO;
                sDdcWssBase = WSS_DDC_REAL_TIME_RELEASE;
                sFlashBase = FLASH_RELEASE;
                sWowsBase = WOWS_RELEASE;
                sFrontendWebBase = FRONTEND_WEB_RELEASE;
                break;
            case PROD:
                sBase = PROD_API + DOMAIN;
                sWssBase = WSS_PROD;
                sStreamerWssProd = STREAMER_WSS_PROD;
                sDdcBase = DDC_RELEASE;
                sDdcBaseXuanGuBao = DDC_RELEASE_XUANGUBAO;
                sDdcWssBase = WSS_DDC_REAL_TIME_RELEASE;
                sFlashBase = FLASH_RELEASE;
                sWowsBase = WOWS_RELEASE;
                sFrontendWebBase = FRONTEND_WEB_RELEASE;
                break;
        }
    }
}
