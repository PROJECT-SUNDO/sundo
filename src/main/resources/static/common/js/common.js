var commonLib = commonLib || {}; // 파일 속성값으로 namespace를 정할 수 있음 / 있으면 사용 없으면 만들기

/**
 * ajax 요청, 응답 편의 함수
 * @param method : 요청 방식(GET, POST, PUT, PATCH, DELETE ... )
 * @param url : 요청 URL
 * @param params : 요청 데이터 -> post, put patch..(바디 쪽에 실릴 데이터)
 * @param responseType : json -> JSON형태 (자바스크립트 객체)로 변환
 */
commonLib.ajaxLoad = function(method, url, params, responseType) {
    method = method || "GET";
    params = params || null;

    const token = document.querySelector("meta[name='_csrf']").content;
    const tokenHeader = document.querySelector("meta[name='_csrf_header']").content;

    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();

        xhr.open(method, url);
        xhr.setRequestHeader(tokenHeader, token);


        xhr.send(params); // 요청 body에 실릴 데이터 키=값&키=값& .... FormData 객체 (POST, PATCH, PUT)


        xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                const resData = (responseType && responseType.toLowerCase() == 'json') ? JSON.parse(xhr.responseText) : xhr.responseText;

                if (xhr.status == 200) {

                    resolve(resData); // 성공시 응답 데이터
                } else {
                    reject(resData);
                }
            }
        };

        xhr.onabort = function(err) {
            reject(err); // 중단 시
        };

        xhr.onerror = function(err) {
            reject(err); // 요청 또는 응답시 오류 발생
        };
    });
};

/*
* 숫자만 입력가능하도록
*/
function formatNumber(num){
    let newNum = "";
    const pattern = /[\d*\.]/g;
    let cnt = 0;
    while(cnt < 100){
        cnt++;
        const result = pattern.exec(num);
        if(!result) break;
        newNum += result[0];
    }

    return newNum.replace(/\.{2,}/g, '.');
};


window.addEventListener("DOMContentLoaded", function(){

    /* form submit 될때 숫자 아닌거는 다 없어지게 */
    const onlyNums = document.querySelectorAll(".onlyNum");

    for(const onlyNum of onlyNums){
        // data-form 은 target form의 id로
        const form = document.getElementById(onlyNum.dataset.form);
        form.addEventListener("submit", function(e){
            e.preventDefault();
            onlyNum.value = formatNumber(onlyNum.value);
            this.submit();
        })
    }

    /* 강수량, 유량, 수위 단위 노출 */
    const units = document.querySelectorAll(".unit");
    for(const unit of units){
        if(unit.classList.contains("rf")){
            // 강수량
            unit.innerHTML = "mm";
        }else if(unit.classList.contains("wl")){
            // 수위
            unit.innerHTML = "m";
        }else if(unit.classList.contains("fw")){
            // 유량
            unit.innerHTML = "m<sup>3</sup>/sec";
        }
    }
})