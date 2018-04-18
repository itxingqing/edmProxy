package com.edmProxy.util.proxy.check;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

/**
 * Socket factory for Socks5 proxy
 * 
 * @author Atul Aggarwal
 */
public class Socks5ProxySocketFactory extends SocketFactory
{
	
	public static void main(String[] args) throws IOException {
		ProxyInfo proxy=new ProxyInfo();
		proxy.setProxyAddress("107.149.216.10");
		proxy.setProxyPort(1080);
		proxy.setProxyUsername("User-001");
		proxy.setProxyPassword("123456");
		Socks5ProxySocketFactory Socks5ProxySocketFactory=new Socks5ProxySocketFactory(proxy);
		Socks5ProxySocketFactory.socks5ProxifiedSocket(proxy.getProxyAddress(),proxy.getProxyPort());
	}
	
	
    private ProxyInfo proxy;
    
    public Socks5ProxySocketFactory(ProxyInfo proxy)
    {
        this.proxy = proxy;
    }

    public Socket createSocket(String host, int port) 
        throws IOException, UnknownHostException
    {
        return socks5ProxifiedSocket(host,port);
    }

    public Socket createSocket(String host ,int port, InetAddress localHost,
                                int localPort)
        throws IOException, UnknownHostException
    {
        
        return socks5ProxifiedSocket(host,port);
        
    }

    public Socket createSocket(InetAddress host, int port)
        throws IOException
    {
        
        return socks5ProxifiedSocket(host.getHostAddress(),port);
        
    }

    public Socket createSocket( InetAddress address, int port, 
                                InetAddress localAddress, int localPort) 
        throws IOException
    {
        
        return socks5ProxifiedSocket(address.getHostAddress(),port);
        
    }
    
