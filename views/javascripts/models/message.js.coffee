class Application.Models.Message extends Backbone.Model
  TYPE_INBOX: 1
  TYPE_SENT: 2
  TYPE_DRAFT: 3
  TYPE_OUTBOX: 4
  TYPE_FAILED: 5
  TYPE_QUEUED: 6

  # TODO handle draft messages properly
  TYPES_INBOX: -> [@TYPE_INBOX]
  TYPES_SENT: -> [@TYPE_INBOX, @TYPE_SENT]
  TYPES_SENDING: -> [@TYPE_QUEUED, @TYPE_OUTBOX]
  TYPES_FAILED: -> [@TYPE_FAILED, @TYPE_DRAFT]

  inbox: ->
    _.include(@TYPES_INBOX(), @get('type'))

  sent: ->
    _.include(@TYPES_SENT(), @get('type'))

  sending: ->
    _.include(@TYPES_SENDING(), @get('type'))

  failed: ->
    _.include(@TYPES_FAILED(), @get('type'))

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

  parse: (resp, xhr) ->
    if _.include(@TYPES_SENDING(), resp.type)
      setTimeout =>
        @fetch()
      , 1000
    super resp, xhr

  initialize: (options) ->
    unless @get('type')
      @set({'type': @TYPE_QUEUED})
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
    new Date(unixDateMs).timeAgoInWords()

  resend: ->
    @set({'type': 6})
    $.ajax({
      url: @url() + '/resend',
      type: 'POST',
      success: (resp, status, xhr) =>
        @parse(resp, xhr)
    })

  markAsRead: ->
    return unless @id
    return if @get('read') == 1
    @set({'read': 1})
    $.ajax({
      url: @url() + '/read',
      type: 'POST',
      success: (resp, status, xhr) =>
        @parse(resp, xhr)
    })

  delete: (collection) ->
    collection.remove(this)
    collection.trigger 'reset'
    $.ajax({
      url: @url(),
      type: 'DELETE'
    })

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
