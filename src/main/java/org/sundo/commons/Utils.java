package org.sundo.commons;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class Utils {
    private final HttpServletRequest request;
    private final HttpSession session;

    /**
     * 메세지를 타입에 따라 가져옴
     * 가져온 메시지를 출력
     */
    private static final ResourceBundle commonsBundle;
    private static final ResourceBundle validationsBundle;

    private static final ResourceBundle errorsBundle;

    static {
        commonsBundle = ResourceBundle.getBundle("messages.commons");
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
    }

    public static String getMessage(String code, String type) {
        try {
            type = StringUtils.hasText(type) ? type : "validations";

            ResourceBundle bundle = null;
            if(type.equals("commons")) {
                bundle = commonsBundle;
            } else if (type.equals("errors")) {
                bundle = errorsBundle;
            } else  {
                bundle = validationsBundle;
            }
            return bundle.getString(code);
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getMessage(String code) {
        return getMessage(code,null);
    }
}
