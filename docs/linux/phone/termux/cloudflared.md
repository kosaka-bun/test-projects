# Cloudflared

## 安装
```shell
pkg install cloudflared
cloudflared -h
```

## 配置
```shell
# 登录
cloudflared tunnel login
# 创建隧道，“phone”为隧道名
cloudflared tunnel create phone
# 创建配置文件
vi ~/.cloudflared/config.yml
```
按照以下格式填写配置文件：
```yaml
# tunnel: 036cc290-03fb-47f6-a009-f7b068f93bf6    # 隧道的UUID
# credentials-file: /data/data/com.termux/files/home/.cloudflared/036cc290-03fb-47f6-a009-f7b068f93bf6.json    # 创建隧道时产生的json文件所在目录，必须是绝对路径
tunnel: xxx
credentials-file: /data/data/com.termux/files/home/.cloudflared/xxx.json
protocol: http2
originRequest:
  connectTimeout: 30s
  noTLSVerify: false

# 入站规则
ingress:
  # 将哪个域名穿透到内网的哪个端口
  - hostname: code-server.honoka.de
    service: http://localhost:8080
  # 所有规则不匹配时，访问哪个服务
  - service: http_status:404
```
```shell
# 为域名添加dns记录，多个域名就进行多次添加
# 这里的域名，一定不能是四级子域名！否则浏览器访问时，报告“不支持的TLS版本”错误！
cloudflared tunnel route dns phone xxx.honoka.de
# 运行，测试
cloudflared tunnel run phone
```