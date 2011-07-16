class Application.Views.Threadlist extends Backbone.View
  el: '#threadList'

  events: {
    "click .thread": "showThread"
  }

  initialize: ->
    @bindCollectionChangeEvent()
    @render()

  bindCollectionChangeEvent: ->
    @collection.bind 'change', =>
      @render()

  render: ->
    $(@el).html JST['threadlist/threadlist']({ threads: @collection })

  showThread: (evt) ->
    threadId = $(evt.currentTarget).attr('data-id')
    thread = @collection.get(threadId)
    thread.fetchMessages({
      success: (messages) ->
        new Application.Views.Threadview({ model: thread, collection: messages })
    })
