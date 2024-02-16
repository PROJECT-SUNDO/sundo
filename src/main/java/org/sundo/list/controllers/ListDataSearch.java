package org.sundo.list.controllers;

import lombok.Data;

@Data
public class ListDataSearch {
    private int page = 1; // 기본값 1page
    private int limit = 10; // 0 : 설정에 있는 1페이지 게시글 갯수, 1이상 : 지정한 갯수


}
