

/**
* 거리 선 그리기
* @param isDraw : true - 선 그리기, false - 선 지우기
* @param map : 적용할 지도
* @param lineSource : 선
*/
function addDraw(isDraw, map, lineSource, drawType){
    let typeStr;
    let maxPnt;
    if(drawType === 'distance'){
        typeStr = 'LineString';
        maxPnt = 2;
    }else{
        typeStr = 'Polygon';
        maxPnt = undefined;
    }
    const draw = new ol.interaction.Draw({
        source: lineSource,
        type: typeStr,
        maxPoints: maxPnt,
        properties: { name: 'draw'},
        style: new ol.style.Style({
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
        }),
    });
    if(isDraw){

        map.addInteraction(draw);
        let measureTooltip = manageMeasureTooltip(map, true);
        let measureTooltipEl = document.querySelector(".ol-tooltip-measure");

        draw.addEventListener("drawstart", drawStart);

        draw.addEventListener("drawend", function(){
            draw.finishDrawing();
            map.removeInteraction(draw);
        });
        function drawStart(e){

                   const sketch = e.feature;

                   listener = sketch.getGeometry().addEventListener("change", function(e){
                        const geom = e.target;
                        const output = (drawType === 'distance') ? formatLength(geom) : formatArea(geom);
                        const tooltipCoord = geom.getLastCoordinate();

                        if(measureTooltipEl) measureTooltipEl.innerHTML = output;
                        measureTooltip.setPosition(tooltipCoord);
                   });
        }
    }else{
        map.removeInteraction(draw);
        manageMeasureTooltip(map,false);
    }

    return draw;
}

/*
* 툴팁 관리
* @param map : 툴팁 적용될 지도
* @param make : true - 만들기, false - 지우기
*/
function manageMeasureTooltip(map, make) {
    let measureTooltipEl = document.querySelector(".ol-tooltip-measure");

    if (measureTooltipEl) {
        measureTooltipEl.parentNode.removeChild(measureTooltipEl);
    }
    if(make){
        measureTooltipEl = document.createElement('div');
        measureTooltipEl.className = 'ol-tooltip-measure';
        measureTooltip = new ol.Overlay({
            element: measureTooltipEl,
            offset: [0, -15],
            positioning: 'bottom-center',
        });
        map.addOverlay(measureTooltip);

        return measureTooltip;
    }
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

/* 실제 면적 계산 */
function formatArea(polygon){
    const area = ol.sphere.getArea(polygon);
    let output;
    if(area > 10000){
        output = Math.round((area / 1000000) * 100) / 100 + ' ' + 'km<sup>2</sup>';
    }else{
        output = Math.round(area * 100) / 100 + ' ' + 'm<sup>2</sup>';
    }

    return output;
}