function init() {
	dwr.engine.setActiveReverseAjax(true);//激活反转
	Chat.updateUsersList(null, false); //当你打开界面的时候,先获得在线用户列表
	//onPageLoad();
}


//读取name值作为推送的唯一标示
function onPageLoad(){
    // 获取URL中的name属性为唯一标识符
	Chat.onPageLoad($('username').value);
 }

function register(button) {
	if ($('username').value == "" || $('username').value.length <= 0) {
		alert("请输入昵称");
		return;
	}
	/* 下面是对一些按钮的禁用和激活操作 */
	button.disabled = true;
	$('text').disabled = false;
	$('sendButton').disabled = false;
	$('receiver').disabled = false;
	/* 把我输入的用户名注册到服务器,并获得用户id(这里用session id 代替) */
	//alert($('username').value);
	
	
	//想到一个办法，通过修改list的id试试
	var id1 = document.getElementById("list");
	//id2 += $('username').value;
	id1.id = id1.id + $('username').value;
	
	//想到一个办法，通过修改list的id试试
	var id2 = document.getElementById("list2");
	//id2 += $('username').value;
	id2.id = id2.id + $('username').value;
	
	
	
	//alert(id2.id);
	
	
	Chat.updateUsersList($('username').value, true, function(data) {
		//alert("data:"+data);
		if (data != null && data.length > 0) {
			$('userid').value = data; // 注册成功,把userid放到当前页面 ，即隐藏的id属性
		}
	});
	
	//读取username 
	onPageLoad();
	
	
	
	
	$('username').disabled = true;
}
function sendMessage() {
	
	//尝试在这里载入name属性
	//onPageLoad();
	var text = dwr.util.getValue("text");
	var sender = dwr.util.getValue('username'); // 获得发送者名字
	var receiver = dwr.util.getValue('receiver'); // 获得接受者
//	var receiver = dwr.util.getValue()
	//alert("接收者id="+receiver);

	/*尝试把这部分交给后台来完成
	Chat.getUsernameById(receiver1, function(data) {
		//alert("data:"+data);	
		  var receiver = data;
		 alert("发送者:"+sender+" 接收者："+receiver+"消息为： "+text);
		 
		 Chat.addMessage(sender,receiver,text);
	});
	*/
	Chat.addMessage(sender,receiver,text);
	//alert("程序执行至此");
	dwr.util.setValue("text", "");
}
//function receiveMessages(messages) {
//	var chatlog = "";
//	for ( var data in messages) {
//		chatlog = "<div>" + dwr.util.escapeHtml(messages[data].text) + "</div>"
//				+ chatlog;
//	}
//	dwr.util.setValue("chatlog", chatlog, {
//		escapeHtml : false
//	});
//}

function receiveMessages(message){
	var lastMessage = message;
	alert("接受消息"+meaasge);
    chatlog = "<p>" + lastMessage + "</p>" + chatlog;
    $("#list").html(chatlog);
}



window.onload = init;// 页面加载完毕后执行初始化方法init
