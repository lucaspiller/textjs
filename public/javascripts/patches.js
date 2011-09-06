(function( $ ){
  var __ajax = $.ajax;
  $.ajax = function(options) {
    options = options || {};

    // set accesscode if defined
    if (Application != undefined) {
      options.beforeSend = function(xhr) {
        xhr.setRequestHeader('Xaccesscode', Application.AccessToken);
      };
    }

    // pagination
    if (!options.baseUrl) {
      options.baseUrl = options.url;
    }
    var success = options.success;
    options.success = function(resp, status, xhr) {
      var nextPage = xhr.getResponseHeader('X-Next-Page');
      if (nextPage) {
        if (options.lastResp) {
          options.lastResp = options.lastResp.concat(resp);
        } else {
          options.lastResp = resp;
        }
        if (resp.length == 0) {
          // end of pagination
          if (success) success(options.lastResp, status, xhr);
        } else {
          options.url = options.baseUrl + '/' + nextPage;
          __ajax(options);
        }
      } else {
        if (options.lastResp) {
          options.lastResp = options.lastResp.concat(resp);
        } else {
          options.lastResp = resp;
        }
        if (success) success(options.lastResp, status, xhr);
      }
    };

    return __ajax(options);
  };
})( jQuery );
