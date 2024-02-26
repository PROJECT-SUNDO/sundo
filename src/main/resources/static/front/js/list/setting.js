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
    const submitFrm = document.querySelector("#submitFrm");

    // 데이터 수정 버튼
    const editBtns = document.querySelectorAll(".editBtn");
    for(const editBtn of editBtns){
        editBtn.addEventListener("click", function(){
            const seqId = this.dataset.seqId;
            const seq = document.getElementById(seqId).value;
            submitFrm.action = '/list/setting/edit/' + seq;
            submitFrm.submit();
        });
    }

    // 데이터 삭제버튼

    const deleteBtn = document.querySelector(".deleteBtn");

    deleteBtn.addEventListener("click", function(){
        const seqId = this.dataset.seqId;
        const seq = document.getElementById(seqId).value;
        const type = document.querySelector("#type").value;
        const queryString = '?type=' + type;
        const url = '/list/setting/delete/' + seq + queryString;

        popup.open(url, 450, 200);
    })



    // 이상치 사용안하면
    const useOutlier = document.getElementById("useOutlier");
    const outlier = document.getElementById("outlier");
    if(!useOutlier.checked){
        outlier.setAttribute("readonly", true);
    }


    useOutlier.addEventListener("click", function(){
        if(!useOutlier.checked){
            outlier.setAttribute("readonly", true);
        }else{
            outlier.removeAttribute("readonly");
        }
    })


})