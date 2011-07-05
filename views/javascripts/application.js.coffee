Application = {
    Views: {},
    Controllers: {},
    Models: {},
    Collections: {},
    Config: {},
    init: (config) ->
      Application.Config = config
      new Application.Controllers.Dashboard()
      Backbone.history.start()
}
