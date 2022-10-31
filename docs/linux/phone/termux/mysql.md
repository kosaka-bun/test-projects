# Termux安装MySQL

```shell
pkg install mariadb
mysqld &
mysql -uroot
```
```sql
# 假设密码为root
use mysql;
set password for 'root'@'localhost' = password('root');
# 允许root用户远程登录
grant all privileges on *.* to 'root'@'%' identified by 'root' with grant option;
# 刷新权限
flush privileges;
quit;
```
```shell
sudo killall -TERM mysqld
mysqld &
```
通过数据库连接工具查看能否连接成功。