.PHONY: build clean server

build:
	gulp

clean:
	rm -Rf build
	rm -Rf dist

server:
	bundle exec ruby tools/server.rb

proxy:
	node tools/proxy.js
