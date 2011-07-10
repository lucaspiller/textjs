class Application.Controllers.Dashboard extends Backbone.Controller
  routes: {
    '': 'loading'
  }

  loading: ->
    Application.Threads.fetch({
      success: (collection, response) ->
        new Application.Views.Threadlist({ threads: collection })

      error: (collection, response) ->
        console.log "Error response fetching threads: ", response
    })
    Application.Contacts.fetch({
      success: (collection, response) ->
        new Application.Views.Contactslist({ contacts: collection })

      error: (collection, response) ->
        console.log "Error response fetching contacts: ", response
    })
