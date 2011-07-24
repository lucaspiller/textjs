class Application.Views.Replyview extends Backbone.View
  className: 'replyView'
  attributes: { style: 'display: none' }

  events: {
    'click input[type=button].cancel': 'cancelReply'
    'click input[type=submit]': 'sendReply'
  }

  initialize: (options) ->
    @attachToParent(options.parent)
    @render()
    @resizeTextarea()
    $(@el).resize =>
      @resizeTextarea()
    @startCounterTimer()

  attachToParent: (parent) ->
    $(parent).append(@el)

  render: ->
    $(@el).html JST['replyview/replyview']({})
    $(@el).slideDown()

  resizeTextarea: ->
    $(@el).find('textarea').width(
      ($(@el).find('.reply-cont').width() - 22) + 'px'
    )

  updateCounter: ->
    length = $(@el).find('textarea')[0].value.length - 1
    pages = Math.floor(length / 160) + 1
    if pages == 0
      remaining = 160
      pages = 1
    else
      pageLength = length - ((pages - 1) * 160)
      remaining = 159 - pageLength
    $(@el).find('.counter').text(remaining + " / " + pages)
    @startCounterTimer()

  startCounterTimer: ->
    setTimeout =>
      @updateCounter()
    , 250

  cancelReply: ->
    $(@el).slideUp 'fast', => @remove()

  sendReply: ->
    body = $(@el).find('textarea')[0].value
    @collection.create({'body': body})
    $(@el).slideUp 'fast', => @remove()
