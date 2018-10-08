package com.blankj.utilcode.util;

import com.blankj.util.ZipUtils;
import com.google.gson.Gson;

import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.HashMap;

import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/09/21
 *     desc  :
 * </pre>
 */
public class BusTest {

    //    @Test
//    public void test() throws Exception {
//        ClassPool classPool = ClassPool.getDefault();
//        //定义User类
//        CtClass ctClassUser = classPool.makeClass("com.blankj.bus.User");
//
//        //定义name字段
//        CtClass fieldType = classPool.get("java.lang.String");//字段类型
//        String name = "name";//字段名称
//        CtField ctFieldName = new CtField(fieldType, name, ctClassUser);
//        ctFieldName.setModifiers(Modifier.PRIVATE);//设置访问修饰符
//        ctClassUser.addField(ctFieldName, CtField.Initializer.constant("javasssit"));//添加name字段，赋值为javassist
//
//        //定义构造方法
//        CtConstructor constructor0 = new CtConstructor(new CtClass[]{}, ctClassUser);
//        constructor0.setBody("{}");
//        ctClassUser.addConstructor(constructor0);
//
//        //定义构造方法
//        CtClass[] parameters = new CtClass[]{classPool.get("java.lang.String")};//构造方法参数
//        CtConstructor constructor = new CtConstructor(parameters, ctClassUser);
//        String body = "{this.name=$1;}";//方法体 $1表示的第一个参数
//        constructor.setBody(body);
//        ctClassUser.addConstructor(constructor);
//
//        //setName getName方法
//        ctClassUser.addMethod(CtNewMethod.setter("setName", ctFieldName));
//        ctClassUser.addMethod(CtNewMethod.getter("getName", ctFieldName));
//
//        //toString方法
//        CtClass returnType = classPool.get("java.lang.String");
//        String methodName = "toString";
//        CtMethod toStringMethod = new CtMethod(returnType, methodName, null, ctClassUser);
//        toStringMethod.setModifiers(Modifier.PUBLIC);
//        String methodBody = "{return \"name=\"+$0.name;}";//$0表示的是this
//        toStringMethod.setBody(methodBody);
//        ctClassUser.addMethod(toStringMethod);
//
//        //代表class文件的CtClass创建完成，现在将其转换成class对象
//        Class clazz = ctClassUser.toClass();
//        Constructor cons = clazz.getConstructor(String.class);
//        Object user = cons.newInstance("wangxiaoxiao");
//        Method toString = clazz.getMethod("toString");
//        System.out.println(toString.invoke(user));
//
//        ctClassUser.writeFile("./src/test/java");//在当前目录下，生成com/tianshouzhi/User.class文件
//    }
//
    @Test
    public void findClass() throws Exception {


        ClassPool classPool = ClassPool.getDefault();
        ClassPath classPath = classPool.appendClassPath("./src/test/java/com/blankj/utilcode/util");
        File file = new File("./src/test/java/com/blankj/utilcode/util");

        File[] files = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(".class");
            }
        });

        final Class iBusClass = Class.forName("com.blankj.utilcode.util.BusUtils$IBus");

        final CtClass iBusCtClass = classPool.makeClass("com.blankj.utilcode.util.BusUtils$IBus");
        final CtClass busManagerCtClass = classPool.get("com.blankj.utilcode.util.BusUtils$BusManager");


        StringBuilder sb = new StringBuilder();
        sb.append("static {");

        for (File file1 : files) {
            String fileName = file1.getAbsolutePath();
            String className = fileName.substring(fileName.indexOf("/java/") + 6).replace("/", ".");
            CtClass ctClass = classPool.get(className.substring(0, className.length() - 6));

            if (ctClass.isInterface()) return;
            try {
                ctClass.getDeclaredMethod("getName");
            } catch (Exception e) {
                continue;
            }
            if (iBusClass.isAssignableFrom(ctClass.toClass())) {
                System.out.println(className);
            }

//            if (ctClass.isInterface() || !ctClass.subtypeOf(iBusCtClass)) continue;
//            try {
//                ctClass.getDeclaredMethod("getName");
//            } catch (Exception e) {
//                continue;
//            }
//            System.out.println(className);
//            ctClass.debugWriteFile();


//            CtClass[] interfaces = ctClass.getInterfaces();
//            if (interfaces.length > 0) {
//                for (CtClass anInterface : interfaces) {
//                    System.out.println(anInterface);
//                    if (anInterface.getName().equals("com.blankj.utilcode.util.BusUtils$IBus")) {
//                        Class aClass = ctClass.toClass();
//                        BusUtils.IBus o = (BusUtils.IBus) aClass.newInstance();
//                        BusUtils.BusManager.register(o);
//
//                        break;
//                    }
//                }
//            }


            sb.append("");

            ctClass.detach();
        }
        sb.append("}");


    }

    @Test
    public void testStaticCall() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath("./src/test/java/com/blankj/utilcode/util");

        CtClass ctClass = classPool.get("com.blankj.utilcode.util.BusImpl");
        CtConstructor ctConstructor = ctClass.makeClassInitializer();

        ctConstructor.insertAfter("run((com.blankj.utilcode.util.BusUtils.IBus)Class.forName(\"com.blankj.utilcode.util.BusImpl\").newInstance());");
        CtClass etype = ClassPool.getDefault().get("java.lang.Exception");
        ctConstructor.addCatch("{$e.printStackTrace();return;}", etype);
