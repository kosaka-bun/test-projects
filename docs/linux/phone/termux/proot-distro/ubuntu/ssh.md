# SSH

```shell
apt-get install openssh-server
vim /etc/ssh/sshd_config
```

编辑配置，允许root登录。

```
# 找到这一行
#PermitRootLogin prohibit-password

# 修改为
PermitRootLogin yes
```

```shell
# 修改root密码
passwd
```