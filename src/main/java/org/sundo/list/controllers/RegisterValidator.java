package org.sundo.list.controllers;

public interface RegisterValidator {

    /**
     * 비밀번호에 숫자 포함 여부
     *
     */
    default boolean numberCheck(String obscd) {
        return obscd.matches(".*\\d*");
    }

    /**
     * 비밀번호에 특수 문제 포함 여부
     *
     */
    default boolean specialCharsCheck(String addr) {
        String pattern = ".*[`-`]+.*";
        return addr.matches(pattern);
    }
}
