<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<%--<%@ page import="java.util.*"  %> 
<%@ page import="com.model2.mvc.common.Search" %>
<%@page import="com.model2.mvc.common.Page"%>
<%@page import="com.model2.mvc.common.util.CommonUtil"%>
<%@ page import="com.model2.mvc.service.domain.*" %>
    
<%
List<Purchase> list= (List<Purchase>)request.getAttribute("list");
List<User> list02= (List<User>)request.getAttribute("list02");
Page resultPage=(Page)request.getAttribute("resultPage");
//System.out.println("jsp"+list02);
Search searchVO = (Search)request.getAttribute("search");
//==> null 을 ""(nullString)으로 변경
String searchCondition = CommonUtil.null2str(searchVO.getSearchCondition());
String searchKeyword = CommonUtil.null2str(searchVO.getSearchKeyword());
//User user = (User)session.getAttribute("userVO");
//Product productVO = (Product)request.getAttribute("productVO");
//User user = (User)session.getAttribute("user");


%>--%> 
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script type="text/javascript">

function fncGetList(currentPage) {
	//document.getElementById("currentPage").value = currentPage;
   	//document.detailForm.submit();	
	$("#currentPage").val(currentPage);
   	$("form").attr("method" , "POST").attr("action" , "/purchase/listPurchase").submit();
}

$(function() {
	
	$( ".ct_list_pop td:nth-child(1)" ).on("click" , function() {
			
			self.location = "/purchase/getPurchase?tranNo="+$($('input:hidden[name="tranNo"]')[$(".ct_list_pop td:nth-child(1)").index(this)]).val()
	});
	
	$( ".ct_list_pop td:nth-child(3)" ).on("click" , function() {
		
		self.location = "/user/getUser?userId="+$($('input:hidden[name="userId"]')[$(".ct_list_pop td:nth-child(3)").index(this)]).val()
	});
	
	$( "td.ct_condition:contains('물건도착')" ).on("click" , function() {
		 self.location = "/purchase/updateTranCode?tranNo="+$($('input:hidden[name="tranNo"]')[$(".ct_list_pop td:nth-child(11)").index(this)]).val()+"&tranCode="+
		 +$($('input:hidden[name="tranCode"]')[$(".ct_list_pop td:nth-child(11)").index(this)]).val();
	});
	
	$(".ct_list_pop:nth-child(4n+6)" ).css("background-color" , "whitesmoke");
	
});


</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">구매 목록조회</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11">전체 ${resultPage.totalCount } 건수, 현재 ${resultPage.currentPage}  페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">전화번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">정보수정</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>

	
<%--<%
	for(int i=0; i<list.size(); i++) {
		Purchase purchase = (Purchase)list.get(i);
		User user = (User)list02.get(i);
		
	%>	--%>
	
	<c:set var="i" value="0" />
	<c:forEach var="purchase" items="${list}">	
		<c:set var="i" value="${ i+1 }" />
	<tr class="ct_list_pop">
		<td align="center">
		<input type="hidden" name="tranNo" value="${purchase.tranNo}">
			<!-- <a href="/purchase/getPurchase?tranNo=${purchase.tranNo}">${ i }</a>-->
			<ins>${ i }</ins>
		</td>
		<td></td>
		<td align="left">
		<input type="hidden" name="userId" value="${purchase.buyer.userId}">
			<!-- <a href="/user/getUser?userId=${purchase.buyer.userId }">${purchase.buyer.userId }</a>-->
			<ins>${purchase.buyer.userId }</ins>
		</td>
		<td></td>
		<td align="left">${purchase.receiverName}</td>
		<td></td>
		<td align="left">${purchase.receiverPhone}</td>
		<td></td>
		<td align="left"><c:if test = "${purchase.tranCode==null}">
		<input type="hidden" name="tranCode" value="${purchase.tranCode}">
				판매중
			</c:if>
			<c:if test = "${purchase.tranCode=='0  '}">
				구매완료
				<a href="/purchase/updatePurchase?tranNo=${purchase.tranNo}">배송정보수정</a>
				
				<a href="/purchase/deletePurchase?tranNo=${purchase.tranNo}">환불하기</a>
			</c:if>
			<c:if test = "${purchase.tranCode=='1  '}">
				배송중
			</c:if>
			<c:if test = "${purchase.tranCode=='2  '}">
				배송완료  (배송지 : ${purchase.divyAddr})
			</c:if></td>
		<td></td>
		<td align="left" class="ct_condition">
		<c:if test = "${purchase.tranCode=='1  '}">
			배송중 (정보수정,환불불가)
		</c:if>
		<c:if test = "${purchase.tranCode=='2  '}">
			배송완료 (정보수정,환불불가)
		</c:if>
		<c:if test = "${purchase.tranCode=='1  '}">
		<!-- <a href="/purchase/updateTranCode?tranNo=${purchase.tranNo}&tranCode=${purchase.tranCode}">물건도착</a>		 -->
			<ins>물건도착</ins>
		</c:if>
		</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="D6D7D6" height="1"></td>
	</tr>
	</c:forEach>
	<%--<%} %>--%>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
	<tr>
		<td align="center">
		 
			<input type="hidden" id="currentPage" name="currentPage" value=""/>
			<%--<% if( resultPage.getCurrentPage() <= resultPage.getPageUnit() ){ %>
					◀ 이전
			<% }else{ %>
					<a href="javascript:fncGetPurchaseList('<%=resultPage.getCurrentPage()-1%>')">◀ 이전</a>
			<% } %>

			<%	for(int i=resultPage.getBeginUnitPage();i<= resultPage.getEndUnitPage() ;i++){	%>
					<a href="javascript:fncGetPurchaseList('<%=i %>');"><%=i %></a>
			<% 	}  %>
	
			<% if( resultPage.getEndUnitPage() >= resultPage.getMaxPage() ){ %>
					이후 ▶
			<% }else{ %>
					<a href="javascript:fncGetPurchaseList('<%=resultPage.getEndUnitPage()+1%>')">이후 ▶</a>
			<%} %>--%>
		<jsp:include page="../common/pageNavigator.jsp"/>	
		</td>
	</tr>
</table>

<!--  페이지 Navigator 끝 -->
</form>

</div>

</body>
</html>