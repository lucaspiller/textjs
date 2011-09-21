require 'rack/reverse_proxy'
require "middleman-jammit"

activate :jammit
# CodeRay syntax highlighting in Haml
# activate :code_ray

# Automatic sitemaps (gem install middleman-slickmap)
# require "middleman-slickmap"
# activate :slickmap

# Automatic image dimension calculations
# activate :automatic_image_sizes

# Per-page layout changes
# With no layout
# page "/path/to/file.html", :layout => false
# With alternative layout
# page "/path/to/file.html", :layout => :otherlayout

use Rack::ReverseProxy do
  reverse_proxy '/threads', 'http://127.0.0.1:5982'
  reverse_proxy '/contacts', 'http://127.0.0.1:5982'
  reverse_proxy '/messages', 'http://127.0.0.1:5982'
end

# Helpers
helpers do
  #def some_helper(*args)
  #"Helping"
  #end
end

get "/assets/*" do |splat|
  File.read("build/assets/#{splat}")
end

# Change the CSS directory
# set :css_dir, "alternative_css_directory"

# Change the JS directory
# set :js_dir, "alternative_js_directory"

# Change the images directory
# set :images_dir, "alternative_image_directory"

# Build-specific configuration
configure :build do
  # For example, change the Compass output style for deployment
  #activate :minify_css

  # Minify Javascript on build
  #activate :minify_javascript

  # Enable cache buster
  # activate :cache_buster

  # Use relative URLs
  # activate :relative_assets

  # Compress PNGs after build (gem install middleman-smusher)
  # require "middleman-smusher"
  # activate :smusher

  # Generate ugly/obfuscated HTML from Haml
  #activate :ugly_haml

  # Or use a different image path
  # set :http_path, "/Content/images/"
end
