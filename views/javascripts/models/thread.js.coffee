class Application.Models.Thread extends Backbone.Model

class Application.Collections.Threads extends Backbone.Collection
  model: Application.Models.Thread
  url: ->
      "/threads"
