class Application.Views.Threadview extends Backbone.View
  el: '#threadView'

  events: {
    "click .reply-button": "showReply"
  }

  initialize: ->
    @bindChangeEvent()
    @render()

  bindChangeEvent: ->
    @collection.bind 'change', =>
      @render()
    @model.bind 'change', =>
      @render()

  render: ->
    $(@el).html JST['threadview/threadview']({ thread: @model, messages: @collection })

  showReply: (evt) ->
    new Application.Views.Replyview({ model: @model, collection: @collection })
