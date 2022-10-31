# Termux安装sshd

```shell
pkg install vim openssl openssh
# 查看用户名，并设置密码
whoami
passwd
# 启动
sshd
```

## 设置sshd开机启动的脚本
```shell
cd ~/.termux
mkdir boot
cd boot
vim boot.sh
```
添加以下内容：
```shell
#!/data/data/com.termux/files/usr/bin/sh
termux-wake-lock
sshd
```
```shell
chmod +x boot.sh
```