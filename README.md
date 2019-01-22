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