<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html>
  <head>
    <title>${targetUser.nickname }</title>


  </head>
  
  <body>
  	<header>
  		<div id="head_container">
  			<div id="firstimg">
  				<img src="${targetUser.headimgurl }" alt="${targetUser.nickname }">
  			</div>
  			<div id="info">
  				<span id="nickname">ID:${targetUser.nickname }</span>
  				<span id="messNum">${messageSize }个愿望</span>
  				<span id="commNum">${commentSize }条评论</span>
  			</div>
  		</div>
  	</header>
  	<div>
  		<section>
  		<s:if test="targetMess!=null && targetMess.size>0">
  			<s:iterator value="targetMess" var="mess" status="st">
  				<section class="messSection">
  					<div class="headimg">
  					<s:if test="#mess.anonymity>0">
  						<img alt="" src="......niming.png">
  					</s:if>	
  					<s:else>
  						<img src="#mess.headimgurl">
  					</s:else>
  					</div>
  					<s:elseif  test="#mess.messType=='pray'">
  					
  					</s:elseif>
  					<s:elseif  test="#mess.messType=='complain'">
  					
  					</s:elseif>
  					<s:elseif  test="#mess.messType=='sayhi'">
  					
  					</s:elseif>
  					<s:else>
  					
  					</s:else>
  					<s:if test="#mess.state>0">
  						<button type="button" class="del">
  							<img alt="" src="../img/delete.png">
  						</button>
  					</s:if>
  					<p class="to">TO:<s:property value="#mess.towho"/></p>
  					<p class="message"><s:property value="#mess.content"/></p>
  					<s:if test="#mess.anonymity>0">
  						<p class="from">From:<s:property value="#mess.fromwho"/></p>
  					</s:if>
  					<s:else>
  						<p>From:<s:property value="#mess.nickname"/></p>
  					</s:else>
  					
  					<time datetime="<s:property value="#mess.timestamp"/>"><s:property value="#mess.simpleTime"/></time>
  					<div class="comment">
  						<span><s:property value="#mess.commentNum"/></span>
  						<a>
  							<img alt="" src="....comment.png">
  						</a>
  					</div>
  					<div class="like">
  						<span><s:property value="#mess.liketimes"/></span>
  						<s:if test="#mess.sstate>0">
  							<button type="button">
  								<img alt="" src="....redheart.png">
  							</button>
  						</s:if>
  						<s:else>
  							<button type="button">
  								<img alt="" src="....whitrheart.png">
  							</button>
  						</s:else>
  					</div>
  				</section>		
  			</s:iterator>
  		</s:if>
  		</section>
  	</div>
  </body>
</html>
