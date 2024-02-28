const mapLib = {
    draw: null,
    map: null,
};

window.addEventListener("DOMContentLoaded", function(){

    /*  지도 표시 S */
    const mapProjection = "EPSG:3857";
    const dataProjection = "EPSG:4326";
    const center = new ol.geom.Point([126.976882 , 37.574187 ]).transform(dataProjection, mapProjection);

    const mapBaseSource = new ol.source.XYZ({
        name: "baseLayer",
        url: 'http://api.vworld.kr/req/wmts/1.0.0/241FCB6A-1BD1-38F9-AF43-74053FB44469/Base/{z}/{y}/{x}.png'
    });

    const mapSatelliteSource = new ol.source.XYZ({
        name: "satelliteLayer",
        url: 'http://api.vworld.kr/req/wmts/1.0.0/241FCB6A-1BD1-38F9-AF43-74053FB44469/Satellite/{z}/{y}/{x}.jpeg'
    });
    const geoSource = new ol.source.TileWMS({

        url: 'http://3.39.34.98:8080/geoserver/wms',
        params: {
            'VERSION' : '1.1.0',
            'LAYERS' : 'hangangRiver',
            'BBOX' : [157399.453125,377790.5,271335.40625 ,526259.5],
            'SRS' : 'EPSG:5174',
            'FORMAT' : 'image/png'
        },
        serverType: 'geoserver',

    });

    /* 오버뷰 맵 S */
    const overViewMap = new ol.control.OverviewMap({
        layers : [
            new ol.layer.Tile({
                source: mapBaseSource,
                visible: true,
            }),
            new ol.layer.Tile({
                source: mapSatelliteSource,
                visible: false,
            }),
            new ol.layer.Tile({
                source: geoSource,
                visible: true,
                opacity: 0.5,
            }),
        ],
        view: new ol.View({
            projection: mapProjection,
            center: center.getCoordinates(),
            zoom: 8.5,
        }),
        collapsed: false,
    });
    /* 오버뷰 맵 E */

    const map = new ol.Map({
        target : 'map',
        layers : [
            new ol.layer.Tile({
                source: mapBaseSource,
            }),
            new ol.layer.Tile({
                source:mapSatelliteSource,
            }),
        ],
        view: new ol.View({
            projection: mapProjection,
            center: center.getCoordinates(),
            zoom: 8.5
        }),
        controls: ol.control.defaults().extend([overViewMap]),
    });
    mapLib.map = map;
    map.addControl(new ol.control.ScaleLine());

    /* 지도 표시 E */

    /* geoserver - 한강하천, 한강수계 S */

    const river = new ol.layer.Tile({
        source: geoSource,
        opacity: 0.5,
    });

    map.addLayer(river);
    /* geoserver - 한강하천, 한강수계 E */

    /* 거리 측정 S */
    /* 지도에 선 긋기 */
    const vectorStyle = new ol.style.Style({
        fill: new ol.style.Fill({
            color: 'rgba(255, 255, 255, 0.2)',
        }),
        stroke: new ol.style.Stroke({
            color: 'rgba(2, 2, 2, 1)',
            lineDash: [10, 10],
            width: 2,
        }),
        image: new ol.style.Circle({
            radius: 5
        }),
    });

    const distanceBtn = document.querySelector("#distance");
    const areaBtn = document.querySelector("#area");
    const distanceEl = distanceBtn.querySelector("span");
    distanceBtn.addEventListener("click", function(){

        if(distanceBtn.classList.contains("on")){
            distanceBtn.classList.remove("on");
            areaBtn.disabled=false;
        }else{
            distanceBtn.classList.add("on");
            areaBtn.disabled=true;
        }

        let isDraw = distanceBtn.classList.contains("on");

        if(!isDraw){
            const lineVector = map.getAllLayers().filter(s => s.get('name') ==='distance')[0];
            map.removeLayer(lineVector);
            manageMeasureTooltip(map, false);
        }else{
            const lineSource = new ol.source.Vector();

            const lineVector = new ol.layer.Vector({
                properties: {name: 'distance'},
                source: lineSource,
                style: vectorStyle,
            });
            map.addLayer(lineVector);
            addDraw(isDraw, map, lineSource, 'distance');
        }
    });

    /* 거리 측정 E */
    /* 면적 측정 S */

    const areaEl = areaBtn.querySelector("span");

    areaBtn.addEventListener("click", function(){
        if(areaBtn.classList.contains("on")){
            areaBtn.classList.remove("on");
            distanceBtn.disabled=false;
        }else{
            areaBtn.classList.add("on");
            distanceBtn.disabled=true;
        }

        const isDraw = areaBtn.classList.contains("on");
        if(!isDraw){
            const lineVector = map.getAllLayers().filter(s => s.get('name') ==='area')[0];
            map.removeLayer(lineVector);
            manageMeasureTooltip(map, false);
            mapLib.draw.finishDrawing();

        }else{
            const lineSource = new ol.source.Vector();

            const lineVector = new ol.layer.Vector({
                properties: { name: 'area'},
                source: lineSource,
                style: vectorStyle,
            });
            map.addLayer(lineVector);
            mapLib.draw = addDraw(isDraw, map, lineSource, 'area');
        }

    })

    /* 면적 측정 E */

    /* 지도 전환 버튼 S */
    const mapArray = map.getLayers().getArray();
    const baseLayer = mapArray[0];
    const satelliteLayer = mapArray[1];
    const overViewArray = overViewMap.getOverviewMap().getLayers().getArray();
    const baseOverview = overViewArray[0];
    const satelliteOverview = overViewArray[1];

    satelliteLayer.setVisible(false);
    satelliteOverview.setVisible(false);

    const mapBtns = document.querySelectorAll(".map_btns .map_btn");

    for(const mapBtn of mapBtns){
        mapBtn.addEventListener("click", function(){
            const version = this.dataset.map;
            for(const el of mapBtns){
                if(el.classList.contains("on")){
                    el.classList.remove("on");
                }
            }
            mapBtn.classList.add("on");

            if(version == 'Base'){
                satelliteLayer.setVisible(false);
                baseLayer.setVisible(true);
                satelliteOverview.setVisible(false);
                baseOverview.setVisible(true);

            }else if(version == 'Satellite'){
                satelliteLayer.setVisible(true);
                baseLayer.setVisible(false);
                satelliteOverview.setVisible(true);
                baseOverview.setVisible(false);
            }
        })
    }
    /* 지도 전환 버튼 E */

    /* 전체 버튼 - 경기도까지 보이게 줌 설정 S */
    const allBtn = document.querySelector("#all");
    allBtn.addEventListener("click", function(){
        const view = map.getView();

        view.setCenter(center.getCoordinates());
        view.setZoom(8.5);
    });

    /* 전체 버튼 - 경기도까지 보이게 줌 설정 E */


    /* 저장하기 버튼 S */

    // 버튼 DOM 객체를 변수에 저장
    const saveBtn = document.getElementById("save_map");
    const savePngBtn = document.getElementById("save_map_png");
    const savePdfBtn = document.getElementById("save_map_pdf");
    const btnBox = document.getElementById("save_map_box");

    // 저장 버튼을 눌렀을 때의 동작
    saveBtn.addEventListener("click", function(){
        if(saveBtn.classList.contains("on")){
            saveBtn.classList.remove("on");
            btnBox.classList.add("dn");
        }else{
            saveBtn.classList.add("on");
            btnBox.classList.remove("dn");
        }
    });

    // 이미지 저장 버튼을 눌렀을 때의 동작
    savePngBtn.addEventListener("click", function() {
        btnBox.style.display = "none"; // 이미지 저장, PDF 저장 버튼을 숨김
        // 'on' 클래스 추가
        this.classList.add("on");
        // saveBtn.disabled = false; // 저장 버튼을 활성화
        // 이미지 저장 관련 코드를 작성

        html2canvas(document.querySelector("#map"), {
            useCORS: true,
            allowTaint: true,
        }).then(canvas => {
            document.body.appendChild(canvas);
            let dataURL = canvas.toDataURL("image/png");
            dataURL = dataURL.replace(/^data:image\/[^;]*/, 'data:application/octet-stream');
            dataURL = dataURL.replace(/^data:application\/octet-stream/, 'data:application/octet-stream;headers=Content-Disposition%3A%20attachment%3B%20filename=map.png');

            const aLink = document.createElement("a");
            aLink.download="map.png";
            aLink.href = dataURL;
            aLink.click();

            // 이미지 저장 작업이 끝난 후 'on' 클래스 제거
            saveBtn.classList.remove("on");
            btnBox.style.display = ""; // 빈 문자열을 설정하면 원래의 값으로 변경 btnBox 스타일 초기화
            btnBox.classList.add("dn");
            this.classList.remove("on");
        });
    });

    // PDF 저장 버튼을 눌렀을 때의 동작
    savePdfBtn.addEventListener("click", function() {
        btnBox.style.display = "none"; // 이미지 저장, PDF 저장 버튼을 숨김
        // 'on' 클래스 추가
        this.classList.add("on");
        // saveBtn.disabled = false; // 저장 버튼을 활성화

        // PDF 저장 관련 코드를 작성
        print();

        // PDF 저장 작업이 끝난 후 'on' 클래스 제거
        saveBtn.classList.remove("on");
        btnBox.style.display = ""; // 빈 문자열을 설정하면 원래의 값으로 변경 btnBox 스타일 초기화
        btnBox.classList.add("dn");
        this.classList.remove("on");
    });
    /* 저장하기 버튼 E */

    /* 마커 추가 s */
    window.addEventListener("message", function(e) {
        if (!e.data.map) {
            return;
        }
        const { popup } = commonLib;
        popup.close();

        const items = e.data.map;
        addMarker(items);
    });
    /* 마커 추가 e */
});


function mapToMove(lonDecimal, latDecimal) {
    if (mapLib.map) {
        mapLib.map.getView().animate({center: ol.proj.fromLonLat([lonDecimal, latDecimal])});
        mapLib.map.getView().setZoom(11);
    }
}