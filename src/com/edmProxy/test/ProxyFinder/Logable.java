
/**
 * @author ����� hmily_yu@hotmail.com
 * Logable�ӿ��ṩһ��ļ�¼��Ϣ����
 */
package com.edmProxy.test.ProxyFinder;

public interface Logable {
    public void addLog(String log);

    public void addResult(String log);

    public void setStatus();

    public boolean getFlag();
}
