window.addEventListener("DOMContentLoaded", function(){
    const listBtn = document.querySelector("#listBtn");
    listBtn.addEventListener("click", function(){
        history.back();
    });
})