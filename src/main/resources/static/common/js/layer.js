var commonLib = commonLib || {};

commonLib.popup = {
/**
* 레이어 팝업 열기
*
* @param url : 팝업으로 열 주소
* @param width : 팝업 너비, 기본값 350
* @param height : 팝업 높이, 기본값 350
*/
    open(url, width, height) {
        if(!url) return;

        width = width || 350;
        height =  height || 350;

        /* 이미 열려있는 레이어팝업이 있다면 제거 */
        this.close();

        /* 레이어 팝업 요소 생성 S */
        const popupEl = document.createElement("div"); // 팝업
        popupEl.id = "layer_popup";
        popupEl.style.width = width + "px";
        popupEl.style.height = height + "px";

        const iframeEl = document.createElement("iframe"); // 웹페이지 안에 다른 웹페이지를 삽입
        iframeEl.width = width;
        iframeEl.height = height;
        iframeEl.src = url;
        /**
        * appendChild
        * 부모의 자식 노드 리스트 중 마지막 자식으로 붙인다
        parentNode.appendChild(childNode);
        */
        popupEl.appendChild(iframeEl);


        /* 레이어 팝업 가운데 배치 좌표 구하기 S */
        const centerX = Math.round((window.innerWidth - width) / 2);
        const centerY = Math.round((window.innerHeight - height) / 2);
        popupEl.style.top = centerY + "px";
        popupEl.style.left = centerX + "px";
        /* 레이어 팝업 가운데 배치 좌표 구하기 E */

        const layerDimEl = document.createElement("div"); // 레이어 배경
        layerDimEl.id = "layer_dim";

        /* 레이어 팝업 요소 생성 E */

        /* 레이어 팝업 노출 S */
        document.body.appendChild(layerDimEl);
        document.body.appendChild(popupEl);
        /* 레이어 팝업 노출 S */

        /* 레이어 배경 클릭 시 close 처리 */
        layerDimEl.addEventListener("click", this.close);
        },
        /**
        * 레이어 팝업 닫기
        *
        */
        close() {
            const popupEl = document.getElementById("layer_popup");
            if (popupEl) popupEl.parentElement.removeChild(popupEl); // 하위 노드 삭제

            const layerDimEl = document.getElementById("layer_dim");
            if(layerDimEl) layerDimEl.parentElement.removeChild(layerDimEl); // 하위 노드 삭제

        }






}