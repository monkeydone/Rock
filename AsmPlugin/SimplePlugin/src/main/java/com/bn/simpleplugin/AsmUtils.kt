package com.bn.simpleplugin

import com.kronos.plugin.base.ClassUtils
import javassist.*
import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.*
import java.io.File


object AsmUtils {
    fun generatorClass(){
        val cn = ClassNode()
        cn.version = V1_5
        cn.access = ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE
        cn.name = "pkg/Comparable"
        cn.superName = "java/lang/Object"
        cn.interfaces.add("pkg/Mesurable")
        cn.fields.add(FieldNode(ACC_PUBLIC+ ACC_FINAL+ ACC_STATIC,"LESS","I",null,-1))
        cn.fields.add(FieldNode(ACC_PUBLIC+ ACC_FINAL+ ACC_STATIC,"EQUAL","I",null,0))
        cn.fields.add(FieldNode(ACC_PUBLIC+ ACC_FINAL+ ACC_STATIC,"GREATER","I",null,1))
        cn.methods.add(MethodNode(ACC_PUBLIC+ ACC_ABSTRACT,"compareTo","(Ljava/lang/Object;)I",null,null))
//        val classF = "D:\\dev\\studio\\DProject\\app\\build\\intermediates\\transforms\\SimpleTransform\\debug\\111\\temp\\com\\a\\dproject\\ar\\fragment\\CustomArCoreFragment.class"
        val classF2 = "D:\\dev\\studio\\DProject\\app\\build\\C.class"
        ClassUtils.saveFile(File(classF2), toByteArray(cn))
        val classF3 = "D:\\dev\\studio\\DProject\\app\\build\\D.class"
        val class1 = createClass("A")
        ClassUtils.saveFile(File(classF3), toByteArray(class1!!))
    }

    private fun createClass(className: String): ClassNode? {
        val classNode = ClassNode()
        classNode.visit(49, ACC_SUPER + ACC_PUBLIC, className, null, "java/lang/Object", null)
        val mv: MethodVisitor = classNode.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        mv.visitCode()
        mv.visitVarInsn(ALOAD, 0)
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        mv.visitInsn(RETURN)
        mv.visitMaxs(1, 1)
        mv.visitEnd()
        classNode.visitEnd()
        return classNode
    }

    /**
     *
     * public void checkAndSet(int var1) {
    if(var1 >= 0) {
    this.f = var1;
    } else {
    throw new IllegalArgumentException();
    }
    }
     */
    /**
     *  javap -c -l ./app/build/intermediates/transforms/SimpleTransform/debug/111/com/a/dproject/javassist/PersonService.class
     */

