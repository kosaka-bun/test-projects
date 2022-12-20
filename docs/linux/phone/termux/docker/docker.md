# Docker

```shell
pkg install root-repo
pkg install docker
```

为dockerd编写一个启动脚本，内容如下：
```shell
sudo mount -t tmpfs -o uid=0,gid=0,mode=0755 cgroup /sys/fs/cgroup
sudo mkdir $HOME/logs
sudo nohup dockerd --iptables=false > $HOME/logs/dockerd.log 2>&1 &
```

测试docker的功能是否正常。

需要注意的是，Termux中的docker并不支持网桥模式，端口映射在这里将会失败，使用Termux中的docker启动容器时必须指定使用宿主机的网络。

[https://ivonblog.com/posts/run-docker-natively-on-android/](https://ivonblog.com/posts/run-docker-natively-on-android/)
```shell
tsu
docker run --name hw hello-world
docker run -d --name nginx --net=host nginx
```
