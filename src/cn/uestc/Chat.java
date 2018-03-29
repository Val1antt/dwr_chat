package cn.uestc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ui.dwr.Util;

public class Chat {
	
	//向ScriptSession中注册当前页面用户
    // 载入页面时调用，传入name值作为推送的标识
    public void onPageLoad(String name) {
        ScriptSession session = WebContextFactory.get().getScriptSession();
        session.setAttribute("name", name);
        System.out.println("注意观察已注册的name："+name);
    }
	
	public Chat() {
		boolean existUser = false;
		User user = new User("所有人", "所有人");
		for (User temp : users) {
			if (user.getUserid().equals(temp.getUserid())) {
				existUser = true;
				break;
			}
		}
		if (existUser == false) {
			users.add(user);
		}
	}

	/**
	 * 
	 * @param sender 发送者
	 * @param receiverid 接收者
	 * @param test 发送内容
	 * @param request
	 */
	public void addMessage(final String sender, final String receiverid, final String test, HttpServletRequest request) {
		
		System.out.println(this.getClass().getName()+"程序执行至此！");
		if ("所有人".equals(receiverid)) {
			
			String msg = sender + " 发送给  " + receiverid + ":" + test;
			System.out.println("1目前接受消息为："+msg);
		
			
			
			if (msg != null && msg.trim().length() > 0) {
				messages.addFirst(new Message(msg));
				while (messages.size() > 10) {
					messages.removeLast();
				}
			}
			Util.setValue("text", "");
			Browser.withCurrentPage(new Runnable() {
				public void run() {
					Util.removeAllOptions("chatlog");
					Util.addOptions("chatlog", messages, "text"); //直接操作chatlog页面元素，为其增加元素li
				}
			});
		} else if ("".equals(receiverid)) {
			String msg = "receiverid is null!";
			if (msg != null && msg.trim().length() > 0) {
				messages.addFirst(new Message(msg));
				while (messages.size() > 10) {
					messages.removeLast();
				}
			}

		} else {
			System.out.println(sender + " 发送给  " + receiverid + ":" + test);
			// 精准推送
			// 通过ScriptSessionFilter筛选符合条件的ScriptSession

			//final String  reid = new Chat().getUsernameById(receiverid);  //后台完成转换id --》 name
			//System.out.println(sender + " 发送给  " + reid + ":" + test);
			
			/*
			Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
				// 实现match方法，条件为真为筛选出来的session
				public boolean match(ScriptSession session) {
					String name = session.getAttribute("name").toString();  //每一个session个中的name属性
					System.out.println("match函数中当前session中的name:"+name);
	                return name == null ? false : receiverid.equals(name);
					//return true;
				}
			}, new Runnable() {
				
				private ScriptBuffer script = new ScriptBuffer();

				public void run() {
					System.out.println("run方法");
					// 设定前台接受消息的方法和参数
					script.appendCall("receiveMessages", test);
					Collection<ScriptSession> sessions = Browser.getTargetSessions();
					// 向所有符合条件的页面推送消息
					for (ScriptSession scriptSession : sessions) {
						System.out.println("当前session中的name:"+scriptSession.getAttribute("name"));
						System.out.println("当前的接收者:"+receiverid);
						
						if (scriptSession.getAttribute("name").equals(receiverid)) {
							System.out.println("推送成功！");
							scriptSession.addScript(script);
						}
					}
				}
			});
			
			*/
			
			//测试代码3
			//想获取list的id
			
			final String listid = "list"+receiverid;
			final String listid2 = "list2"+sender;
			final ArrayList<String> data = new ArrayList<String>();
			final ArrayList<String> data2 = new ArrayList<String>();

			String msg = sender +":给你的私信："+test;
			String msg2 =  "你私信给"+receiverid+":"+test;
			data.add(msg);
			data2.add(msg2);
			
			
			Browser.withCurrentPage(new Runnable()
	        {
	            public void run()
	            {
	                // Clear the list and add in the new set of messages
	              Util.removeAllOptions("listid");
	                Util.addOptions(listid, data, null);
	            }
	        });
			
			Browser.withCurrentPage(new Runnable()
			{
				public void run()
				{
					// Clear the list and add in the new set of messages
				
					Util.removeAllOptions(listid2);
					
					Util.addOptions(listid2, data2, null);
				}
			});
			
			
			
			
			
			/*测试代码2
			Browser.withAllSessionsFiltered(new ScriptSessionFilter() {

                public boolean match(ScriptSession session) {

                       if (session.getAttribute("name") == null)

                              return false;

                       else

                              return (session.getAttribute("name")).equals(receiverid);

                }

         }, new Runnable(){

                private ScriptBuffer script = new ScriptBuffer();

                public void run() {

                       script.appendCall("receiveMessages", test);

                       Collection<ScriptSession> sessions = Browser

                       .getTargetSessions();

                       for (ScriptSession scriptSession : sessions) {
                    	   	
                              scriptSession.addScript(script);
                              //
                              System.out.println("推送成功！");
                       }

                }

               

         });
	*/
         
		}
	}

	public String updateUsersList(String username, boolean flag,
			HttpServletRequest request) {
		User user = null;
		if (flag) {
			System.out.println("添加新用户");
			user = new User(request.getSession().getId(), username);	
			System.out.println(user);
			users.add(user);
			this.setScriptSessionFlag(user.getUserid());
			
		}

		Browser.withCurrentPage(new Runnable() {
			public void run() {
				System.out.println("刷新用户列表");
				Util.removeAllOptions("users");
				Util.addOptions("users", users, "username");
				Util.removeAllOptions("receiver");
				//Util.addOptions("receiver", users, "userid", "username"); //更新用户列表下拉选择框
				Util.addOptions("receiver", users, "username", "username");
			}
		});
		if (!flag) {
			return "未添加新用户";
		}
		//System.out.println("user.getUserid():" + user.getUserid());
		return user.getUserid();  //获取新增加用户的id
//		return user.getUsername();
	}

	
	
	public void setScriptSessionFlag(String userid) {
		WebContextFactory.get().getScriptSession()
				.setAttribute("userid", userid);
	}

	public ScriptSession getScriptSession(String userid,
			HttpServletRequest request) {
		ScriptSession scriptSessions = null;
		Collection<ScriptSession> sessions = new HashSet<ScriptSession>();
		sessions.addAll(ServerContextFactory.get(
				request.getSession().getServletContext())
				.getScriptSessionsByPage("/index.jsp"));
		for (ScriptSession session : sessions) {
			String xuserid = (String) session.getAttribute("userid");
			if (xuserid != null && xuserid.equals(userid)) {
				scriptSessions = session;
			}
		}
		return scriptSessions;
	}

	
	public String getUsernameById(String id){
		
		System.out.println("id="+id);
		for(User l: users){
			System.out.println("遍历得到的用户名"+l.getUsername());
			if(l.getUserid().equals(id)){
				System.out.println("haonaohuo");
				return l.getUsername();
			}
		}
		
		return null;
	}
	
	//public final String meg = null;		//临时消息
	public final LinkedList<User> users = new LinkedList<User>();
	private final LinkedList<Message> messages = new LinkedList<Message>();
	
}
