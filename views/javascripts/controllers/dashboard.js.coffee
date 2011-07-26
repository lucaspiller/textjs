class Application.Controllers.Dashboard extends Backbone.Router
  routes: {
    '': 'dashboard'
  }

  dashboard: ->
    new Application.Views.Threadlist({ collection: Application.Threads })
    #new Application.Views.Contactslist({ collection: Application.Contacts })
    Application.Contacts.fetch({
      error: (collection, response) ->
        console.log "Error response fetching contacts: ", response
    })
