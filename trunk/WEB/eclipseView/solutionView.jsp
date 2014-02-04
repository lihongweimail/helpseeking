<%@page import="lhw.pojo.Recommandlist"%>
<%@page import="java.util.List"%>
<%@page import="lhw.action.RecommandlistUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<script type="text/javascript" src="../js/jquery.js" ></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>queryView</title>
</head>
<style>
.td1 {
	border:1px black solid;
}   
</style>
<%
String keyWord= request.getParameter("keyWord");

RecommandlistUtil util = new RecommandlistUtil();
List<Recommandlist> list = util.queryById(keyWord ==null?"1":keyWord);

Recommandlist recommandList = (Recommandlist)list.get(0);
String[] tags = util.splitTag(recommandList.getTag()==null?"":recommandList.getTag());
%>
<body>
<br/>
<table border="0" width="97%"  height="100%" align="left" cellSpacing=0 cellPadding=0>
<tr>
<td>
<fieldset style="width:100%;height=100%;margin-left: 2px;padding-left: 2px" align="left">
<table border="0" width="98%" align="left" cellSpacing=0 cellPadding=0 >
<tr>
<td style="background:#1080df"> You are looking for the Help-seeking infomation:</td>
</tr>
</table>
<table border="0" width="98%" align="left"  cellSpacing=0 cellPadding=0 >
<tr>
<td  width="10%">&nbsp;缩略图</td>
<td>
<table  border="0" align="left" width="98%"  cellSpacing=0 cellPadding=0 >
<tr>
<td  height="36"><img alt="" src="../image/title_tag.jpg">&nbsp;  <a  href="<%=recommandList.getUrl() %>" target="completeContent"><%=recommandList.getTitle() %></a></td>
</tr>
<tr>
<td  height="36"><font  color="red" ><B>SUMMARY:</B></font><%=recommandList.getSummary().substring(0,40)+"..." %>
<br/>
<font color="green">URL:<a href="<%=recommandList.getUrl() %>" target="completeContent"><%=recommandList.getUrl().substring(0,40)+"..." %></a></font>
</td>
</tr>
<tr>
<td  height="30">
<table border="0" align="left" width="98%">
<tr>
<td width="15%">Similarity:</td><td width="30%" height="10"> <div style="height: 10px;border:1px black solid ">
<div style="height: 10px;width:<%=recommandList.getSimilarity()*100%>%;background-color: <%if(recommandList.getSimilarity()<0.7){%>blue<%}else{%>green<%}%>">&nbsp;</div></div></td>
<td width="15%">Popularity:</td><td height="10">
<%for(int k=0;k<Math.floor(recommandList.getPopularity());k++) 
{%>
<img src="../image/full_star.jpg"/>
<%} 
if(recommandList.getPopularity() % 1 !=0) 
{%>
<img src="../image/half_star.jpg"/>
<%} 
for(int n = 0;n<5-(Math.floor(recommandList.getPopularity()))-((recommandList.getPopularity() % 1 !=0)?1:0);n++)
{%>
		<img src="../image/empty_star.jpg"/>
<%}
%>
</td>
</tr>
</table>
</td>
</tr>
<tr>
<td><%for(int i=0;i<tags.length;i++) {%><label style="background-color: <% if(i==0){%>blue<%}else{%>#6495ED<%}%>">
<%=tags[i] %></label>&nbsp;&nbsp;
<%} %></td>
</tr>
</table>
</td>
</tr>
</table>
<br/>
<table border="0" width="98%" align="left" cellSpacing=0 cellPadding=0 ><tr>
<td style="background:#d0d0d0"> <font style="color: red">Recommand:</font> Click the URL for detail information.</td></tr></table>
</fieldset>
</td>
</tr>
<tr>
<td >
<iframe frameborder="1" width="100%"  height="100%"  id="otherItem" scrolling="auto" src="otherItem.jsp?queryId=<%=keyWord%>">
</iframe>
</td>
</tr>
</table>
<script type="text/javascript">
$(document).ready(function() { 	  
	//	 document.getElementById("table1").width=window.innerWidth;	  
	//     document.getElementById("table1").height=window.innerHeight;
	document.getElementById("otherItem").height=window.innerHeight;
});
</script>
</body>
</html>