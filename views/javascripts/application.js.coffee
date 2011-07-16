Application = {
    Views: {},
    Controllers: {},
    Models: {},
    Collections: {},
    Config: {},

    Contacts: undefined,
    Threads: undefined,
    init: (config) ->
      @buildUi()
      Application.Config = config
      Application.Contacts = new Application.Collections.Contacts()
      Application.Threads = new Application.Collections.Threads()
      new Application.Controllers.Dashboard()
      Backbone.history.start()

    buildUi: ->
      uki({
        view: 'HSplitPane',
        rect: '1000 1000',
        anchors: 'left top right bottom',
        handlePosition: 400,
        handleWidth: 1,
        leftMin: 350,
        rightMin: 200,
        leftChildViews: [
          {
            view: 'Box',
            rect: '0 0 75 1000',
            anchors: 'top left bottom',
            background: '#252525'
          },
          {
            view: 'ScrollPane',
            rect: '75 0 325 1000',
            anchors: 'left top bottom right',
            background: '#fafafa',
            childViews: [
              {
                view: 'List',
                id: 'threadList',
                rect: '325 1000',
                anchors: 'top left bottom right',
                textSelectable: false,
                rowHeight: 50
              },
              {
                view: 'List',
                id: 'contactsList',
                rect: '325 1000',
                anchors: 'top left bottom right',
                textSelectable: false,
                rowHeight: 50,
                visible: false
              }
            ]
          }
        ],
        rightChildViews: [
          {
            view: 'Box',
            rect: '0 0 599 1000',
            anchors: 'top left right bottom',
            childViews: [
              {
                view: 'ScrollPane',
                rect: '0 0 599 930',
                anchors: 'top left right bottom',
                childViews: [
                  {
                    view: 'List',
                    id: 'threadView',
                    rect: '599 530',
                    anchors: 'top left bottom right',
                    rowHeight: '50',
                    textSelectable: true
                  }
                ]
              },
              {
                view: 'Box',
                rect: '0 930 599 70',
                anchors: 'left right bottom',
                background: '#eee',
                childViews: [
                  {
                    view: 'MultilineTextField',
                    rect: '5 5 510 60',
                    anchors: 'left right bottom',
                    id: 'composeBody'
                  },
                  {
                    view: 'Button',
                    rect: '520 41 75 24',
                    text: 'Send',
                    anchors: 'right bottom',
                    id: 'sendButton'
                  },
                  {
                    view: 'Label',
                    rect: '520 18 75 24',
                    anchors: 'right bottom',
                    text: '160 / 1',
                    id: 'composeLength'
                  }
                ]
              }
            ]
          }
        ]
      }).attachTo(window, '1000 1000')
}
