require 'sinatra'
require 'json'

get '/threads' do
  [
    {
      :sender_key => "1",
      :id => 1,
      :read => 0,
      :body => "Hey, how was your weekend, did you get away in the end?",
      :date => 1316361559753,
      :address => ''
    },
    {
      :sender_key => "2",
      :id => 2,
      :read => 1,
      :body => "Sounds like a plan :)",
      :date => 1316346452685,
      :address => ''
    },
    {
      :sender_key => "3",
      :id => 3,
      :read => 1,
      :body => "No worries, well have a good one! Let me know when is good for you next week.",
      :date => 1316336868113,
      :address => ''
    },
    {
      :sender_key => "4",
      :id => 4,
      :read => 1,
      :body => "See you Monday at 5pm!",
      :date => 1316291111607,
      :address => ''
    },
    {
      :sender_key => "5",
      :id => 5,
      :read => 1,
      :body => "I <3 Android",
      :date => 1312979668809,
      :address => ''
    }
  ].to_json
end

get '/contacts' do
  [
    {
      :key => 1,
      :name => 'Malcolm'
    },
    {
      :key => 2,
      :name => 'Zoe'
    },
    {
      :key => 3,
      :name => 'Jayne'
    },
    {
      :key => 4,
      :name => 'Hoban'
    },
    {
      :key => 5,
      :name => 'Kaylee'
    }
  ].to_json
end

get '/threads/3' do
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
