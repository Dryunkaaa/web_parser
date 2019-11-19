/* When the user clicks on the button,
toggle between hiding and showing the dropdown content */
var dropButton = document.getElementById("dropButton");
dropButton.onclick = function myFunction() {
    document.getElementById("my-dropdown").classList.toggle("show");
};

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
    if (!event.target.matches('.dropButton')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}


// show successful authorization block
const block = document.querySelector('.successful-authorization-block');

function hide(){
    block.style.display = "none";
}

setTimeout(hide, 3500);