    /**
     * public void checkAndSet(int);
    Code:
    0: iload_1
    1: iflt          12
    4: aload_0
    5: iload_1
    6: putfield      #22                 // Field org/by/Cwtest.f:I
    9: goto          20
    12: new           #24                 // class java/lang/IllegalArgumentException
    15: dup
    16: invokespecial #28                 // Method java/lang/IllegalArgumentException."<init>":()V
    19: athrow
    20: return
    }

     */
    fun createMethod2(classWriter:ClassWriter){
        val methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "toastText", "()Ljava/lang/String;", null, null)
        methodVisitor.visitCode()
        val label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(7, label0)
        methodVisitor.visitLdcInsn("This is toast text")
        methodVisitor.visitInsn(ARETURN)
        val label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLocalVariable("this", "Lcom/a/dproject/javassist/PersonService;", null, label0, label1, 0)
        methodVisitor.visitMaxs(1, 1)
        methodVisitor.visitEnd()
    }

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


    private fun toByteArray(cn: ClassNode): ByteArray? {
        val cw = ClassWriter(ClassWriter.COMPUTE_MAXS)
        cn.accept(cw)
        return cw.toByteArray()
    }
    fun needHandle(className:String):Boolean {
        val flag =  className.contains("PersonService")
//        val flag =  className.contains("PersonService") || className.contains("MoshiFragment")
//        System.out.println("test:"+flag+">>>"+className)
        if(flag){
            System.out.println("test:"+className)
        }
        return flag
    }

    fun handleTestClass2(srcClass: ByteArray): ByteArray {
        val classReader = ClassReader(srcClass)
        val classWriter = ClassWriter(0)
        val ca = MyClassAdapter(classWriter)
        classReader.accept(ca,0)
        return classWriter.toByteArray()
    }

    fun handleTestClass3(srcClass: ByteArray):ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)
        val classWriter = ClassWriter(0)
        var methodVisitor: MethodVisitor
        classNode.accept(classWriter)

        genField(classWriter)

        classWriter.visitEnd()
        return classWriter.toByteArray()
    }

    private fun genField(classWriter: ClassWriter) {
        genStaticField(classWriter)
        genIntField(classWriter)
        genStringField(classWriter)
        modifyField(classWriter)
    }

    fun modifyField(classWriter: ClassWriter) {
        val methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        val label2 = Label();
        methodVisitor.visitLabel(label2);
        methodVisitor.visitLineNumber(9, label2);
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitLdcInsn("modifyStr");
        methodVisitor.visitFieldInsn(PUTFIELD, "com/bn/pd/javassist/PersonService", "testStr1", "Ljava/lang/String;");
        methodVisitor.visitInsn(RETURN)
        methodVisitor.visitEnd();
    }

    fun genStaticField(classWriter: ClassWriter) {
        val fieldVisitor = classWriter.visitField(
            ACC_PUBLIC or ACC_FINAL or ACC_STATIC,
            "GEN_TAG",
            "Ljava/lang/String;",
            null,
            "PersonService2"
        )
        fieldVisitor.visitEnd()
    }
    fun genIntField(classWriter: ClassWriter) {
        val fieldVisitor = classWriter.visitField(0, "testInt", "I", null, null);
        fieldVisitor.visitEnd();
    }
    fun genStringField(classWriter: ClassWriter) {
        val fieldVisitor = classWriter.visitField(0, "testString", "Ljava/lang/String;", null, null);
        fieldVisitor.visitEnd();
    }
    
    fun handleTestClass4(srcClass: ByteArray):ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)
        val classWriter = ClassWriter(0)
        var methodVisitor: MethodVisitor
        classNode.methods.clear()
        classNode.accept(classWriter)





        var fieldVisitor = classWriter.visitField(0, "test1", "I", null, null)
        fieldVisitor.visitEnd()


        fieldVisitor = classWriter.visitField(0, "test2", "I", null, null)
        fieldVisitor.visitEnd()


        fieldVisitor = classWriter.visitField(0, "testStr1", "Ljava/lang/String;", null, null)
        fieldVisitor.visitEnd()


        fieldVisitor = classWriter.visitField(0, "testStr2", "Ljava/lang/String;", null, null)
        fieldVisitor.visitEnd()


        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        methodVisitor.visitCode()
        var label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(3, label0)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        var label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(5, label1)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitInsn(ICONST_0)
        methodVisitor.visitFieldInsn(PUTFIELD, "com/bn/pd/javassist/PersonService", "test1", "I")
        var label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLineNumber(6, label2)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitInsn(ICONST_1)
        methodVisitor.visitFieldInsn(PUTFIELD, "com/bn/pd/javassist/PersonService", "test2", "I")
        var label3 = Label()
        methodVisitor.visitLabel(label3)
        methodVisitor.visitLineNumber(7, label3)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitLdcInsn("test1")
        methodVisitor.visitFieldInsn(
            PUTFIELD,
            "com/bn/pd/javassist/PersonService",
            "testStr1",
            "Ljava/lang/String;"
        )
        var label4 = Label()
        methodVisitor.visitLabel(label4)
        methodVisitor.visitLineNumber(8, label4)
        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitLdcInsn("test2")
        methodVisitor.visitFieldInsn(
            PUTFIELD,
            "com/bn/pd/javassist/PersonService",
            "testStr2",
            "Ljava/lang/String;"
        )
        methodVisitor.visitInsn(RETURN)
        var label5 = Label()
        methodVisitor.visitLabel(label5)
        methodVisitor.visitLocalVariable(
            "this",
            "Lcom/bn/pd/javassist/PersonService;",
            null,
            label0,
            label5,
            0
        )
        methodVisitor.visitMaxs(2, 1)
        methodVisitor.visitEnd()


        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "getPerson", "()V", null, null)
        methodVisitor.visitCode()
        label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(10, label0)
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        methodVisitor.visitLdcInsn("get Person")
        methodVisitor.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        )
        label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(11, label1)
        methodVisitor.visitInsn(RETURN)
        label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLocalVariable(
            "this",
            "Lcom/bn/pd/javassist/PersonService;",
            null,
            label0,
            label2,
            0
        )
        methodVisitor.visitMaxs(2, 1)
        methodVisitor.visitEnd()


        methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "personFly", "()V", null, null)
        methodVisitor.visitCode()
        label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(14, label0)
        methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        methodVisitor.visitLdcInsn("oh my god,I can fly")
        methodVisitor.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        )
        label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLineNumber(15, label1)
        methodVisitor.visitInsn(RETURN)
        label2 = Label()
        methodVisitor.visitLabel(label2)
        methodVisitor.visitLocalVariable(
            "this",
            "Lcom/bn/pd/javassist/PersonService;",
            null,
            label0,
            label2,
            0
        )
        methodVisitor.visitMaxs(2, 1)
        methodVisitor.visitEnd()


        methodVisitor =
            classWriter.visitMethod(ACC_PUBLIC, "toastText", "()Ljava/lang/String;", null, null)
        methodVisitor.visitCode()
        label0 = Label()
        methodVisitor.visitLabel(label0)
        methodVisitor.visitLineNumber(18, label0)
        methodVisitor.visitLdcInsn("This is modify toast text")
        methodVisitor.visitInsn(ARETURN)
        label1 = Label()
        methodVisitor.visitLabel(label1)
        methodVisitor.visitLocalVariable(
            "this",
            "Lcom/bn/pd/javassist/PersonService;",
            null,
            label0,
            label1,
            0
        )
        methodVisitor.visitMaxs(1, 1)
        methodVisitor.visitEnd()

        classWriter.visitEnd()

        return classWriter.toByteArray()
    }

    fun handleTestClass(srcClass: ByteArray): ByteArray {
        val classNode = ClassNode(ASM5)
        val classReader = ClassReader(srcClass)
        //1 将读入的字节转为classNode
        classReader.accept(classNode, 0)
        val methods = classNode.methods.iterator()

//        for(method in methods){
//            if(method.name.contains("getPerson")){
//                System.out.println("method:"+method.name)
//                classNode.methods.remove(method)
//            }
//        }

        classNode.methods.clear()
        classNode.fields.add(FieldNode(ACC_PUBLIC + ACC_FINAL + ACC_STATIC,
                "EQUAL", "I", null, 0))

        classNode.methods.add(MethodNode(ACC_PUBLIC + ACC_ABSTRACT,
                "compareTo", "(Ljava/lang/Object;)I", null, null))

        val m1 = createMethod()
        m1?.let {
            classNode.methods.add(it)
        }
        val classWriter = ClassWriter(0)
        //3  将classNode转为字节数组
        classNode.accept(classWriter)
        return classWriter.toByteArray()
    }

    class MyClassAdapter(cv:ClassVisitor):ClassNode(ASM4) {
        override fun visitEnd() {
//            super.visitEnd()
            accept(cv)
        }
    }


    @Throws(Exception::class)
    fun createPerson() {
        val pool = ClassPool.getDefault()

        // 1. 创建一个空类
        val cc = pool.makeClass("com.a.dproject.javassist.Person")
        cc.defrost()

        // 2. 新增一个字段 private String name;
        // 字段名为name
        val param = CtField(pool["java.lang.String"], "name", cc)
        // 访问级别是 private
        param.modifiers = Modifier.PRIVATE
        // 初始值是 "xiaoming"
        cc.addField(param, CtField.Initializer.constant("xiaoming"))

        // 3. 生成 getter、setter 方法
        cc.addMethod(CtNewMethod.setter("setName", param))
        cc.addMethod(CtNewMethod.getter("getName", param))

        // 4. 添加无参的构造函数
        var cons = CtConstructor(arrayOf(), cc)
        cons.setBody("{name = \"xiaohong\";}")
        cc.addConstructor(cons)

        // 5. 添加有参的构造函数
        cons = CtConstructor(arrayOf(pool["java.lang.String"]), cc)
        // $0=this / $1,$2,$3... 代表方法参数
        cons.setBody("{$0.name = $1;}")
        cc.addConstructor(cons)

        // 6. 创建一个名为printName方法，无参数，无返回值，输出name值
        val ctMethod = CtMethod(CtClass.voidType, "printName", arrayOf(), cc)
        ctMethod.modifiers = Modifier.PUBLIC
        ctMethod.setBody("{System.out.println(name);}")
        cc.addMethod(ctMethod)

//        D:\dev\studio\DProject\app\src\main\java\com\a\dproject\javassist\PersonService.java
        //这里会将这个创建的类对象编译为.class文件
        cc.writeFile("D:\\dev\\studio\\DProject\\app\\src\\main\\java\\")
    }

    @Throws(java.lang.Exception::class)
    fun updatePerson() {
        val pool = ClassPool.getDefault()
        pool.appendClassPath("D:\\dev\\studio\\DProject\\app\\build\\intermediates\\javac\\debug\\classes")
        val cc = pool.get("com.a.dproject.javassist.PersonService")
//        val cc = pool["com.a.dproject.javassist.PersonService"]
        cc.defrost()
        val personFly = cc.getDeclaredMethod("personFly")
        personFly.insertBefore("System.out.println(\"起飞之前准备降落伞\");")
        personFly.insertAfter("System.out.println(\"成功落地。。。。\");")


        //新增一个方法
        val ctMethod = CtMethod(CtClass.voidType, "joinFriend", arrayOf(), cc)
        ctMethod.modifiers = Modifier.PUBLIC
        ctMethod.setBody("{System.out.println(\"i want to be your friend\");}")
        cc.addMethod(ctMethod)
        val person = cc.toClass().newInstance()
        // 调用 personFly 方法
        val personFlyMethod = person.javaClass.getMethod("personFly")
        personFlyMethod.invoke(person)
        //调用 joinFriend 方法
        val execute = person.javaClass.getMethod("joinFriend")
        execute.invoke(person)
    }


}