# YuanShenServer

### Description

`YuanShenServer` is the Java-written-server of `YuanShen`. 

> *YuanShen, boot!*



### Feature

+ Main process and daemon process provided.   *Keep running for a long time!*
+ Customize settings and configs to adapt to different needs.   *You decide it!*
+ One-command deployment and **help** command support.   *User-friendly!*
+ Theoretically support multi-user connection.   *Not tested. I believe it can do it.*



### Dependency

+ `Java Runtime Environment` >= `64.0`
+ `MySQL` *Only tested on `8.0`*



### Deployment

> **IMPORTANT**
> + Before deployment, allow the port `YuanShenServer` listens (default 10005, you can set it by `-p PORT` ) to pass your firewall, or the server may be CRASHED by the infinite `while` loop.   *Will be fixed in later version.*
> 
> + Before deployment, create a database named `BUAAChat` in `MySQL`.

#### Debian

To automatically deploy `YuanShenServer` on your server, input the command below:

```
sudo wget https://github.com/BUAA-bond/BUAAChat/raw/feature-server/Server && chmod +x ./Server && ./Server
```

Note that first-time initiation downloads file from `Github`. While running, server `standard(debug)` and `error` output will be redirected to `./YuanShenServer.log`.

#### Windows

To ~~automatically~~ manually deploy `YuanShenServer` on your server or computer:

1. Download server file from this [link](https://github.com/BUAA-bond/BUAAChat/raw/feature-server/YuanShenServer.jar) (`Github`).
2. Execute command below in the directory where you place the file downloaded just now.

```
java -jar ./YuanShenServer.jar [options]
```

Note that output redirection and daemon are not supported **temporarily** on `Windows`.



### Usage and options

Server parameters are listed below. `./Server --help` may help you too.

```
Usage:
 ./Server [command] [<options>...]

Commands:
 start                      Start server
 stop                       Stop server and quit
 reload                     Stop server and restart

Options:
 -h, --help                 Print this page and quit
 -d, --debug                Debug mode, print additional messages
 -t TIME, --time TIME       Set server daemon retry time (unit: s) [default: 10s]
 -p PORT, --port PORT       Set the port server listens [default: 10005]
```



### Interface

> The server-client cooperation relies on `Message` object. See `src/com/BUAAChat/Message.java` for its class structure. The `Content` of a `Message` contains at least `code` and `data`, which are listed below.

#### Send by client

##### `1xx` User account related

| Code |      Type       |                  Data                   | Respond-Data |
| :--: | :-------------: | :-------------------------------------: | :----------: |
| 101  |      Login      |     `account`, `password`, `avatar`     | `null` if OK |
| 102  |    Register     | `account`, `password`, `name`, `avatar` | `null` if OK |
| 103  | Change password |       `account`, `password`(new)        | `null` if OK |
| 109  |     Logout      |                `account`                | `null` if OK |
| 199  | Delete account  |          `account`, `password`          | `null` if OK |

##### `2xx` Friend relationship related

> If user `A` want to add/accept/reject/block/remove user `B`.

| Code |                           Type                           |             Data             |             Respond-Data             |
| :--: | :------------------------------------------------------: | :--------------------------: | :--------------------------: |
| 201  | Send friend request | `account_A`(A), `account_B`(B) | `null` if OK |
| 202  |                  Accept friend request                  | `account_A`(A), `account_B`(B) | `null` if OK |
| 203  |                  Decline friend request                  | `account_A`(A), `account_B`(B) | `null` if OK |
| 204  |    Remove friend    | `account_A`(A), `account_B`(B) | `null` if OK |
| 205  |                   Block friend                   |         `account_A`(A), `account_B`(B)         |         `null` if OK         |
| 209 | Create new group | `uAccount`, `gAccount`, `gName`, `gAvatar` | `null` if OK |
| 210  |                    join group                    |    `uAccount`, `gAccount`    |    `null` if OK    |
| 211  |                        quit from group                        |    `uAccount`, `gAccount`    |    `null` if OK    |

##### `3xx` Config modification related

| Code |        Type        |          Data           | Respond-Data |
| :--: | :----------------: | :---------------------: | :----------: |
| 301  |  Modify user name  |  `account`,`name`(new)  | `null` if OK |
| 302  | Modify user avatar | `account`,`avator`(new) | `null` if OK |

##### `4xx` Get data

| Code |            Type            |             Data              |           Respond-Data            |
| :--: | :------------------------: | :---------------------------: | :-------------------------------: |
| 401  |       Get user info        | `uAccount`(user to get info)  | ` name `, ` account `, ` avatar ` |
| 402  |    Get all friends info    |   `uAccount`(current user)    |           ` friends[] `           |
| 403  |       Get group info       | `gAccount`(group to get info) |  `account`, `name`, `members[]`   |
| 404  |    Get all groups info     |   `uAccount`(current user)    |           ` groups[] `            |
| 405  | Get friend request history |   `uAccount`(current user)    |            `history[]`            |

##### `5xx` Send chat message

| Code |      Type       |          Data           | Respond-Data |
| :--: | :-------------: | :---------------------: | :----------: |
| 501  | Send plain text | `from`, `to`, `content` | `null` if OK |

#### Server respond

##### `9xxx` Server respond

> Server respond `code` remains same as the client `Message`.

| Respond-Code |          Type          | Respond-Status |   Respond-Data    |
| :----------: | :--------------------: | :------------: | :---------------: |
|   `$code$`   |           OK           |      9000      | `$requestedData$` |
|              |                        |                |                   |
|   `$code$`   |     User not exist     |      9101      |      `null`       |
|   `$code$`   | Account already exist  |      9102      |      `null`       |
|   `$code$`   |     Wrong password     |      9103      |      `null`       |
|   `$code$`   |  Invalid user status   |      9104      |      `null`       |
|              |                        |                |                   |
|   `$code$`   |   Parameter missing    |      9091      | `$missingParam$`  |
|   `$code$`   | Parameter format error |      9092      |  `$wrongParam$`   |
|   `$code$`   |    Parameter error     |      9093      |  `$wrongParam$`   |
|              |                        |                |                   |
|   `$code$`   | Server internal error  |      9999      |      `null`       |