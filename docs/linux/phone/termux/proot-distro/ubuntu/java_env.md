# Java运行环境

## freetype
```shell
apt-get install libfreetype6 libfreetype6-dev
```

## 中文字体
```shell
# 使mkfontscale和mkfontdir命令正常运行
apt-get install ttf-mscorefonts-installer
# 使fc-cache命令正常运行
apt-get install fontconfig
# 拉取字体文件
cd /root/.fonts
# 如果没有这个目录，请创建，然后进入该目录执行：
# mkfontscale
# mkfontdir
rz
# 更新缓存
fc-cache -fv 
# 查看是否安装成功
fc-list :lang=zh
```

## libawt_xawt.so所在的库
```shell
apt-get install libxtst-dev libxrender-dev
```