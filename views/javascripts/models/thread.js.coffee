class Application.Models.Thread extends Backbone.Model
  MAX_PREVIEW_LENGTH: 45

  initialize: (options) ->
    Application.Contacts.bind 'all', =>
      @fetchContact()
    @fetchContact()
    @messages = new Application.Collections.Messages
    @messages.bind 'reset', =>
      @updateSnippetText()
    @messages.url = '/threads/' + @get('id')
    super options

  fetchMessages: (options) ->
    if @messages.size() == 0
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

  updateSnippetText: ->
    if @messages.at(0)
      @set({
        'body': @messages.at(0).get('body'),
        'date': @messages.at(0).get('date')
      })
    else
      Application.Threads.remove(this)
      Application.Threads.trigger 'reset'

  fetchContact: ->
    contactId = @get('sender_key')
    @set({'contact': Application.Contacts.get(contactId)})

  contactName: ->
    if @get('name')
      @get('name')
    else
      @get('address')

  date: ->
    unixDateMs = @get('date')
    new Date(unixDateMs).timeAgoInWords()

  preview: ->
    body = @get('body')
    if body == undefined
      ""
    else
      if body.length > @MAX_PREVIEW_LENGTH
        preview = body.substring(0, @MAX_PREVIEW_LENGTH)
        preview.replace(/\s*\w+$/, '') + '...'
      else
        body

  unreadMessages: ->
    @get('read') == 0

class Application.Collections.Threads extends Backbone.Collection
  model: Application.Models.Thread

  # sort in reverse date order
  comparator: (thread) ->
      -thread.get('date')

  url: ->
      "/threads"
