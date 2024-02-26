window.addEventListener("DOMContentLoaded", function() {

    /* 헤더 버튼 처리 S */
    const dashboardBtnArea = document.querySelector(".dashboardBtns");
    if(dashboardBtnArea.classList.contains("dn")){
        dashboardBtnArea.classList.remove("dn");
    }

    const dashboardBtns = dashboardBtnArea.querySelectorAll("button");

    for(const dashboardBtn of dashboardBtns){
        dashboardBtn.addEventListener("click", function(){
            const type = this.dataset.type;
            location.href = '/dashboard/' + type;
        });

        if(window.location.pathname.includes('/dashboard/' + dashboardBtn.dataset.type)){
            dashboardBtn.classList.add("on");
        }
    }
    /* 헤더 버튼 처리 E */

    /* 목록 클릭 처리 S */
    const observatories = document.querySelectorAll(".observatory");
    const type = document.querySelector("#type");
    for(const observatory of observatories){
        observatory.addEventListener("click", function(){
            const obscd = this.querySelector(".obscd").value;
            console.log(obscd);

        });
    }
    /* 목록 클릭 처리 E */
});