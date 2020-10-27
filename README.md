# Galcon 2 #

This game is a space war game.

![Galcon2 Snapshot](Capture.png?raw=true "snapshot")

As you can see, the design part is not finished yet.

## Goal 

Conquer the space by sending vessels on ennemie's planets. Once a planet has 0 HP, the next vessel that come on it conquere it.  

## How to play

The basic game just include a 1v1 gamemode, that can be versus a friend or versus the computer. 

The advanced mode include a 1v1v1v1 game mode, a 1v1v1 mode, that can be versus an IA or a friend, then strong vessels. 

To play you have to choose a planet color, then drag and drop from your planet to another one. If the target planet is one of yours, you'll just add vessels on it, otherwise you'll attack it. 

If you want to use strong vessels, then right clicked on your planet before sending vessels. You'll see a black outline outside your planet, that mean that you are in strong mode for this planet. Of course, your planet produce much less strong vessels than normal vessels. 

Dont forget to choose the pourcentage your reseve that you want to send.

## How to compile and execute 

First of all, of course, you'll need java and javac installed on your computer. 

After that, you have to install javafx : 

```
  sudo apt install openjfx
```

then you can compile from the game mode folder than you want to play (advanced or basic) :

```
  javac -classpath /usr/share/openjfx/lib/ --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml *.java

```

then you can execute :

```
  /usr/lib/jvm/java-13-openjdk-amd64/bin/java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -Dfile.encoding=UTF-8 -classpath /PATH/TO/THE/CLONE/YOU/DID/bin:/PATH/TO/THE/CLONE/YOU/DID/jfxrt.jar:/usr/share/openjfx/lib/javafx.base.jar Game

```
