class Application.Views.Threadview extends Backbone.View
  className: 'threadView'

  events: {
    "click .reply-button": "showReply"
    "click .message-resend-button": "resendMessage"
  }

  initialize: ->
    @attachToParent()
    @bindChangeEvent()
    @render()

  attachToParent: ->
    $('#rightColumn .threadView').remove()
    $('#rightColumn').append(@el)

  bindChangeEvent: ->
    @collection.bind 'change', =>
      @render()
    @model.bind 'change', =>
      @render()

  render: ->
    $(@el).html JST['threadview/threadview']({ thread: @model, messages: @collection })

  showReply: (evt) ->
    new Application.Views.Replyview({ model: @model, collection: @collection, parent: @el })

  resendMessage: (evt) ->
    messageId = $(evt.currentTarget).closest('.message').attr('data-id')
    message = @collection.get(messageId)
    message.resend()
