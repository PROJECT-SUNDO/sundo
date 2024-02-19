const mapLib = {
    draw: null,
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
    const distanceEl = distanceBtn.querySelector("span");
    distanceBtn.addEventListener("click", function(){

        if(distanceBtn.classList.contains("on")){
            distanceBtn.classList.remove("on");
            distanceEl.innerText = '거리';
        }else{
            distanceBtn.classList.add("on");
            distanceEl.innerText = '거리 초기화';
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
    const areaBtn = document.querySelector("#area");
    const areaEl = areaBtn.querySelector("span");

    areaBtn.addEventListener("click", function(){
        if(areaBtn.classList.contains("on")){
            areaBtn.classList.remove("on");
            areaEl.innerText = '면적';
        }else{
            areaBtn.classList.add("on");
            areaEl.innerText = '면적 초기화';
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
});









