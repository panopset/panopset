function initGame() {
 const s = Snap(1000, 600);
 $.ajax({
  type: "GET",
  contentType : 'application/json; charset=utf-8',
  url: "/denebola/ajaxInit",
   success: function(msg) {
        const toard = JSON.parse(msg)
        if (toard.tronks.length > 0) {
         const tronk = toard.tronks[0]
         c0 = createOrb(s, tronk)
         c0.circle.attr({
                       fill: tronk.fill,
                       stroke: tronk.stroke,
                       strokeWidth: 1
                     });
        }


    c0.circle.mouseover(function () {
        reportMouseOver(c0)
    });

    },
    error: function(xhr) {
     reportError(xhr)
    }
 });
}

function createOrb(s, tronk) {
 return {
  tronk: tronk,
  circle: s.circle(tronk.x,tronk.y,tronk.r)
 }
}

function reportMouseOver(z0) {
 const tronkJson = JSON.stringify(z0.tronk)
 $.ajax({
  type: "POST",
  contentType : 'application/json; charset=utf-8',
  url: "/denebola/ajaxGetTarget",
  data: tronkJson,
   success: function(msg) {
            const tronk = JSON.parse(msg)
            z0.tronk = tronk
            z0.circle.attr({
              x: tronk.x,
              y: tronk.y,
              r: tronk.r,
              fill: tronk.fill,
              stroke: "#fff",
              strokeWidth: 1
            });
    },
    error: function(xhr) {
     reportError(xhr)
    }
 });
}

function loopOut(z0) {
 z0.circle.animate({
  r: 20,
  cx: 400
 }, 2000, mina.easein, function() {
  loopIn(z0);
 });
}

function loopIn(z0) {
 if (!z0.active) {
  return
 }
 z0.circle.animate({
  r: 0,
  cx: 600
 }, 1000, mina.easein, function() {
  loopOut(z0);
 });
}

function reportError(xhr) {
     const errorMessage = JSON.parse(xhr)
     console.log(errorMessage)
}
