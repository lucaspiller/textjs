class Application.Views.Threadview extends Backbone.View
  initialize: ->
    @bindChangeEvent()
    @render()

  bindChangeEvent: ->
    @collection.bind 'change', =>
      @render()
    @model.bind 'change', =>
      @render()

  render: ->
    uki('#threadView')[0].data(@collection.map (message) ->
      JST['threadview/threadview']({ message: message })
    )
    uki('#threadView')[0].relayout()
