package pro.dengyi.test;

import org.junit.Test;

/**
 * 协议测试类
 *
 * @author 邓艺
 * @version v1.0
 * @date 2018-12-26 14:42
 */
public class ProtocolTest {

    @Test
    public void demo1() {
        long n =10;
        byte[] bs;
        bs = new byte[8];
        bs[0] = (byte) ((n >> 56) & 0xFF);
        bs[1] = (byte) ((n >> 48) & 0xFF);
        bs[2] = (byte) ((n >> 40) & 0xFF);
        bs[3] = (byte) ((n >> 32) & 0xFF);
        bs[4] = (byte) ((n >> 24) & 0xFF);
        bs[5] = (byte) ((n >> 16) & 0xFF);
        bs[6] = (byte) ((n >> 8) & 0xFF);
        bs[7] = (byte) (n & 0xFF);

        for (byte b : bs) {

            System.out.println(b);
        }
    }

     @Test
     public void demo2(){
        byte[] b= new byte[]{0,0,0,0,0,0,0,10};
         byte[] header = new byte[10];
         System.arraycopy(b,0,header,0,8);
         for (byte b1 : header) {
             System.out.println(b1);
         }
     }
      @Test
      public void demo3(){

      }

}
