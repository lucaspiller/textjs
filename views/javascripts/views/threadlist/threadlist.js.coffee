class Application.Views.Threadlist extends Backbone.View
  THREAD_FOCUS_UPDATE_INTERVAL: 5
  THREAD_BLUR_UPDATE_INTERVAL: 300

  threadsFetched: false
  connectionError: false
  currentThreadId: -1

  el: '#threadList'

  events: {
    "click .thread": "showThread"
  }

  initialize: ->
    @bindCollectionResetEvent()
    @bindApplicationFocusEvent()
    @updateCollection()
    @render()

  bindCollectionResetEvent: ->
    @collection.bind 'reset', =>
      @render()

  bindApplicationFocusEvent: ->
    Application.onFocus (evt) =>
      @updateCollection()

  runPeriodicUpdate: ->
    $(@el).stopTime()
    if Application.Focus
      $(@el).oneTime @THREAD_FOCUS_UPDATE_INTERVAL * 1000, =>
        @updateCollection()
    else
      $(@el).oneTime @THREAD_BLUR_UPDATE_INTERVAL * 1000, =>
        @updateCollection()

  updateCollection: ->
    @collection.fetch {
      success: (collection, response) =>
        @threadsFetched = true
        @render()
        @runPeriodicUpdate()
      error: (collection, response) =>
        Application.connectionError()
        @connectionError = true
        @render()
      silent: true
    }

  render: ->
    if @threadsFetched
      $(@el).html JST['threadlist/threadlist']({ threads: @collection, currentThreadId: @currentThreadId })
    else if @connectionError
      $(@el).html ''
    else
      $(@el).html JST['threadlist/threadlist_loading']({})

  showThread: (evt) ->
    $('.thread').removeClass('selected')
    $(evt.currentTarget).addClass('selected')
    @currentThreadId = $(evt.currentTarget).attr('data-id')
    thread = @collection.get(@currentThreadId)
    new Application.Views.Threadview({ model: thread, collection: thread.messages })
