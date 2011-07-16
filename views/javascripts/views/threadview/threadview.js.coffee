class Application.Views.Threadview extends Backbone.View
  el: '#threadView'

  initialize: ->
    @bindChangeEvent()
    @render()

  bindChangeEvent: ->
    @collection.bind 'change', =>
      @render()
    @model.bind 'change', =>
      @render()

  render: ->
    $(@el).html JST['threadview/threadview']({ messages: @collection })
