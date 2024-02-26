window.addEventListener("DOMContentLoaded", function(){
    const angleBtn = document.querySelector(".angleBtn");
    const angleEl = angleBtn.querySelector("i");
    const aside = document.querySelector(".aside");

    /* 사이드 버튼 클릭하면 관측소 목록 노출 S */
    const tabBtns = document.querySelectorAll(".asideBtns button");
    const searchFrm = document.querySelector(".searchFrm");

    for(const tabBtn of tabBtns){
        tabBtn.addEventListener("click", function(){

            // 모든 버튼에서 'on' 클래스 제거
            tabBtns.forEach((btn) => {
                btn.classList.remove('on');
            });

            // 현재 클릭된 버튼에 'on' 클래스 추가
            this.classList.add('on');

            if(aside.classList.contains("dn")){
                aside.classList.remove("dn");
                angleEl.className="xi-angle-left-thin";
            }

        });
    }

    /* 사이드 버튼 클릭하면 관측소 목록 노출 E */

    /* 꺽쇠 버튼 클릭 - 관측소 목록 토글 S */
    angleBtn.addEventListener("click", function(){
        if(aside.classList.contains("dn")){
            aside.classList.remove("dn");
            angleEl.className="xi-angle-left-thin"
        }else{
            aside.classList.add("dn");
            angleEl.className="xi-angle-right-thin";
        }

    })

    /* 꺽쇠 버튼 클릭 - 관측소 목록 토글 E */

});