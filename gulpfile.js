var gulp      = require('gulp');
var coffee    = require('gulp-coffee');
var concat    = require('gulp-concat');
var jstConcat = require('gulp-jst-concat');
var sass      = require('gulp-sass');
var run       = require('gulp-run');
var uglify    = require('gulp-uglify');
var minifyCSS = require('gulp-minify-css');

gulp.task('coffee', function() {
  return gulp.src('./views/**/*.coffee')
    .pipe(coffee({bare: true}))
    .pipe(gulp.dest('./build'))
});

gulp.task('jst', function() {
  return gulp.src('./views/**/*.jst')
    .pipe(jstConcat('javascripts/jst.js', {
      renameKeys: ['^.*views/(.*).jst$', '$1']
    }))
    .pipe(gulp.dest('./build'))
});

gulp.task('images', function() {
  return run('./tools/spicy_bbq public/images > build/javascripts/sb.js').exec()
});

gulp.task('sass', function() {
  return gulp.src('./views/**/*.scss')
    .pipe(sass())
    .pipe(concat('style.css'))
    .pipe(gulp.dest('./build'))
});

gulp.task('html', function() {
  return gulp.src('./views/index.html')
    .pipe(gulp.dest('./build'))
});

gulp.task('combine', function() {
  gulp.start(['coffee', 'jst', 'images', 'sass', 'html']);

  gulp.src([
    'public/javascripts/jquery-1.6.2.js',
    'public/javascripts/jquery.timers.js',
    'public/javascripts/jquery.autocomplete.js',
    'public/javascripts/patches.js',
    'public/javascripts/keymaster.js',
    'public/javascripts/underscore.js',
    'public/javascripts/backbone.js',
    'public/javascripts/stacktrace.js',
    ])
    .pipe(concat('libs.js'))
    .pipe(gulp.dest('./build'))

  gulp.src([
    'build/javascripts/application.js.js',
    'build/javascripts/*_util.js.js',
    'build/javascripts/models/*.js',
    'build/javascripts/views/**/*.js',
    'build/javascripts/controllers/*.js',
    'build/javascripts/jst.js',
    'build/javascripts/sb.js'
    ])
    .pipe(concat('application.js'))
    .pipe(gulp.dest('./build'))
});

gulp.task('default', ['combine']);

gulp.task('dist', function() {
  gulp.src([
    'build/*.js',
    ])
    .pipe(uglify())
    .pipe(gulp.dest('./dist'))

  gulp.src([
    'build/*.css',
    ])
    .pipe(minifyCSS())
    .pipe(gulp.dest('./dist'))
});
