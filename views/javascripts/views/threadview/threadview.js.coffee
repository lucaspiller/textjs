class Application.Views.Threadview extends Backbone.View
  THREAD_FOCUS_UPDATE_INTERVAL: 5
  THREAD_BLUR_UPDATE_INTERVAL: 300

  className: 'threadView'

  events: {
    "click .reply-button": "showReply"
    "click .message-resend-button": "resendMessage"
    "click .message-delete-button": "deleteMessage"
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
    if Application.Focus
      $(@el).oneTime @THREAD_FOCUS_UPDATE_INTERVAL * 1000, =>
        @updateCollection()
    else
      $(@el).oneTime @THREAD_BLUR_UPDATE_INTERVAL * 1000, =>
        @updateCollection()

  updateCollection: ->
    if $(@el).parent()[0]
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
      @markMessagesAsRead()

  markMessagesAsRead: ->
    @collection.each (message) ->
      message.markAsRead()

  showReply: (evt) ->
    new Application.Views.Replyview({ model: @model, collection: @collection, parent: @el })

  resendMessage: (evt) ->
    messageId = $(evt.currentTarget).closest('.message').attr('data-id')
    message = @collection.get(messageId)
    message.resend()

  deleteMessage: (evt) ->
    if confirm('Are you sure you want to delete this message?')
      messageId = $(evt.currentTarget).closest('.message').attr('data-id')
      message = @collection.get(messageId)
      message.delete(@collection)
