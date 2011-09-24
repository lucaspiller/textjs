class Application.Views.Composeview extends Backbone.View
  id: 'composeView'
  attributes: { style: 'display: none' }

  events: {
    'keydown textarea': 'handleKeyDown'
    'keyup textarea': 'handleKeyUp'
    'click input[type=button].cancel': 'cancelCompose'
    'click input[type=submit]': 'sendCompose'
  }

  initialize: (options) ->
    @attachToParent(options.parent)
    @render()
    @startCounterTimer()

  attachToParent: (parent) ->
    $(parent).prepend(@el)

  render: ->
    $(@el).html JST['composeview/composeview']({})
    $(@el).slideDown => 
      #     $(@el).find('input[type=text]').width( $(@el).find('textarea').width() + 'px')
      $(@el).find('input[type=text]')[0].focus()

  handleKeyDown: (evt) ->
    if evt.keyCode == 27
      # esc - cancel message
      evt.preventDefault()
      @cancelCompose()
    else if evt.keyCode == 13 and (evt.metaKey or evt.ctrlKey)
      # ctrl/cmd enter - send message
      evt.preventDefault()
      @sendCompose()
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

  cancelCompose: ->
    $(@el).slideUp 'fast', => @remove()

  sendCompose: ->
    body = $(@el).find('textarea')[0].value
    model = new @collection.model({'body': body})
    @collection.add(model)
    @collection.trigger('reset')
    model.save()
    $(@el).slideUp 'fast', => @remove()
