# Auto-Clicker and Screen-Capturer

It is a program made to experiment with Android's adb commands.

## Purpose
The intention of the app is to create a helper for my mom to use a company app more easily, by doing the monitoring and clicking by the app, instead of human, hoping that the app will run faster than human nerve system (~200 ms). However, certain commands that do the core functions are too slow (~3000 ms for screen capture and ~600 ms for clicking). So the project is abandoned due to this performance issue.

## Mechanism
The mechanism of the program is as follows:

- monitor the screen at a constant rate by taking screenshots using  
  `/system/bin/screencap -p /sdcard/autoClicker/img.png`
- then examinate a certain pixel of the taken screenshot. 
- If that very pixel is the desired color, then do a rapid click at a certain point using  
  `/system/bin/input tap x y` (where x y being the coordinates).

Even the idea of making this app into an auto-clicker is cannot be implimented due to the performance issue mentioned above.

Here's a screenshot of the app:
![screencap](https://raw.githubusercontent.com/AlicJ/android-auto-clicker/master/img.png "screencap")

## What I learnt
However, much new knowledge are gained through this project:

-  this code can be used to run adb shell commands on a rooted android device:
  `Process sh = Runtime.getRuntime().exec("su", null, null);`  
  `OutputStream os = sh.getOutputStream();`  
  `os.write((/* command */).getBytes("ASCII"));`  
  `os.flush();`  
  `os.close();`  
  `sh.waitFor();`
- mechanism behind floating window

-  how to include .jar file/library in Android Studio:

```
android {
...
}

dependencies {
    compile fileTree(dir: 'libs', include: '*.jar')
}
```

- also this app prompted me to start learning android from coursera
