var commonLib = commonLib || {};

window.addEventListener("DOMContentLoaded", function(){
    const referrer = document.referrer;
    if (referrer.indexOf("/info") == -1) {
        sessionStorage.setItem("referrer", referrer);
    }

    // 목록 버튼
    const listBtn = document.querySelector("#listBtn");
    listBtn.addEventListener("click", function(){

       const referrer = sessionStorage.getItem("referrer");
       const url = referrer ? referrer : '/list';

       sessionStorage.removeItem("referrer");
       location.href=url;

    });
});
