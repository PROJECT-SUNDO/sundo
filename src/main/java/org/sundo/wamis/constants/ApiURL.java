package org.sundo.wamis.constants;

public class ApiURL {
    // 강수량 관측소 목록 API 주소
    public static final String RF_OBSERVATORY_LIST = "https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/rainfall/info.json";

    // 수위 + 유량 관측소 목록 API 주소
    public static final String WLF_OBSERVATORY_LIST = "https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/info";

    // 수위 + 유량 조회 API 주소
    public static final String WATER_LEVEL_FLOW = "https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/list/10M";


}
