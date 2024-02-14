window.addEventListener("DOMContentLoaded", function(){
	
	let map = new ol.Map({
		target : 'map',
		layers : [
			new ol.layer.Tile({
				source: new ol.source.OSM({
					url:'https://maps.wikimedia.org/osm-intl/{z}/{x}/{y}.png'
				})
			})
		],
		view: new ol.View({
			center: ol.proj.fromLonLat([128.4, 35.7]),
			zoom: 7
		})
	});
	
	var wms = new ol.layer.Tile({
		source: new ol.source.TileWMS({
			url: 'http://localhost:8080/geoserver/wms',
			params: {
				'VERSION' : '1.1.0',
				'LAYERS' : 'hanriver',
				'BBOX' : [157399.453125,377790.5,271335.40625,526259.5],
				'SRS' : 'EPSG:5174',
				'FORMAT' : 'application/openlayers'
			},
			serverType: 'geoserver',
		})
	});
	
	map.addLayer(wms);
	
});