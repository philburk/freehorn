## Creating a Local Repository ##

Follow the instructions here to check out FreeHorn for the **first** time.

https://code.google.com/p/freehorn/source/checkout

All subsequent commands should be done after CDing into the freehorn folder that is creted.

## Basic Checkout, Modify, Checkin ##

After the initial checkout, you can update your local repository using:
```
    git pull --rebase
```

Make your changes to the code.

To see what you have changed:
```
    git diff
```

To see what files have been modified:
```
    git status
```

If you see that the "bin" folder created by Eclipse is going to be added then you should close Eclipse then remove it.
```
    rm -rf bin
```

To add your modified files:
```
    git add .
```

To commit your changes to the local repository:
```
    git commit -s
```

To merge your changes with any the latest changes:
```
    git pull --rebase
```

To add any recent changes to the existing commit:
```
    git add .
    git commit --amend
```

To push your changes to the shared Google repository:
```
    git push origin master
```

## Working in a Branch ##

You can make multiple branches in your local repository. This allow you to be working on several independent changes at the same time. You might, for example, be improving the synthesizer in one branch and fixing a GUI bug in another branch. You can commit changes from either branch at any time.

To create a branch called "fix\_gui". Also make it the current branch.
```
    git checkout -b fix
    git status
```

In order to synchronize your branch with the shared repository, you can update the master branch then rebase your custom branch on top of it.

First commit your current changes, then:
```
    git checkout master
    git pull --rebase
    git checkout fix_gui
    git rebase master
```