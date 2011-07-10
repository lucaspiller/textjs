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
        handlePosition: 249,
        handleWidth: 1,
        leftMin: 249,
        rightMin: 200,
        leftChildViews: [
          {
            view: 'Box',
            rect: '0 0 249 30',
            anchors: 'top left right',
            background: '#000',
            childViews: [
              {
                 view: 'TextField',
                 rect: '5 5 239 20',
                 anchors: 'top left right',
                 placeholder: 'Search'
              }
            ]
          },
          {
            view: 'ScrollPane',
            rect: '0 30 249 970',
            anchors: 'left top bottom right',
            background: '#fff',
            childViews: [
              {
                view: 'List',
                id: 'contactsList',
                rect: '249 970',
                anchors: 'top left bottom right',
                rowHeight: '30',
                textSelectable: false
              }
            ]
          }
        ],
        rightChildViews: [{
            view: 'VSplitPane',
            handlePosition: 600,
            handleWidth: 1,
            topMin: 200,
            bottomMin: 100,
            rect: '0 0 750 1000',
            anchors: 'top left right bottom',
            topChildViews: [
              {
                view: 'Box',
                rect: '0 0 750 600',
                anchors: 'top left right bottom',
                childViews: [
                  {
                    view: 'ScrollPane',
                    rect: '0 0 750 530',
                    anchors: 'top left right bottom',
                    childViews: [
                      {
                        view: 'List',
                        id: 'threadView',
                        rect: '750 530',
                        anchors: 'top left bottom right',
                        rowHeight: '50',
                        textSelectable: true
                      }
                    ]
                  },
                  {
                    view: 'Box',
                    rect: '0 530 750 70',
                    anchors: 'left right bottom',
                    background: '#eee',
                    childViews: [
                      {
                        view: 'MultilineTextField',
                        rect: '5 5 660 60',
                        anchors: 'left right bottom',
                        id: 'composeBody'
                      },
                      {
                        view: 'Button',
                        rect: '670 41 75 24',
                        text: 'Send',
                        anchors: 'right bottom',
                        id: 'sendButton'
                      },
                      {
                        view: 'Label',
                        rect: '670 18 75 24',
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
                rect: '0 0 750 399',
                anchors: 'top left right bottom',
                childViews: [
                  {
                    view: 'List',
                    id: 'threadList',
                    rect: '750 399',
                    anchors: 'top left bottom right',
                    rowHeight: '50',
                    textSelectable: false
                  }
                ]
              }
            ]
        }]
      }).attachTo(window, '1000 1000')
}
