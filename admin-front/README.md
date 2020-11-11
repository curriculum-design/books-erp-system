# 如何运行
### 1. 安装nodejs
下载地址: https://nodejs.org/, 选择当前最新`LTS`版本进行安装

> 安装成功后，运行 `npm -v 或者 node -v 查看当前版本`

### 2. 设置 npm 仓库镜像
运行 `npm config set registry https://registry.npm.taobao.org` 设置淘宝镜像（提升国内下载包速度）

配置成功后， 可通过运行 `npm config get registry` 验证是否运行成功 （执行该命令会输出 `https://registry.npm.taobao.org`)

### 3. 安装依赖包
在当前项目根目录下运行 `npm i`， 下载包依赖需要一定的时间

### 4. 运行项目
> 项目默认的接口配置文件在 `config/profile-default.js` ， 此目录指向的默认是本地环境，不要随便改，如果自己有需要转发到其他路径，复制该文件，并且重命名为 `profile-xxxxxx.js` xxxxx替换为你定义的名称

运行项目命令格式为 `npm run dev projectname... [profile=xxxxx]`;

例如 `npm run dev channel system profile=zhouchao` , 该命令会运行 渠道版和系统版的后台，并且使用 profile-zhouchao.js 这个配置文件

# 前端开发
* 代码生成器 --`npm run gen <table>` 
