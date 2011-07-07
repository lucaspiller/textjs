class Application.Views.Threadlist extends Backbone.View
  el: '#threads'

  initialize: ->
    @threads = @options.threads
    @render()

  render: ->
    $(this.el).html(JST.threadlist({ threads: @threads }))
