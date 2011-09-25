require 'rake/clean'

COFFEE_SRC = FileList["**/*.coffee"]
COFFEE_OBJ = COFFEE_SRC.pathmap("%{views,build}X")

SASS_SRC = FileList["**/*.scss"]
SASS_OBJ = SASS_SRC.pathmap("%{views,build}X")

COFFEE_TARGET_DIRS = COFFEE_SRC.map { |f| File.dirname(f) }.uniq.pathmap("%{views,build}X")
SASS_TARGET_DIRS = SASS_SRC.map { |f| File.dirname(f) }.uniq.pathmap("%{views,build}X")

CLEAN.include COFFEE_OBJ
CLEAN.include SASS_OBJ
CLEAN.include FileList["build/**/*.jst"]

(COFFEE_TARGET_DIRS + SASS_TARGET_DIRS).each do |dir|
  directory dir
end
directory "dist"

COFFEE_SRC.each do |srcfile|
  target = srcfile.pathmap("%{views,build}X")
  file target => [srcfile, File.dirname(target)] do
    sh "coffee --compile --lint --bare --print #{srcfile} > #{target}"
  end
end

SASS_SRC.each do |srcfile|
  target = srcfile.pathmap("%{views,build}X")
  file target => [srcfile, File.dirname(target)] do
    sh "sass --scss --line-numbers #{srcfile} #{target}"
  end
end

task :default => :compile

desc "Compile Coffee-Scripts"
task :compile => COFFEE_OBJ + SASS_OBJ

desc "Monitor Coffee-Scripts for changes and compile"
task :watchr => :compile do
  sh "watchr Watchfile"
end

desc "Start server to run under Charles Proxy"
task :server do
  sh "mm-server --port 4568"
end

desc "Generate production application"
task :generate => :compile do
  sh "mm-build -r"
end

desc "Create distribution tarball"
task :tarball => [:generate, :dist] do
  sh %Q{tar -zcvf dist/#{File.basename(Dir.pwd)}.tgz -C build --exclude="javascripts" --exclude="stylesheets" --exclude="*.DS_Store" .}
end

desc "Generate assets"
task :assets do
  require "jammit"
  require "middleman"
  require "middleman/builder"

  system("spicy_bbq public/images > build/javascripts/sb.js")

  full_build_dir = File.join(Middleman::Server.root, Middleman::Server.build_dir)

  FileUtils.mkdir_p File.join(full_build_dir, Middleman::Server.js_dir)
  FileUtils.mkdir_p File.join(full_build_dir, Middleman::Server.css_dir)

  jammit_config_file = File.join(Middleman::Server.root, 'config', 'assets.yml')
  raise ConfigurationNotFound, "could not find \"#{jammit_config_file}\" " unless File.exists?(jammit_config_file)
  jammit_conf = YAML.load(ERB.new(File.read(jammit_config_file)).result)

  #touch_asset_files jammit_conf['javascripts']
  #touch_asset_files jammit_conf['stylesheets']

  ::Jammit.load_configuration(jammit_config_file)

  full_package_path = File.join(full_build_dir, ::Jammit.package_path)
  ::Jammit.packager.precache_all(full_package_path, Middleman::Server.root)
end

desc "Run Varnish"
task :varnish do
  system("killall varnishd || true")
  system("/usr/local/Cellar/varnish/3.0.0/sbin/varnishd -a :8081 -f varnish.vcl -s malloc")
end

desc "Run demo"
task :demo do
  system("ruby demo.rb -p 5982")
end
