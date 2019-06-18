# TAuth

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5263b39af61a4d40b5ff09463a684b72)](https://app.codacy.com/app/TheSeems/TAuth?utm_source=github.com&utm_medium=referral&utm_content=TheSeems/TAuth&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.com/TheSeems/TAuth.svg?branch=master)](https://travis-ci.com/TheSeems/TAuth)

Simple auth sysytem in minecraft

## Session system
If player logined in from one IP, he/she is able to enter the game from **the same IP**.  
Without logining for certain time interval

## Server balancer
This feature allows you to spread your players to auth lobbies and main lobbies efficiently

## API
Check if player is authenticated  
Login for a player  
Register for a player  
You can implement any part of api independently.  
For example, MongoDB storage support or even support of any proxy (implement AuthServer and add your listeners)

## Commands
 - /login <pass>
 - /register <pass> <repeat-pass>
 - /logout
 
## Storage
Currently, TAuth supports memory storage and JDBC (PostgreSQL works as well, tested)
 
 ## Config
Session expire time, checker period etc
Full ingame localization provided in config.
```yaml
# Auth server pool
auth:
  - limbo
  - limbo-1

# Pool of servers to connect after authorization
next:
  - instance

# Server info update period in seconds
update_period: 30

# Player checker period in milliseconds
checker_period: 5000

# Database. By default TAuth uses memory to store their data there
# WARNING: If you are using memo db type, many players may crash your server. Consider using database such as PostgreSQL

# db:
#  type: "postgres"
#  url: "jdbc:postgresql://localhost/minecraft"
#  user: "minecraft"
#  pass: "heresthemftea"

# Localization
messages:
  login:
    usage: '§eUsage§7: §e/login §7<pass>'
    titles:
      UNREGISTERED:
        title: §7Please, register
        subtitle: §e/register §7<pass> <pass>
        fade_in: 10
        fade_out: 10
        stay: 30
      EXPIRED:
        title: §7Please, login
        subtitle: §e/login §7<pass>
        fade_in: 10
        fade_out: 10
        stay: 30
      FORBIDDEN:
        title: §cWrong password!
        subtitle: ''
        fade_in: 10
        fade_out: 10
        stay: 30
      OK:
        title: §aYou are logged in
        subtitle: ''
        fade_in: 10
        fade_out: 10
        stay: 30
  register:
    titles:
      OK:
        title: §aYou are registered
        subtitle: ''
        fade_in: 10
        fade_out: 10
        stay: 30
    usage: "§eUsage§7 §e/register §7<pass> <repeat-pass>"
    no_match: "§7Passwords §cdont match"
  kick:
    timed_out: §c§lYou have run out of time to log in

# Session expire seconds
expire: 7200

# Non-authorized player lifetime before kick in seconds
kick_period: 30
```
 
 ## TODO
  - Other kinds of login/register (2 factor auth, register from social networks)
