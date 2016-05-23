class Application.Controllers.Dashboard extends Backbone.Router
  routes: {
    '': 'dashboard'
  }

  dashboard: ->
    new Application.Views.Threadlist({ collection: Application.Threads })
    new Application.Views.Menubar({ collection: Application.Contacts })
