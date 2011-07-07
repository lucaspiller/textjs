class Application.Views.Threadview extends Backbone.View
  el: '#threadview'

  initialize: ->
    @messages = @options.messages
    @render()

  render: ->
    $(this.el).html(JST['threadview/threadview']({ messages: @messages }))
