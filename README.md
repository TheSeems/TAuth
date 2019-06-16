# TAuth
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
 ```yaml
# Auth server pool
auth:
  - limbo

# Next server pool (remove to disable)
next:
  - instance

# 2 hours
expire: 7200000

# 5 seconds for bungee checker
checker_period: 5000
```
 
 ## TODO
  - Localization (custom messages)
  - Support for PostgreSQL, MySQL
  - Other kinds of login/register (2 factor auth, register from social networks)
