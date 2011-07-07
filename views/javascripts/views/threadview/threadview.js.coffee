class Application.Views.Threadview extends Backbone.View
  el: '#threadview'

  initialize: ->
    @messages = @options.messages
    @render()

  render: ->
    console.log 'Rendering', @messages
    $(this.el).html(JST['threadview/threadview']({ messages: @messages }))
