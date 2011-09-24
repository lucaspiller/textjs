class Application.Views.Menubar extends Backbone.View
  CONTACT_FOCUS_UPDATE_INTERVAL: 60
  CONTACT_BLUR_UPDATE_INTERVAL: 900

  contactsFetched: false
  connectionError: false

  el: '#menuBar'

  events: {
    "click .compose-button": "showCompose"
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
    key '⌘+n, ctrl+n', (evt) =>
      # ctrl / cmd n - compose message
      evt.preventDefault()
      @showCompose()

  runPeriodicUpdate: ->
    $(@el).stopTime()
    if Application.Focus
      $(@el).oneTime @CONTACT_FOCUS_UPDATE_INTERVAL * 1000, =>
        @updateCollection()
    else
      $(@el).oneTime @CONTACT_BLUR_UPDATE_INTERVAL * 1000, =>
        @updateCollection()

  updateCollection: ->
    @collection.fetch {
      success: (collection, response) =>
        @contactsFetched = true
        @runPeriodicUpdate()
      error: (collection, response) =>
        Application.connectionError()
        @connectionError = true
      silent: true
    }

  render: ->
    $(@el).html JST['menubar/menubar']({})
    $('#rightBar').width(
      ($('body').width() - $('#leftColumn').width() - 2) + 'px'
    )

  showCompose: ->
    new Application.Views.Composeview({ parent: $('body') })
