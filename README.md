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
 
 ## TODO
  - Configs
  - Support for PostgreSQL, MySQL
  - Other kinds of login/register (2 factor auth, register from social networks)
