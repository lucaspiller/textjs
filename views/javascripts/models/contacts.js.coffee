class Application.Models.Contact extends Backbone.Model
  set: (attributes) ->
    if !attributes.id && attributes.key
      attributes.id = attributes.key
      attributes.key = undefined
    super attributes

  autocompleteText: ->
    @get('name') + " <" + @get('address') + ">"

class Application.Collections.Contacts extends Backbone.Collection
  model: Application.Models.Contact
  url: ->
      "/contacts"

  # sort in alphabetical
  comparator: (contact) ->
      contact.get('name')
