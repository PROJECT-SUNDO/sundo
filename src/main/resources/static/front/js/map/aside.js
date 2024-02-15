window.addEventListener("DOMContentLoaded", function(){
    const angleBtn = document.querySelector(".angleBtn");
    const angleEl = angleBtn.querySelector("i");
    const aside = document.querySelector(".aside");

    /* 사이드 버튼 클릭하면 관측소 목록 노출 S */
    const tabBtns = document.querySelectorAll(".asideBtns button");

    for(const tabBtn of tabBtns){
        tabBtn.addEventListener("click", function(){
            const targetVal = this.dataset.target;
            const targetEl = document.querySelector("#target");
            const targetNm = document.querySelector(".targetNm");
            const orderNm = document.querySelector(".orderNm");
            const colNm = document.querySelector("#colNm");
            const colVal = document.querySelector("#colVal");

            colNm.innerText = '관측소명';
            targetEl.value = targetVal;

            if(targetVal === 'wl'){
                targetNm.innerText = '수위 관측소';
                colVal.innerText = '실시간 수위';
                orderNm.innerText = '수위';
                orderNm.value = targetVal;
            }else if(targetVal === 'rf'){
                targetNm.innerText = '강수량 관측소';
                colVal.innerText = '실시간 강수량';
                orderNm.innerText = '강수량';
                orderNm.value = targetVal;
            }else if(targetVal === 'flw'){
                targetNm.innerText = '유량 관측소';
                colVal.innerText = '실시간 유량';
                orderNm.innerText = '유량';
                orderNm.value = targetVal;
            }else{
                colNm.innerText = '지점명';
                colVal.innerText = '상태';
                targetNm.innerText = 'CCTV';
                orderNm.innerText = '상태';
                orderNm.value = targetVal;
            }
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
            const targetEl = document.querySelector("#target");
            if(!targetEl.value){
                tabBtns[0].click();
            }else{
                aside.classList.remove("dn");
                angleEl.className="xi-angle-left-thin"
            }

        }else{
            aside.classList.add("dn");
            angleEl.className="xi-angle-right-thin";
        }

    })



    /* 꺽쇠 버튼 클릭 - 관측소 목록 토글 E */

});