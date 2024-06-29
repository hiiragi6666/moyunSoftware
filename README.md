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
