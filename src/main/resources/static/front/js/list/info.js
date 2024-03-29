var commonLib = commonLib || {};

window.addEventListener("DOMContentLoaded", function(){
    const onlyYearDiv = document.querySelector(".onlyYear");
    const exceptYearDiv = document.querySelector(".exceptYear");
    getExceptYearDiv();

    const referrer = document.referrer;
    if (referrer.indexOf("/info") == -1) {
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

    const timeUnitEl = document.querySelector("#timeUnit");


    timeUnitEl.addEventListener("change", function(){
        if(timeUnitEl.value === 'YEAR') {
            getOnlyYearDiv();
            const startYearSelect = document.querySelector("#startYear");
            const endYearSelect = document.querySelector("#endYear");
            const currentYear = new Date().getFullYear();

            for (let year = currentYear - 3; year <= currentYear; year++) {
                const startOption = document.createElement("option");
                startOption.value = year;
                startOption.text = year;
                startYearSelect.appendChild(startOption);
                startYearSelect.lastElementChild.selected = true;

                const endOption = document.createElement("option");
                endOption.value = year;
                endOption.text = year;
                endYearSelect.appendChild(endOption);
                endYearSelect.lastElementChild.selected = true;
            }
        } else {
            getExceptYearDiv();
        }
    });




const info = {
    async init() {
const unit = document.querySelector("#timeUnit").value;
        try {
            const statData = await api.getData();
            console.log(statData);

            let type = frmSearch.type.value;
            if(type === 'flw') {
                type = 'fw';
            }

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
                                     const var_substring = `${dateKey.substring(3, 5)}:${dateKey.substring(5)}`;
                                     const combinedText = `${key} ${var_substring}`;
                                     const row = document.createElement("div");
                                     row.classList.add("divTableRow");
                                    if(type === 'wl' || type === 'fw') {
                                        if(rowData[dateKey] !== " ") {
                                            // 날짜 + 시간
                                            const dateCell = document.createElement("div");
                                            dateCell.classList.add("divTableCell");
                                            dateCell.textContent = combinedText;
                                            row.appendChild(dateCell);
                                            // 데이터
                                            const dataCell = document.createElement("div");
                                            dataCell.classList.add("divTableCell");

                                            dataCell.textContent = rowData[dateKey];
                                            row.appendChild(dataCell);

                                            tbody.appendChild(row);
                                        }
                                    } else {
                                    //현재 시간-3분 까지만 출력
                                    const date = getCurrentDateTime().substring(0, 10);
                                    const time = getCurrentDateTime().substring(10, 13) + '0';
                                        if(key === date) {
                                            if (dateKey.substring(3) <= time) {
                                            // 날짜 + 시간
                                            const dateCell = document.createElement("div");
                                            dateCell.classList.add("divTableCell");
                                            dateCell.textContent = combinedText;
                                            row.appendChild(dateCell);
                                            // 데이터
                                            const dataCell = document.createElement("div");
                                            dataCell.classList.add("divTableCell");

                                            dataCell.textContent = rowData[dateKey];
                                            row.appendChild(dataCell);

                                            tbody.appendChild(row);
                                            }

                                        }
                                    }

                                }
                            }
                        }
                    }
                }

        } catch (err) {
            console.error(err);
        }
    }
}



