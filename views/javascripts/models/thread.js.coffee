class Application.Models.Thread extends Backbone.Model
  fetchMessages: (options) ->
    if !@messages
      @messages = new Application.Collections.Messages
      @messages.url = '/threads/' + @get('id')
      @messages.fetch({
        success: (collection, response) ->
          if options.success
            options.success(collection)
      })
      @messages
    else
      if options.success
        options.success(@messages)
    @messages

class Application.Collections.Threads extends Backbone.Collection
  model: Application.Models.Thread
  url: ->
      "/threads"
