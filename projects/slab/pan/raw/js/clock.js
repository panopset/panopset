
/* Inspired by https://www.w3schools.com/js/tryit.asp?filename=tryjs_timing_clock */
function startTime() {
    const today = new Date();
    var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    let h = today.getHours();
    ampm = 'AM';
    if (h > 12) {
     h = h - 12;
     ampm = 'PM';
    }
    let m = today.getMinutes();
    let s = today.getSeconds();
    m = checkTime(m);
    s = checkTime(s);
//  document.getElementById('clock').innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;" + h + ":" + m + ":" + s + "  " + ampm +  "  " + today.toLocaleDateString("en-US", options);
    document.getElementById('clock').innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;" + h + ":" + m + ":" + s + "  " + ampm ;
    setTimeout(startTime, 1000);
}

function checkTime(i) {
    if (i < 10) {i = "0" + i};
    return i;
}
