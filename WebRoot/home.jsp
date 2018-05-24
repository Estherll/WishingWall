<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>许愿墙</title>
  </head>
  
  	
	<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
  	<script type="text/javascript">
      $.ajax({
		type:"get",
		dataType:"json",
		url:"testAction_test.action",
		success: function(result){
			console.log(result);
		}
	 })
      console.log("😁😁😁😁😁😁😁😁");
    </script>
  
  <body>
    <header>
    	<div id="head_container" class="head_bg" onclick="window.location.href='homePage_showBgs.action';">
    		<div id="firstimg">
    			<a href="userPage_*************.action?********=${sessionScope.suruser.uid}"><img src="${sessionScope.curuser.headimgurl } " alt="${sessionScope.curuser.nickname }"></a>
    		</div>
    		<s:if test="#session.curuser.sysnotify>0">
    			<div id="changebg"><a href="homePage_*******.action">&nbsp;&nbsp;点击可修改背景哟&nbsp;&nbsp;</a></div>
    		</s:if>
    	</div>
    </header>
    <section id="mosthot">
    	<s:if test="hotMessages!=null && hotMessages.size>0">
    		<s:iterator vaulue="hotMessage" var="mess" status="st">
    			<section class="messSection">
    				<div class="hesdimg">
    					<s:if test="#mess.anonymity>0">
    						<a><img src='xxxxx.png'/></a>
    					</s:if>
    					<s:else>
    						<a href='userPage_getTargetHome.action?targetUid=<s:property value="uid" />'>
    						<img src='<s:property value="mess.headimgurl"/>'/></a>
    					</s:else>
    				</div>
    				<s:if test="#session.curuser.bground=='xxxxxx.png'">
    					<s:if test="#mess.messType=='pray'">
    						<img src=".....xxxxx.png" class="prayico">
    					</s:if>
    					<s:elseif test="#mess.messType=='complain'">
    						<img src=".....xxxxx.png" class="complainico">
    					</s:elseif>
    					<s:elseif test="#mess.messType=='sayhi'">
    						<img src=".......xxxxx.png" class="sayhiico">
    					</s:elseif>
    					<s:else>
    						<img src=".......xxx.png" class="defaultico">
    					</s:else>
    				</s:if>
    				<s:elseif test="#session.curuser.bground=='xxxxxx.png'">
    					<s:if test="#mess.messType=='pray'">
    						<img src=".....xxxxx.png" class="prayico">
    					</s:if>
    					<s:elseif test="#mess.messType=='complain'">
    						<img src=".....xxxxx.png" class="complainico">
    					</s:elseif>
    					<s:elseif test="#mess.messType=='sayhi'">
    						<img src=".......xxxxx.png" class="sayhiico">
    					</s:elseif>
    					<s:else>
    						<img src=".......xxx.png" class="defaultico">
    					</s:else>
    				</s:elseif>
    				<s:else>
    					<s:if test="#mess.messType=='pray'">
    						<img src=".....xxxxx.png" class="prayico">
    					</s:if>
    					<s:elseif test="#mess.messType=='complain'">
    						<img src=".....xxxxx.png" class="complainico">
    					</s:elseif>
    					<s:elseif test="#mess.messType=='sayhi'">
    						<img src=".......xxxxx.png" class="sayhiico">
    					</s:elseif>
    					<s:else>
    						<img src=".......xxx.png" class="defaultico">
    					</s:else>
    				</s:else>
    				<p class="to">To:<s:property value="#mess.towho"/></p>
    				<p class="message"><s:property value="#mess.content"/></p>
    				<s:if test="#mess.anonymity>0">
    				  <p class="from">From:<s:property value="#mess.fromwho"/></p>
    				</s:if>
    				<s:else>
    				  <p class="from">From:<s:property value="#mess.nickname"/></p>
    				</s:else>
    				<time datetime=""><s:property value="#mess.simpleTime"/></time>
    				<div class="like">
    				  <span><s:property value="#mess.liketimes"/>赞</span>
    				  <button type="button">
    				    <s:if test="#mess.sstate>0">
    				      <img src="redheart" mid=<s:property value="#mess.mid"/>>
    				    </s:if>
    				    <s:else>
    				      <img src="whiteheart" mid=<s:property value="mess.mid"/>>
    				    </s:else>
    				  </button>
    				</div>
    				<div class="comment">
    				  <img src="comment.png">
    				  <s:property value="#mess.commentNum"/>
    				</div>
    			</section>
    		</s:iterator>
    	</s:if>
    </section>
    
    <section id="Newest">
      <s:if test="messagePage.messList != null && messagePage.messList.size>0">
        <s:iterator value="messagePage.messList" var="mess">
          <section class="messSection">
            <div class="headimg">
              <s:if test="#mess.anonymity>0">
                <img src=".....niming.png"/>
              </s:if>
              <s:else>
                <a href='userPage_getTargetHome.action?targetUid=<s:property value="uid"/>' >
                  <img src='<s:property value="#mess.headimgurl"/>'>
                </a>
              </s:else>
            </div>
            	<s:if test="#session.curuser.bground=='xxxxxx.png'">
    					<s:if test="#mess.messType=='pray'">
    						<img src=".....xxxxx.png" class="prayico">
    					</s:if>
    					<s:elseif test="#mess.messType=='complain'">
    						<img src=".....xxxxx.png" class="complainico">
    					</s:elseif>
    					<s:elseif test="#mess.messType=='sayhi'">
    						<img src=".......xxxxx.png" class="sayhiico">
    					</s:elseif>
    					<s:else>
    						<img src=".......xxx.png" class="defaultico">
    					</s:else>
    				</s:if>
    				<s:elseif test="#session.curuser.bground=='xxxxxx.png'">
    					<s:if test="#mess.messType=='pray'">
    						<img src=".....xxxxx.png" class="prayico">
    					</s:if>
    					<s:elseif test="#mess.messType=='complain'">
    						<img src=".....xxxxx.png" class="complainico">
    					</s:elseif>
    					<s:elseif test="#mess.messType=='sayhi'">
    						<img src=".......xxxxx.png" class="sayhiico">
    					</s:elseif>
    					<s:else>
    						<img src=".......xxx.png" class="defaultico">
    					</s:else>
    				</s:elseif>
    				<s:else>
    					<s:if test="#mess.messType=='pray'">
    						<img src=".....xxxxx.png" class="prayico">
    					</s:if>
    					<s:elseif test="#mess.messType=='complain'">
    						<img src=".....xxxxx.png" class="complainico">
    					</s:elseif>
    					<s:elseif test="#mess.messType=='sayhi'">
    						<img src=".......xxxxx.png" class="sayhiico">
    					</s:elseif>
    					<s:else>
    						<img src=".......xxx.png" class="defaultico">
    					</s:else>
    				</s:else>
    				<p class="to">To:<s:property value="#mess.towho"/></p>
    				<p class="message"><s:property value="#mess.content"/></p>
    				<s:if test="#mess.anonymity>0">
    				  <p class="from">From:<s:property value="#mess.fromwho"/></p>
    				</s:if>
    				<s:else>
    				  <p class="from">From:<s:property value="#mess.nickname"/></p>
    				</s:else>
    				<time datetime=""><s:property value="#mess.simpleTime"/></time>
    				<div class="like">
    				  <span><s:property value="#mess.liketimes"/>赞</span>
    				  <button type="button">
    				    <s:if test="#mess.sstate>0">
    				      <img src="redheart" mid=<s:property value="#mess.mid"/>>
    				    </s:if>
    				    <s:else>
    				      <img src="whiteheart" mid=<s:property value="mess.mid"/>>
    				    </s:else>
    				  </button>
    				</div>
    				<div class="comment">
    				  <img src="comment.png">
    				  <s:property value="#mess.commentNum"/>
    				</div>
          </section>
        </s:iterator>
      </s:if>
    </section>
    
    <section id="hotTopicMess">
      <s:if test=""></s:if>
    </section>
  </body>
</html>
