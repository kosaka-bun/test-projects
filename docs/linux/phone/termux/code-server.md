# Code Server

```shell
pkg install tur-repo
pkg install code-server
```

[GitHub Issue](https://github.com/coder/code-server/issues/5745)

如需修改密码和绑定端口，请编辑`~/.config/code-server/config.yaml`。

## 安装中文语言包
直接从插件界面安装时，由于VS Code版本不匹配，可能导致安装失败。
可前往[商店](https://marketplace.visualstudio.com/_apis/public/gallery/publishers/MS-CEINTL/vsextensions/vscode-language-pack-zh-hans/1.72.10050946/vspackage)下载与code-server所使用的VS Code版本相匹配的插件，然后在插件界面，通过安装vsix包的方式进行离线安装。