class Application.Controllers.Dashboard extends Backbone.Controller
  routes: {
    '': 'dashboard'
  }

  dashboard: ->
    $('#threads').css('height', ($(window).height() - 460) + 'px');
    Application.Threads.fetch({
      success: (collection, response) ->
        new Application.Views.Threadlist({ collection: collection })
      error: (collection, response) ->
        console.log "Error response fetching threads: ", response
    })
    Application.Contacts.fetch({
      success: (collection, response) ->
        new Application.Views.Contactslist({ collection: collection })
      error: (collection, response) ->
        console.log "Error response fetching contacts: ", response
    })
