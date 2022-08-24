# office文件格式转换demo工程

## 环境配置

需要安装libreOffice 下载地址：https://zh-cn.libreoffice.org/

安装参考[centos7安装libreOffice.md](centos7安装libreOffice.md)

## maven
`maven`核心依赖
```xml
        <dependency>
            <groupId>org.jodconverter</groupId>
            <artifactId>jodconverter-local</artifactId>
            <version>4.4.2</version>
        </dependency>
        <dependency>
            <groupId>org.jodconverter</groupId>
            <artifactId>jodconverter-spring-boot-starter</artifactId>
            <version>4.4.2</version>
        </dependency>
```
## 配置说明
```yml
jodconverter:
  local:
    # 是否开启jodconverter
    enabled: true
    # 代表office主目录。如果未设置，则会自动检测 Office 安装目录，需要是最新版本的 LibreOffice。
    officeHome: /opt/libreoffice7.1
    # 如果需要多个实例则填写多个端口，如启动两个实例，应填写两个端口：2004,2005
    port-numbers: 2004
    # 临时文件夹
    working-dir: /tmp
    # 格式设置
    format-options:
      html:
        store:
          TEXT:
            # 嵌入图片
            FilterOptions: EmbedImages
```
