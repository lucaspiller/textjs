class Application.Views.Contactslist extends Backbone.View
  el: '#contactsList'

  initialize: ->
    @bindCollectionChangeEvent()
    @render()

  bindCollectionChangeEvent: ->
    @collection.bind 'change', =>
      @render()

  render: ->
    uki('#contactsList')[0].data(@collection.map (contact) ->
      JST['contactslist/contactslist']({ contact: contact })
    )
