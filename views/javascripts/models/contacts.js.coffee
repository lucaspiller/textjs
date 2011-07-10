class Application.Models.Contact extends Backbone.Model
  set: (attributes) ->
    if !attributes.id && attributes.key
      attributes.id = attributes.key
      attributes.key = undefined
    super attributes

class Application.Collections.Contacts extends Backbone.Collection
  model: Application.Models.Contact
  url: ->
      "/contacts"
