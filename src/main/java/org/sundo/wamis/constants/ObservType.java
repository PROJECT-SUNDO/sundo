package org.sundo.wamis.constants;

import java.util.Arrays;
import java.util.List;

public enum ObservType {
    FLW("유량"),
    WL("수위"),
    RF("강수량"),
    CCTV("CCTV");

    private String title;

    ObservType(String title){this.title = title;}


    public String getTitle(){

        return this.title;
    }

    public static List<String[]> getList(){
        return Arrays.asList(
                new String[]{FLW.name(), FLW.getTitle()},
                new String[]{WL.name(), WL.getTitle()},
                new String[]{RF.name(), RF.getTitle()},
                new String[]{CCTV.name(), CCTV.getTitle()}
        );
    }

}
