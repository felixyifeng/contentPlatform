# Content Platform 接口说明

后端服务基于 Spring Boot，默认端口 `8080`，接口统一前缀为 `/api`。

## 通用说明

### 统一响应格式

所有接口均返回如下结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `code`：`200` 表示成功；`400` 表示参数或业务错误；`500` 表示服务端异常。
- `message`：提示信息。
- `data`：业务数据，无数据时为 `null`。

### 鉴权说明

- 除 `/api/users/**`（注册、登录）外，所有接口都需要在请求头携带 `Authorization: Bearer <token>`。
- 未携带或 token 无效时返回 HTTP `401`；角色权限不足时返回 HTTP `403`。
- 删除文章、删除分类需要 `admin` 角色。

### 返回对象字段

`Article`（文章）：

| 字段 | 类型 | 说明 |
|---|---|---|
| id | Long | 文章 ID |
| title | String | 标题 |
| content | String | 正文 |
| summary | String | 摘要 |
| cover | String | 封面 URL |
| categoryId | Long | 分类 ID |
| authorId | Long | 作者 ID |
| viewCount | Integer | 浏览量 |
| status | Integer | 状态 |
| createTime | LocalDateTime | 创建时间 |
| updateTime | LocalDateTime | 更新时间 |

`ArticleVO`（文章列表项）：`id`、`title`、`summary`、`viewCount`、`categoryName`（分类名）、`authorName`（作者昵称）。

`Category`（分类）：`id`、`name`、`sort`、`createTime`、`updateTime`。

---

## 一、用户接口 `/api/users`

以下接口无需 token。

### 1. 用户注册

- **方法与路径**：`POST /api/users/register`
- **功能**：注册新用户，用户名重复时注册失败；密码使用 BCrypt 加密存储。
- **请求参数**（JSON Body，`RegisterDTO`）：

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| username | String | 是 | 用户名，长度 3-50 |
| password | String | 是 | 密码，至少 6 位 |
| nickname | String | 否 | 昵称，不传则默认等于用户名 |
| email | String | 否 | 邮箱 |

- **返回数据**：`Result<?>`，成功时 `data` 为 `null`；用户名已存在时 `code=400`，`message="用户名已存在"`。

### 2. 用户登录

- **方法与路径**：`POST /api/users/login`
- **功能**：校验账号密码并签发 JWT。
- **请求参数**（JSON Body，`LoginDTO`）：

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

- **返回数据**：`Result<Map<String,Object>>`，`data` 包含：

| 字段 | 类型 | 说明 |
|---|---|---|
| token | String | JWT，后续请求通过 `Authorization: Bearer <token>` 使用 |
| userId | Long | 用户 ID |
| username | String | 用户名 |
| nickname | String | 昵称 |

- 用户名或密码错误时 `code=400`，`message="用户名或密码错误"`；账号被禁用时 `message="账号已被禁用"`。

---

## 二、文章接口 `/api/articles`

以下接口均需 token。

### 3. 查询文章列表

- **方法与路径**：`GET /api/articles`
- **功能**：查询所有文章，附带分类名和作者昵称，按文章 ID 倒序排列。
- **请求参数**：无。
- **返回数据**：`Result<List<ArticleVO>>`，`data` 为数组，元素字段见上文 `ArticleVO`。

### 4. 根据 ID 查询文章详情

- **方法与路径**：`GET /api/articles/{id}`
- **功能**：按文章 ID 查询详情，结果使用 Redis 缓存 5 分钟。
- **请求参数**：路径参数 `id`（Long，文章 ID）。
- **返回数据**：`Result<Article>`，`data` 为 `Article` 对象；未查到时 `data` 为 `null`。

### 5. 分页查询文章

- **方法与路径**：`GET /api/articles/page`
- **功能**：分页返回文章列表。
- **请求参数**（Query 参数）：

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|---|---|---|---|---|
| pageNum | int | 否 | 1 | 页码 |
| pageSize | int | 否 | 10 | 每页条数 |

- **返回数据**：`Result<Page<Article>>`，`data` 主要字段：`records`（`Article` 数组）、`total`（总条数）、`current`（当前页）、`size`（每页条数）、`pages`（总页数）。

### 6. 搜索文章

- **方法与路径**：`GET /api/articles/search`
- **功能**：按标题模糊匹配和/或分类精确匹配搜索文章；两个参数都不传时查询全部。
- **请求参数**（Query 参数）：

| 参数 | 类型 | 必填 | 说明 |
|---|---|---|---|
| title | String | 否 | 标题关键字，模糊查询 |
| categoryId | Integer | 否 | 分类 ID，精确查询 |

- **返回数据**：`Result<List<Article>>`，`data` 为 `Article` 数组。

### 7. 新增文章

- **方法与路径**：`POST /api/articles`
- **功能**：新增一篇文章。
- **请求参数**（JSON Body，`ArticleSaveDTO`）：

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| title | String | 是 | 标题，长度不超过 200 |
| content | String | 是 | 正文 |
| summary | String | 否 | 摘要 |
| cover | String | 否 | 封面 URL |
| categoryId | Long | 是 | 分类 ID |
| authorId | Long | 是 | 作者 ID |
| viewCount | Integer | 否 | 浏览量 |
| status | Integer | 是 | 状态 |

