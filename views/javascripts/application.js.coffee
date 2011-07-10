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
        rect: '800 600',
        anchors: 'left top right bottom',
        handlePosition: 260,
        handleWidth: 1,
        leftMin: 260,
        rightMin: 200,
        leftChildViews: [
          {
            view: 'Box',
            rect: '0 0 260 30',
            anchors: 'top left right',
            background: '#000',
            childViews: [
              {
                 view: 'TextField',
                 rect: '5 5 250 20',
                 anchors: 'top left right',
                 placeholder: 'Search'
              }
            ]
          },
          {
            view: 'ScrollPane',
            rect: '0 30 260 570',
            anchors: 'left top bottom right',
            background: '#fff',
            childViews: [
              {
                view: 'List',
                id: 'contactsList',
                rect: '260 570',
                anchors: 'top left bottom right',
                rowHeight: '30',
                textSelectable: false
              }
            ]
          }
        ],
        rightChildViews: [{
            view: 'VSplitPane',
            handlePosition: 400,
            handleWidth: 1,
            topMin: 200,
            bottomMin: 100,
            rect: '0 0 539 600',
            anchors: 'top left right bottom',
            topChildViews: [
              {
                view: 'Box',
                rect: '0 0 539 400',
                anchors: 'top left right bottom',
                childViews: [
                  {
                    view: 'ScrollPane',
                    rect: '0 0 539 330',
                    anchors: 'top left right bottom',
                    childViews: [
                      {
                        view: 'List',
                        id: 'threadView',
                        rect: '539 330',
                        anchors: 'top left bottom right',
                        rowHeight: '50',
                        textSelectable: true
                      }
                    ]
                  },
                  {
                    view: 'Box',
                    rect: '0 330 539 70',
                    anchors: 'left right bottom',
                    background: '#eee',
                    childViews: [
                      {
                        view: 'MultilineTextField',
                        rect: '5 5 449 60',
                        anchors: 'left right bottom',
                        id: 'composeBody'
                      },
                      {
                        view: 'Button',
                        rect: '459 41 75 24',
                        text: 'Send',
                        anchors: 'right bottom',
                        id: 'sendButton'
                      },
                      {
                        view: 'Label',
                        rect: '459 18 75 24',
                        anchors: 'right bottom',
                        text: '160 / 1',
                        id: 'composeLength'
                      }
                    ]
                  }
                ]
              }
            ],
            bottomChildViews: [
              {
                view: 'ScrollPane',
                rect: '0 0 539 199',
                anchors: 'top left right bottom',
                childViews: [
                  {
                    view: 'List',
                    id: 'threadList',
                    rect: '539 199',
                    anchors: 'top left bottom right',
                    rowHeight: '50',
                    textSelectable: false
                  }
                ]
              }
            ]
        }]
      }).attachTo( window, '800 600')
}
