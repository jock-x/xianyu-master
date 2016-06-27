
### 鱼塘介绍

* 鱼塘是一款二手买卖市场开源软件

* 该APP是通过新浪微博登录的，为防止大家clone下来后可能无法编译或不能正常使用微博登录，现将证书上传，如果你还是不能编译，请与我联系。

### 该项目使用动态代理AOP编程框架，使开发起来更简洁、更高效

该动态代理框架的核心是通过dexmaker和Spring的拦截器实现AOP编程；dexmaker是运行在Android DVM上，
利用Java编写，来动态生成DEX字节码的API。如果了解Spring AOP编程的话，应该听说过cglib or ASM，
但这两个工具生成都是Java字节码，而DVM加载的必须是DEX字节码。所以，想要在Android上进行AOP编程，
Google 的dexmaker可以说是一个非常好的选择。

辅助的还有注解和反射，使用注解来标注同步、异步、加载框和加载显示的文字；反射回调继承以下Base类子类的方法：

    BaseAsyncActivity
    BaseAsyncFragment
    BaseAsyncListAdapter
    BaseAsyncObject

同时着重使用系统的Handler并封装为MessageProxy进行消息的分发与处理。

封装映射Map为ModelMap，方便回调时的数据传递。

### [APP下载地址](https://fir.im/Bingo)

	欢迎您的加入，共同收集好的技术文章，一起学习！共同进步！

### [GitHub开源地址](https://github.com/jock-x/xianyu-master)
### [借鉴地址](https://github.com/sfsheng0322/Bingo)


### ScreenShots

<table>
    <tr>
        <td><img src="/screenshots/1.jpg" style="width: 50%;"></td>
        <td><img src="/screenshots/2.jpg" style="width: 50%;"></td>
        <td><img src="/screenshots/4.jpg" style="width: 50%;"></td>
    </tr>
</table>

<br/>

<table>
    <tr>
        <td><img src="/screenshots/5.jpg" style="width: 50%;"></td>
        <td><img src="/screenshots/6.jpg" style="width: 50%;"></td>
        <td><img src="/screenshots/7.jpg" style="width: 50%;"></td>
    </tr>
</table>

### 用到的开源库，感谢

* [OkHttp3](https://github.com/square/okhttp)
* [Glide](https://github.com/bumptech/glide)
* [esperandro](https://github.com/dkunzler/esperandro)
* [SystemBarTint](https://github.com/jgilfelt/SystemBarTint)
* [Material Dialog](https://github.com/afollestad/material-dialogs)
* [logger](https://github.com/orhanobut/logger)
* [PhotoView](https://github.com/chrisbanes/PhotoView)
* [EventBus](https://github.com/greenrobot/EventBus)



### 关于我

个人邮箱：864015769@qq.com





