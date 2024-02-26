const api = {
    apiUrl: "https://api.hrfco.go.kr",
    apiKey: "FD14A031-75BC-4BB4-B271-E68E7470A8BF",

};

 //url : https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/list/10M/1018683/202402150930/202402160930.json

  //https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/list/rainfall/10M/10011100/202402240000/202402242359.json

window.addEventListener("DOMContentLoaded", function() {
    frmSearch.addEventListener("submit", async function(e) {
    //console.log('클릭');
        e.preventDefault();

        const formData = new FormData(frmSearch);
        const obscd = formData.get("obscd");
        const type = formData.get("type");
        let sdate = formData.get("sdate");
        let edate = formData.get("edate");
        const unit = formData.get("timeUnit");
        console.log('formData '+formData);
        const { apiUrl, apiKey } = api;
        let url = `${apiUrl}/${apiKey}/`;
        switch(type) {
            case "rf" : url += "rainfall"; break;
            case "wl":
            case "fw":
                url += "waterlevel"; break;
        }
        url += "/list";
        if (unit !== 'MONTH' && unit !== 'YEAR') {
            url += `/${unit}`;
        }  else {
            url += "/1D";
        }
        const date = new Date();
        sdate = sdate || `${date.getFullYear()}${("" + (date.getMonth() + 1)).padStart(2, '0')}${("" + date.getDate()).padStart(2, '0')}`
        edate = edate || sdate;
        sdate = `${sdate.replace(/\D/g, '')}0000`;
        edate = `${edate.replace(/\D/g, '')}2359`;

        url += `/${obscd}/${sdate}/${edate}.json`;

        console.log('url ' + url);
        try {
            const res = await fetch(url); // 비동기(await)방식으로 api 데이터 요청
            const result = await res.json(); // 응답데이터 -> JSON형식으로 파싱

            if (!result) {
                return;
            }

            const { content: items } = result;
            if (!items) return;

            const statData = {};
            for (const item of items) {
                const { ymdhm } = item;
                const year = ymdhm.substring(0, 4);
                const month = ymdhm.substring(4, 6);
                const day = ymdhm.substring(6,8);
                let hour, min;
                if (ymdhm.length > 8) {
                    hour = ymdhm.substring(8, 10);
                    min = ymdhm.substring(8, 12);
                }

                let key = `${year}-${month}-${day}`;

                switch (unit) {
                    case "1D": key = `${year}-${month}`; break;
                    case "MONTH": key = `${year}`;
                    case "YEAR": break;
                }

                if (unit !== 'YEAR') {
                    statData[key] = statData[key] || {};
                }
                if (type === 'rf') {
                    switch (unit) {
                        case "10M" : statData[key]['rf_' + min] = item.rf; break;
                        case "1H" : statData[key]['rf_' + hour] = item.rf; break;
                        case "1D" : statData[key]['rf_' + day] = item.rf; break;
                        case "MONTH" :
                            statData[year]['rf_' + `${year}_${month}`] = statData[year]['rf_' + `${year}_${month}`] || 0;
                            statData[year]['rf_' + `${year}_${month}`] += isNaN(item.rf) ? 0 : Number(item.rf);
                            break;
                       case "YEAR" :
                            statData[year] = statData[year] || 0;
                            statData[year] += isNaN(item.rf) ? 0 : Number(item.rf);
                            break;
                    }
                } else {
                    switch (unit) {
                        case "10M":
                            statData[key]['wl_' + min] = item.wl;
                            statData[key]['fw_' + min] = item.fw;
                            break;
                        case "1H":
                            statData[key]['wl_' + hour] = item.wl;
                            statData[key]['fw_' + hour] = item.fw;
                            break;
                        case "1D":
                            statData[key]['wl_' + day] = item.wl;
                            statData[key]['fw_' + day] = item.fw;
                            break;
                       case "MONTH":
                            statData[year]['wl_' + `${year}_${month}`] = statData[year]['wl_' + `${year}_${month}`] || 0;
                            statData[year]['wl_' + `${year}_${month}`] += isNaN(item.wl) ? 0 : Number(item.wl);

                            statData[year]['fw_' + `${year}_${month}`] = statData[year]['fw_' + `${year}_${month}`] || 0;
                            statData[year]['fw_' + `${year}_${month}`] += isNaN(item.fw) ? 0 : Number(item.fw);
                            break;
                        case "YEAR":
                             const v = type === 'wl' ? item.wl : item.fw;
                            statData[year] = statData[year] || 0;
                             statData[year] += isNaN(v) ? 0 : Number(v);


                    }
                }
            } // endfor

            console.log(statData);

            /* list info 데이터 S */
            const statTable = document.querySelector(".divTable");
            const thead = statTable.querySelector(".divTableHeading");
            const tbody = statTable.querySelector(".divTableBody");

            // thead 출력
            thead.innerHTML = "";
            const headerRow = document.createElement("div");
            headerRow.classList.add("divTableRow");
            const fixedHeader = document.createElement("div");
            fixedHeader.classList.add("divTableHead");
            // 고정항목
            fixedHeader.textContent = "관측일시";
            headerRow.appendChild(fixedHeader);

            const rfText = "강수량(mm)";
            const wlText = "수위(m)";
            const fwText = "유량(㎥/sec)";



            /* 10M */
            if (unit === '10M') {
                thead.innerHTML = "";
                const typeHeader = document.createElement("div");
                typeHeader.classList.add("divTableHead");
                if (type === 'rf') {
                    typeHeader.textContent = rfText;
                } else if (type === 'wl') {
                    typeHeader.textContent = wlText;
                } else if (type === 'fw') {
                    typeHeader.textContent = fwText;
                }
                headerRow.appendChild(typeHeader);
                thead.appendChild(headerRow);

                // tbody
                tbody.innerHTML = "";
                for (const key in statData) {
                    if (statData.hasOwnProperty(key)) {
                        const rowData = statData[key];
                        for (const dateKey in rowData) {
                            if (rowData.hasOwnProperty(dateKey)) {
                                if (dateKey.includes(type)) {
                                    // ex) wl_2350 -> 2350   dateKey.substring(3, 5) + ':' + dateKey.substring(5);
                                    const var_substring = `${dateKey.substring(3, 5)}:${dateKey.substring(5)}`;
                                    const combinedText = `${key} ${var_substring}`;

                                    const row = document.createElement("div");
                                    row.classList.add("divTableRow");
                                    // 날짜 + 시간
                                    const dateCell = document.createElement("div");
                                    dateCell.classList.add("divTableCell");
                                    dateCell.textContent = combinedText;
                                    row.appendChild(dateCell);
                                    // 데이터
                                    const dataCell = document.createElement("div");
                                    dataCell.classList.add("divTableCell");
                                    dataCell.textContent = rowData[dateKey];
                                    console.log('rowData[dateKey]' + rowData[dateKey]);
                                    row.appendChild(dataCell);

                                    tbody.appendChild(row);
                                }
                            }
                        }
                    }
                }
            } else if(unit === '1H') {
                const firstData = statData[Object.keys(statData)[0]];
                const head_keys = Object.keys(firstData).reverse();
                for (const key of head_keys) {
                    // 변수명에 타입 포함할 때만
                    if(key.includes(type)) {
                    // ex) wl_00 -> 00 으로 자르기
                    const var_substring = key.substring(3);
                    const th = document.createElement("div");
                    th.classList.add("divTableHead");
                    th.textContent = var_substring
                    headerRow.appendChild(th);
                    }
                }
                thead.appendChild(headerRow);

                // tbody 출력
                tbody.innerHTML = ""; // tbody 초기화
                for (const key in statData) {
                    if (statData.hasOwnProperty(key)) {
                        const rowData = statData[key];
                        const row = document.createElement("div");
                        row.classList.add("divTableRow");
                        // 날짜 추가
                        const dateCell = document.createElement("div");
                        dateCell.classList.add("divTableCell");
                        dateCell.textContent = key;
                        row.appendChild(dateCell);

                        // 데이터 필드 순회 및 추가
                        const row_keys = Object.keys(rowData).reverse();
                        for (const dataKey of row_keys) {
                            if (rowData.hasOwnProperty(dataKey)) {
                                if(dataKey.includes(type)) {
                                    const dataCell = document.createElement("div");
                                    dataCell.classList.add("divTableCell");
                                    dataCell.textContent = rowData[dataKey];
                                    row.appendChild(dataCell);
                                }
                            }
                        }

                        tbody.appendChild(row);
                    }

                }
            } else if(unit === '1D') {
                thead.innerHTML = "";
                const typeHeader = document.createElement("div");
                typeHeader.classList.add("divTableHead");
                if (type === 'rf') {
                    typeHeader.textContent = rfText;
                } else if (type === 'wl') {
                    typeHeader.textContent = wlText;
                } else if (type === 'fw') {
                    typeHeader.textContent = fwText;
                }
                headerRow.appendChild(typeHeader);
                thead.appendChild(headerRow);

                // tbody
                tbody.innerHTML = "";
                for (const key in statData) {
                    if (statData.hasOwnProperty(key)) {
                        const rowData = statData[key];
                        for (const dateKey in rowData) {
                            if (rowData.hasOwnProperty(dateKey)) {
                                if (dateKey.includes(type)) {
                                    // ex) wl_2350 -> 2350
                                    const var_substring = `-${dateKey.substring(3)}`;
                                    const combinedText = `${key}${var_substring}`;

                                    const row = document.createElement("div");
                                    row.classList.add("divTableRow");
                                    // 날짜
                                    const dateCell = document.createElement("div");
                                    dateCell.classList.add("divTableCell");
                                    dateCell.textContent = combinedText;
                                    row.appendChild(dateCell);
                                    // 데이터
                                    const dataCell = document.createElement("div");
                                    dataCell.classList.add("divTableCell");
                                    /* 대시보드 - 월 : rowData[dateKey] */
                                    dataCell.textContent = rowData[dateKey];
                                    console.log('rowData[dateKey]' + rowData[dateKey]);
                                    row.appendChild(dataCell);

                                    tbody.appendChild(row);
                                }
                            }
                        }
                    }
                }
                url += getCurrentDate();
            } else if(unit === 'MONTH') {
                thead.innerHTML = "";
                const typeHeader = document.createElement("div");
                typeHeader.classList.add("divTableHead");
                if (type === 'rf') {
                    typeHeader.textContent = rfText;
                } else if (type === 'wl') {
                    typeHeader.textContent = wlText;
                } else if (type === 'fw') {
                    typeHeader.textContent = fwText;
                }
                headerRow.appendChild(typeHeader);
                thead.appendChild(headerRow);

                // tbody
                tbody.innerHTML = "";
                let sum, avg = 0;
                for (const key in statData) {
                    if (statData.hasOwnProperty(key)) {
                        const rowData = statData[key];
                        for (const dateKey in rowData) {
                            if (rowData.hasOwnProperty(dateKey)) {
                                if (dateKey.includes(type)) {
                                    const yearText = dateKey.substring(3, 7);
                                    const monthText = dateKey.substring(8);
                                    // ex) 2024-02
                                    const var_substring = `${yearText}-${monthText}`;

                                    const row = document.createElement("div");
                                    row.classList.add("divTableRow");
                                    // 날짜
                                    const dateCell = document.createElement("div");
                                    dateCell.classList.add("divTableCell");
                                    dateCell.textContent = var_substring;
                                    row.appendChild(dateCell);
                                    // 데이터
                                    const dataCell = document.createElement("div");
                                    dataCell.classList.add("divTableCell");

                                    // wl, fw는 평균 구하기
                                    if(type === 'wl' || type === 'fw') {
                                        const daysInMonth = getDaysInMonth(parseInt(yearText), parseInt(monthText));

                                        /* 대시?
                                        sum += rowData[dateKey];
                                        console.log('sum ' + sum);
                                        dataCell.textContent = rowData[dateKey];
                                        */

                                        rowData[dateKey] /= daysInMonth; // 데이터
                                        dataCell.textContent = rowData[dateKey].toFixed(2); // 두 자리까지 반올림
                                    } else {
                                        dataCell.textContent = rowData[dateKey]
                                    }
                                    row.appendChild(dataCell);

                                    tbody.appendChild(row);
                                }
                            }
                        }
                    }
                    /* 대시?
                    avg = (sum / 30).toFixed(2); // 두 자리까지 반올림
                    */
                }
            } else if(unit === 'YEAR') {
                thead.innerHTML = "";
                const typeHeader = document.createElement("div");
                typeHeader.classList.add("divTableHead");
                if (type === 'rf') {
                    typeHeader.textContent = rfText;
                } else if (type === 'wl') {
                    typeHeader.textContent = wlText;
                } else if (type === 'fw') {
                    typeHeader.textContent = fwText;
                }
                headerRow.appendChild(typeHeader);
                thead.appendChild(headerRow);

                // tbody
                tbody.innerHTML = "";
                for (const key in statData) {
                    if (statData.hasOwnProperty(key)) {
                        const rowData = statData[key];
                        for (const dateKey in rowData) {
                            if (rowData.hasOwnProperty(dateKey)) {
                                if (dateKey.includes(type)) {
                                    const row = document.createElement("div");
                                    row.classList.add("divTableRow");
                                    // 날짜
                                    const dateCell = document.createElement("div");
                                    dateCell.classList.add("divTableCell");
                                    dateCell.textContent = dateKey;
                                    row.appendChild(dateCell);
                                    // 데이터
                                    const dataCell = document.createElement("div");
                                    dataCell.classList.add("divTableCell");
                                    // wl, fw는 평균 구하기
                                    if(type === 'wl' || type === 'fw') {
                                        const daysInMonth = getDaysInMonth(parseInt(yearText), parseInt(monthText));
                                        rowData[dateKey] /= daysInMonth;
                                        dataCell.textContent = rowData[dateKey].toFixed(2); // 두 자리까지 반올림
                                    }
                                    dataCell.textContent = rowData[dateKey];
                                    console.log('rowData[dateKey]' + rowData[dateKey]);
                                    row.appendChild(dataCell);

                                    tbody.appendChild(row);
                                }
                            }
                        }
                    }
                }
            }
            /* list info 데이터 E */

        } catch (err) {
            console.error(err);
        }
    });

    /* 대시보드 - 월 */
    // 현재 년월일
    function getCurrentDate() {
        // 현재
        const currentDate = new Date();
        const year = currentDate.getFullYear();
        const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요, 두 자리로 표시하기 위해 0을 채움
        const day = String(currentDate.getDate()).padStart(2, '0'); // 두 자리로 표시하기 위해 0을 채움
        return `${year}${month}${day}`;
    }

    function get30DaysAgo() {
        const currentDate = new Date();
        const thirtyDaysAgo = new Date(currentDate.getTime() - 30 * 24 * 60 * 60 * 1000); // 현재 날짜로부터 30일 전의 날짜를 계산

        const year = thirtyDaysAgo.getFullYear();
        const month = String(thirtyDaysAgo.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요, 두 자리로 표시하기 위해 0을 채움
        const day = String(thirtyDaysAgo.getDate()).padStart(2, '0'); // 두 자리로 표시하기 위해 0을 채움

        return `${year}${month}${day}`;
    }

    function get30DateTime(){
            const url = getCurrentDate() + '/' + get30DaysAgo();
        return url_30;
    }

    // 현재 월이 아니면 해당 월의 전체 날짜 출력, 현재 월이면 오늘까지 날짜 출력
    function getDaysInMonth(year, month) {
        // 현재 날짜
        const currentDate = new Date();
        const currentYear = currentDate.getFullYear();
        const currentMonth = currentDate.getMonth() + 1;
        const currentDay = currentDate.getDate();

        let daysInTargetMonth = new Date(year, month, 0).getDate();
        let daysUntilToday = 0;

        if (year === currentYear && month === currentMonth) {
            daysUntilToday = currentDay;
        } else {
            daysUntilToday = daysInTargetMonth;
        }
        return daysUntilToday;
    }




});