class Application.Views.Threadview extends Backbone.View
  THREAD_UPDATE_INTERVAL: 5

  className: 'threadView'

  events: {
    "click .reply-button": "showReply"
    "click .message-resend-button": "resendMessage"
  }

  initialize: ->
    @attachToParent()
    @bindCollectionResetEvent()
    @updateCollection()
    @render()

  attachToParent: ->
    $(@el).append(@make('div', {'class': 'cont'}, undefined))
    $('#rightColumn .threadView').remove()
    $('#rightColumn').append(@el)

  bindCollectionResetEvent: ->
    @collection.bind 'reset', =>
      @render()
    @model.bind 'change', =>
      @render()

  runPeriodicUpdate: ->
    setTimeout =>
      @updateCollection()
    , (@MESSAGES_UPDATE_INTERVAL * 1000)

  updateCollection: ->
    @collection.fetch {
      success: (collection, response) =>
        @runPeriodicUpdate()
      error: (collection, response) =>
        # TODO some sort of offline notification
        console.log 'Updating messages failed'
        @runPeriodicUpdate()
    }

  render: ->
    if @collection.size() == 0
      $(@el).find('.cont').html JST['threadview/threadview_loading']({ thread: @model, messages: @collection })
    else
      $(@el).find('.cont').html JST['threadview/threadview']({ thread: @model, messages: @collection })

  showReply: (evt) ->
    new Application.Views.Replyview({ model: @model, collection: @collection, parent: @el })

  resendMessage: (evt) ->
    messageId = $(evt.currentTarget).closest('.message').attr('data-id')
    message = @collection.get(messageId)
    message.resend()
