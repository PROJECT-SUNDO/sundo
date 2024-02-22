function addMarker(items){ //경도 위도 이름값(마커들을 구분하기위해)
    if (mapLib.map) {
        mapLib.map.getLayers().forEach(layer => layer.getSource().refresh());
        mapLib.geometry = null;
    }
    for(const item of items) {
        let { lon, lat } = item;
        if (!lon || !lat) continue;
        lon = parseFloat(lon.replace("-", ".").replace(/-/g, ""));  // 경도
        lat = parseFloat(lat.replace("-", ".").replace(/-/g, ""));  // 위도
        const name = item.obsnm;  // 관측소 명
        // 마커 feature 설정
        const geometry = new ol.geom.Point(ol.proj.fromLonLat([lon, lat]));
        if (!mapLib.geometry) mapLib.geometry = geometry;
        const feature = new ol.Feature({
            geometry, //transform()경도 위도에 포인트 설정
            properties : {
                name: "markers",
            },
            name: name
        });

        // 마커 스타일 설정
        const  markerStyle = new ol.style.Style({
            image: new ol.style.Icon({ //마커 이미지
                src: 'https://ifh.cc/g/bfFomb.png',
                scale: 0.1 //크기 1=100%
            })
        });

        // 마커 레이어에 들어갈 소스 생성
        const markerSource = new ol.source.Vector({
            features: [feature] //feature의 집합
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



// function addMarker(items){ //경도 위도 이름값(마커들을 구분하기위해)
//
//     for(const item of items) {
//         const lon = item.lon;  // 경도
//         const lat = item.lat;  // 위도
//         const name = item.obsnm;  // 관측소 명
//
//         // 마커 feature 설정
//         const feature = new ol.Feature({
//             geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat])), //transform()경도 위도에 포인트 설정
//             name: name
//         });
//
//         // 마커 스타일 설정
//         const  markerStyle = new ol.style.Style({
//             image: new ol.style.Icon({ //마커 이미지
//                 src: 'https://ifh.cc/g/bfFomb.png',
//                 scale: 0.1 //크기 1=100%
//             })
//         });
//
//         // 마커 레이어에 들어갈 소스 생성
//         const markerSource = new ol.source.Vector({
//             features: [feature] //feature의 집합
//         });
//
//         // 마커 레이어 생성
//         const markerLayer = new ol.layer.Vector({
//             source: markerSource, //마커 feacture들
//             style: markerStyle //마커 스타일
//         });
//
//         // 지도에 마커가 그려진 레이어 추가
//         map.addLayer(markerLayer);
//     }
// }
//
// // const Airports = new ol.layer.Vector({
// //     source: new ol.source.Vector({
// //         url: `https://api.maptiler.com/data/b2ed9244-387b-4e6b-9de9-737c65fc343b/features.json?key=IZ6PPsUFPNTSxjd3GcHv`,
// //         format: new ol.format.GeoJSON(),
// //     }),
// //     style:  new ol.style.Style({
// //         image: new ol.style.Icon({
// //             src: 'https://ifh.cc/g/bfFomb.png',
// //             size: [0.5, 1],
// //             scale: 0.1
// //         })
// //     })
// // })
// //
// // map.addLayer(Airports)
//
//
// // const marker = new ol.layer.Vector({
// //     source: new ol.source.Vector({
// //         features: [
// //             new ol.Feature({
// //                 geometry: new ol.geom.Point(
// //                     ol.proj.fromLonLat([37.5564844, 126.9451737])
// //                 )
// //             })
// //         ],
// //     }),
// //     style: new ol.style.Style({
// //         image: new ol.style.Icon({
// //             src: 'https://ifh.cc/g/bfFomb.png',
// //             //src: 'https://docs.maptiler.com/opnelayers/default-marker/marker-icon.png',
// //             //src: 'https://icons8.kr/icon/cIUKs5wRJGDi/%EC%9C%84%EC%B9%98',
// //             anchor: [0.5, 1]
// //         })
// //     })
// // })
//
//
//
// //
// // fetch('/api/data')
// //     .then(response => response.json())
// //     .then(data => {
// //         // 데이터를 사용하여 원하는 작업 수행
// //         console.log(data);
// //     })
// //     .catch(error => console.error('Error:', error));
// //
// // addMarker(lon, lat, obsnm);