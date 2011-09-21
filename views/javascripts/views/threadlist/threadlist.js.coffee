class Application.Views.Threadlist extends Backbone.View
  THREAD_FOCUS_UPDATE_INTERVAL: 5
  THREAD_BLUR_UPDATE_INTERVAL: 300

  threadsFetched: false
  connectionError: false
  currentThreadId: -1

  el: '#threadList'

  events: {
    "click .thread": "showThreadEvent"
  }

  initialize: ->
    @bindCollectionResetEvent()
    @bindApplicationFocusEvent()
    @setupShortcuts()
    @updateCollection()
    @render()

  bindCollectionResetEvent: ->
    @collection.bind 'reset', =>
      @render()

  bindApplicationFocusEvent: ->
    Application.onFocus (evt) =>
      @updateCollection()

  setupShortcuts: ->
    key '⌘+r, ctrl+r', (evt) =>
      # ctrl / cmd r - prevent page reload
      evt.preventDefault()
    key '⌘+j, ctrl+j', =>
      # ctrl / cmd j - next thread
      @showNextThread()
    key '⌘+k, ctrl+k', =>
      # ctrl / cmd k - prev thread
      @showPreviousThread()

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

  showNextThread: ->
    nextThreadId = if @currentThreadId != -1
       $('.thread[data-id=' + @currentThreadId + ']').next('.thread').attr('data-id')
    else
      $('.thread').first().attr('data-id')
    if nextThreadId
      @showThread nextThreadId

  showPreviousThread: ->
    prevThreadId = if @currentThreadId != -1
       $('.thread[data-id=' + @currentThreadId + ']').prev('.thread').attr('data-id')
    else
      $('.thread').first().attr('data-id')
    if prevThreadId
      @showThread prevThreadId

  showThread: (id) ->
    $('.thread').removeClass('selected')
    $('.thread[data-id=' + id + ']').addClass('selected')
    @currentThreadId = id
    thread = @collection.get(@currentThreadId)
    new Application.Views.Threadview({ model: thread, collection: thread.messages })

  showThreadEvent: (evt) ->
    $(evt.currentTarget).addClass('selected')
    @showThread $(evt.currentTarget).attr('data-id')
