class Application.Models.Thread extends Backbone.Model
  initialize: (options) ->
    Application.Contacts.bind 'all', =>
      @fetchContact()
    @fetchContact()
    super options

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

class Application.Collections.Threads extends Backbone.Collection
  model: Application.Models.Thread
  url: ->
      "/threads"
