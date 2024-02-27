var commonLib = commonLib || {};

window.addEventListener("DOMContentLoaded", function(){

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

    if (typeof frmSearch === 'undefined' || !frmSearch) return;

    frmSearch.addEventListener("submit", async function(e) {
    //console.log('클릭');
        e.preventDefault();
        try {
            const statData = await api.getData();

            const type = frmSearch.type.value;

            /*처리된 데이터(statData)를 기반으로 HTML 테이블을 동적으로 생성하여 화면에 표시합니다. */
            /* statData 출력 */
            const statDataTable = document.getElementById("statDataTable");
            const thead = statDataTable.querySelector("thead");
            const tbody = statDataTable.querySelector("tbody");

            // thead 출력

            thead.innerHTML = "";
            const headerRow = document.createElement("tr");
            const fixedHeader = document.createElement("th");
            // 고정항목
            fixedHeader.textContent = "관측일시";
            headerRow.appendChild(fixedHeader);

            const firstData = statData[Object.keys(statData)[0]];
            const head_keys = Object.keys(firstData).reverse();
            for (const key of head_keys) {
                // 변수명에 타입 포함할 때만
                if(key.includes(type)) {
                // ex) wl_00 -> 00 으로 자르기
                const var_substring = key.substring(3);
                const th = document.createElement("th");
                th.textContent = var_substring
                headerRow.appendChild(th);
                }
            }
            thead.appendChild(headerRow);

            // tbody 출력
            //const body_keys = Object.keys(statData).reverse();
            tbody.innerHTML = ""; // tbody 초기화
            for (const key in statData) {
                if (statData.hasOwnProperty(key)) {
                    const rowData = statData[key];
                    const row = document.createElement("tr");

                    // 날짜 추가
                    const dateCell = document.createElement("td");
                    dateCell.textContent = key;
                    row.appendChild(dateCell);

                    // 데이터 필드 순회 및 추가
                    const row_keys = Object.keys(rowData).reverse();
                    for (const dataKey of row_keys) {
                        if (rowData.hasOwnProperty(dataKey)) {
                            dataCell = document.createElement("td");
                            dataCell.textContent = rowData[dataKey];
                            row.appendChild(dataCell);
                        }
                    }

                    tbody.appendChild(row);
                }
            }

        /*요청 처리 중 발생할 수 있는 오류를 캐치하고, 콘솔에 오류 메시지를 출력*/
        } catch (err) {
            console.error(err);
        }
    });
});
