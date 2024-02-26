const dashboard = {
     obscd : null,
     update(obscd) {
        if(obscd){
            this.obscd = obscd;
        }else{
            obscd = this.obscd;
        }

        if(!obscd) return;

        let type = location.href.split("/").pop();
        if(type === 'flw') type = 'fw';

        let unit = document.querySelector("#searchFrm input[name='select']:checked").value;
        let sdate = new Date(), edate = new Date();
        if (unit === 'MONTH') { // 1D
            sdate.setMonth(sdate.getMonth() - 1); // 1달 전
            unit = '1D';

        } else { // MONTH
            sdate.setYear(sdate.getYear() - 1); // 1년 전
            unit = 'MONTH';
        }

        sdate = this.dateFormat(sdate);
        edate = this.dateFormat(edate);

        const search = { obscd, type, unit, sdate, edate };

        api.getData(search)
            .then(data => {
                const labels = [], dataset = [];

                for(const [key, items] of Object.entries(data)){
                    if(unit == '1D'){
                        for(const [k, v] of Object.entries(items)){
                            const field = `${key}.${k.split('_').pop()}`;
                            labels.push(field);
                            dataset.push(v);
                        }
                        console.log(labels, dataset);

                    }
                }
            })
            .catch(err => console.error(err));
    },
    dateFormat(date) {
        return `${date.getFullYear()}${("" + (date.getMonth() + 1)).padStart(2, '0')}${("" + date.getDate()).padStart(2, '0')}`;
    }
};


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
            dashboard.update(obscd);

        });
    }
    /* 목록 클릭 처리 E */

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
            dashboard.update(obscd);

            const selectEl = document.querySelectorAll("#searchFrm input[name='select']");

            for(const el of selectEl){
                el.removeEventListener("click", changeUnit);
                el.addEventListener("click", changeUnit);
            }

        });
    }

    function changeUnit(){
        dashboard.update();
    }

    /* 목록 클릭 처리 E */

});