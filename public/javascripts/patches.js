(function( $ ){
  var __ajax = $.ajax;
  $.ajax = function(options) {
    options = options || {};
    if (Application != undefined) {
      options.beforeSend = function(xhr) {
        xhr.setRequestHeader('Xaccesscode', Application.AccessToken);
      };
    }
    return __ajax(options);
  };
})( jQuery );
