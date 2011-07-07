class Application.Controllers.Dashboard extends Backbone.Controller
  routes: {
    '': 'loading'
  }

  loading: ->
    new Application.Collections.Threads().fetch({
      success: (collection, response) ->
        new Application.Views.Threadlist({ threads: collection })

      error: (collection, response) ->
        console.log "Error response fetching threads: ", response
    })
    new Application.Collections.Contacts().fetch({
      success: (collection, response) ->
        new Application.Views.Contactslist({ contacts: collection })

      error: (collection, response) ->
        console.log "Error response fetching contacts: ", response
    })