/* api 데이터 가져오기 */
if (typeof frmSearch === 'undefined' || !frmSearch) return;
    info.init();
    frmSearch.addEventListener("submit", async function(e) {
        const unit = document.querySelector("#timeUnit").value;
        e.preventDefault();
        try {
            const statData = await api.getData();
            console.log(statData);

            let type = frmSearch.type.value;
            if(type === 'flw') {
                type = 'fw';
            }

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
                                     const var_substring = `${dateKey.substring(3, 5)}:${dateKey.substring(5)}`;
                                     const combinedText = `${key} ${var_substring}`;
                                     const row = document.createElement("div");
                                     row.classList.add("divTableRow");
                                    if(type === 'wl' || type === 'fw') {
                                        if(rowData[dateKey] !== " ") {
                                            // 날짜 + 시간
                                            const dateCell = document.createElement("div");
                                            dateCell.classList.add("divTableCell");
                                            dateCell.textContent = combinedText;
                                            row.appendChild(dateCell);
                                            // 데이터
                                            const dataCell = document.createElement("div");
                                            dataCell.classList.add("divTableCell");

                                            dataCell.textContent = rowData[dateKey];
                                            row.appendChild(dataCell);

                                            tbody.appendChild(row);
                                        }
                                    } else {
                                    //현재 시간-3분 까지만 출력
                                    const date = getCurrentDateTime().substring(0, 10);
                                    const time = getCurrentDateTime().substring(10, 13) + '0';
                                        if(key === date) {
                                            if (dateKey.substring(3) <= time) {
                                            // 날짜 + 시간
                                            const dateCell = document.createElement("div");
                                            dateCell.classList.add("divTableCell");
                                            dateCell.textContent = combinedText;
                                            row.appendChild(dateCell);
                                            // 데이터
                                            const dataCell = document.createElement("div");
                                            dataCell.classList.add("divTableCell");

                                            dataCell.textContent = rowData[dateKey];
                                            row.appendChild(dataCell);

                                            tbody.appendChild(row);
                                            }

                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            } else if(unit === '1H') {
                const firstData = statData[Object.keys(statData)[0]];
                const head_keys = Object.keys(firstData).reverse();
                const typeUnit = document.querySelector(".typeUnit");
                typeUnit.innerHTML = "";
                if (type === 'rf') {
                    typeUnit.textContent = rfText;
                } else if (type === 'wl') {
                    typeUnit.textContent = wlText;
                } else if (type === 'fw') {
                    typeUnit.textContent = fwText;
                }
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
                                    const var_substring = `${dateKey.substring(3)}`;
                                    const combinedText = `${key}.${var_substring}`;

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
                                    dataCell.textContent = rowData[dateKey];
                                    row.appendChild(dataCell);

                                    tbody.appendChild(row);
                                }
                            }
                        }
                    }
                }
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
                for (const key in statData) {
                    if (statData.hasOwnProperty(key)) {
                        const rowData = statData[key];
                        for (const dateKey in rowData) {
                            if (rowData.hasOwnProperty(dateKey)) {
                                if (dateKey.includes(type)) {
                                    const yearText = dateKey.substring(3, 7);
                                    const monthText = dateKey.substring(8);
                                    const var_substring = `${yearText}.${monthText}`;

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
                                        dataCell.textContent = rowData[dateKey].toFixed(2);
                                    } else {
                                        dataCell.textContent = rowData[dateKey]
                                    }
                                    row.appendChild(dataCell);

                                    tbody.appendChild(row);
                                }
                            }
                        }
                    }
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
    function getCurrentDateTime() {
        const currentDate = new Date();
        const delayedDate = new Date(currentDate.getTime() - 3 * 60000); // 현재 시간에서 3분을 더한 시간을 계산
        const year = currentDate.getFullYear();
        const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 필요, 두 자리로 표시하기 위해 0을 채움
        const day = String(currentDate.getDate()).padStart(2, '0'); // 두 자리로 표시하기 위해 0을 채움
        const hours = String(currentDate.getHours()).padStart(2, '0'); // 시간을 두 자리로 표시하기 위해 0을 채움
        const minutes = String(currentDate.getMinutes()).padStart(2, '0'); // 분을 두 자리로 표시하기 위해 0을 채움

        return `${year}.${month}.${day}${hours}${minutes}`;
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

    function getExceptYearDiv() {
        exceptYearDiv.style.display = "inline-block";
        onlyYearDiv.style.display = "none";
    }

    function getOnlyYearDiv() {
        exceptYearDiv.style.display = "none";
        onlyYearDiv.style.display = "inline-block";
    }


});


