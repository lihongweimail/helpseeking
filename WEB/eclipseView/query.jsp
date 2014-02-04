<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<script type="text/javascript" src="../js/jquery.js" ></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>query parameter</title>
</head>

<%  String keyWord = request.getParameter("queryId");
     if(keyWord==null || "".equals(keyWord)) keyWord ="1";
 %>
 
<body>
<br/>
<table id="table1" width="100%" height="100%"   border="0"  align="left" cellSpacing=0>
	<tr>
	<td  style="padding-left: 8px" width="85%">&nbsp;<input id="keyWord" style="width:85%" value="" type="text"/> &nbsp;&nbsp;
	</td>
	<td>
	<input type="button" value="查询" onclick="query('1')"/>&nbsp;&nbsp;  
	</td>
	</tr>
	<tr>
	 <td colspan="2" width="100%" >
	 <iframe src="queryView.jsp?keyWord=<%=keyWord %>" frameborder="2" style="width:95%"  id="queryView" name="queryView"></iframe>
	 </td>
	</tr>
</table>
<br/>

<script type="text/javascript">
 function query(queryId){
	   // var keyWord =  document.getElementById('keyWord').value;
	    document.getElementById('queryView').src="queryView.jsp?keyWord="+queryId;
	    document.getElementById('queryView').reload();
	}	
 
 $(document).ready(function() { 	  
//	 document.getElementById("table1").width=window.innerWidth;	  
//     document.getElementById("table1").height=window.innerHeight;
    document.getElementById("queryView").height=window.innerHeight;
  });
 
 </script>
</body>
</html>