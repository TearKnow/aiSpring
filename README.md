# 记事本后端

基于 Java 25、Spring Boot 和 H2 的记事 REST API。H2 使用本地文件数据库，默认数据保存在运行目录下的 `data/notepad`，应用重启后仍会保留。

运行前请安装 Java 25 和 Maven。

## 运行

```bash
mvn spring-boot:run
```

默认监听 `http://localhost:8080`。

## API

请求和响应均使用 JSON。

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `POST` | `/api/notes` | 新建记事，成功返回 `201` |
| `GET` | `/api/notes` | 按更新时间倒序获取全部记事 |
| `GET` | `/api/notes?keyword=Spring` | 按标题或内容进行不区分大小写的关键词搜索 |
| `GET` | `/api/notes/{id}` | 获取单条记事 |
| `PUT` | `/api/notes/{id}` | 更新标题和内容 |
| `DELETE` | `/api/notes/{id}` | 删除记事，成功返回 `204` |

新建和更新的请求格式：

```json
{
  "title": "我的记事",
  "content": "记事正文"
}
```

标题不能为空且最多 200 个字符；内容可以为空字符串，但不能缺省或为 `null`。记录会包含 `id`、`createdAt` 和 `updatedAt`。不存在的记事返回 `404`，校验失败返回 `400`。

## 测试

```bash
mvn test
```
