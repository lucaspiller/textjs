class Application.Models.Contact extends Backbone.Model

class Application.Collections.Contacts extends Backbone.Collection
  model: Application.Models.Contact
  url: ->
      "/contacts"
