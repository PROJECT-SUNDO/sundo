window.addEventListener("DOMContentLoaded", function(){
    const cctv = document.querySelector("#cctv");
    if(cctv){
        const player = videojs('cctv');
        player.play();
    }

    console.log(window);
    const width = window.innerWidth;
    const height = window.innerHeight;

    const el = parent.document.querySelector("#layer_popup_map iframe");
    el.width = width;
    el.height = height;

});
