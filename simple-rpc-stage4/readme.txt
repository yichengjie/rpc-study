1.使用netty结合jboos序列化与服务端通信
2.使用代理类生成clien实现类并放入spring容器
  2.1 使用示例:
      @Autowired
      private IUserService userService ;

      User user = new User(id,name,address) ;
      List<User> users = userService.insertUser(user);