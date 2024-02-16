package org.sundo.wamis.constants;

public class ApiURL {

    // 강수량 관측소 목록 API 주소
    public static final String RF_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/rf_dubrfobs";

    // 수위 관측소 목록 API 주소
    public static final String WL_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/wl_dubwlobs";

    // 유량 관측소 목록 API 주소
    public static final String FLW_OBSERVATORY_LIST = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/flw_dubobsif";

    // 강수량 조회 API 주소
    public static final String PRECIPITATION = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/rf_hrdata";

    // 수위 조회 API 주소
    public static final String WATER_LEVEL = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/wl_hrdata";

    // 유량 실시간 조회 API 주소
    public static final String WATER_FLOW_LEVEL = "http://www.wamis.go.kr:8080/wamis/openapi/wkw/flw_dtdata";


}