//        ctConstructor.addCatch("run((com.blankj.utilcode.util.BusUtils.IBus)Class.forName(\"com.blankj.utilcode.util.BusImpl\").newInstance());", classPool.get("java.lang.Exception"));
//        ctClass.addConstructor(ctConstructor);
        ctClass.debugWriteFile();
//        ctClass.debugWriteFile("./src/test/java/com/blankj/utilcode/util");

//        CtClass ctClass1 = classPool.get("com.blankj.utilcode.util.BusImpl");
//        ctClass.toClass().getDeclaredMethod("run").invoke(null);
        ctClass.detach();

    }


    @Test
    public void testBus() throws Exception {
//        ClassPool classPool = ClassPool.getDefault();
//        classPool.appendClassPath("./src/test/java/com/blankj/utilcode/util");
//
//        CtClass ctClass = classPool.get("com.blankj.utilcode.util.BusClass");
//
//
//        boolean b = ctClass.hasAnnotation(Class.forName("com.blankj.utilcode.util.BusUtils$Bus"));
//        System.out.println(b);
//
//        Class staticMethodClass = Class.forName("com.blankj.utilcode.util.BusUtils$Subscribe");
//
//        Class busClass = Class.forName("com.blankj.utilcode.util.BusUtils$Bus");
//        String name = null;
//        String methodName = null;
//        String returnName = null;
//
//        CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
//        for (CtMethod declaredMethod : declaredMethods) {
//            if (declaredMethod.hasAnnotation(staticMethodClass)) {
//                name = ((BusUtils.Subscribe) declaredMethod.getAnnotation(staticMethodClass)).name();
//                methodName = declaredMethod.getLongName();
//                returnName = declaredMethod.getReturnType().getName();
//                System.out.println(methodName);
//                System.out.println(returnName);
//            }
//        }
//
//        String insertAfter = processInsertAfter(name, methodName, returnName);
//
//
//        CtClass busUtilsClass = classPool.get("com.blankj.utilcode.util.BusUtils");
//        CtMethod callMethod = busUtilsClass.getDeclaredMethod("call");
//        callMethod.insertAfter(insertAfter);
//        busUtilsClass.debugWriteFile("./src/test/java");
//
//        BusUtils.call("busClass");
    }

    private String processInsertAfter(String name, String methodName, String returnName) {
        StringBuilder sb = new StringBuilder();
        sb.append("if (\"").append(name).append("\".equals($1)) {\n");

        int st = methodName.indexOf('(');
        int end = methodName.length();
        String substring = methodName.substring(st + 1, end - 1);
        String[] split = substring.split(",");
        StringBuilder args = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            args.append(",(").append(split[i]).append(")$2[").append(i).append("]");
        }
        methodName = methodName.substring(0, st + 1) + args.substring(1) + ")";

        if (returnName.equals("void")) {
            sb.append(methodName).append(";\n").append("return null;\n");
        } else {
            sb.append("return ").append(methodName).append(";\n");
        }

        return sb.append("}").toString();
    }


    @Test
    public void testJar() throws Exception {

    }
}
