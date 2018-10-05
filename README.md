# GoMoKu Online Multiplayer and AI Game

Thos Gomoku App was mad by some friends and me for a class at Pacific Lutheran University. The application was made in 4 two week long sprints with one week between each sprint for presentations. This application has many bugs due to the professor giving a project that was bigger than possible to accomplish in the time given, his words. This was to teach us what main use cases were and what to get done first to show the client at demo time. For example, we could have spent more time on login in and tested more on Mac, but we were able to show the client how stats show up instead. We had to prioritize our use cases and make the most of out time. I hope to add onto it in the future by fixing the the log in system on Macs, changing the board look and feel, changing the GUIs from NetBeans' packages, and other maintenance. Also, CashNCarry was the name of our group, so that is why things are named that way! It came from two different mottos of "Ca$h Money" and "Carry da Team".

# Getting Started

## Step 1:  Download the Dist Folder and run the Server
To get started, download the dist folder and run the server. One of the bugs of this application is that the server must always run to even start the application. If you are running both the server and the client from the same computer, the give files will be fine. However, if you want to play on the network, you will have to dig into the constants class in the client and give the IP address of ther server there and rebuild. We planed on putting that option into the GUI, but ran out of time in the class. 

## Step 2: Run the Client: Login/Register
If you want to play multiplayer you need to login. If you want to play AI skip to the AI section of this document. You will be greeted with a login in page. If you do not have a login yet, press the register button, put in a username and password, press register, and then login with those credentials. If you don't want a login and just want to play anonymously, press the guest button and you will enter the lobby.

![alt text](https://i.imgur.com/djzgwR0.png "Login Screen and Server")

## Step 3: Lobby Navigation
Once you are in the lobby you will see three main columns. The column to your left is a list of all the players that are online on the server that you are connected to. The middle column is a list of the stats of the player that you highlight from the left/online list (See picture below). The right column is a list of invites that are pending for you, click on one and press accept to be put into a game with that person.


![alt text](https://i.imgur.com/cR76URD.png "Lobby")

## Step 4: Invite
While in the lobby you will want to invite someone to a game. To invite someone you click on their name in the online list and press invite. An invite will appear in their invite list and they will be able to accept or decline the invite. If they declien the invite you will be able to send another one, but you will not be able to send more than one invite at a time to a single person. However, you can send multiple invites at once to multiple people. Once someone accepts your invite or you accept theirs, all your invites are deleted because you are in a game.


![alt text](https://i.imgur.com/x50G86p.png "Server and Invites")

## Step 5: Play!
Once in a game, play! The rules are very similar to Tic-Tac-Toe except that it is 5 in a row rather than three. You and your opponent will have one minute each alternating turns to place a piece on the board to try and get 5 in a row. If time runs out, the other player wins! You can not exit the game with the exit button so that the other player cannot cheat and quit right before they lose. So if they do want to lose, they have to sit for one minute and let the time tic down. After a game is over, the users are put back into the lobby to start inviting other players! The loser will get a +1 in their losses and the winner will get a +1 in their wins.


![alt text](https://i.imgur.com/uDGGHDb.png "Play!")

# AI Gameplay

We highly recommend trying to play against some of the AI. To start a game, run the server (We know this is not preffered, but we ran out of time to fix some issues with it), and in the drop down menu choose the type of AI you want to play against. Note that playing an AI game will not update your stats.


![alt text](https://i.imgur.com/GXkDPVV.png "AI!")

## Easy AI

This AI is truly easy and will not put up a fight. It is design to simply put a random piece in the 8 pieces around the piece you placed. It is trivial and anyone can win!

## Medium AI

Medium AI is a little harder because it will try and block once you have 4 pieces down. It will step through the board and it will block the piece that will give you 5 in a row. However, it plays one step behind and is still fairly easy to beat, it is medium, not too difficult, but more difficult than easy. One more thing to mention is that while there is not 4 in a row on your board, it will use the easy tactic of placing a piece near the one you just placed, so it may seem like easy, until you lay down 4 pieces.

## Hard AI

The hard AI will give anyone a challenging time. It attempts to think two steps ahead of the user so it will block one side at 3 in a row and then it will block the other side at 4 in a row. It is not perfect and there are ways to beat it, but it can be quite frustrating to wear it down. Once again, it uses the easy tactic when there isn't 3 or 4 in a row to place a piece near the piece you placed.

## Evil AI

Evil AI sprouted from a mistake in our algorithm that made it impossible to beat. We won't give away any secrets, you must try it to believe it.

## Authors

* **Isaiah Scheel** -  [Isaiaher](https://github.com/Isaiaher)
* **Nick Wagner** - No public GitHub
* **Jacob Rohweder** - No public GitHub
* **Robin Naggi** - No public GitHub


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

I would like to thank Dr. Hauser at Pacific Lutheran University for assigning this project and giving us the tools to get it done.
