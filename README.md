# 墨韵读书会

墨韵读书会是一个使用Java开发的应用程序，旨在管理书籍集合、社交网络和小组互动，在虚拟读书社区中提供便利的管理和交流功能。本文档提供了项目的结构和设置指南。

## 目录结构
```
software/
|——src/
   ├── Entity/
   │ ├── Book.java // 图书类
   │ ├── Circle.java // 社交圈类
   │ ├── User.java // 用户类
   │ ├── Student.java // 学生实体类
   │ └── Teacher.java // 教师实体类
   ├── Manager/
   │ ├── BookManager.java // 图书管理服务
   │ ├── CircleManager.java // 社交圈管理服务
   │ ├── FriendDAO.java // 朋友管理
   │ ├── FriendManager.java // 朋友管理服务
   │ ├── FriendService.java // 朋友服务
   │ └── UserManager.java // 用户管理服务
   ├── Model/
   │ ├── BookTableModel.java // 图书表格模型
   │ ├── CircleInvitation.java // 社交圈邀请类
   │ └── UserTableModel.java // 用户表格模型
   ├── UI/
   │ ├── AdminUI.java // 管理员界面
   │ ├── BookDialog.java // 图书对话框
   │ ├── BookLibraryPanel.java // 图书馆面板
   │ ├── BookManagementPanel.java // 图书管理面板
   │ ├── CirclePanel.java // 圈子面板
   │ ├── FriendManagementPanel.java // 朋友管理面板
   │ ├── FriendProfileDialog.java // 朋友详情对话框
   │ ├── FriendSearchDialog.java // 朋友搜索对话框
   │ ├── LoginUI.java // 登录界面
   │ ├── MainUI.java // 主界面
   │ ├── PersonalInfoPanel.java // 个人信息面板
   │ └── UserManagementPanel.java // 用户管理面板
   └── Main.java // 主入口类
```

## 项目概述

这个项目是为了模拟和管理一个读书俱乐部的图书管理和社交活动。主要功能包括：

- **图书管理**：包括添加、编辑、删除图书，以及显示图书列表和详细信息。
- **社交圈管理**：用户可以创建、加入和管理不同的社交圈，与朋友分享和交流。
- **朋友管理**：用户可以添加、查看和删除朋友，以及查看朋友的详细信息和社交圈活动。
- **用户管理**：包括用户的登录、注册和个人信息管理。

# 项目详细介绍

## Entity 目录

### Book.java

- **描述：** 表示图书的实体类。
- **功能：** 包含图书的基本属性和方法，用于表示和操作图书对象。

### Circle.java

- **描述：** 表示社交圈的实体类。
- **功能：** 定义社交圈的属性和方法，用于管理社交圈的成员和活动。

### User.java

- **描述：** 表示用户的实体类。
- **功能：** 提供用户的基本信息和操作方法，如登录、注册等。

### Student.java

- **描述：** 学生实体类，继承自 User.java。
- **功能：** 扩展了用户的特定属性和行为，如学生的学校信息等。

### Teacher.java

- **描述：** 教师实体类，继承自 User.java。
- **功能：** 扩展了用户的特定属性和行为，如教师的教学科目等。

## Manager 目录

### BookManager.java

- **描述：** 图书管理服务类。
- **功能：** 实现对图书的管理功能，如添加、删除、修改图书信息等操作。

### CircleManager.java

- **描述：** 社交圈管理服务类。
- **功能：** 管理社交圈的创建、管理成员、发布活动等功能。

### FriendDAO.java

- **描述：** 朋友数据访问对象。
- **功能：** 处理与朋友关系相关的数据访问操作，如存储、检索朋友信息等。

### FriendManager.java

- **描述：** 朋友管理服务类。
- **功能：** 提供对朋友关系的管理功能，如添加、删除、查找朋友等操作。

### FriendService.java

- **描述：** 朋友服务类。
- **功能：** 提供与朋友关系相关的服务，如发送消息、接受好友请求等功能。

### UserManager.java

- **描述：** 用户管理服务类。
- **功能：** 管理用户的注册、登录、注销等操作，确保用户信息的安全性和一致性。

## Model 目录

### BookTableModel.java

- **描述：** 图书表格模型类。
- **功能：** 定义图书信息在表格中的展示和操作规则，支持图书列表的显示和编辑。

### CircleInvitation.java

- **描述：** 社交圈邀请类。
- **功能：** 定义社交圈成员之间的邀请关系，管理邀请状态和操作。

### UserTableModel.java

- **描述：** 用户表格模型类。
- **功能：** 定义用户信息在表格中的展示和操作规则，支持用户列表的显示和编辑。

## UI 目录

### AdminUI.java

- **描述：** 管理员界面类。
- **功能：** 提供管理员操作界面，支持对系统功能和数据进行管理和监控。

### BookDialog.java

- **描述：** 图书对话框类。
- **功能：** 显示和处理图书信息的对话框，支持图书的新增、编辑操作。

### BookLibraryPanel.java

- **描述：** 图书馆面板类。
- **功能：** 展示图书馆的图书列表和相关操作，支持图书的查看和搜索功能。

### BookManagementPanel.java

- **描述：** 图书管理面板类。
- **功能：** 管理图书的增删改查操作，提供图书管理的主要功能入口。

### CirclePanel.java

- **描述：** 社交圈面板类。
- **功能：** 展示社交圈的成员列表和相关操作，支持社交圈的管理和互动功能。

### FriendManagementPanel.java

- **描述：** 朋友管理面板类。
- **功能：** 管理用户的朋友关系和相关操作，支持朋友的添加、删除和搜索功能。

### FriendProfileDialog.java

- **描述：** 朋友详情对话框类。
- **功能：** 展示朋友的详细信息和操作选项，支持查看朋友的个人资料和发送消息功能。

### FriendSearchDialog.java

- **描述：** 朋友搜索对话框类。
- **功能：** 支持用户查找和添加新朋友，提供搜索朋友和发送好友请求的功能。

### LoginUI.java

- **描述：** 登录界面类。
- **功能：** 提供用户登录的界面，支持用户身份验证和登录操作。

### MainUI.java

- **描述：** 主界面类。
- **功能：** 应用程序的主界面，集成了各个功能模块和操作入口，提供用户整体的使用体验。

### PersonalInfoPanel.java

- **描述：** 个人信息面板类。
- **功能：** 展示和编辑用户的个人信息，支持用户信息的修改和保存操作。

### UserManagementPanel.java

- **描述：** 用户管理面板类。
- **功能：** 管理用户的注册、管理和权限控制，支持用户账号的创建和权限的管理。

## 主入口类

### Main.java

- **描述：** 主入口类。
- **功能：** 启动整个应用程序，初始化必要的资源和配置，加载主界面等操作。



## 如何运行项目

1. **环境要求**：
   - Java 开发环境（JDK 版本 >= 8）
   - IntelliJ IDEA 或其他 Java 开发 IDE

2. **设置项目**：
   - 使用 IDE 导入项目源代码。
   - 确保项目依赖项正确配置。

3. **运行应用程序**：
   - 在 IDE 中运行 `Main.java` 中的 `main` 方法。
   - 应用程序将启动主界面 `MainUI.java`，您可以从这里访问所有功能。

## 注意事项

- 本项目仅作为学习和演示用途，未实现实际的数据库和网络连接。
- 欢迎提出改进建议和功能请求。

## 联系我们

如果您有任何问题或建议，请通过 GitHub 提交 issue 或联系开发团队。

---

这份 README 文件提供了项目的基本信息、目录结构、功能概述以及如何运行和使用项目的说明。
