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
 
 ## Config
Full ingame localization provided in messages.yml  
In config.yml you can manage session expire time, checker period etc..
```yaml
# Auth server pool
auth:
  - limbo
  - limbo-1

# Pool of servers to connect after authorization
next:
  - instance

# Force teleport player to the next balanced server
force_next: false

# Server info update period in seconds
update_period: 30

# Player checker period
checker_period: 5000

# Non-authorized player lifetime before kick in seconds
kick_period: 30

# Session expire seconds
expire: 7200

# Debug information
# debug: false

# Database (type memo by default)
# db:
#  type: "jdbc"
#  url: "jdbc:postgresql://192.168.1.48/minecraft"
#  user: "minecraft"
#  pass: "heresthemftea"

# Balancers (me.theseems.tauth.balancers.SimpleBalancer by default)
# auth_balancer:
#  class: me.theseems.tauth.balancers.AsyncBalancer
#  settings:
# Delay in milliseconds
#    - 10000
# next_balancer:
#  class: me.theseems.tauth.balancers.SimpleBalancer

# You can change hashing algorithm here (me.theseems.tauth.hashers.SHA512AuthHasher by default)
# hasher:
#  class: me.theseems.tauth.hashers.SHA512AuthHasher
```
 
 ## Storage
 Currently, TAuth supports memory storage and JDBC (PostgreSQL driver provided by default)  
 For other JDBC-supporting databases you will need to extract driver to this plugin (.jar)  
 Or add it to dependencies of (bungee/pom.xml) and build project using
 ```bash
 mvn compile assembly:single
 ```
 
 ## TODO
  - Autologin licensed people
  - Other kinds of login/register (2 factor auth, register from social networks)
