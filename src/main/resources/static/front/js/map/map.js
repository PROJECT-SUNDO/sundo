window.addEventListener("DOMContentLoaded", function(){
	var mapProjection = "EPSG:3857";
	var dataProjection = "EPSG:4326";

	var center = new ol.geom.Point([127.01499999999999, 37.53 ]).transform(dataProjection, mapProjection);
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
			zoom: 9
		})
	});

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




	var wms = new ol.layer.Tile({
		source: new ol.source.TileWMS({
			url: 'http://localhost:8080/geoserver/wms',
			params: {
				'VERSION' : '1.1.0',
				'LAYERS' : 'hanriver',
				'BBOX' : [157399.453125,377790.5,271335.40625,526259.5],
				'SRS' : 'EPSG:5174',
				'FORMAT' : 'image/png'
			},
			serverType: 'geoserver',
		}),
	});

    wms.setOpacity(0.3);

	map.addLayer(wms);

});