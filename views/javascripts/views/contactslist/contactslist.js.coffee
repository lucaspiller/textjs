class Application.Views.Contactslist extends Backbone.View
  el: '#contacts'

  initialize: ->
    @contacts = @options.contacts
    @render()

  render: ->
    $(this.el).html(JST['contactslist/contactslist']({ contacts: @contacts }))
