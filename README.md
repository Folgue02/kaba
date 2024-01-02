# Kaba
Kaba is a cli program designed to organize tasks with the inspiration of kanban boards written in Java.

## 1. How to install

### 1.1 Linux

There is an installation script that will install it under the `/opt/kaba/` directory. Remember to do not run this as `root`, since the script will already do this for you.
```bash
./install.sh
```

### 1.2 Windows

For Windows on the other hand I haven't made an installation script yet, so the process would be manual.
```cmd
./gradlew installDist
```

This will compile and create a script to execute kaba which is located in `./app/build/install/app/bin/app.cmd`.

## 2. A usage guide

### 2.1 Startup
Starting Kaba requires the user to specify the type of storage that will be used by the program, do so by passing the `-t {storage-type}` as a command line argument (*i.e. `kaba -t json`*).

This will get the us into a prompt like the following:
```
(Json)DISCONNECTED>>> 
 ^    ^
 |    |
 |    Address of the storage (currently disconnected since no storage was previously loaded).
 | 
 Current storage type in use.
```

### 2.2 Creating a new storage
To create a new storage of the specified type (*the one specified at launch with the `-t` flag*) we'll use the `create` command and the desired address of the storage (*in the case of using the json storage type, this would be the path to the file*):
```
(Json)DISCONNECTED>>> create ~/my_new_kaba_board.kb
Creating a storage with address 'my_new_kaba_board.kb' of type 'Json'
What will be the ID string of the new board? KABA_BOARD <--- Id of the board.
(Json)DISCONNECTED>>> 
```

### 2.3 Loading a storage
Note that creating a storage doesn't load it. To do so, the `load` command will be used:
```
(Json)DISCONNECTED>>> load ~/my_new_kaba_board.kb
(Json)JSON:my_new_kaba_board.kb>>>
 ^    ^                       ^
 |    |-----------------------/
 |    |
 |    The display name of the storage, telling us that now,
 |    there is a board and storage loaded.
 | 
 Still, the type of storage in use.
```

### 2.4 Creating a new task

The command to create a new task is `newtask`, this command can either take two arguments, or none:

When passing two arguments to the command:
```
                                            Name of the task
                                            |
                                            |               Description of the task.
                                            |               |
                                            V               V
(Json)JSON:my_new_kaba_board.kb>>> newtask "My first task" "The description of my first task"
```

And this is what happens when executing the `newtask` command without any arguments passed:
```
(Json)JSON:my_new_kaba_board.kb>>> newtask
What's the name of the task? My first task                            <--- The name of the task
What's the description of the task? The description of my first task  <--- The description of that task
```

### 2.5 Listing tasks
If we wanted to see the stored tasks in the storage, we would have to use the `list` command which takes no arguments.
```
(Json)JSON:my_new_kaba_board.kb>>> list

     ╔═══════════════════════════════════════════════╗
     ║ ID: 0                                         ║
     ║ Name: My first task                           ║
     ║ Description: The description of my first task ║
     ║ State: Todo                                   ║
     ╚═══════════════════════════════════════════════╝

     ╔═══════════════════════════════════════════════╗
     ║ ID: 1                                         ║
     ║ Name: My first task                           ║
     ║ Description: The description of my first task ║
     ║ State: Todo                                   ║
     ╚═══════════════════════════════════════════════╝
```

Even if those two tasks are identically the same, they can co-exist, since the provided id they've got it's different (*every time a task is created, it gets a new ID, which would be the id of the last task created + 1*).

### 2.6 Removing tasks
As the last point has shown, there are two identical tasks, so it would be the right thing to do if we removed one of them.

The command `rmtask` is made to remove a task, which is identified by its identifier, that will be passed as an argument to the command.
```
                                          ID of the task to be removed.
                                          |
                                          V
(Json)JSON:my_new_kaba_board.kb>>> rmtask 0
Task removed.
```

### 2.7 Changing the state of a task
Let's imagine that we are going to start working on the first task (*the one with the ID `1`*), to mark it on the board, we'll have to use the `setstate` command, which takes two arguments, the id of the task, and it's new state.

As the new state of our task, we can pass one of the following three states:
- `todo` (*the task is in the todo list, but currently no one's working on it*)
- `inprogress` (*someone's working on the task*)
- `done` (*the task is done*)

```
                                            Id of the task that will be assigned the following state
                                            |
                                            | New state for the previously mentioned task
                                            | | 
                                            V V
(Json)JSON:my_new_kaba_board.kb>>> setstate 1 inprogress
(Json)JSON:my_new_kaba_board.kb>>> list

     ╔═══════════════════════════════════════════════╗
     ║ ID: 1                                         ║
     ║ Name: My first task                           ║
     ║ Description: The description of my first task ║
     ║ State: InProgress                             ║ <--- The state of the task '1' is now 'InProgress'
     ╚═══════════════════════════════════════════════╝
```

### 2.8 Saving the currently loaded board to the storage
Once everything it's done; it would make sense to save the modified board back to the storage we loaded it from, to do so, we'll use the `write` (*or `save`*) command.
```
(Json)JSON:my_new_kaba_board.kb>>> write
The board has been saved.
```

### 2.9 Unloading and quitting
Now that we've created a task, modified it, and saved it, we want to leave Kaba. To do so we would use the `quit` command, but this won't work since the stoarge is still loaded, as seen below:
```
(Json)JSON:my_new_kaba_board.kb>>> quit
There is a storage still loaded. Unload ('unload') it before quitting to avoid corruption.
```

As the output points out, we will unload the stoarge with the `unload` command, and then we'll use the quit command. But all of this can be done in one single line using '`;`' to separate commands, doing the following: 
```
(Json)JSON:my_new_kaba_board.kb>>> unload; quit
```

