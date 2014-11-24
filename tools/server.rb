require 'sinatra'
require 'json'

set :public_folder, File.join(File.dirname(__FILE__), '../build')

get '/threads' do
  [
    {
      :id => 1,
      :read => 0,
      :body => "Hey, how was your weekend, did you get away in the end?",
      :date => 1316361559753,
      :address => '',
      :name => 'Malcolm'
    },
    {
      :key => "2",
      :id => 2,
      :read => 1,
      :body => "Sounds like a plan :)",
      :date => 1316346452685,
      :address => '',
      :name => 'Zoe'
    },
    {
      :key => "3",
      :id => 3,
      :read => 1,
      :body => "No worries, well have a good one! Let me know when is good for you next week.",
      :date => 1316336868113,
      :address => '',
      :name => 'Jayne'
    },
    {
      :key => "4",
      :id => 4,
      :read => 1,
      :body => "See you Monday at 5pm!",
      :date => 1316291111607,
      :address => '',
      :name => 'Hoban'
    },
    {
      :key => "5",
      :id => 5,
      :read => 1,
      :body => "I <3 Android",
      :date => 1312979668809,
      :address => '',
      :name => 'Kaylee'
    }
  ].to_json
end

get '/contacts' do
  [
    {
      :address => '555-809-102',
      :name => 'Malcolm'
    },
    {
      :address => '555-093-942',
      :name => 'Zoe'
    },
    {
      :address => '555-921-820',
      :name => 'Jayne'
    },
    {
      :address => '555-393-391',
      :name => 'Hoban'
    },
    {
      :address => '555-602-281',
      :name => 'Kaylee'
    }
  ].to_json
end

get '/threads/1' do
  [
    {
      :id => 1,
      :body => "Hey, how was your weekend, did you get away in the end?",
      :date => 1316361559753,
      :type => 1,
      :read => 0
    },
  ].to_json
end

get '/threads/2' do
  [
    {
      :id => 1,
      :body => "Sounds like a plan :)",
      :date => 1316346452685,
      :type => 0,
      :read => 0
    },
  ].to_json
end

get '/threads/*' do
  [
    {
      :id => 1,
      :body => "No worries, well have a good one! Let me know when is good for you next week.",
      :date => 1316336868113,
      :type => 1,
      :read => 1
    },
    {
      :id => 2,
      :body => "Hey, sorry dude, but I'm going to have to give it a miss tonight. Beer sometime next week?",
      :date => 1316336468113,
      :type => 2,
      :read => 1
    },
    {
      :id => 3,
      :body => "Still all good for the game tonight?",
      :date => 1316335468113,
      :type => 1,
      :read => 1
    },
    {
      :id => 4,
      :body => "Hey, I'm still not sure about the game on Saturday, I'll give you a shout. I've got all these beers though that we need to get rid of though...",
      :date => 1316210868113,
      :type => 2,
      :read => 1
    }
  ].to_json
end

get '/' do
  redirect 'index.html'
end
