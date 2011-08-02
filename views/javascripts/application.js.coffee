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

    init: (config) ->
      @buildUi()
      Application.Config = config
      Application.Contacts = new Application.Collections.Contacts()
      Application.Threads = new Application.Collections.Threads()
      new Application.Controllers.Dashboard()
      Backbone.history.start()

    buildUi: ->
      @resizeRightColumn()
      $(window).resize(() =>
        @resizeRightColumn()
      )

    resizeRightColumn: ->
      $('#rightColumn').width(
        ($('body').width() - 350) + 'px'
      )
}
