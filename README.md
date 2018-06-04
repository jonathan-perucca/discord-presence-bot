# Bot commands

## !start & !stop

#### Start a session
```bash 
    !session:start          
```

#### Stop a session
```bash        
    !session:stop
```


## !session:list

#### Presence list of every users of every sessions since "from_date"

```bash
    !session:list from <from_date>
```

example: `!session:list from 2018-01-01`

### !session:list -- mandatory
* from `<from_date>`

## !session:all

#### All presence list sessions
```bash
    !session:all      
```

## !session:stats

#### Presence statistics of every users of every sessions since "from_date"

```bash
    !session:stats from <from_date> (csv)
```


#### !session:stats -- examples

`!session:stats from 2018-01-01`

`!session:stats from 2018-01-01 csv`

#### !session:stats -- mandatory
* from `<from_date>`

#### !session:stats -- optional
* csv

from_date
- format: `yyyy-MM-dd`

(optional) csv
- provide report as csv file

## !session:remove 

#### Remove a user presence of a specific session
```bash
    !session:remove user <user_name> on <session_date>
```
TODO :
- command implementation

#### !session:remove -- examples

`!session:remove user under on 2018-02-10`

#### !session:remove -- mandatory

* user <user_name> -- mandatory
* on <session_date> -- mandatory

user_name :
- can be found through `!session:all` or `!session:list` commands

session_date : 
- format: `yyyy-MM-dd`


#### Helper command
```
    !help
```
TODO :
- command implementation

# Install

### Package from source
```bash 
    mvn package
```

#### Launch bot
```bash
    java -jar -Dspring.config.location=<path_to_conf_folder>/ discord-presence-bot-0.0.1-SNAPSHOT.jar
```

#### Launch configuration file

###### application.properties
```
    BOT_TOKEN=...
    bot.admin-names=... // comma-seperated values
    cron.session.start=0 0 21 * * WED
    cron.session.stop=0 0 0 * * THU
```