    private Socket socks5ProxifiedSocket(String host, int port) 
        throws IOException
    {
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        String proxy_host = proxy.getProxyAddress();
        int proxy_port = proxy.getProxyPort();
        String user = proxy.getProxyUsername();
        String passwd = proxy.getProxyPassword();
        
        try
        {
            socket=new Socket(proxy_host, proxy_port); 

           // System.out.println(socket.getSoTimeout());
            in=socket.getInputStream();
            out=socket.getOutputStream();

            socket.setTcpNoDelay(true);

            byte[] buf=new byte[1024];
            int index=0;

/*
                   +-----+----------------+---------------+
                   |VER | NMETHODS | METHODS  |
                   +-----+----------------+---------------+
                   |   1   |          1         |    1 to 255   |
                   +-----+----------------+---------------+

   The VER field is set to X'05' for this version of the protocol.  The
   NMETHODS field contains the number of method identifier octets that
   appear in the METHODS field.
   
   VER(�汾)�����Э��汾�б�����ΪX'05'��NMETHODS(����ѡ��)�а�������METHODS(����)�г��ֵķ�����ʶ ��λ�����Ŀ��


   The values currently defined for METHOD are:

          o  X'00' NO AUTHENTICATION REQUIRED
          o  X'01' GSSAPI
          o  X'02' USERNAME/PASSWORD
          o  X'03' to X'7F' IANA ASSIGNED
          o  X'80' to X'FE' RESERVED FOR PRIVATE METHODS
          o  X'FF' NO ACCEPTABLE METHODS
          
          
          ��ǰ�������METHOD��ֵ�У� 
����>> X'00' ����֤���� 
����>> X'01' ͨ�ð�ȫ����Ӧ�ó���ӿ�(GSSAPI) 
����>> X'02' �û���/����(USERNAME/PASSWORD) 
����>> X'03' �� X'7F' IANA ����(IANA ASSIGNED) 
����>> X'80' �� X'FE' ˽�˷�������(RESERVED FOR PRIVATE METHODS) 
����>> X'FF' �޿ɽ��ܷ���(NO ACCEPTABLE METHODS) 
    ***IANA�Ǹ���ȫ��INTERNET�ϵ�IP��ַ���б�ŷ���Ļ���(������)***

*/

            buf[index++]=5;

            buf[index++]=2;//��ʾMETHODS �ֶ����������ֽڵı�ʶ
            buf[index++]=0;           // NO AUTHENTICATION REQUIRED
            buf[index++]=2;           // USERNAME/PASSWORD

            out.write(buf, 0, index);

/*
    The server selects from one of the methods given in METHODS, and
    sends a METHOD selection message:
    
    ��������METHODS�����ķ�����ѡ��һ�֣�����һ��METHOD selection(����ѡ��)���ģ�

                         +-----+------------+
                         |VER | METHOD |
                         +-----+------------+
                         |    1  |       1        |
                         +-----+------------+
*/
      //in.read(buf, 0, 2);
            fill(in, buf, 2);

            boolean check=false;
            switch((buf[1])&0xff)
            {
                case 0:                // NO AUTHENTICATION REQUIRED  ���������ѡ���ˡ�����֤���󡱣���ôֱ�ӷ���
                    check=true;
                    break;
                case 2:                // USERNAME/PASSWORD  ���������ѡ���ˡ��û���/���롱����ôʹ�á��û���/���롱������Э��
                    if(user==null || passwd==null)
                    {
                        break;
                    }

/*
 * Ȼ��ͻ��ͷ�����������ѡ����֤��������������Э�̹���(sub-negotiation)(����case 2:�Ѿ�ѡ���ˡ��û���/���롱��Э��)�����ֲ�ͬ�ķ�������Э�̹��̵�������ο����Եı���¼��
 * 
   Once the SOCKS V5 server has started, and the client has selected the
   Username/Password Authentication protocol, the Username/Password
   subnegotiation begins.  This begins with the client producing a
   Username/Password request:
   
   һ��SOCKS V5���������У����ҿͻ���ѡ�����û���/������֤Э���Ժ󣬾Ϳ�ʼ���û���/����Э�����Э�̹��̡��ͻ����Ȳ���һ���û���/����Э�������


           +-----+--------+------------+--------+--------------+
           |VER | ULEN |  UNAME   | PLEN |  PASSWD  |
           +-----+--------+------------+--------+--------------+
           |   1   |    1     |  1 to 255  |   1     |   1 to 255   |
           +-----+--------+------------+--------+--------------+

   The VER field contains the current version of the subnegotiation,
   which is X'01'. The ULEN field contains the length of the UNAME field
   that follows. The UNAME field contains the username as known to the
   source operating system. The PLEN field contains the length of the
   PASSWD field that follows. The PASSWD field contains the password
   association with the given UNAME.
   
   VER��ָ������Э�̵ĵ�ǰ�汾������ʹ�õ���X��01����ULEN���а�������һ��UNAME��ĳ��ȡ�
   UNAME�а���һ��Դ����ϵͳ(source operating system)��֪�����û�����
   PLEN��ָ���˽�������PASSWD�ĳ��ȡ�PASSWD��������˶�ӦUNAME�û������롣

*/
                    index=0;
                    buf[index++]=1;//����VER Ϊ X'01'
                    buf[index++]=(byte)(user.length());//����ULEN��ֵ
                    System.arraycopy(user.getBytes(), 0, buf, index, 
                        user.length());//����UNAME��ֵ
                    index+=user.length();//�ƶ������±�
                    buf[index++]=(byte)(passwd.length());//����PLEN��ֵ
                    System.arraycopy(passwd.getBytes(), 0, buf, index, 
                        passwd.length());//����PASSWD��ֵ
                    index+=passwd.length();//�ƶ������±�

                    out.write(buf, 0, index);

/*
   The server verifies the supplied UNAME and PASSWD, and sends the
   following response:
   
   ��������֤�û��������룬���ҷ��أ�

                        +-----+------------+
                        |VER | STATUS  |
                        +-----+------------+
                        |   1   |      1        |
                        +-----+------------+

   A STATUS field of X'00' indicates success. If the server returns a
   `failure' (STATUS value other than X'00') status, it MUST close the
   connection.
   
   һ��X��00����״̬����ζ�ųɹ����������������һ����failure��״̬(X'00'�����״ֵ̬)������ر����ӡ�
  **** ���STATUS�з���X��00����˵��ͨ����֤��������������ط�X��00����˵����֤ʧ�ܣ����ҹر����ӡ�****
*/
                    //in.read(buf, 0, 2);
                    fill(in, buf, 2);
                    if(buf[1]==0)
                    {
                        check=true;
                    }
                    break;
                default:
            }

            //�������������ʧ��״̬����ر�����
            if(!check)
            {
                try
                {
                    socket.close();
                }
                catch(Exception eee)
                {
                }
                //throw new ProxyException(ProxyInfo.ProxyType.SOCKS5,
                    //"fail in SOCKS5 proxy");
            }

/*
 * ������سɹ������һ��ϸ�ڵ�����
 * 
      The SOCKS request is formed as follows:      
      SOCKS�������±���ʾ��

        +-----+-------+--------+-------+---------------+---------------+
        |VER | CMD |  RSV  | ATYP | DST.ADDR | DST.PORT |
        +-----+-------+--------+-------+---------------+---------------+
        |   1   |    1    |  X'00'  |    1    |   Variable    |         2         |
        +-----+-------+--------+-------+---------------+---------------+

      Where:

      o  VER    protocol version: X'05'  VER Э��汾: X��05��
      o  CMD
         o  CONNECT X'01'
         o  BIND X'02'
         o  UDP ASSOCIATE X'03'
      o  RSV    RESERVED  ����
      o  ATYP   address type of following address  ����ĵ�ַ����
         o  IP V4 address: X'01'
         o  DOMAINNAME: X'03'  ������X��03��
         o  IP V6 address: X'04'
      o  DST.ADDR       desired destination address  Ŀ�ĵ�ַ
      o  DST.PORT desired destination port in network octet order  �������ֽ�˳����ֵ�Ŀ�ĵض˿ں�
         
         ATYP�ֶ��������˵�ַ�ֶ�(DST.ADDR��BND.ADDR)�������ĵ�ַ���ͣ�
            �� X'01'   ����IPV4��IP��ַ��4���ֽڳ�
            �� X'03'   ���������ĵ�ַ����ַ�ֶ��еĵ�һ�ֽ������ֽ�Ϊ��λ�ĸ������ĳ��ȣ�û�н�β��NUL�ֽڡ�
            �� X'04'   ����IPV6��IP��ַ��16���ֽڳ�
*/
     
            index=0;
            buf[index++]=5;
            buf[index++]=1;       // CONNECT
            buf[index++]=0;

            byte[] hostb=host.getBytes();
            int len=hostb.length;
            buf[index++]=3;      // DOMAINNAME ��ʶ�õ�ַʹ�á����������ĵ�ַ�� X'03'
            buf[index++]=(byte)(len);//��ַ�ֶ��еĵ�һ�ֽ��Ǹõ�ַ�����ĳ��ȣ����õ�һ���ֽڵ�ֵ
            System.arraycopy(hostb, 0, buf, index, len);//��ַ�ֶε�ֵ
            index+=len;
            buf[index++]=(byte)(port>>>8);//���޷��š�����λ�������>>>������ʹ���ˡ�����չ�����������������ڸ�λ����0����portת��Ϊ32λ��Ȼ������8λ��Ȼ��ת��Ϊ�ֽڣ�8λ�����������ȡ��port�ĸ�24λ�ĵ�8λ���ֽ�
            buf[index++]=(byte)(port&0xff);//���Ǹ�int��portת����ʮ��λ��byte���㣬Ȼ����ȡ0xffλ��ֵ�����һ���ֽڣ�0xff �Ķ����Ʊ�ʾΪ11111111�������ȡ��port�ĵ�8λ���ֽ�

            out.write(buf, 0, index);

/*
   The SOCKS request information is sent by the client as soon as it has
   established a connection to the SOCKS server, and completed the
   authentication negotiations.  The server evaluates the request, and
   returns a reply formed as follows:
   
   һ��������һ����SOCKS�����������ӣ������������֤��ʽ��Э�̹��̣��ͻ������ᷢ��һ��SOCKS������Ϣ�����������������������������������¸�ʽ���ش�

        +-----+------+--------+--------+---------------+--------------+
        |VER | REP |  RSV   | ATYP | BND.ADDR | BND.PORT |
        +-----+------+--------+--------+---------------+--------------+
        |   1   |    1   |  X'00'  |     1    |   Variable    |       2          |
        +-----+------+--------+--------+---------------+--------------+

   Where:

   o  VER    protocol version: X'05'
   o  REP    Reply field:
      o  X'00' succeeded
      o  X'01' general SOCKS server failure
      o  X'02' connection not allowed by ruleset
      o  X'03' Network unreachable
      o  X'04' Host unreachable
      o  X'05' Connection refused
      o  X'06' TTL expired
      o  X'07' Command not supported
      o  X'08' Address type not supported
      o  X'09' to X'FF' unassigned
   o  RSV    RESERVED
   o  ATYP   address type of following address
      o  IP V4 address: X'01'
      o  DOMAINNAME: X'03'
      o  IP V6 address: X'04'
   o  BND.ADDR       server bound address
   o  BND.PORT       server bound port in network octet order
*/

      //in.read(buf, 0, 4);
            fill(in, buf, 4);//��buf��[0]λ�����4���ȵ��ֽڸ�buf

            //������ɹ�
            if(buf[1]!=0)
            {
                try
                {
                    socket.close();
                }
                catch(Exception eee)
                {
                }
//                throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, 
//                    "server returns "+buf[1]);
            }

            //���Ӧ��ɹ������һ���鿴��ַ����,buf[3]����ATYP
            switch(buf[3]&0xff)
            {
                case 1:
                    //in.read(buf, 0, 6);
                 //�����IP V4 address: X'01'�����������������ǰλ��(�Ѿ���4)��ʼ��
                 //�������6�����ȵ��ֽڣ���ȡ�����ֽڱ���BND.ADDR��BND.PORT��ע��IP V4 �У�BND.ADDR�ĳ�����4                 
                    fill(in, buf, 6);
                    break;
                case 3:
                    //in.read(buf, 0, 1);
                 //�����DOMAINNAME: X'03'�����������������ǰλ��(�Ѿ���4)��ʼ��
                 //����������ɸ��ֽڣ���ȡ�����ֽڱ���BND.ADDR��BND.PORT��ע��DOMAINNAME: X'03'�У�
                 //��ַBND.ADDR�ֶ��еĵ�һ�ֽ��Ǹõ�ַ�����ĳ���
                    fill(in, buf, 1);//���ڵ�ַBND.ADDR�ֶ��еĵ�һ�ֽ��Ǹõ�ַ�����ĳ��ȣ����԰Ѹõ�ַ��ĳ��ȶ�ȡ������������buf[0]��
                    //in.read(buf, 0, buf[0]+2);
                    fill(in, buf, (buf[0]&0xff)+2);//������ȡʣ���ֽڣ�(buf[0]&0xff)+2  ΪBND.ADDR ��ʣ���ֽڼ���BND.PORT���ֽ�
                    break;
                case 4:
                    //in.read(buf, 0, 18);
                 //�����IP V6 address: X'04'�����������������ǰλ��(�Ѿ���4)��ʼ��
                 //�������18�����ȵ��ֽڣ���ȡ�����ֽڱ���BND.ADDR��BND.PORT��ע��IP V6 �У�BND.ADDR�ĳ�����16�����Ϻ����BND.PORT 2���ֽڵ���18���ֽ�
                    fill(in, buf, 18);//
                    break;
                default:
            }
            return socket;
            
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            try
            {
                if(socket!=null)
                {
                    socket.close(); 
                }
            }
            catch(Exception eee)
            {
            }
            String message="ProxySOCKS5: "+e.toString();
            if(e instanceof Throwable)
            {
//                throw new ProxyException(ProxyInfo.ProxyType.SOCKS5,message, 
//                    (Throwable)e);
            }
            throw new IOException(message);
        }
    }
    
    private void fill(InputStream in, byte[] buf, int len) 
      throws IOException
    {
        int s=0;
        while(s<len)
        {
         //��ͼ��ȡlen-s���ֽڵ�buf�У�������ֻ�ܶ�ȡ��i�ֽ�(i < len-s),
         //�� i < len-s ʱ����Ҫѭ����ȡ��len-s���ֽڸ�buf����ô�´ζ�ȡ��bufʱ�򣬲����ֽڵ�λ�þ���s+i
            int i=in.read(buf, s, len-s);
            if(i<=0)
            {
//                throw new ProxyException(ProxyInfo.ProxyType.SOCKS5, "stream " +
//                    "is closed");
            }
            s+=i;
        }
    }
}

