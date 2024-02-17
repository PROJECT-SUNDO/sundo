package org.sundo.wamis.constants;

public class ApiURL {
    // 강수량 관측소 목록
    public static final String RF_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/rf_dubrfobs";

    // 수위 관측소 목록
    public static final String WL_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/wl_dubwlobs";

    // 유량 관측소 목록
    public static final String FLW_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/flw_dubobsif";


    // 수위 + 유량 데이터 조회 API 주소
    public static final String WATER_LEVEL_FLOW = "https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/list/";




}
