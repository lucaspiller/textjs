backend default {
  .host = "127.0.0.1";
  .port = "8082";
}

sub vcl_recv {
  unset req.http.cookie;
}

sub vcl_fetch {
  unset beresp.http.set-cookie;

  /* Remove Expires from backend, it's not long enough */
  unset beresp.http.expires;

  /* Set how long Varnish will keep it */
  set beresp.ttl = 1w;

  /* marker for vcl_deliver to reset Age: */
  set beresp.http.magicmarker = "1";
}

sub vcl_deliver {
  if (resp.http.magicmarker) {
    /* Remove the magic marker */
    unset resp.http.magicmarker;

    /* By definition we have a fresh object */
    set resp.http.age = "0";
  }
}
