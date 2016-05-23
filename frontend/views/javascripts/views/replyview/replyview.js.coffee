class Application.Views.Replyview extends Backbone.View
  className: 'replyView'
  attributes: { style: 'display: none' }

  events: {
    'keydown textarea': 'handleKeyDown'
    'keyup textarea': 'handleKeyUp'
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
    $(parent).find('.replyView').remove()
    $(parent).prepend(@el)

  render: ->
    $(@el).html JST['replyview/replyview']({})
    $(@el).slideDown => $(@el).find('textarea')[0].focus()

  resizeTextarea: ->
    $(@el).find('textarea').width(
      ($(@el).find('.reply-cont').width() - 22) + 'px'
    )

  handleKeyDown: (evt) ->
    if evt.keyCode == 27
      # esc - cancel message
      evt.preventDefault()
      @cancelReply()
    else if evt.keyCode == 13 and (evt.metaKey or evt.ctrlKey)
      # ctrl/cmd enter - send message
      evt.preventDefault()
      @sendReply()
    else if evt.keyCode == 82 and (evt.metaKey or evt.ctrlKey)
      # ctrl/cmd r - prevent page reload
      evt.preventDefault()

  handleKeyUp: (evt) ->
    @updateCounter()

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
    model = new @collection.model({'body': body})
    @collection.add(model)
    @collection.trigger('reset')
    model.save()
    $(@el).slideUp 'fast', => @remove()
