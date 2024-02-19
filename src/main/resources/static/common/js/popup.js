window.addEventListener("DOMContentLoaded", function(){
    const popupClsBtn = document.querySelector(".popup_close");

    popupClsBtn.addEventListener("click", function(){
        const { popup } = parent.commonLib;
        popup.close();

    });


});