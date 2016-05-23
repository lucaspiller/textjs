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
    $(parent).find('#composeView').remove()
    $(parent).prepend(@el)

  render: ->
    $(@el).html JST['composeview/composeview']({})
    $(@el).find('input[type=text]').autocomplete @contactAutocomplete(), {
      matchContains: true,
      autoFill: true,
      multiple: true,
      multipleSeparator: "; "
    }
    $(@el).slideDown =>
      $(@el).find('input[type=text]')[0].focus()

  contactAutocomplete: ->
    @collection.map (contact) ->
      contact.autocompleteText()

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

  extractRecipients: ->
    regex = /\<(.*)\>/
    recipients = []
    _.each $(@el).find('input[type=text]').val().split(';'), (text) =>
      text = text.trim()
      if text
        match = regex.exec(text)
        if match
          recipients.push match[1]
        else
          recipients.push text
    recipients

  sendCompose: ->
    body = $(@el).find('textarea')[0].value
    _.each @extractRecipients(), (recipient) =>
      new Application.Models.Message({
        'body': body,
        'address': recipient
      }).save()
    $(@el).slideUp 'fast', => @remove()
