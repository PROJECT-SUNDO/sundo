function addMarker(items){
    // 마커 레이어 비우기
    if (mapLib.map) {
        mapLib.map.getLayers().forEach(layer => layer.getSource().refresh());
        mapLib.geometry = null;
    }

    // 마커 값 설정
    for(const item of items) {

        let { lon, lat } = item;

        // 텍스트 형태 변환
        if (!lon || !lat) continue;

        const [lon1, lon2, lon3] = lon.split("-");
        lon = Number(lon1) + Number(lon2)/60 + Number(lon3)/3600;

        const [lat1, lat2, lat3] = lat.split("-");
        lat = Number(lat1) + Number(lat2)/60 + Number(lat3)/3600;
        const name = item.obsnm;  // 관측소 명

        const mapProjection = "EPSG:3857";
    	const dataProjection = "EPSG:4326";
        // 마커 feature 설정

        const geometry = new ol.geom.Point([lon, lat]).transform(dataProjection, mapProjection);
        mapLib.geometry = geometry;

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