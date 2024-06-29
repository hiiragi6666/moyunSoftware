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
