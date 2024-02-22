window.addEventListener("DOMContentLoaded", function(){
    const parentWd = this.parent;
    const aside = parentWd.document.querySelector(".aside");

    const popupClose = document.querySelector(".popup_close");
    popupClose.addEventListener("click", function(){
        aside.classList.add("dn");
    })

});