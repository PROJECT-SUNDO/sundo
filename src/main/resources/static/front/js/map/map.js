window.addEventListener("DOMContentLoaded", function(){

    /*  지도 표시 S */
	const mapProjection = "EPSG:3857";
	const dataProjection = "EPSG:4326";
    const center = new ol.geom.Point([126.976882 , 37.574187 ]).transform(dataProjection, mapProjection);



	let map = new ol.Map({
		target : 'map',
		layers : [
            new ol.layer.Tile({
                source: new ol.source.XYZ({
                    name: "baseLayer",
                    url: 'http://api.vworld.kr/req/wmts/1.0.0/241FCB6A-1BD1-38F9-AF43-74053FB44469/Base/{z}/{y}/{x}.png'
                })
            }),
            new ol.layer.Tile({
               source: new ol.source.XYZ({
                   name: "satelliteLayer",
                   url: 'http://api.vworld.kr/req/wmts/1.0.0/241FCB6A-1BD1-38F9-AF43-74053FB44469/Satellite/{z}/{y}/{x}.jpeg'
               })
            }),
		],
		view: new ol.View({
		    projection: mapProjection,
			center: center.getCoordinates(),
			zoom: 8.5
		})
	});

	/* 거리 측정 S */
    /* 지도에 선 긋기 */
    lineSource = new ol.source.Vector();
    const lineVector = new ol.layer.Vector({
        source: lineSource,
        style: function(feature){
            var style = new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: '#ffcc33',
                    width: 2,
                })
            })
        }
    });

    var sketch; //라인스트링 이벤트 시 geometry객체를 담을 변수
    var measureTooltipElement;//draw 이벤트가 진행 중일 때 담을 거리 값 element
    var measureTooltip; //툴팁 위치

    const distanceBtn = document.querySelector("#distance");
    distanceBtn.addEventListener("click", function(){
        if(distanceBtn.classList.contains("on")){
            distanceBtn.classList.remove("on");
            map.removeLayer(lineVector);
        }else{
            distanceBtn.classList.add("on");
            map.addLayer(lineVector);
        }

        let draw = distanceBtn.classList.contains("on");

        addDraw(draw, map, lineSource, lineVector);
    });

    /* 거리 측정 E */

    /* 지도 전환 버튼 S */
    const mapArray = map.getLayers().getArray();
    const baseLayer = mapArray[0];
    const satelliteLayer = mapArray[1];

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
            }else if(version == 'Satellite'){
                satelliteLayer.setVisible(true);
                baseLayer.setVisible(false);
            }
   	    })
   	}
    /* 지도 전환 버튼 E */

    /* geoserver - 한강하천, 한강수계 S */
	var river = new ol.layer.Tile({
		source: new ol.source.TileWMS({
			url: 'http://3.39.34.98:8080/geoserver/wms',
			params: {
				'VERSION' : '1.1.0',
				'LAYERS' : 'HangangRiver',
				'BBOX' : [156556.1947859727,277468.6485166226,272050.4658484871,426497.85228758736],
				'SRS' : 'EPSG:5174',
				'FORMAT' : 'image/png'
			},


			serverType: 'geoserver',

		}),
	});

    river.setOpacity(0.5);
	map.addLayer(river);
	/* geoserver - 한강하천, 한강수계 E */

	/* 전체 버튼 - 경기도까지 보이게 줌 설정 S */
	const allBtn = document.querySelector("#all");
	allBtn.addEventListener("click", function(){
	    const view = map.getView();

	    view.setCenter(center.getCoordinates());
	    view.setZoom(8.5);
	});

	/* 전체 버튼 - 경기도까지 보이게 줌 설정 E */
});


function addDraw(isDraw, map, lineSource, lineVector){
    const draw = new ol.interaction.Draw({
        source: lineSource,
        type: 'LineString',
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: 'rgba(255, 255, 255, 0.2)',
            }),
            stroke: new ol.style.Stroke({
                color: 'rgba(0, 0, 0, 0.5)',
                lineDash: [10, 10],
                width: 2,
            }),
            image: new ol.style.Circle({
                radius: 5
            }),
        })
    });

    map.addInteraction(draw);
    let measureTooltip = createMeasureTooltip(map);
    let measureTooltipEl = document.querySelector("#ol-tooltip-measure");

    if(isDraw){

        draw.addEventListener("drawstart", function(e){
           const sketch = e.feature;

           listener = sketch.getGeometry().addEventListener("change", function(e){
                const geom = e.target;
                const output = formatLength(geom);
                const tooltipCoord = geom.getLastCoordinate();

                if(measureTooltipEl) measureTooltipEl.innerHTML = output;
                measureTooltip.setPosition(tooltipCoord);

           });
        });

        draw.addEventListener("drawend", function(){
            console.log("그리기끝");
            measureTooltip.setOffset([0, -7]);

            sketch = null;
            measureTooltipEl = null;
            createMeasureTooltip(map);
            map.removeInteraction(draw);
            lineVector.getSource().clear(true);
            map.removeLayer(lineVector);
            new ol.Observable(listener);
        });
    }
}


function createMeasureTooltip(map) {
    let measureTooltipEl = document.querySelector("#ol-tooltip-measure");

    if (measureTooltipEl) {
        measureTooltipEl.parentNode.removeChild(measureTooltipEl);
    }

    measureTooltipEl = document.createElement('div');
    measureTooltipEl.id = 'ol-tooltip-measure';
    measureTooltip = new ol.Overlay({
        element: measureTooltipEl,
        offset: [0, -15],
        positioning: 'bottom-center',
    });
    map.addOverlay(measureTooltip);

    return measureTooltip;
}


/* 실제 거리 계산 */
function formatLength(geom){
    const length = ol.sphere.getLength(geom);

    if (length > 100) {
       return Math.round((length / 1000) * 100) / 100 + ' ' + 'km';
    } else {
       return Math.round(length * 100) / 100 + ' ' + 'm';
    }
}
