<%-- 
    Document   : product
    Created on : 2017-09-05, 20:52:13
    Author     : mirek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    
    <jsp:include page="./fragments/header.jsp" />
        
        <section class="main">
            
            <jsp:include page="./fragments/navi.jsp"/>
            
            <div class="container">
            
                <hr class="gray">
                
                <div class="row">
                    <c:if test="${not empty css}">
                        <div class="alert alert-${css} alert-dismissable" role="alert">
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <c:choose> 
                                    <c:when test="${typeProcess == 'create'}">
                                        <strong> <spring:message code="product.alert.afterAddUser.message"/> </strong>
                                    </c:when>
                                    <c:when test="${typeProcess == 'update'}">
                                        <strong> <spring:message code="product.alert.afterUpdateUser.message"/> </strong>
                                    </c:when>
                                </c:choose>
                        </div>                        
                    </c:if>
                </div>
                
                <div class="row">
                        <div class="page-header">
                            <h2> <spring:message code="product.pageHeader.label"/> ${product.manufacturer} <small>${product.name}</small> </h2>
                        </div>
                </div>
                        
                <div class="row">
                    <div class="col-xs-12 col-sm-5 col-md-5 col-lg-5">
                        <spring:url value="/resource/img/${product.productNo}.jpg" var="imageUrl"/>
                        <img src="${imageUrl}" class="img-responsive center-block" alt="Image"/>   
                    </div>
                    
                    <div class="col-xs-12 col-sm-7 col-md-offset-1 col-md-6 col-lg-offset-1 col-lg-6 text-left">
                        <div class="row">
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-5"> 
                                <spring:message code="product.list.productNo.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.productNo}
                            </div>
                        </div>
                            
                        <div class="row">
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-5"> 
                                <spring:message code="product.list.name.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.name}
                            </div>
                        </div>
                            
                        <div class="row">
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-5"> 
                                <spring:message code="product.list.manufacturer.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.manufacturer}
                            </div>
                        </div>
                            
                        <div class="row">
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-5"> 
                                <spring:message code="product.list.category.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.category}
                            </div>
                        </div>
                            
                        <div class="row">
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-5"> 
                                <spring:message code="product.list.description.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.description}
                            </div>
                        </div>
                            
                        <div class="row">
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-"> 
                                <spring:message code="product.list.unitPrice.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.unitPrice}
                            </div>
                        </div>
                            
                        <div class="row">
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-5"> 
                                <spring:message code="product.list.quantityInBox.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.quantityInBox}
                            </div>
                        </div>
                            
                        <div class="row">   
                            <label class="col-xs-12 col-sm-6 col-md-5 col-lg-5"> 
                                <spring:message code="product.list.status.label"/>
                            </label>
                            <div class="col-xs-12 col-sm-6 col-md-7 col-lg-7">
                                ${product.status}
                            </div>
                        </div> 
                    </div> 
                </div>
                            
                <hr class="gray">
                
                <div class="row">
                        <spring:url value="/admin/products" var="productsUrl"/>
                        <button class="btn btn-primary pull-right" onclick="location.href='${productsUrl}'">
                            <span class="glyphicon glyphicon-hand-left"></span> <spring:message code="product.button.bactToProducts.label"/> 
                        </button>
                </div>
            </div>
        </section>

    <jsp:include page="./fragments/footer.jsp" />
    
    </body>
</html>
