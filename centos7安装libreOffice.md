# centos7安装libreOffice7.1

**官网：**https://zh-cn.libreoffice.org/

**国内下载镜像**：https://mirrors.cloud.tencent.com/libreoffice/libreoffice/stable/



## 安装

安装之前确保已经安装jdk

### 下载rpm文件

 ```sh
[root@localhost ~]# wget https://mirrors.cloud.tencent.com/libreoffice/libreoffice/stable/7.1.4/rpm/x86_64/LibreOffice_7.1.4_Linux_x86-64__rpm.tar.gz
[root@localhost ~]# wget  https://mirrors.cloud.tencent.com/libreoffice/libreoffice/stable/7.1.4/rpm/x86_64/LibreOffice_7.1.4_Linux_x86-64_rpm_langpack_zh-CN.tar.gz
[root@localhost ~]# wget  https://mirrors.cloud.tencent.com/libreoffice/libreoffice/stable/7.1.4/rpm/x86_64/LibreOffice_7.1.4_Linux_x86-64_rpm_sdk.tar.gz

 ```

#### 解压rpm文件

```sh
[root@localhost ~]# mkdir /usr/libreoffice
[root@localhost ~]# tar zxf LibreOffice_7.1.4_Linux_x86-64_rpm.tar.gz -C /usr/libreoffice/
[root@localhost ~]# tar zxf LibreOffice_7.1.4_Linux_x86-64_rpm_sdk.tar.gz -C /usr/libreoffice/
[root@localhost ~]# tar zxf LibreOffice_7.1.4_Linux_x86-64_rpm_langpack_zh-CN.tar.gz -C /usr/libreoffice/
[root@localhost ~]# ls /usr/libreoffice/
LibreOffice_7.1.4.2_Linux_x86-64_rpm  LibreOffice_7.1.4.2_Linux_x86-64_rpm_langpack_zh-CN  LibreOffice_7.1.4.2_Linux_x86-64_rpm_sdk
```

### 安装rpm文件

上面三个文件解压之后 每个里面都会有一个RPMS的文件夹，分别进入每个文件夹

```sh
[root@localhost ~]# cd /usr/libreoffice/
[root@localhost libreoffice]# cd LibreOffice_7.1.4.2_Linux_x86-64_rpm/RPMS/
[root@localhost RPMS]#yum localinstall *.rpm
[root@localhost libreoffice]# cd ..
[root@localhost libreoffice]# cd LibreOffice_7.1.4.2_Linux_x86-64_rpm_langpack_zh-CN/RPMS/
[root@localhost RPMS]# yum localinstall *.rpm
[root@localhost libreoffice]# cd ..
[root@localhost libreoffice]# cd LibreOffice_7.1.4.2_Linux_x86-64_rpm_sdk/RPMS/
[root@localhost RPMS]# yum localinstall *.rpm
[root@localhost libreoffice]# cd
```

用yum来进行rpm的安装，不要用rpm命令来进行安装

注意：因为有依赖关系 libgnomevfs-2.so.0()(64bit)，它被软件包 libobasis5.0-gnome-integration-5.0.4.2-2.x86_64 需要所以不要使用rpm命令来进行安装，rpm -ivh  *.rpm 命令无法解决上面的依赖系。使用yum遇到上面的依赖关系的时候可以从网络下载相应的包来解决依赖关系。

### 解决转换pdf之后文档字体乱码问题

先创建一个中文字体文件夹

```
[root@localhost ~]# yum groupinstall "fonts"  ##安装字体识别目录
[root@localhost ~]# cd /usr/share/fonts/   ##存放字体路径
[root@localhost ~]# mkdir chinese/      ##创建中文目录
[root@localhost ~]# cd chinese/
```

把windows中字体复制到上面创建的文件夹，windows的字体文件夹：`C:\Windows\Fonts`

给字体目录及所有字体可读权限

```sh
[root@localhost ~]# chmod -R 755 /usr/share/fonts/chinese/
```

修改字体的识别的配置文件

```shell
[root@localhost ~]# vi /etc/fonts/fonts.conf
#找到 Font directory list  字体列表，将目录改成/chinese下 
<dir>/usr/share/fonts/chinese</dir> ##更改后
:wq 保存并推出
[root@localhost ~]# fc-cache
[root@localhost ~]# fc-list
```

### 快捷命令

```shell
[root@localhost ~]# vim /etc/profile
##在最后添加两行；
export LibreOffice_PATH=/opt/libreoffice6.2/program
export PATH=$LibreOffice_PATH:$PATH
:wq 保存并退出
[root@localhost ~]# source /etc/profile                                                 ##重读环境变量
#这时命令就可以用了，命令格式如下
[root@localhost ~]# soffice --headless --convert-to 目标格式（如pdf） 被转换位置（如/tmp/test.doc） --outdir 转换之后文件生成位置
#例：
[root@localhost ~]# soffice --headless --convert-to pdf /root/test1.docx --outdir /opt/abc/
```

## 关于centos7安装libreoffice遇到的问题

到` /opt/libreoffice6.0/program/ `文件下执行

```sh
soffice -help
```

### 报错一

```shell
/opt/libreoffice7.1/program/soffice.bin: error while loading shared libraries: libcairo.so.2: cannot open shared object file: No such file or directory
```

解决方法：

执行命令下面命令

```sh
yum install cairo
```

### 报错二

```shell
/opt/libreoffice7.1/program/soffice.bin: error while loading shared libraries: libcups.so.2: cannot open shared object file: No such file or directory0
```

解决方法：

执行命令下面命令

```sh
yum install cups-libs
```

### 报错三

```shell
/opt/libreoffice7.1/program/soffice.bin: error while loading shared libraries: libSM.so.6: cannot open shared object file: No such file or directory
```

解决方法：

执行命令下面命令

```shell
yum install libSM
```



## 参考资料

Centos7下的LibreOffice的搭建及自动化脚本部署 https://blog.csdn.net/weixin_44691065/article/details/91888726

关于centos7安装libreoffice遇到的问题 https://blog.csdn.net/xujingcheng123/article/details/84636750
