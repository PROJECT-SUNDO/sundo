function addMarker(items){
    // 마커 레이어 비우기
    if (mapLib.map) {
        mapLib.map.getLayers().forEach(layer => layer.getSource().refresh());
        mapLib.geometry = null;
    }

    const markerImg = {
        'rf': 'https://ifh.cc/g/3O0MmJ.png',  // 강수량관측소
        'wl': 'https://ifh.cc/g/onQwV8.png',   // 수위관측소
        'flw': 'https://ifh.cc/g/onQwV8.png'   // 유량 관측소
    }

    // 마커 값 설정
    for(const item of items) {

        let { lon, lat } = item;

        // 텍스트 형태 변환
        if (!lon || !lat) continue;
        lon = parseFloat(lon.replace("-", ".").replace(/-/g, ""));  // 경도
        lat = parseFloat(lat.replace("-", ".").replace(/-/g, ""));  // 위도
        const name = item.obscd;  // 관측소 코드


        // 마커 feature 설정
        const mapProjection = "EPSG:3857";  // 지도 좌표계
        const markerDataProjection = "EPSG:5186";  // 데이터 좌표계
        // let lonLat = ol.proj.fromLonLat([lon, lat]);
        // let transformedLonLat = ol.proj.transform(lonLat, markerDataProjection, mapProjection);
        // const geometry = new ol.geom.Point(transformedLonLat);
        const geometry = new ol.geom.Point(ol.proj.fromLonLat([lon, lat]));
        // const geometry = new ol.geom.Point(ol.proj.fromLonLat([lon, lat])).transform(dataProjection, mapProjection);
        if (!mapLib.geometry) mapLib.geometry = geometry;
        const feature = new ol.Feature({
            geometry, //transform()경도 위도에 포인트 설정

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
                src: 'https://ifh.cc/g/3O0MmJ.png',
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
}