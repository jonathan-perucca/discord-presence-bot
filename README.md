# Bot commands

#### Start a session
```bash 
    !session:start          
```
_NB: Every currently connected user to any voice channel will be considered as part of the started session_

#### Stop a session
```bash        
    !session:stop
```

#### Presence list of current session

```bash
    !session:current
```
#### All presence list sessions
```bash
    !session:all      
```

#### Presence list of every users of every sessions since "from_date"

```bash
    !session:list from <from_date>
```

example: `!session:list from 2018-01-01`

### mandatory
* from `<from_date>`

#### Presence statistics of every users of every sessions since "from_date"

```bash
    !session:stats from <from_date> (csv)
```


#### examples

`!session:stats from 2018-01-01`

`!session:stats from 2018-01-01 csv`

#### mandatory
* from `<from_date>`

#### optional
* csv

from_date
- format: `yyyy-MM-dd`

(optional) csv
- provide report as csv file


#### Remove a user presence of a specific session
```bash
    !session:remove user <user_name> on <session_date>
```

#### examples

`!session:remove user under on 2018-02-10`

#### mandatory

* user <user_name> -- mandatory
* on <session_date> -- mandatory

user_name :
- can be found through `!session:all` or `!session:list` commands

session_date : 
- format: `yyyy-MM-dd`


#### Helper command
```
    !session:help
```

# Install

### Create a discord bot

Follow this tutorial : [How to register a discord bot](https://github.com/reactiflux/discord-irc/wiki/Creating-a-discord-bot-&-getting-a-token)

### Package from source
```bash 
    mvn package
```

#### Launch bot
```bash
    java -jar -Dspring.config.location=<path_to_conf_folder>/ discord-presence-bot-0.0.1-SNAPSHOT.jar
```

#### Launch configuration file

###### in your path_to_conf_folder, create application.properties with following variable to replace
```
    BOT_TOKEN=... // your bot token
    GUILD_NAME=... // the discord server name to monitor 
    bot.admin-names=... // comma-seperated values - discord user nicknames authorized to query the bot
    bot.session-time-seconds=1200 // minimal amount of seconds needed per user for validating a session
    cron.session.start=0 0 21 * * WED
    cron.session.stop=0 0 0 * * THU
```