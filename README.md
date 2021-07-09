# Android 插桩Demo

# 目标

通过gradle的插件完成android的插桩入门
- 首先学习编写一个简单的gradle插件
- 通过composing builds 方法引入这个插件方便调试
- 自己实现Transform方法来实现自己的插桩功能
- 通过Asm outline插件完成学习插桩代码

# 参考
本文中的代码通过参考以下的文章和仓库来学习
- [AndroidAutoTrack](https://github.com/Leifzhang/AndroidAutoTrack)
- [Hunter](https://github.com/Leaking/Hunter)
- [gradle脚本调试](https://www.jianshu.com/p/0c7d5fd5792d)
- [ASM插入方法](https://blog.csdn.net/ljz2016/article/details/83508931)

## 完成一个简单的插件
本文设计到的代码都在这个项目中的AsmPlugin目录下[代码](https://github.com/lijunjieone/PluginDemo)
- 首先创建一个工程，这里是AsmPlugin，我是在PluginDemo的工程下。因为我希望后面调试的时候，可以很容易的把这俩工程关联起来
- AsmPlugin是一个插件工程，是新建了一个android工程，然后根据AndroidAutoTrack里面的插件模板修改相应的build.gradle文件来完成的
- 在AsmPlugin中建立比较简单的插件模板，我引入了AndroidAutoTrack中的基类插件库，然后自己增加一个简单的Demo
- 关联这个插件到PluginDemo中。

### 建立一个插件工程
我是建立一个普通的的Android工程，修改相应的文件完成插件的改造，
新建一个空的Android模块，这里是SimplePlugin
修改模块中的build.gradle文件
插件的gradle文件如下

```

apply plugin: 'groovy'
apply plugin: 'kotlin'
apply plugin: 'java-gradle-plugin'
apply plugin: 'kotlin-kapt'
//apply from: '../upload_bintray.gradle'

dependencies {
    implementation gradleApi()
    implementation localGroovy()
    implementation project(":BasePlugin")
    implementation 'commons-io:commons-io:2.6'
    implementation 'org.javassist:javassist:3.27.0-GA'
    implementation 'com.google.auto.service:auto-service:1.0-rc6'
    kapt "com.google.auto.service:auto-service:1.0-rc6"
}


gradlePlugin {
    plugins {
        version {
            // 在 app 模块需要通过 id 引用这个插件
            id = 'simple-plugin'
            // 实现这个插件的类的路径
            implementationClass = 'com.bn.simpleplugin.SimplePlugin'
        }
    }
}

```

- 其中的 id为插件的名称，跟引用这个插件时的名称对应
- implementationClass 是指定插件入口类，需要实现gradle的Plugin类

文件结构如下


![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/28a80907dc804923af30b39d05fbaebc~tplv-k3u1fbpfcp-watermark.image)
### 完成简单的插件模块

增加两个类SimplePlugin类和SimpleTransform类
SimplePlugin.kt
```
class SimplePlugin : Plugin<Project> {
    override fun apply(p0: Project) {
        val appExtension = p0.extensions.getByType(
            AppExtension::class.java
        )
        appExtension.registerTransform(SimpleTransform(p0))
    }

}

```

SimpleTransform.kt

```

class SimpleTransform(private val project: Project):Transform() {
    override fun getName(): String {
        return "SimpleTransform"
    }


    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_JARS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun isIncremental(): Boolean {
        return true
    }

    @Throws(TransformException::class, InterruptedException::class, IOException::class)
    override fun transform(transformInvocation: TransformInvocation) {
        val injectHelper = AutoTrackHelper()
        val baseTransform = BaseTransform(transformInvocation, object : TransformCallBack {
             override fun process(className: String, classBytes: ByteArray?): ByteArray? {
                 if(TestAsm.needHandle(className)){
                     return TestAsm.handleTestClass3(classBytes!!)
                 }
                if (ClassUtils.checkClassName(className)) {
                    try {
                        return injectHelper.modifyClass(classBytes!!)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                return null
            }
        })
        baseTransform.startTransform()
    }


}

```

在transform方法中，我们可以拿到所有需要打包的类，根据自己的需要来完成插桩。


### 关联插件工程到主工程
修改主工程的根目录的settings.gradle文件，通过includeBuild关键字完成两个工程的关联，这个是为了方便后面定义的插件，能够去插件仓库中找到

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/c119ca651cfd421f8d0ed4c732eb81b0~tplv-k3u1fbpfcp-watermark.image)

修改主工程的根目录下的build.gradle文件。来完成插件定义

```
plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle 文件内定义的id
    id "simple-plugin" apply false
}
```

这个是为了声明插件

修改主模块的build.gradle文件,这里是app/build.gradle，启用插件

```
plugins {
    id "simple-plugin"
}

```

通过上面的操作我们就使用ComposingBuild方式完成了插件工程引入到主工程中，下面我们来调试这个插件


## 调试插件
我是通过其他大佬提供的这个网址来完成的调试，大家可以直接看这个文章来自己尝试
[gradle脚本调试](https://www.jianshu.com/p/0c7d5fd5792d)

根据文章提到的内容主要是两点
- 增加debug运行方式
- 本地运行./gradlew :app:asD -Dorg.gradle.debug=true --no-daemon 这个命令

运行上述命令首先会挂住，等待运行debug模式，
我们在SimplePlugin中的apply方法中增加断点，如果能够在中断在这个位置，我们就算完成了调试。

## 使用插件完成代码插入和创建方法
### ASM代码资料
如果想自己完成一些插件功能，需要对ASM功能的一些了解。以下是我自己查看的一些资料，大佬们可以参考下。
- [创建方法](https://blog.csdn.net/ljz2016/article/details/83508931)
- ASM官方资料
- [javap分析java汇编命令](https://www.jianshu.com/p/6a8997560b05)

通过ASM的资料可以看到ASM有两个Api，一个是Core Api，一个是Tree Api。
我们这里用到的是TreeApi
### 插入一个方法

Asm插入一个方法的代码
```
    fun createMethod(): MethodNode? {
        val mn = MethodNode(ACC_PUBLIC, "checkAndSet", "(I)V", null, null)
        val il: InsnList = mn.instructions
        il.add(VarInsnNode(ILOAD, 1))
        val label = LabelNode()
        il.add(JumpInsnNode(IFLT, label))
        il.add(VarInsnNode(ALOAD, 0))
        il.add(VarInsnNode(ILOAD, 1))
        il.add(FieldInsnNode(PUTFIELD, "org/by/Cwtest", "f", "I"))
        val end = LabelNode()
        il.add(JumpInsnNode(GOTO, end))
        il.add(label)
        il.add(FrameNode(F_SAME, 0, null, 0, null))
        il.add(TypeInsnNode(NEW, "java/lang/IllegalArgumentException"))
        il.add(InsnNode(DUP))
        il.add(MethodInsnNode(INVOKESPECIAL,
                "java/lang/IllegalArgumentException", "<init>", "()V", false))
        il.add(InsnNode(ATHROW))
        il.add(end)
        il.add(FrameNode(F_SAME, 0, null, 0, null))
        il.add(InsnNode(RETURN))
        mn.maxStack = 2
        mn.maxLocals = 2

        return mn
    }

```

通过反编译来查看以上代码生成的代码

```
public void checkAndSet(int var1) {
    if(var1 >= 0) {
    this.f = var1;
    } else {
    throw new IllegalArgumentException();
    }

```


通过以下命令来查看是否正常生成了代码

通过javap -c -l TestDate.class 



## 通过Asm outline插件来查看java字节码
通过android studio的这个查看可以看到java代码对应的二进制代码，所以通过这个可以用来学习如何编写插件的处理代码
需要的同学可以查看这个文章来[AndroidStudio 插件查看字节码](https://blog.csdn.net/ForwardSailing/article/details/106494116)

希望通过本文的简单介绍，可以让大家能够快速的自己建立一个可以调试，可以运行的简单的插件工程。

如果有需要沟通的同学欢迎留言沟通。



# 模板生成
- ./RTemplates.py c -ap /d/dev/studio/PluginDemo -pn com.bn.pd -mn app  -tn Simple -sn Simple
- ./RTemplates.py c -ap /d/dev/studio/PluginDemo -pn com.bn.pd -mn app  -tn Page -sn VideoList
- ./RTemplates.py c -ap /d/dev/studio/PluginDemo -pn com.bn.pd -mn app  -tn List -sn List
- ./RTemplates.py c -ap /d/dev/studio/PluginDemo -pn com.bn.pd -mn app  -tn Detail -sn Detail
- ./RTemplates.py c -ap /d/dev/studio/PluginDemo -pn com.bn.pd -mn app  -tl Detail,List -sn X
