var commonLib = commonLib || {};

window.addEventListener("DOMContentLoaded", function(){
    const referrer = document.referrer;
    if (referrer.indexOf("/setting") == -1) {
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

    const { popup } = commonLib;
    const editFrm = document.querySelector("#editFrm");

    // 데이터 수정 버튼
    const editBtns = document.querySelectorAll(".editBtn");
    for(const editBtn of editBtns){
        editBtn.addEventListener("click", function(){
            const seqId = this.dataset.seqId;
            const seq = document.getElementById(seqId).value;
            editFrm.action = '/list/setting/edit/' + seq;
            editFrm.submit();
        });
    }



})