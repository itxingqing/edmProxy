
/**
 * @author ����� hmily_yu@hotmail.com
 * DisplayTask�ඨʱ���µ�ǰ����״̬
 */
package com.edmProxy.test.ProxyFinder;

//~--- JDK imports ------------------------------------------------------------

import java.util.TimerTask;

public class DisplayTask extends TimerTask {
    private Logable m_logger;

    public DisplayTask(Logable logger) {
        m_logger = logger;
    }

    @Override
    public void run() {

        // TODO Auto-generated method stub
        m_logger.setStatus();
    }
}
