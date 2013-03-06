<div class="row">
  <div class="span12">
    <h2>Download File</h2>
    <form:form
      action="${pageContext.request.contextPath}/files/${f:h(filesForm.id)}"
      modelAttribute="filesForm" class="form-horizontal" method="get">
      <div class="control-group">
        <form:label path="downloadKey" class="control-label">Download key</form:label>
        <div class="controls">
          <form:input path="downloadKey" type="password" />
          <form:errors path="downloadKey" cssClass="text-error" />
        </div>
      </div>
      <div class="form-actions">
        <input type="submit" class="btn btn-primary" name="confirm"
          value="Download" />
      </div>
    </form:form>
  </div>
</div>
