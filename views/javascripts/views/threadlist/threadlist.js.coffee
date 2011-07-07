class Application.Views.Threadlist extends Backbone.View
  el: '#threads'

  events: {
    "click .thread": "showThread"
  }

  initialize: ->
    @threads = @options.threads
    @render()

  render: ->
    $(this.el).html(JST['threadlist/threadlist']({ threads: @threads }))

  showThread: (evt) ->
    threadId = $(evt.currentTarget).attr('data-id')
    thread = @threads.get(threadId)
    thread.fetchMessages({
      success: (messages) ->
        new Application.Views.Threadview({ thread: thread, messages: messages })
    })
