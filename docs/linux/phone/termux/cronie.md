# Cron定时任务服务

```shell
pkg install cronie
# 查看cron任务列表
crontab -l
```

安装完成后，`/data/data/com.termux/files/usr/var/spool/cron`目录下会有一个与当前登录用户的用户名相同的文件，可在其中编辑cron任务列表，遵循crontab文件的格式。