package com.edmProxy.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Properties;

public class NetProxy {
	// ���Ա���JVM������ȱʡ����
	public void setLocalProxy() {
		Properties prop = System.getProperties();
		// ����http����Ҫʹ�õĴ���������ĵ�ַ
//		prop.setProperty("http.proxyHost", "183.234.16.37");
//		// ����http����Ҫʹ�õĴ���������Ķ˿�
//		prop.setProperty("http.proxyPort", "80");
		// ���ò���Ҫͨ��������������ʵ�����������ʹ��*ͨ����������ַ��|�ָ�
		//prop.setProperty("http.nonProxyHosts", "localhost|10.10.*");

		// ���ð�ȫ����ʹ�õĴ����������ַ��˿�
		// ��û��https.nonProxyHosts���ԣ�������http.nonProxyHosts �����õĹ������
//		prop.setProperty("https.proxyHost", "183.234.16.37");
//		prop.setProperty("https.proxyPort", "443");
		
		
//
//		// ʹ��ftp������������������˿��Լ�����Ҫʹ��ftp���������������
//		prop.setProperty("ftp.proxyHost", "183.234.16.37");
//		prop.setProperty("ftp.proxyPort", "2121");
//		prop.setProperty("ftp.nonProxyHosts", "localhost|10.10.*");
//
//		// socks����������ĵ�ַ��˿�
		prop.setProperty("socksProxyHost", "61.19.42.244");
		prop.setProperty("socksProxyPort", "80");
	}

	// ���proxy����
	public void removeLocalProxy() {
		Properties prop = System.getProperties();
		prop.remove("http.proxyHost");
		prop.remove("http.proxyPort");
		prop.remove("http.nonProxyHosts");

		prop.remove("https.proxyHost");
		prop.remove("https.proxyPort");

		prop.remove("ftp.proxyHost");
		prop.remove("ftp.proxyPort");
		prop.remove("ftp.nonProxyHosts");

		prop.remove("socksProxyHost");
		prop.remove("socksProxyPort");
	}

	//   

	// ����http
	public void showHttpProxy(Object... proxy) {
		URL url = null;
		try {
			url = new URL("http://blog.csdn.com/smallnest");
		} catch (MalformedURLException e) {
			return;
		}
		try {
			URLConnection conn = null;
			switch (proxy.length) {
			case 0:
				conn = url.openConnection();
				break;
			case 1:
				conn = url.openConnection((Proxy) proxy[0]);
				break;
			default:
				break;
			}

			if (conn == null)
				return;

			conn.setConnectTimeout(3000); // �������ӳ�ʱʱ��
			InputStream in = conn.getInputStream();
			byte[] b = new byte[1024];
			try {
				while (in.read(b) > 0) {
					System.out.println(new String(b));
				}
			} catch (IOException e1) {
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// ����ftp
	public void showFtpProxy(Object... proxy) {
		URL url = null;
		try {
			url = new URL("ftp://ftp.tsinghua.edu.cn");
		} catch (MalformedURLException e) {
			return;
		}
		try {
			URLConnection conn = null;
			switch (proxy.length) {
			case 0:
				conn = url.openConnection();
				break;
			case 1:
				conn = url.openConnection((Proxy) proxy[0]);
				break;
			default:
				break;
			}

			if (conn == null)
				return;

			conn.setConnectTimeout(3000); // �������ӳ�ʱʱ��
			InputStream in = conn.getInputStream();
			byte[] b = new byte[1024];
			try {
				while (in.read(b) > 0) {
					System.out.println(new String(b));
				}
			} catch (IOException e1) {
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	// �õ�һ��proxy
	public Proxy getProxy(Proxy.Type type, String host, int port) {
		SocketAddress addr = new InetSocketAddress(host, port);
		Proxy typeProxy = new Proxy(type, addr);
		return typeProxy;
	}

	public static void main(String[] args) {
		NetProxy proxy = new NetProxy();
		// ���Դ��������
		proxy.setLocalProxy();
		proxy.showHttpProxy();

		// �������������ϵͳ���ԣ���ͨ��Proxy��ָ�����������
		// proxy.removeLocalProxy
		// proxy.showHttpProxy(proxy.getProxy(Proxy.Type.SOCKS,"10.10.0.96",1080));

	}
}