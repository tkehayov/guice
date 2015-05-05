$(document).ready(function () {
  var pathname = location.pathname;
  pathname = pathname.split("/");

  var active = pathname[2];

  active = active == undefined ? 'profile' : pathname[2];

  $(".nav a:contains(" + active + ")").parent().addClass('active');
});
