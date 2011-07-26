class Application.Views.Threadlist extends Backbone.View
  THREAD_UPDATE_INTERVAL: 5
  threadsFetched: false

  el: '#threadList'

  events: {
    "click .thread": "showThread"
  }

  initialize: ->
    @bindCollectionResetEvent()
    @updateCollection()
    @render()

  bindCollectionResetEvent: ->
    @collection.bind 'reset', =>
      @render()

  runPeriodicUpdate: ->
    setTimeout =>
      @updateCollection()
    , (@THREAD_UPDATE_INTERVAL * 1000)

  updateCollection: ->
    @collection.fetch {
      success: (collection, response) =>
        @threadsFetched = true
        @runPeriodicUpdate()
      error: (collection, response) =>
        # TODO some sort of offline notification
        console.log 'Updating threads failed'
        @runPeriodicUpdate()
    }

  render: ->
    if @threadsFetched
      $(@el).html JST['threadlist/threadlist']({ threads: @collection })
    else
      $(@el).html JST['threadlist/threadlist_loading']({})

  showThread: (evt) ->
    threadId = $(evt.currentTarget).attr('data-id')
    thread = @collection.get(threadId)
    thread.fetchMessages({
      success: (messages) ->
        new Application.Views.Threadview({ model: thread, collection: messages })
    })
    $('.thread').removeClass('selected')
    $(evt.currentTarget).addClass('selected')
