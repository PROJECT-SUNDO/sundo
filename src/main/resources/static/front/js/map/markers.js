// function addMarker(lon, lat, name){ //경도 위도 이름값(마커들을 구분하기위해)
//     // 마커 feature 설정
//     var feature = new ol.Feature({
//         geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat])), //경도 위도에 포인트 설정
//         name: name
//     });
//
//     // 마커 스타일 설정
//     var markerStyle = new ol.style.Style({
//         image: new ol.style.Icon({ //마커 이미지
//             opacity: 1, //투명도 1=100%
//             scale: 1.2, //크기 1=100%
//             //src: 'http://map.vworld.kr/images/ol3/marker_blue.png'
//             src: 'https://icons8.kr/icon/cIUKs5wRJGDi/%EC%9C%84%EC%B9%98'
//         }),
//         zindex: 10
//     });
//
//     // 마커 레이어에 들어갈 소스 생성
//     var markerSource = new ol.source.Vector({
//         features: [feature] //feature의 집합
//     });
//
//     // 마커 레이어 생성
//     var markerLayer = new ol.layer.Vector({
//         source: markerSource, //마커 feacture들
//         style: markerStyle //마커 스타일
//     });
//
//     // 지도에 마커가 그려진 레이어 추가
//     map.addLayer(markerLayer);
//
// }