- **返回数据**：`Result<?>`，成功时 `data` 为 `null`；参数校验失败时 `code=400`。

### 8. 修改文章

- **方法与路径**：`PUT /api/articles`
- **功能**：修改文章。
- **请求参数**（JSON Body，`ArticleUpdateDTO`）：字段与新增文章的 `ArticleSaveDTO` 完全相同（不含 `id` 字段）。
- **返回数据**：`Result<?>`，成功时 `data` 为 `null`。
- **注意**：当前实现中 DTO 未包含 `id`，而 Service 按 ID 更新，直接使用可能无法定位目标文章。

### 9. 删除文章

- **方法与路径**：`DELETE /api/articles/{id}`
- **功能**：按 ID 删除文章，需要 `admin` 角色。
- **请求参数**：路径参数 `id`（Long，文章 ID）。
- **返回数据**：`Result<?>`，成功时 `data` 为 `null`；权限不足时 HTTP `403`。

---

## 三、分类接口 `/api/categories`

以下接口均需 token。

### 10. 查询分类列表

- **方法与路径**：`GET /api/categories`
- **功能**：查询所有分类。
- **请求参数**：无。
- **返回数据**：`Result<List<Category>>`，`data` 为 `Category` 数组。

### 11. 新增分类

- **方法与路径**：`POST /api/categories`
- **功能**：新增分类。
- **请求参数**（JSON Body，`Category`）：

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| name | String | 否 | 分类名称 |
| sort | Integer | 否 | 排序值 |
| id | Long | 否 | 自增主键，新增时无需传入 |
| createTime / updateTime | LocalDateTime | 否 | 由系统维护 |

- **返回数据**：`Result<?>`，成功时 `data` 为 `null`。

### 12. 修改分类

- **方法与路径**：`PUT /api/categories`
- **功能**：按 ID 修改分类。
- **请求参数**（JSON Body，`Category`）：需传入 `id` 用于定位记录，其余字段为要更新的 `name`、`sort` 等。
- **返回数据**：`Result<?>`，成功时 `data` 为 `null`。

### 13. 删除分类

- **方法与路径**：`DELETE /api/categories/{id}`
- **功能**：按 ID 删除分类，需要 `admin` 角色。
- **请求参数**：路径参数 `id`（Long，分类 ID）。
- **返回数据**：`Result<?>`，成功时 `data` 为 `null`；权限不足时 HTTP `403`。

---

## 四、评论接口 `/api/comments`

以下接口均需 token。

### 14. 查询文章评论列表

- **方法与路径**：`GET /api/comments/article/{articleId}`
- **功能**：查询指定文章的评论，按创建时间倒序，附带评论用户昵称。
- **请求参数**：路径参数 `articleId`（Long，文章 ID）。
- **返回数据**：`Result<List<Map<String,Object>>>`，`data` 为数组，每条评论包含：`id`、`article_id`（或 `articleId`）、`content`、`create_time`（或 `createTime`）、`user_name`（或 `userName`，评论用户昵称）。

### 15. 发布评论

- **方法与路径**：`POST /api/comments`
- **功能**：新增评论；用户 ID 从 token 中解析，不接收前端传入。
- **请求参数**（JSON Body，`CommentDTO`）：

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| articleId | Long | 是 | 文章 ID |
| content | String | 是 | 评论内容 |

- **返回数据**：`Result<?>`，成功时 `data` 为 `null`。

### 16. 删除评论

- **方法与路径**：`DELETE /api/comments/{id}`
- **功能**：按评论 ID 删除评论（当前实现仅校验登录，未校验评论归属或管理员角色）。
- **请求参数**：路径参数 `id`（Long，评论 ID）。
- **返回数据**：`Result<?>`，成功时 `data` 为 `null`。

---

## 接口汇总

| 方法 | 路径 | 功能 | 是否需要 token | 特殊权限 |
|---|---|---|---|---|
| POST | `/api/users/register` | 用户注册 | 否 | 无 |
| POST | `/api/users/login` | 用户登录，返回 token | 否 | 无 |
| GET | `/api/articles` | 文章列表（带分类/作者） | 是 | 无 |
| GET | `/api/articles/{id}` | 文章详情 | 是 | 无 |
| GET | `/api/articles/page` | 文章分页 | 是 | 无 |
| GET | `/api/articles/search` | 文章搜索 | 是 | 无 |
| POST | `/api/articles` | 新增文章 | 是 | 无 |
| PUT | `/api/articles` | 修改文章 | 是 | 无 |
| DELETE | `/api/articles/{id}` | 删除文章 | 是 | admin |
| GET | `/api/categories` | 分类列表 | 是 | 无 |
| POST | `/api/categories` | 新增分类 | 是 | 无 |
| PUT | `/api/categories` | 修改分类 | 是 | 无 |
| DELETE | `/api/categories/{id}` | 删除分类 | 是 | admin |
| GET | `/api/comments/article/{articleId}` | 文章评论列表 | 是 | 无 |
| POST | `/api/comments` | 发布评论 | 是 | 无 |
| DELETE | `/api/comments/{id}` | 删除评论 | 是 | 无 |
