package cn.uestc;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.WebContextFactory;

public class Meg {

	//
    // 载入页面时调用，传入name值作为推送的标识
    public void onPageLoad(String name) {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        session.setAttribute("name", name);
    }
    
    public void addMessage(String userid, String message) {
        final String userId = userid;
        final String autoMessage = message;
        ScriptSession session = WebContextFactory.get().getScriptSession();
        final String from = session.getAttribute("name").toString();
        System.out.println("From: " + from + ", To: " + userid + ", Msg: "
                + message);
        // 通过ScriptSessionFilter筛选符合条件的ScriptSession
        Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
            // 实现match方法，条件为真为筛选出来的session
            public boolean match(ScriptSession session) {
                String name = session.getAttribute("name").toString();
                return name == null ? false : userId.equals(name);
            }
        }, new Runnable() {
            private ScriptBuffer script = new ScriptBuffer();
            public void run() {
                // 设定前台接受消息的方法和参数
                script.appendCall("receiveMessages", autoMessage);
                Collection<ScriptSession> sessions = Browser
                        .getTargetSessions();
                // 向所有符合条件的页面推送消息
                for (ScriptSession scriptSession : sessions) {
                    if (scriptSession.getAttribute("name").equals(userId)) {
                        scriptSession.addScript(script);
                    }
                }
            }
        });
    }
}
