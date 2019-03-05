package io.jun.lession.nio;


import org.junit.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * 一、缓冲区：buffer
 * 在java nio中负责数据的存取。缓冲区就是数组，用于存取不同数据类型的数据
 * <p>
 * 根据数据类型不同（boolean除外），提供了相应类型的缓冲区：
 * ByteBuffer
 * CharBuffer
 * ShortBuffer
 * IntBuffer
 * LongBuffer
 * FloatBuffer
 * DoubleBuffer
 * <p>
 * 上述缓冲区管理方式几乎一致，通过allocate()获取缓冲区
 * <p>
 * 二、缓冲区用于存取数据的两个核心方法：
 * put(): 存入数据到缓冲区中
 * get(): 获取缓冲区中的数据
 * <p>
 * 三、缓冲区中4个的核心属性
 * capacity:容量，表示缓冲区中最大的存储数据的容量。一旦声明不能改变。
 * limit: 界限，表示缓冲区中可以操作数据的大小（limit 后面的数据是不能进行读写的）
 * position: 位置，表示缓冲区中正在操作数据的位置。
 * mark: 标记，表示记录当前position的位置，可以通过reset()恢复到mark的位置
 * <p>
 * 0<=mark<=position <= limit <= capacity
 * <p>
 * 四、直接缓冲区和非直接缓冲区：
 * 非直接缓冲区：通过allocate() 方法分配缓冲区，将缓存区建立在JVM的内存中
 * 直接缓冲区：通过allocateDirect()方法分配直接缓冲区，将缓存区建立在操作系统的物理内存中（使用情况特定）
 */
public class BufferTest {

    @Test
    public void test1() {
        //1.分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("----------allocate----------------");
        //查看当前缓冲区的位置
        System.out.println(byteBuffer.position());
        //查看当前缓冲区的界限
        System.out.println(byteBuffer.limit());
        //查看当前缓冲区的容量
        System.out.println(byteBuffer.capacity());

        //2.利用put 添加数据到缓冲区中
        String str = "abcde";
        byteBuffer.put(str.getBytes());
        System.out.println("----------put----------------");
        //查看当前缓冲区的位置
        System.out.println(byteBuffer.position());
        //查看当前缓冲区的界限
        System.out.println(byteBuffer.limit());
        //查看当前缓冲区的容量
        System.out.println(byteBuffer.capacity());

        //3、切换读取数据模式
        byteBuffer.flip();
        System.out.println("----------flip----------------");
        //查看当前缓冲区的位置
        System.out.println(byteBuffer.position());
        //查看当前缓冲区的界限
        System.out.println(byteBuffer.limit());
        //查看当前缓冲区的容量
        System.out.println(byteBuffer.capacity());

        //4、读取有效数据
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst);
        System.out.println(new String(dst));
        System.out.println("----------get----------------");
        //查看当前缓冲区的位置
        System.out.println(byteBuffer.position());
        //查看当前缓冲区的界限
        System.out.println(byteBuffer.limit());
        //查看当前缓冲区的容量
        System.out.println(byteBuffer.capacity());

        //5、重读
        byteBuffer.rewind();
        byte[] dst2 = new byte[byteBuffer.limit()];
        System.out.println("----------rewind----------------");
        //查看当前缓冲区的位置
        System.out.println(byteBuffer.position());
        //查看当前缓冲区的界限
        System.out.println(byteBuffer.limit());
        //查看当前缓冲区的容量
        System.out.println(byteBuffer.capacity());

        //6、清空缓冲区，但是缓冲区中的数据依然存在，只是处于“被遗忘”状态,由于position、limit发生改变
        byteBuffer.clear();
        System.out.println("----------clear----------------");
        //查看当前缓冲区的位置
        System.out.println(byteBuffer.position());
        //查看当前缓冲区的界限
        System.out.println(byteBuffer.limit());
        //查看当前缓冲区的容量
        System.out.println(byteBuffer.capacity());

        System.out.println((char) byteBuffer.get());


    }

    @Test
    public void test2() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("abcde".getBytes());
        byteBuffer.flip();
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst, 0, 2);
        System.out.println(new String(dst, 0, 2));
        System.out.println(byteBuffer.position());

        byteBuffer.mark();//记录当前position的位置

        byteBuffer.get(dst, 2, 2);
        System.out.println(new String(dst, 2, 2));
        System.out.println(byteBuffer.position());
        byteBuffer.reset();
        System.out.println(byteBuffer.position());
        System.out.println(new String(dst));
        //判断缓冲区中是否还有剩余数据
        if (byteBuffer.hasRemaining()) {
            //获取缓冲区中还可以操作的数量
            System.out.println(byteBuffer.remaining());
        }
    }

    @Test
    public void test3() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        System.out.println(byteBuffer.isDirect());
    }
}
