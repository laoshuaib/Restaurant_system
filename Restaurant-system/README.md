## 已知存在问题

#### 1.大数传输可能存在精度丢失

数据库采用Big Integer进行存储，且多数请求收发时仍然直接使用

Big Integer进行构建（@requestBody）

##### 方案：在有精度需求时，对相应数据进行转换为String进行传输

#### 2.mybatis-plus版本使用非法反射

暂不解决

