String::formatBody = ->
  @convertLinks()

String::convertLinks = ->
  @replace /(https?:\/\/\S+)/gi, (s) ->
     '<a href="' + s + '">' + s + '</a>'
