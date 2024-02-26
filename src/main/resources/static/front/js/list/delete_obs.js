window.addEventListener("DOMContentLoaded", function(){

    const cancleBtn = document.querySelector("#cancleBtn");
    cancleBtn.addEventListener("click", function(){
         const { popup } = parent.commonLib;
         popup.close();

     });
})