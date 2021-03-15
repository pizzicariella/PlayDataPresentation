$(window).on('load', function(){
    const title = document.getElementById("title")
    navbarSelection(title.innerText);
});

function navbarSelection(title){
    const navbarItems = document.getElementsByClassName("w3-bar-item");
    for(let i=0; i<navbarItems.length; i++){
        if(navbarItems.item(i).innerText == title){
            navbarItems.item(i).classList = "w3-bar-item w3-button w3-theme-l1";
        } else {
            navbarItems.item(i).classList = "w3-bar-item w3-button w3-hide-small w3-hover-white";
        }
    }
}
