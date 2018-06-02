# Bot commands

#### Start a session
```bash 
    !start          
```

#### Stop a session
```bash        
    !stop
```


#### Report last presence list session
```bash
    !read:last      
```
TODO : 
- set csv export as a parameter option

#### Report since past date the presence list session

```bash
    !read:from <from_date>     
```
TODO :
- set csv export as a parameter option

from_date
- format: `yyyy-MM-dd`

#### Remove a user presence of a specific session
```bash
    !remove <user_name> <session_date>
```
TODO :
- command implementation

user_name :
- can be found through !read section commands
session_date : 
- format: `yyyy-MM-dd`


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