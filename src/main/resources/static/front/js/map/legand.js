window.addEventListener("DOMContentLoaded", function(){
    const legandBtn = document.querySelector(".legandBtn");
    const legandEl = legandBtn.querySelector("i");
    const legand = document.querySelector(".legand");


    /* 사이드 버튼 클릭하면 관측소 목록 노출 E */

    /* 꺽쇠 버튼 클릭 - 관측소 목록 토글 S */
    legandBtn.addEventListener("click", function(){
        if(legand.classList.contains("dn")){
            legand.classList.remove("dn");
            legandEl.className="xi-angle-right-thin"
        }else{
            legand.classList.add("dn");
            legandEl.className="xi-angle-left-thin";
        }
    })

    /* 꺽쇠 버튼 클릭 - 관측소 목록 토글 E */

});