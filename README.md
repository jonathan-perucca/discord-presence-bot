# Bot commands

#### Start a session
```bash 
    !session:start          
```

#### Stop a session
```bash        
    !session:stop
```


#### Report all presence list sessions
```bash
    !session:all      
```

#### Presence list of every users of every sessions since "from_date"

```bash
    !session:from <from_date>     
```

from_date
- format: `yyyy-MM-dd`

#### Presence statistics of every users of every sessions since "from_date"

```bash
    !session:stats from <from_date>     
```
TODO :
- set csv export as a parameter option

from_date
- format: `yyyy-MM-dd`

#### Remove a user presence of a specific session
```bash
    !session:remove user <user_name> on <session_date>
```
TODO :
- command implementation

user_name :
- can be found through `!session:all` or `!session:from` commands

session_date : 
- format: `yyyy-MM-dd`


#### Helper command
```
    !help
```
TODO :
- command implementation

# Automatic behavior

#### Session start each ...
```
    At application launch : 
    java -jar discord-presence-bot.jar \
        -Dcron.session.start= * * * * * *
        -Dcron.session.stop= * * * * * *
```
TODO : 
- parameters for cron.session.(start|stop)