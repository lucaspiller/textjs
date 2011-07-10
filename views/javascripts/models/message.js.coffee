class Application.Models.Message extends Backbone.Model
  initialize: (options) ->
    Application.Contacts.bind 'all', =>
      @fetchContact()
    @fetchContact()
    super options

  fetchContact: ->
    contactId = @get('sender_key')
    @set({'contact': Application.Contacts.get(contactId)})

  contactName: ->
    if @get('contact')
      @get('contact').get('name')
    else
      @get('name')

class Application.Collections.Messages extends Backbone.Collection
  model: Application.Models.Message
