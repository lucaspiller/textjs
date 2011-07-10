class Application.Views.Threadlist extends Backbone.View
  el: '#threads'

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
    uki('#threadList')[0].data(@collection.map (thread) ->
      JST['threadlist/threadlist']({ thread: thread })
    )
    uki('#threadList')[0].relayout()

  showThread: (evt) ->
    threadId = $(evt.currentTarget).attr('data-id')
    thread = @collection.get(threadId)
    thread.fetchMessages({
      success: (messages) ->
        new Application.Views.Threadview({ model: thread, collection: messages })
    })
