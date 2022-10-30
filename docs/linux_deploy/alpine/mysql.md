# Linux Deploy下的Alpine Linux安装MySQL

```shell
# 安装
apk add --no-cache mysql mysql-client
# 初始化
mysql_install_db --user=mysql --datadir=/var/lib/mysql
# 修改配置文件
# 1.注释掉 skip-networking
# 2.在[mysqld]一栏中填写"user=root"（不包含引号）
vi /etc/my.cnf.d/mariadb-server.cnf
# 解决Can't start server : Bind on unix socket: No such file or directory
mkdir /var/run/mysqld
# 启动mysqld
mysqld &
# 设置root密码
mysqladmin -u root password "root"
# 进入mysql控制台
mysql -uroot
```

```sql
# 允许root用户远程登录
grant all privileges on *.* to 'root'@'%' identified by 'root' with grant option;
# 刷新权限
flush privileges;
```