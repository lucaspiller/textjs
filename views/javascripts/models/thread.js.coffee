class Application.Models.Thread extends Backbone.Model
  MAX_PREVIEW_LENGTH: 45

  initialize: (options) ->
    Application.Contacts.bind 'all', =>
      @fetchContact()
    @fetchContact()
    super options

  fetchMessages: (options) ->
    if !@messages
      @messages = new Application.Collections.Messages
      @messages.bind 'reset', =>
        @updateSnippetText()
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

  updateSnippetText: ->
    @set({
      'body': @messages.at(0).get('body'),
      'date': @messages.at(0).get('date')
    })

  fetchContact: ->
    contactId = @get('sender_key')
    @set({'contact': Application.Contacts.get(contactId)})

  contactName: ->
    if @get('contact')
      @get('contact').get('name')
    else
      @get('address')

  date: ->
    unixDateMs = @get('date')
    date = new Date(unixDateMs)

    hours = date.getHours()
    if hours < 10
      hours = '0' + hours
    minutes = date.getMinutes()
    if minutes < 10
      minutes = '0' + minutes
    day = date.getDate()

    months = [
      'Jan', 'Feb', 'Mar',
      'Apr', 'May', 'Jun',
      'Jul', 'Aug', 'Sep',
      'Oct', 'Nov', 'Dec'
    ]
    month = months[date.getMonth()]

    # 10:54, 10 Jul
    hours + ":" + minutes + ", " + day + " " + month

  preview: ->
    body = @get('body')
    if body.length > @MAX_PREVIEW_LENGTH
      preview = body.substring(0, @MAX_PREVIEW_LENGTH)
      preview = preview.replace(/\s*\w+$/, '') + '...'
    else
      preview = body

  unreadMessages: ->
    @get('read') == 0

class Application.Collections.Threads extends Backbone.Collection
  model: Application.Models.Thread

  # sort in reverse date order
  comparator: (thread) ->
      -thread.get('date')

  url: ->
      "/threads"
