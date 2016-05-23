.PHONY: build clean server dist

build:
	gulp

clean:
	rm -Rf build
	rm -Rf dist

server:
	bundle exec ruby tools/server.rb

proxy:
	node tools/proxy.js

dist:
	gulp dist
