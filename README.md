Installation
============

Create and use gemset (you are using RVM right?):

    rvm gemset create smsjs
    rvm gemset use smsjs

Install the gems:

    bundle

Install CoffeeScript:

    brew install node
    npm install -g coffee-script

Developing
==========

Start Sinatra server to dynamically render pages

    rake server

Start Watchr to compile CoffeeScript and Asset bundles on code change

    rake watchr

Generate production app to build/

    rake generate

Create a release tarball to dist/

    rake tarball
