function navbarSelection(){
    console.log("clicked");
    const navbarGrey = document.getElementsByClassName("w3-theme-l1");
    console.log(navbarGrey)
    const navbarBlack = document.getElementsByClassName("w3-bar-item w3-button w3-hide-small w3-hover-white");
    for(let i; i<navbarGrey.length; i++){
        console.log("infor")
        console.log(navbarGrey.item(i))
        navbarGrey.item(i).class = "w3-bar-item w3-button w3-hide-small w3-hover-white";
    }
    for(let i; i<navbarBlack.length; i++){
        if(navbarBlack.item(i).innerText == "Corpus"){
            navbarBlack.item(i).class = "w3-bar-item w3-button w3-theme-l1"
        }
    }
}
//function muss später aufgerufen werden, da liste hier noch nicht existiert
navbarSelection()