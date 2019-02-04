How to commit:

```bash
cd .../PI
git pull origin master
git add .
git status
git commit -m "message"
git push origin master
```

To overwrite local changes with Github current version:

```bash
git fetch origin
git reset --hard origin/master
```

Timothee ```tasks.json```:

```
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "Main",
            "type": "shell",
            "command": "cd /home/utilisateur/Documents/polytechnique/2A/COURS_2A/2/INF421/PI/src && rm -f *.class && javac -cp .:./lib/* Main.java && java -ea -cp .:./lib/* Main",
            "group": {
                "kind": "build",
                "isDefault": true
            },
            "problemMatcher": []
        },
        
        {
            "label": "PointCloud",
            "type": "shell",
            "command": "cd /home/utilisateur/Documents/polytechnique/2A/COURS_2A/2/INF421/PI/src && rm -f *.class && javac -cp .:./lib/* PointCloudViewer.java && java -ea -cp .:./lib/* PointCloudViewer /home/utilisateur/Documents/polytechnique/2A/COURS_2A/2/INF421/PI/data/pointclouds/${input:pointCloud}.off",
            "group": {
                "kind": "build",
                "isDefault": true
            },
            "problemMatcher": []
        },

        {
            "label": "DrawGraph",
            "type": "shell",
            "command": "cd /home/utilisateur/Documents/polytechnique/2A/COURS_2A/2/INF421/PI/src && rm -f *.class && javac -cp .:./lib/* NetworkLayout.java && java -ea -cp .:./lib/* NetworkLayout /home/utilisateur/Documents/polytechnique/2A/COURS_2A/2/INF421/PI/data/networks/${input:network}.mtx",
            "group": {
                "kind": "build",
                "isDefault": true
            },
            "problemMatcher": []
        },

        {
            "label": "ComputeGraph",
            "type": "shell",
            "command": "cd /home/utilisateur/Documents/polytechnique/2A/COURS_2A/2/INF421/PI/src && rm -f *.class && javac -cp .:./lib/* GraphDrawingResults.java && java -ea -cp .:./lib/* GraphDrawingResults /home/utilisateur/Documents/polytechnique/2A/COURS_2A/2/INF421/PI/data/networks/${input:network}.mtx",
            "group": {
                "kind": "build",
                "isDefault": true
            },
            "problemMatcher": []
        },
    ],

    "inputs": [
        {
            "id": "network",
            "description": "Which network of points?",
            "default": "ash85",
            "type": "promptString"
        },

        {
            "id": "pointCloud",
            "description": "Which point cloud?",
            "default": "nefertiti",
            "type": "promptString"
        },
    ],
}
```