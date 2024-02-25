var commonLib = commonLib || {};

window.addEventListener("DOMContentLoaded", function(){
    const referrer = document.referrer;
    if (referrer.indexOf("/setting") == -1) {
        sessionStorage.setItem("referrer", referrer);
    }

    // 목록 버튼
    const listBtn = document.querySelector("#listBtn");
    listBtn.addEventListener("click", function(){
        console.log('목록');

       const referrer = sessionStorage.getItem("referrer");
       const url = referrer ? referrer : '/list';

       sessionStorage.removeItem("referrer");
       location.href=url;
    });
});