function addMarker(items){
    // 마커 레이어 비우기
    if (mapLib.map) {
        mapLib.map.getLayers().forEach(layer => layer.getSource().refresh());
        mapLib.geometry = null;
    }

    const markerImages = {
        'wl': 'https://ifh.cc/g/LOYpqF.png',  // 수위관측소: 파랑
        'rf': {
            normal: 'https://ifh.cc/g/T3dOrD.png',  // 강수량관측소 기본: 빨강
            outlier: 'https://ifh.cc/g/zv2YT2.png'  // 강수량관측소 이상치 값 넘겼을떄
        },
        'flw': 'https://ifh.cc/g/5vB6gZ.png',   // 유량 관측소: 노랑
        'cctv' : 'https://ifh.cc/g/LMzbNh.png'   // cctv
    }

    // 타입에 따른 팝업 크기 정의
    const popupSizes = {
        'rf': {width: 430, height: 260},  // 강수량
        'wl': {width: 430, height: 260},  // 수위
        'flw': {width: 430, height: 230},  // 유량
        'cctv' : {width: 380, height: 380}   // cctv
    };

    const type = frmSide.location.pathname.split("/").pop();

    // 마커 값 설정
    for(const item of items) {

        let { lon, lat } = item;

        // 텍스트 형태 변환
        if (!lon || !lat) continue;

        const [lon1, lon2, lon3] = lon.split("-");
        lon = Number(lon1) + Number(lon2)/60 + Number(lon3)/3600;   // 경도

        const [lat1, lat2, lat3] = lat.split("-");
        lat = Number(lat1) + Number(lat2)/60 + Number(lat3)/3600;   // 위도

        const obscd = item.obscd;  // 관측소 코드

        const mapProjection = "EPSG:3857";
    	const dataProjection = "EPSG:4326";


        // 마커 feature 설정
        const geometry = new ol.geom.Point([lon, lat]).transform(dataProjection, mapProjection);
        mapLib.geometry = geometry;

        const feature = new ol.Feature({
            geometry, //transform()경도 위도에 포인트 설정

            properties : {
                name: "markers",
                item: item, // 관측소의 모든 정보를 저장
            },
            name: obscd
        });

        // 마커 레이어에 들어갈 소스 생성
        const markerSource = new ol.source.Vector({
            features: [feature] //feature의 집합
        });

        // 해당 타입과 상황에 맞는 마커 이미지 URL 가져오기
        let markerImageUrl;

        if (type === 'rf') { // 강수량 관측소인 경우
            // 이상치 값을 넘는지 확인
            if (item.data > item.outlier) {
                // 이상치값을 넘으면 이상치마커
                markerImageUrl = markerImages['rf'].outlier;
            } else {
                // 이상치값을 넘지 않으면 기본마커
                markerImageUrl = markerImages['rf'].normal;
            }
        } else { // 그 외의 경우
            // 관측소 타입별로 마커사용
            markerImageUrl = markerImages[type];
        }

        // 마커 스타일 설정
        const  markerStyle = new ol.style.Style({
            image: new ol.style.Icon({ //마커 이미지
                // src : 'https://ifh.cc/g/3O0MmJ.png',
                src: markerImageUrl, // 타입에 맞는 마커 이미지 사용
                opacity: 1, // 투명도 설정 (0: 완전 투명, 1: 완전 불투명)
                scale: 0.06 //크기 1=100%
            })
        });

        // 마커 레이어 생성
        const markerLayer = new ol.layer.Vector({
            source: markerSource, //마커 feacture들
            style: markerStyle //마커 스타일
        });

        // 지도에 마커가 그려진 레이어 추가s
        if (mapLib.map) {
            mapLib.map.addLayer(markerLayer);
        }

        // // 마커 레이어 저장
        // mapLib.markerLayer = markerLayer;

        mapLib.map.getView().setCenter(mapLib.geometry.getCoordinates());
        mapLib.map.getView().setZoom(11);



    }

    // 마커를 클릭하면 팝업 함수 실행
    mapLib.map.addEventListener('click', function(event) {
        // const pixel = event.pixel;
        // console.log('Screen coordinates:', pixel);

        mapLib.map.forEachFeatureAtPixel(event.pixel, function(feature) {
            const { popup } = commonLib;
            const obscd = feature.get('name');   // 클릭된 마커의 name: obscd
            const item = feature.get('properties').item;    // 클릭된 마커의 item: item

            const coordinates = feature.getGeometry().getCoordinates();
            const mapProjection = "EPSG:3857";
            const dataProjection = "EPSG:4326";
            const position = ol.proj.transform(coordinates, mapProjection, dataProjection); // 마커의 위치를 화면 좌표로 변환


            // 화면 좌표를 사용하여 팝업의 위치를 설정
            const pixel = mapLib.map.getPixelFromCoordinate(position);
            const popupEl = document.getElementById("layer_popup_map");
            if (popupEl) {
                popupEl.style.left = pixel[0] + 'px';  // 마커 오른쪽에 팝업이 뜨도록 left 속성 조정
                popupEl.style.top = (pixel[1] - 300) + 'px';  // 마커 위에 팝업이 뜨도록 top 속성 조정
            }

            // item이 undefined인지 확인
            if (!item) {
                console.error('Item is undefined:', feature);
                return;
            }

            let endpoint;
            switch(type) {
                case 'rf':  // 강수량
                    endpoint = '/map/popup/rf';
                    break;
                case 'wl':  // 수위
                    endpoint = '/map/popup/wl';
                    break;
                case 'flw':  // 유량
                    endpoint = '/map/popup/flw';
                    break;
                case 'cctv':  // cctv
                    endpoint = '/map/popup/cctv';
                    break;
                default:
                    console.error('Unknown marker type:', item.type);
                    return;
            }

            const url = `${endpoint}?obscd=${obscd}&item=${encodeURIComponent(JSON.stringify(item))}`;  // 타입에 맞게 url결정

            // 팝업을 띄우는 코드
            //popup.open(url, 430, 350);
            const {width, height} = popupSizes[type]; // 타입에 맞는 팝업 크기 가져오기
            popup.open(url, width, height);

            // 반복 끝내기
            return true;
        });
    });

    // 맵의 viewport에 'mousemove' 이벤트 리스너를 추가합니다.
    mapLib.map.getViewport().addEventListener('mousemove', function(e) {
        // 현재 마우스 위치에 대한 픽셀 값을 가져옵니다.
        const pixel = mapLib.map.getEventPixel(e);

        // 해당 픽셀 위치에 feature(마커)가 있는지 확인합니다.
        const hit = mapLib.map.hasFeatureAtPixel(pixel);

        // feature가 있으면 마우스 커서를 'pointer'로 변경하고,
        // 그렇지 않으면 기본 값으로 설정합니다.
        mapLib.map.getTargetElement().style.cursor = hit ? 'pointer' : '';
    });

}

window.addEventListener("DOMContentLoaded", function() {
    const el = document.getElementById("map");
    if (el) {
        el.addEventListener("click", function(e) {
           const xpos = e.pageX + e.offsetX;
           const ypos = e.pageY + e.offsetY;
           console.log(xpos, ypos);
        });
    }
});