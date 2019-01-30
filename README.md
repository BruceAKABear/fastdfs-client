# fastdfs java客户端
**概述:** 在开发中难免会用到文件存储，在众多的文件系统中个人对fastdfs情有独钟，但是鉴于
官方提供的java客户端及其的难使用，以及存在的线程安全性等问题，最终个人决定对fastdfs的java客户端
进行大面积的重构以方便使用，在此对fastdfs作者余庆表示万分感谢！
## 重构以后的java客户端亮点
1. 采用java风格进行编程,抛弃官方客户端的类C编程风格,方便用户对源码的阅读及修改
2. 在重构时着重考虑客户端在高并发时的可用性
3. 客户端更方便的与优秀框架spring整合
4. 提供springboot启动器，极大地简化fastdfs与springboot的整合
5. 提供缩略图上传功能，同时能为缩略图添加水印
## 期待与展望
1. 由于自己知识浅薄，客户端内很多部分需要推敲，欢迎大家指正
2. 将着手利用netty替换socket以提升性能
3. 欢迎大家积极参与创造出优秀的文件系统
4. 由于fastdfs很多设计个人觉得没有必要所以没有实现，需要良久思考

## 客户端使用
### 客户端初始化
```java
//1.创建配置对象
FastdfsConfiguration fastdfsConfiguration = new FastdfsConfiguration();
//2.对配置对象进行设置
fastdfsConfiguration.setTrackers(new String[]{"192.168.199.2:22122", "192.168.199.3:22122"});
//3.创建操作模板对象，并将配置对象作为参数传入
FastdfsTemplate fastdfsTemplate = new FastdfsTemplate(fastdfsConfiguration);
//4.利用fasdfs模板对文件进行操作
```

+ 配置对象属性解释
  - networkTimeOut 文件操作连接超时时间,单位为毫秒  **默认超时时间30秒**
  - trackers tracker集合 
  - connectTimeout 连接tracker超时时间，单位毫秒 **默认超时时间为5秒**
  - openAntiSteal 是否开启防盗链功能 **默认关闭,如果开启需要同步在服务器端配置文件中修改**
  - secretKey 防盗链密钥  **默认为fastdfs123456**
  - openThumbnail 是否开启缩略图  **默认关闭,缩略图只对于图片有效**
  - thumbnailStrategy 缩略图生成策略 0严格按照给定尺寸生成，1宽度优先，2高度优先
  - thumbnailWidth 缩略图宽度
  - thumbnailHeight 缩略图高度
  - openWaterMark 是否开启水印
  - waterMarktransparency  水印透明度0-1之间
  - waterMarkPosition 水印位置 一共9种位置 **默认为BOTTOM_RIGHT**
  > TOP_LEFT:左上      TOP_CENTER:上中     TOP_RIGHT:右上
  > CENTER_LEFT:左中   CENTER:中中         CENTER_RIGHT:右中
  > BOTTOM_LEFT:左下   BOTTOM_CENTER:下中  BOTTOM_RIGHT:右下
  - accessHead 文件访问方式  默认http方式
  - accessPort 文件访问端口(nginx访问端口) 
  
### 1. 系统相关
1.1. 查询存储组
查询所有存储的基本信息
```java
List<StorageGroupInfo> allStorageGroupInfo = fastdfsTemplate.getAllStorageGroupInfo();
```
1.2. 查询每个存储组下所有存储storage信息
查询制定group存储组下所有的存储服务器的基本信息

```java
List<StorageInfo> allStorageInfo = fastdfsTemplate.doGetAllStorageInfo(groupName);
```
> 也可以通过指定groupName和存储服务器的ip查询指定的存储服务器的相关信息(但是文件会在同组内自行同步，那么查询单个
存储服务器将没什么意义)




1.3. 将存储服务器从存储服务器中删除

```java

```
### 2 文件相关
2.1. 系统初始化



2. 调用API进行操作

2.1 查询所有存储服务器组信息
```java
 List<StorageGroupInfo> allStorageGroupInfo = fastdfsTemplate.getAllStorageGroupInfo();
```

2.2 查询组下所有所有存储服务器相关信息
```java
List<StorageInfo> allStorageInfo = fastdfsTemplate.getAllStorageInfo(groupName);
```
2.3 文件上传
2.3.1 以文件字节数组的形式上传文件
```java
String s = fastdfsTemplate.uploadFile(fileBytes, "112.jpg");
```
2.4 文件的删除
```java

```