window.addEventListener("DOMContentLoaded", function(){
    const cctv = document.querySelector("#cctv");
    if(cctv){
        const player = videojs('cctv');
        player.play();
    }
})
