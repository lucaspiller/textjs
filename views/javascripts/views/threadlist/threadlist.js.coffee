class Application.Views.Threadlist extends Backbone.View
  initialize: ->
    @threads = @options.threads
    @render()

  render: ->
    $(this.el).html(JST.threadlist({ threads: @threads }))
    $('body').html(this.el)
