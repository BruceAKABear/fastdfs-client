# fastdfs java客户端
**概述:** 在开发中难免会用到文件存储，在众多的文件系统中个人对fastdfs情有独钟，但是鉴于
官方提供的java客户端及其的难使用，以及存在的线程安全性等问题，最终个人决定对fastdfs的java客户端
进行大面积的重构以方便使用，在此对fastdfs作者余庆表示万分感谢！
## 重构以后的java客户端亮点
1. 采用java风格进行编程,抛弃官方客户端的类C编程风格,方便用户对源码的阅读及修改
2. 在重构时着重考虑客户端在高并发时的可用性
3. 客户端更方便的与优秀框架spring整合
4. 提供springboot启动器，极大地简化fastdfs与springboot的整合
## 期待与展望
1. 由于自己知识浅薄，客户端内很多部分需要推敲，欢迎大家指正
2. 将着手利用netty替换socket以提升性能
3. 欢迎大家积极参与创造出优秀的文件系统

## 所提供的功能
### 1. 查询存储组
### 2. 查询每个存储组下所有存储storage信息
## 3.基本使用
### 3.1 添加maven依赖
```java

```
### 3.2 创建配置对象
```java
FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
fastdfsConfiguration.setTrackers(new String[]{"192.168.199.2:22122", "192.168.199.3:22122"});
//创建模板对象
FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
//利用fasdfs模板对文件进行操作
```
+ 配置文件解释
  - networkTimeOut 网络连接超时时间,单位为毫秒  默认超时时间30秒
  - trackers tracker集合 
  - connectTimeout 连接tracker超时时间 
  - openAntiSteal 是否开启防盗链功能，默认关闭
  - secretKey 防盗链密钥  
  - openThumbnail 是否开启缩略图  默认关闭，缩略图之对于图片有效
  - thumbnailStrategy 缩略图生成策略 0严格按照给定尺寸生成，1宽度优先，2高度优先
  - thumbnailWidth 缩略图宽度
  - thumbnailHeight 缩略图高度
  - openWaterMark 是否开启水印
  - waterMarktransparency  水印透明度0-1
  - waterMarkPosition 水印位置 一共9中位置
  > TOP_LEFT:左上      TOP_CENTER:上中     TOP_RIGHT:右上
  > CENTER_LEFT:左中   CENTER:中中         CENTER_RIGHT:右中
  > BOTTOM_LEFT:左下   BOTTOM_CENTER:下中  BOTTOM_RIGHT:右下
  - accessHead 文件访问方式  默认http方式
  - accessPort 文件访问端口 
### 3.3 调用API进行操作
#### 3.3.1 查询所有存储服务器组信息
```java
 List<StorageGroupInfo> allStorageGroupInfo = fastdfsTemplate.getAllStorageGroupInfo();
```

#### 3.3.2 查询组下所有所有存储服务器相关信息
```java
List<StorageInfo> allStorageInfo = fastdfsTemplate.getAllStorageInfo(groupName);
```
#### 3.3.3 文件上传
##### 3.3.3.1 以文件字节数组的形式上传文件
```java
String s = fastdfsTemplate.uploadFile(fileBytes, "112.jpg");
```
#### 3.3.4 文件的删除
```java

```