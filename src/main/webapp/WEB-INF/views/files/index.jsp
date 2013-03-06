
<div class="row">
  <div class="span12">
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error">${f:h(errorMessage)}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
      <div class="alert alert-success">${f:h(successMessage)}</div>
    </c:if>

    <div>
      <h3>
        Upload a file (max
        <fmt:formatNumber value="${f:h(maxUploadSize)}"
          pattern="###,###" />
        byte)
      </h3>
      <form:form action="${pageContext.request.contextPath}/files"
        modelAttribute="filesForm" class="form-horizontal" method="post"
        enctype="multipart/form-data">
        <div class="control-group">
          <form:label path="file" class="control-label">File (*)</form:label>
          <div class="controls">
            <form:input path="file" type="file" />
            <form:errors path="file" cssClass="text-error" />
          </div>
        </div>
        <div class="control-group">
          <form:label path="description" class="control-label">Description (*)</form:label>
          <div class="controls">
            <form:input path="description" type="text"
              placeholder="Comment..." />
            <form:errors path="description" cssClass="text-error" />
          </div>
        </div>
        <div class="control-group">
          <form:label path="deleteKey" class="control-label">Delete key</form:label>
          <div class="controls">
            <form:input path="deleteKey" type="password" />
            <form:errors path="deleteKey" cssClass="text-error" />
          </div>
        </div>
        <div class="control-group">
          <form:label path="downloadKey" class="control-label">Download key</form:label>
          <div class="controls">
            <form:input path="downloadKey" type="password" />
            <form:errors path="downloadKey" cssClass="text-error" />
          </div>
        </div>
        <div class="control-group">
          <div class="controls">
            <input type="submit" class="btn" name="confirm"
              value="Upload" /> <span class="help-inline">(*) is
              required.</span>
          </div>
        </div>
      </form:form>
    </div>

    <h3>File List</h3>
    <table class="table table-striped table-bordered table-condensed">
      <thead>
        <tr>
          <th>ID</th>
          <th>Description</th>
          <th>File Size</th>
          <th>Content Type</th>
          <th>Uploaded Date</th>
          <th>Original</th>
          <th>Download Count</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="file" items="${page.content}">
          <tr>
            <td><c:if test="${!empty file.downloadKey}"
                var="isPrivate" /><a
              href="${pageContext.request.contextPath}/files/${f:h(file.id)}<c:if test="${isPrivate}">?private</c:if>">${f:h(file.id)}</a>
              <c:if test="${isPrivate}">
                <span class="label label-info">Private</span>
              </c:if> <c:if test="${!empty file.deleteKey}">
                <span class="label label-important">Deletable</span>
              </c:if></td>
            <td>${f:h(file.description)}</td>
            <td><fmt:formatNumber value="${f:h(file.fileSize)}"
                pattern="###,###" /> byte</td>
            <td>${f:h(file.contentType)}</td>
            <td>${f:h(file.uploadedDate)}</td>
            <td>${f:h(file.originalFileName)}</td>
            <td>${f:h(file.downloadCount)}</td>
          </tr>
        </c:forEach>
      </tbody>
    </table>

    <div>
      <form:form modelAttribute="filesForm" method="delete"
        class="form-inline">
      ID <form:input path="id" class="input-mini" />
        <form:errors path="id" cssClass="text-error" />
      Delete key <form:input path="deleteKey" type="password"
          class="input-mini" />
        <form:errors path="deleteKey" cssClass="text-error" />
        <input type="submit" value="Delete" class="btn btn-danger" />
      </form:form>
    </div>
    <div class="pagination">
      <util:pagination page="${page}" />
    </div>
  </div>

</div>
