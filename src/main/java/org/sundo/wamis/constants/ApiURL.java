package org.sundo.wamis.constants;

public class ApiURL {

    /**
     * 관측소
     */
    // 강수량 관측소 목록
    public static final String RF_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/rf_dubrfobs?basin=1";

    // 수위 관측소 목록
    public static final String WL_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/wl_dubwlobs?basin=1";

    // 유량 관측소 목록
    public static final String FLW_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/flw_dubobsif?basin=1";

    // 강수량 관측소 상세
    public static final String RF_OBSERVATORY_DETAIL = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/rainfall/info.json";

    // 수위 + 유량 관측소 상세
    public static final String WL_FLW_OBSERVATORY_DETAIL = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/info.json";

    /**
     * 데이터
     */
    // 기간별 강수량 데이터 조회 API주소
    public static final String PRECIPITATION = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/rainfall/list/";

    // 기간별 수위 + 유량 데이터 조회 API 주소
    public static final String WATER_LEVEL_FLOW = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/list/";

    // 전체 10분 강수량 데이터
    public static final String ALL_PRECIPITATION = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/rainfall/list/10M.json";

    // 전체 10분 수위 + 유량 데이터
    public static final String ALL_WATER_LEVEL_FLOW = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/list/10M.json";

    // 최근 10분 강수량 데이터 (1건)
    public static final String RCT_PRECIPITATION = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/rainfall/list/10M/10184100.json";


    // 최근 10분 수위 + 유량 데이터 (1건)
    public static final String RCT_WATER_LEVEL_FLOW = "http://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/list/10M/1018683.json";






}
