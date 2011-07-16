Application = {
    Views: {},
    Controllers: {},
    Models: {},
    Collections: {},
    Config: {},

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
        ($('body').width() - 425) + 'px'
      )
}
