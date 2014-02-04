<%@page import="lhw.pojo.Recommandlist"%>
<%@page import="java.util.List"%>
<%@page import="lhw.action.RecommandlistUtil"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <style>
.td1 {
	border:1px black solid;
}   
</style>
<html>
<body>

<%
   String keyWord = request.getParameter("queryId");
   RecommandlistUtil util = new RecommandlistUtil();
   List<Recommandlist> list = util.queryById(keyWord==null?"1":keyWord);
   for(int i =1 ;i<list.size();i++){
	   Recommandlist recommandList = (Recommandlist)list.get(i);
       String[] tags = util.splitTag(recommandList.getTag());
%>
<table border="0" width="100%"  align="left" cellSpacing=0 cellPadding=0>
    <tr><td height="10px">&nbsp;</td></tr>   
    <tr>
      <td class="td1">
       <table border="0" width="98%" align="left" >
          <tr>
            <td  width="10%">&nbsp;缩略图</td>
            <td>
	            <table  border="0" align="left" width="100%"  cellSpacing=0 cellPadding=0 >
	                <tr>
	                 <td  height="36"><img alt="" src="../image/title_tag.jpg">&nbsp;  <a  href="<%=recommandList.getUrl() %>" target="completeContent"><%=recommandList.getTitle() %></a></td>
	                </tr>
	                <tr>
	                 <td  height="36"><font  color="red" ><B>SUMMARY</B>:</font><%=recommandList.getSummary().substring(0,40)+"..."%>
	                 <br/><font color="green">URL:<a href="<%=recommandList.getUrl() %>" target="completeContent"><%=recommandList.getUrl().substring(0,40)+"..."%></a></font></td>
	                </tr>
	                <tr>
	                 <td  height="30">
	                  <table border="0" align="left" width="100%">
	                     <tr>
	                     <td width="15%">Similarity:</td><td width="30%" height="10"> <div style="height: 10px;border:1px black solid ">
	                     <div style="height: 10px;width:<%=recommandList.getSimilarity()*100%>%;background-color:<%if(recommandList.getSimilarity()<0.7){%>blue<%}else{%>green<%}%>">&nbsp;</div></div></td>
	                     <td width="15%">Popularity:</td><td height="10"><%for(int k=0;k<Math.floor(recommandList.getPopularity());k++) {%>
	                     <img src="../image/full_star.jpg"/>
	                     <%} if(recommandList.getPopularity() % 1 !=0) {%>
	                     <img src="../image/half_star.jpg"/>
	                     <%} for(int n = 0;n<5-(Math.floor(recommandList.getPopularity()))-
	                     ((recommandList.getPopularity() % 1 !=0)?1:0)
	                     ;n++){%>
	                      <img src="../image/empty_star.jpg"/>
	                     <%}
	                      %></td>
	                     </tr>
	                  </table>
	                 </td>
	                </tr>
	                <tr>
	                 <td><%for(int j=0;j<tags.length;j++) {%><label style="background-color: <% if(j==0){%>blue<%}else{%>#6495ED<%}%>">
	                 <%=tags[j] %></label>&nbsp;&nbsp;
	                 <%} %></td>
	                </tr>
	            </table>
            </td>
          </tr>
       </table>
      </td>
    </tr>
     </tr>
    <tr><td height="5px">&nbsp;</td></tr>  
    <%} %> 
    </table>
    </body>
 </html>