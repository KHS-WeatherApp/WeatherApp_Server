package com.server.KH_StudyProjects_WeatherServer.global.logging;

/**
 * 로그 코드 상수.
 * 형식: {도메인}-{HTTP/유형}-{일련번호}
 */
public final class LogCode {

    private LogCode() {
    }

    // Validation / System
    public static final String VAL_400_001 = "VAL-400-001";
    public static final String SYS_400_001 = "SYS-400-001";
    public static final String SYS_500_001 = "SYS-500-001";

    // External API
    public static final String EXT_5XX_001 = "EXT-5XX-001";
    public static final String EXT_REQ_001 = "EXT-REQ-001";
    public static final String EXT_200_001 = "EXT-200-001";
    public static final String EXT_204_001 = "EXT-204-001";
    public static final String EXT_502_001 = "EXT-502-001";
    public static final String EXT_502_002 = "EXT-502-002";
    public static final String EXT_502_003 = "EXT-502-003";
    public static final String EXT_504_001 = "EXT-504-001";

    // Weather
    public static final String WTH_REQ_001 = "WTH-REQ-001";
    public static final String WTH_200_001 = "WTH-200-001";
    public static final String WTH_404_001 = "WTH-404-001";

    // FineDust
    public static final String FDT_REQ_001 = "FDT-REQ-001";
    public static final String FDT_200_001 = "FDT-200-001";
    public static final String FDT_404_001 = "FDT-404-001";

    // Favorite(SideMenu)
    public static final String FAV_REQ_001 = "FAV-REQ-001";
    public static final String FAV_REQ_002 = "FAV-REQ-002";
    public static final String FAV_REQ_003 = "FAV-REQ-003";
    public static final String FAV_REQ_004 = "FAV-REQ-004";
    public static final String FAV_REQ_005 = "FAV-REQ-005";
    public static final String FAV_200_001 = "FAV-200-001";
    public static final String FAV_200_002 = "FAV-200-002";
    public static final String FAV_200_003 = "FAV-200-003";
    public static final String FAV_201_001 = "FAV-201-001";
    public static final String FAV_400_001 = "FAV-400-001";
    public static final String FAV_404_001 = "FAV-404-001";
    public static final String FAV_409_001 = "FAV-409-001";
    public static final String FAV_500_001 = "FAV-500-001";
    public static final String FAV_500_002 = "FAV-500-002";
}
