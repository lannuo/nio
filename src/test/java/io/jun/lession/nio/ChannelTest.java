package io.jun.lession.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 一、通道（Channel）:用于源节点和目标节点的连接。在java nio 中负责缓冲区中数据的传输，Channel本身不存储数据，
 * 因此需要配合缓冲区进行传输。
 *
 * 二、通道channel的主要实现类：
 * java.nio.channels.Channel 接口：
 *   -- FileChannel 本地
 *   -- SocketChannel 网络 tcp
 *   -- ServerSocketChannel 网络 tcp
 *   -- DatagramChannel 网络 udp
 *
 * 三、获取通道
 * 1.java针对支持通道的类提供了getChannel()方法
 *   本地io：
 *   FileInputStream/FileOutputStream
 *   RandomAccessFile
 *
 *   网络io:
 *   Socket
 *   ServerSocket
 *   DatagramSocket
 * 2.在jdk1.7 中的NIO.2针对各个通道提供了静态方法open();
 *
 * 3.在jdk1.7 中的NIO.2 的Files工具类的newByteChannel();
 *
 * 4.通道之间的数据传输
 * transferFrom()
 * transferTo()
 *
 */
public class ChannelTest {
    /**
     * 1.利用通道完成文件的复制
     */
    @Test
    public void test(){
        FileInputStream fis=null;
        FileOutputStream fos=null;
        FileChannel fisChannel=null;
        FileChannel fosChannel=null;
        try {
            fis=new FileInputStream("1.jpg");
            fos=new FileOutputStream("2.jpg");

            //1.获取通道
            fisChannel = fis.getChannel();
            fosChannel = fos.getChannel();
            //2.分配指定大小的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //3.将通道中的数据存到缓冲区中
            while (fisChannel.read(buffer)!=-1){
                buffer.flip();//切换读取数据模式
                //4.将缓冲区中的数据写到通道中
                fosChannel.write(buffer);
                buffer.clear();//清空缓冲区
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fosChannel!=null){
                try {
                    fosChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fisChannel!=null){
                try {
                    fisChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 使用直接缓冲区完成文件的复制（内存映射文件）
     */
    @Test
    public void test2()throws IOException{
        FileChannel inChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.CREATE_NEW, StandardOpenOption.READ, StandardOpenOption.WRITE);
        //内存映射文件
        MappedByteBuffer inMappedBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBuf=outChannel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());

        //直接对缓冲区进行数据的读写操作
        byte[] dst=new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);

        inChannel.close();
        outChannel.close();
    }

    /**
     * 通道直接数据传输(直接缓冲区的方式)
     * @throws IOException
     */
    @Test
    public void test3()throws IOException{
        FileChannel inChannel = FileChannel.open(Paths.get("2.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);

        inChannel.transferTo(0,inChannel.size(),outChannel);
//        outChannel.transferFrom(inChannel,0,inChannel.size());
        inChannel.close();
        outChannel.close();
    }
}
