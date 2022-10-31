# Termux Ubuntu修改时区

```shell
apt-get update
apt-get install tzdata
cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
```