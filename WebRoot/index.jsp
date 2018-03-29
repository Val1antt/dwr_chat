<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>在线聊天</title>
<script type='text/javascript' src='dwr/engine.js'>
	
</script>
<script type='text/javascript' src='dwr/interface/Chat.js'>
	
</script>
<script type='text/javascript' src='dwr/util.js'>
	
</script>
<script type="text/javascript" src='javascript-chat.js'>
	
</script>
<script type='text/javascript' src='tabs/tabs.js'>
	
</script>
<link rel="stylesheet" type="text/css" href="tabs/tabs.css" />
<link rel="stylesheet" type="text/css" href="generic.css" />
</head>

<body>
	<div id="tabContents">
		<input type="hidden" name="userid" id="userid" /> <br> 昵称: 
		<input	type="text" name="username" id="username" /> 
		<input type="button" value="注册" onclick="register(this);" /> 
		<br /> 
		<br /> 
		我要对 
		<select
			name="receiver" id="receiver" disabled=true" >
		</select> 
		说: <input id="text" 
						onkeypress="dwr.util.onReturn(event, sendMessage)"  disabled=true" /> 
			<input id="sendButton"
						type="button" value="发送" onclick="sendMessage()"  disabled=true" /> 
		<br /> 
		<br /> 
		在线用户列表:
		<div>
			<div id="usersList">
				<ul id="users" style="list-style-type:none;">
				</ul>
			</div>
		</div>
		
		<div id="showMessage" style="display: none">
			<span id="sender"></span>对你说: <span id="msg"></span>
		</div>
		<div>
			<div id="demoDiv">

				<hr />
				<h4>群聊内容：</h4>
				<ul id="chatlog" style="list-style-type:none;">
				</ul>
				
				<hr/>
				<h4>私聊内容:</h4>
				    <ul id="list" style="list-style-type:none;"></ul>
				<hr>
				    <ul id="list2" style="list-style-type:none;"></ul>
				<hr>
			</div>
		</div>
	</div>
</body>
</html>