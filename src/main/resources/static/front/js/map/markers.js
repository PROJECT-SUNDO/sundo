function addMarker(items){ //경도 위도 이름값(마커들을 구분하기위해)
    // 마커 레이어 비우기
    if (mapLib.map) {
        mapLib.map.getLayers().forEach(layer => layer.getSource().refresh());
        mapLib.geometry = null;
    }

    // 마커 값 설정
    for(const item of items) {

        let { lon, lat } = item;

        if (!lon || !lat) continue;
        lon = parseFloat(lon.replace("-", ".").replace(/-/g, ""));  // 경도
        lat = parseFloat(lat.replace("-", ".").replace(/-/g, ""));  // 위도
        const name = item.obsnm;  // 관측소 명

        // 마커 feature 설정
        const mapProjection = "EPSG:3857";
        const dataProjection = "EPSG:5186";
        const geometry = new ol.geom.Point(ol.proj.fromLonLat([lon, lat]));
        if (!mapLib.geometry) mapLib.geometry = geometry;
        const feature = new ol.Feature({
            geometry, //transform()경도 위도에 포인트 설정
            projection : {
                mapProjection
            },
            properties : {
                name: "markers",
            },
            name: name
        });

        // 마커 레이어에 들어갈 소스 생성
        const markerSource = new ol.source.Vector({
            features: [feature] //feature의 집합
        });

        // 마커 스타일 설정
        const  markerStyle = new ol.style.Style({
            image: new ol.style.Icon({ //마커 이미지
                src: 'https://ifh.cc/g/bfFomb.png',
                scale: 0.07 //크기 1=100%
            })
        });

        // 마커 레이어 생성
        const markerLayer = new ol.layer.Vector({
            source: markerSource, //마커 feacture들
            style: markerStyle //마커 스타일
        });

        // 지도에 마커가 그려진 레이어 추가
        if (mapLib.map) {
            mapLib.map.addLayer(markerLayer);
        }

        mapLib.map.getView().setCenter(mapLib.geometry.getCoordinates());
        mapLib.map.getView().setZoom(11);
    }
}