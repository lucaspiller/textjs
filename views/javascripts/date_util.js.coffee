Date::timeAgoInWords = ->
  hours = @getHours()
  if hours < 10
    hours = '0' + hours
  minutes = @getMinutes()
  if minutes < 10
    minutes = '0' + minutes

  secondDiff = (new Date().getTime() - @getTime()) / 1000
  minDiff = Math.floor(secondDiff / 60)
  hourDiff = Math.floor(minDiff / 60)
  dayDiff = Math.floor(hourDiff / 24)

  days = [
    'Sunday', 'Monday', 'Tuesday',
    'Wednedsay', 'Thursday', 'Friday',
    'Saturday'
  ]

  if dayDiff < 1
    return hours + ':' + minutes
  else if dayDiff < 7
    day = @getDay()
    return days[day]

  date = @getDate()
  if date < 10
    date = '0' + date
  month = @getMonth()
  if month < 10
    month = '0' + month
  year = @getFullYear() - 2000

  return date + '/' + month + '/' + year
