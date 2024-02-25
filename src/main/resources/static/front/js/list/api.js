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
            const res = await fetch(url);
            const result = await res.json();

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

                let key = `${year}.${month}.${day}`;

                switch (unit) {
                    case "1D": key = `${year}.${month}`; break;
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

            /* statData 출력 */
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


            /* 10M */
            if (unit === '10M') {
                thead.innerHTML = "";
                const typeHeader = document.createElement("div");
                typeHeader.classList.add("divTableHead");
                if (type === 'rf') {
                    typeHeader.textContent = "강수량";
                } else if (type === 'wl') {
                    typeHeader.textContent = "수위";
                } else if (type === 'fw') {
                    typeHeader.textContent = "유량";
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
                                    const var_substring = dateKey.substring(3, 5) + ':' + dateKey.substring(5);
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
            }

            else if(unit === '1H') {
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
            }


        } catch (err) {
            console.error(err);
        }
    });
});