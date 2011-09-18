window.onerror = (msg, file, line) ->
  alert('Uh-oh! Something went wrong... please send this to the developers!\n\n' + msg + ' ' + file + ':' + line + '\n\n' + printStackTrace().join('\n\n'));

Application = {
    Views: {},
    Controllers: {},
    Models: {},
    Collections: {},
    Config: {},

    AccessToken: '1234',
    Contacts: undefined,
    Threads: undefined,
    Focus: false,

    init: (config) ->
      @buildUi()
      @bindWindowFocus()
      Application.Config = config
      Application.Contacts = new Application.Collections.Contacts()
      Application.Threads = new Application.Collections.Threads()
      new Application.Controllers.Dashboard()
      Backbone.history.start()

    buildUi: ->
      $('.threadView').html('<div class="loading">No Conversation Selected</div>')
      @resizeRightColumn()
      $(window).resize(() =>
        @resizeRightColumn()
      )

    resizeRightColumn: ->
      $('#rightColumn').width(
        ($('body').width() - 320) + 'px'
      )

    bindWindowFocus: ->
      $(window).bind 'focus', (evt) ->
        Application.Focus = true
      .bind 'blur', (evt) ->
        Application.Focus = false
      .focus()

    onFocus: (callback) ->
      $(window).bind 'focus', (evt) ->
        callback(evt)

    connectionError: ->
      $('.threadView').html('<div class="loading">Connection Error</div>')
}
