# StockTrading-Simulator

# 股票投资模拟器

为新手投资者准备的安卓股票投资模拟器！

Socket Server代码: https://github.com/RobertYYF/StockSimulator_SocketServer

v0: 完成页面布局和爬虫获取信息

v1.0： 使用SQLite存储用户信息，在客户端本地运行买入卖出监听操作

v1.1： 完成全本地版基本功能，包括添加到观察列表，买入，卖出，搜索股票等

v2.0： 重构代码，重新设计页面布局，整体采用MVC架构，使用Activity+多个Fragment代替多个Activity; 加入远程MySQL数据库储存用户信息；加入Socket Server负责客户端与DB信息往来以及买入卖出监听操作
