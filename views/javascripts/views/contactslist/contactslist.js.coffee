class Application.Views.Contactslist extends Backbone.View
  el: '#contacts'

  initialize: ->
    @bindCollectionChangeEvent()
    @render()

  bindCollectionChangeEvent: ->
    @collection.bind 'change', =>
      @render()

  render: ->
    $(this.el).html(JST['contactslist/contactslist']({ contacts: @collection }))
