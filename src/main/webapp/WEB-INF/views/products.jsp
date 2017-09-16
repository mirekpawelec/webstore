<%-- 
    Document   : products
    Created on : 2017-09-05, 19:31:57
    Author     : mirek
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="pl">

    <jsp:include page="./fragments/header.jsp"/>      
        
        <section class="main">
            
            <jsp:include page="./fragments/navi.jsp"/>
            
            <div class="container">
                <div class="row">
                    <div class="page-header">
                        <h2> 
                            <spring:message code="products.pageHeaderPart1.label"/> 
                                <small> <spring:message code="products.pageHeaderPart2.label"/></small> 
                        </h2>
                    </div>
                </div>
                <div class="row">
                    <c:if test="${success}">
                        <div class="alert alert-${css} alert-dismissable" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <strong> <spring:message code="products.table.processResult.message" arguments="${deletedProductNo}"/> </strong>
                        </div>                        
                    </c:if>
                </div>
                <div class="row">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <tr>
                                <th> <spring:message code="products.table.productNo.label"/> </th>
                                <th> <spring:message code="products.table.name.label"/> </th>
                                <th> <spring:message code="products.table.manufacturer.label"/> </th>
                                <th> <spring:message code="products.table.category.label"/> </th>
                                <th> <spring:message code="products.table.price.label"/> </th>
                                <th> <spring:message code="products.table.status.label"/> </th>
                                <th> </th>
                            </tr>
                            <c:forEach var="item" items="${products}">
                                <tr>
                                    <td>${item.productNo}</td>
                                    <td>${item.name}</td>
                                    <td>${item.manufacturer}</td>
                                    <td>${item.category}</td>
                                    <td>${item.unitPrice}</td>
                                    <td>${item.status}</td>
                                    <td>
                                        <spring:url value="/admin/products/product?id=${item.productId}" var="productUrl"/>
                                        <spring:url value="/admin/products/${item.productId}/update" var="updateUrl"/>
                                        <spring:url value="/admin/products/param;id=${item.productId};productNo=${item.productNo}/delete" var="deleteUrl"/>   
                                        
                                        <a href="${productUrl}" class="btn btn-primary btn-xs"><span class="glyphicon glyphicon-check"> </span>
                                            <spring:message code="products.button.selectProduct.label"/> 
                                        </a>
                                        <a href="${updateUrl}" class="btn btn-primary btn-xs"><span class="glyphicon glyphicon-edit"> </span>
                                            <spring:message code="products.button.updateProduct.label"/> 
                                        </a>
                                        <a href="${deleteUrl}" class="btn btn-danger btn-xs"><span class="glyphicon glyphicon-remove"> </span>
                                            <spring:message code="products.button.deleteProduct.label"/> 
                                        </a>
                                        
<!--                                        <button class="btn btn-primary btn-sm" style="width: 75px;" onclick="location.href='$ {updateUrl}'">
                                        <spring :message code="products.button.updateProduct.label"/>
                                        </button>
                                        <button class="btn btn-danger btn-sm" style="width: 75px;" onclick="location.href='$ {deleteUrl}'">
                                            <spring :message code="products.button.deleteProduct.label"/>
                                        </button>-->
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>
            </div>
        </section>

    <jsp:include page="./fragments/footer.jsp"/>
    
    </body>
</html>
