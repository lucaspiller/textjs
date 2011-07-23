class Application.Models.Message extends Backbone.Model
  TYPE_INBOX: 1

  # TODO fix routes
  urlRoot: '/messages'

  url: ->
    base = @urlRoot || urlError()
    if @isNew()
      base
    else
      base + '/' + encodeURIComponent(@id)

  save: (attrs, options) ->
    if !@get('address')
      if @collection
        address = @collection.thread().get('address')
        @set({'address', address})
    super attrs, options

  initialize: (options) ->
    Application.Contacts.bind 'all', =>
      @fetchContact()
    @fetchContact()
    super options

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

class Application.Collections.Messages extends Backbone.Collection
  model: Application.Models.Message

  thread: ->
    Application.Threads.get(@threadId())

  threadId: ->
    # /threads/52
    @url.split('/')[2]

  # sort in reverse date order
  comparator: (message) ->
      -message.get('date')

  _add: (model, options) ->
    super model, options
    @trigger('change')

  _remove: (model, options) ->
    super model, options
    @trigger('change')
