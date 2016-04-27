 var xl = document.getElementsByName("x-coordinate")[0];
 var xi = document.getElementsByName("x-coordinate")[1];
 var name_label = document.getElementsByName("name")[0];
 var name_input = document.getElementsByName("name")[1];
 var yl = document.getElementsByName("y-coordinate")[0];
 var yi = document.getElementsByName("y-coordinate")[1];
 var zl = document.getElementsByName("z-coordinate")[0];
 var zi = document.getElementsByName("z-coordinate")[1];


 var rxl = document.getElementsByName("x-coordinate")[2];
 var rxi = document.getElementsByName("x-coordinate")[3];
 var rname_label = document.getElementsByName("name")[2];
 var rname_input = document.getElementsByName("name")[3];
 var ryl = document.getElementsByName("y-coordinate")[2];
 var ryi = document.getElementsByName("y-coordinate")[3];
 var rzl = document.getElementsByName("z-coordinate")[2];
 var rzi = document.getElementsByName("z-coordinate")[3];

function check_coord() {
    name_label.style.display = "none";
    name_input.style.display ="none";
    xl.style.display = "block";
    xi.style.display ="block";
    yl.style.display = "block";
    yi.style.display ="block";
    zl.style.display = "block";
    zi.style.display ="block";
}

function check_name() {

    name_label.style.display = "block";
    name_input.style.display ="block";
   
    xl.style.display = "none";
    xi.style.display ="none";
    yl.style.display = "none";
    yi.style.display ="none";
    zl.style.display = "none";
    zi.style.display ="none";
   
}

function rcheck_name() {

    rname_label.style.display = "block";
    rname_input.style.display ="block";
   
    rxl.style.display = "none";
    rxi.style.display ="none";
    ryl.style.display = "none";
    ryi.style.display ="none";
    rzl.style.display = "none";
    rzi.style.display ="none";
   
}


function rcheck_coord() {
    rname_label.style.display = "none";
    rname_input.style.display ="none";
    rxl.style.display = "block";
    rxi.style.display ="block";
    ryl.style.display = "block";
    ryi.style.display ="block";
    rzl.style.display = "block";
    rzi.style.display ="block";
}

