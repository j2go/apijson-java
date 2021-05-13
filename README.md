# 原仓库

- https://github.com/Tencent/APIJSON
- https://github.com/APIJSON/apijson-column
- https://github.com/APIJSON/apijson-framework
- https://github.com/APIJSON/APIJSON-Demo

# 目的

apijson 的 jar 包没有上传到中央仓库，为了方便研究源代码把之前的项目集合了一下

# 快速使用

1. Docker 启动一个 MySQL

```shell
docker run -d --name mysql5 -eMYSQL_ROOT_PASSWORD=123456 -p3306:3306 --restart=always mysql:5.7
```

2. 导入 `sql/mysql/sys.sql`
3. 运行 `example-springboot/src/main/java/apijson/boot/DemoApplication.java`
4. 打开 `http://apijson.org/api/` 进行测试