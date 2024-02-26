/*사용자가 입력한 날짜 범위, 관측소 코드(obscd), 자료 유형(type), 시간 단위(unit)에 따라
기상 현상(강수량 또는 수위) 데이터를 조회하고, 이를 테이블 형태로 화면에 출력*/

/*API의 기본 URL(apiUrl)과 인증을 위한 API 키(apiKey)가 설정 객체인 api에 저장됩니다.*/
const api = {
    apiUrl: "https://api.hrfco.go.kr",
    apiKey: "FD14A031-75BC-4BB4-B271-E68E7470A8BF",
    getData(search) {

        let obscd, type, sdate, edate, unit;

         if (search) {
            obscd = search.obscd;
            type = search.type;
            sdate = search.sdate;
            edate = search.edate;
            unit = search.unit;
         } else {
            /*제출된 폼에서 데이터를 추출*/
            const formData = new FormData(frmSearch);
            obscd = formData.get("obscd");
            type = formData.get("type");
            sdate = formData.get("sdate");
            edate = formData.get("edate");
            unit = formData.get("timeUnit");
         }
        return new Promise((resolve, reject) => {


             /*API 요청 URL 생성*/
             const { apiUrl, apiKey } = api;
             let url = `${apiUrl}/${apiKey}/`;
             /*자료 유형(type)에 따라 URL의 경로가 결정*/
             switch(type) {
                 case "rf" : url += "rainfall"; break;
                 case "wl":
                 case "fw":
                     url += "waterlevel"; break;
             }
             url += "/list";
             /*시간 단위에 따라 URL이 조정*/
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

             fetch(url)
                    .then(res => res.json())
                    .then(result => {
                           const { content: items } = result;
                           if (!items) {
                                reject(null);
                                return;
                           }

                           /*생성된 URL을 사용하여 API 요청을 수행합니다.
                           응답으로 받은 데이터에서 관측 값들을 추출하고, 이를 정리하여 statData 객체에 저장*/
                           const statData = {};
                           for (const item of items) {
                               const { ymdhm } = item;
                               const year = ymdhm.substring(0, 4);
                               const month = ymdhm.substring(4, 6);
                               const day = ymdhm.substring(6,8);
                               let hour, min;
                               if (ymdhm.length > 8) {
                                   hour = ymdhm.substring(8, 10);
                                   min = ymdhm.substring(10);
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

                           resolve(statData);
                    })
                    .catch(err => reject(err));
            });
    }
};

 //url : https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/waterlevel/list/10M/1018683/202402150930/202402160930.json

  //https://api.hrfco.go.kr/FD14A031-75BC-4BB4-B271-E68E7470A8BF/list/rainfall/10M/10011100/202402240000/202402242359.json

window.addEventListener("DOMContentLoaded", function() {
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