const dashboard = {
    obscd: null,
    init() {

        let obscd = sessionStorage.getItem("prevObscd");
        if (!obscd) {
            const el = document.querySelector(".divTableBody .divTableRow");
            obscd = el.dataset.obscd;
        }

        this.update(obscd);
    },
     update(obscd) {
        if (obscd) {
            this.obscd = obscd;
            sessionStorage.setItem("prevObscd", obscd);

        } else {
            obscd = this.obscd;
        }

        if (!obscd) return;

        let type = location.pathname.split("/").pop();
        if (type == 'flw') type = 'fw';

        let label = "강수량";
        switch(type) {
            case "wl": label = "수위"; break;
            case "fw": label = "유량"; break;
        }

        let unit = document.querySelector("#searchFrm input[name='select']:checked").value;
        let sdate = new Date(), edate = new Date();
        if (unit === 'MONTH') { // 1D
            sdate.setMonth(sdate.getMonth() - 1); // 1달 전
            unit = '1D';
            label += '(월단위)';
        } else { // MONTH
            sdate.setYear(sdate.getYear() - 1); // 1년 전
            unit = 'MONTH';
            label += '(연단위)';
        }

        sdate = this.dateFormat(sdate);
        edate = this.dateFormat(edate);

        const search = { obscd, type, unit, sdate, edate };

        api.getData(search)
            .then(data => {
                const labels = [], dataset = [];

                for (const [key, items] of Object.entries(data)) {
                    if (unit == '1D') {
                        for (const [k, v] of Object.entries(items)) {
                            if (type === 'wl' && k.indexOf("wl_") !== -1) continue;
                            if (type === 'fw' && k.indexOf("fw_") !== -1) continue;

                            const field = `${key}.${k.split('_').pop()}`;
                            labels.push(field);
                            dataset.push(v);
                        }

                    } else {
                        for (const [k, v] of Object.entries(items)) {
                            if (type === 'wl' && k.indexOf("wl_") !== -1) continue;
                            if (type === 'fw' && k.indexOf("fw_") !== -1) continue;

                            let field = k.split("_");
                            field.shift();
                            field = field.join(".");

                            labels.push(field);
                            dataset.push(v);
                        }
                    }
                }

                    if (labels.length == 0) return;
                    const canvas = document.createElement("canvas");
                    canvas.id="myChart";
                    canvas.height = 600;
                    canvas.width = "100%";
                    const myChart = canvas.getContext('2d');

                    const chart = document.querySelector(".chart");
                    chart.innerHTML = "";
                    chart.appendChild(canvas);

                    new Chart(myChart, {
                            type: 'line',  // bar, pie, line, doughnut,polarArea
                            data: {
                              labels,
                              datasets: [{
                                label,
                                data: dataset,
                                borderColor: 'rgba(75, 192, 192, 1)',
                                borderWidth: 1,
                                fill: false
                              }]
                            },
                            options: {
                              scales: {
                                x: {
                                  beginAtZero: true
                                },
                                y: {
                                  beginAtZero: true
                                }
                              }
                            }
                        });

            })
            .catch(err => console.error(err));
    },
    dateFormat(date) {
        return `${date.getFullYear()}${("" + (date.getMonth() + 1)).padStart(2, '0')}${("" + date.getDate()).padStart(2, '0')}`;
    }
};


window.addEventListener("DOMContentLoaded", function() {
    // 각 페이지 로딩시 첫번째 행에 그래프 노출
    dashboard.init();

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

    const selectEl = document.querySelectorAll("#searchFrm input[name='select']");

    for (const el of selectEl) {
       el.addEventListener("click", () => dashboard.update());
    }

});