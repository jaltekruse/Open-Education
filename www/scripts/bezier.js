function show(id)
{
  var object = document.getElementById(id);
  object.style.display = "";
}
function hide(id)
{
  var object = document.getElementById(id);
  object.style.display = "none";
}
function interpolate(from, to, t, callback, objectid, finished)
{
  var object = document.getElementById(objectid);
  t += 0.05;
  callback(
    (-2.0 * to + 2.0 * from) * t * t * t +
    (3.0 * to - 3.0 * from) * t * t +
    from, object);
  if(t < 1)
  {
    setTimeout("interpolate(" + from + ", " + to + ", " + t + ", " + callback + ", \"" + objectid + "\", " + finished + ");", 30);
  }
  else
  {
    callback(to, object);
    if(typeof finished == 'function')
    {
      finished();
    }
  }
}

function bezier(from, to, t)
{
  return (-2.0 * to + 2.0 * from) * t * t * t + (3.0 * to - 3.0 * from) * t * t + from
}

function interpolateBlocks(wrapper, a, b) {
  show(b);
  hide(a);
  var objectA = document.getElementById(a);
  var objectB = document.getElementById(b);
  var objectW = document.getElementById(wrapper);
  var heightA = $("#"+a).height();
  var heightB = $("#"+b).height();
  var i;
  for(i = 0; i * 0.04 < 1; i++) {
    var t = i * 0.04;
    //setTimeout("document.getElementById('" + wrapper + "').style.height = bezier(" + heightA + ", " + heightB + ", " + t + ");", 16 * i);
    //setTimeout("document.getElementById('" + a + "').style.opacity = " + (1.0 - i * 0.04 + ";"));
    //setTimeout("document.getElementById('" + b + "').style.opacity = " + (i * 0.04 + ";"), 16 * i);
  }
  //setTimeout("document.getElementById('" + wrapper + "').style.height = " + heightB + ";", 16 * i);
  //setTimeout("show('" + b + "');", 16 * i);
  //setTimeout("hide('" + a + "');", 16 * i);
  //setTimeout("document.getElementById('" + a + "').style.opacity = " + 0.0 + ";", 16 * i);
  //setTimeout("document.getElementById('" + b + "').style.opacity = " + 1.0 + ";", 16 * i);
}