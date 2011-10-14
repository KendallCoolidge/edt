/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
egl.defineClass('eglx.http', 'Response',
{
	'eze$$fileName': 'eglx/http/Response.egl',
	'eze$$runtimePropertiesFile': 'eglx.http.Response',
		"constructor": function() {
			this.eze$$setInitial();
		}
		,
		"eze$$setEmpty": function() {
			this.status = null;
			this.statusMessage = null;
			this.encoding = null;
			this.charset = null;
			this.contentType = null;
			this.headers = null;
			this.body = null;
		}
		,
		"eze$$setInitial": function() {
			this.eze$$setEmpty();
		}
		,
		"eze$$clone": function() {
			var ezert$$1 = this;
			var ezert$$2 = new egl.eglx.http.Response();
			ezert$$2.status = ezert$$1.status === null ? null : ezert$$1.status;
			ezert$$2.statusMessage = ezert$$1.statusMessage === null ? null : ezert$$1.statusMessage;
			ezert$$2.encoding = ezert$$1.encoding === null ? null : ezert$$1.encoding;
			ezert$$2.charset = ezert$$1.charset === null ? null : ezert$$1.charset;
			ezert$$2.contentType = ezert$$1.contentType === null ? null : ezert$$1.contentType;
			ezert$$2.headers = ezert$$1.headers === null ? null : ezert$$1.headers;
			ezert$$2.body = ezert$$1.body === null ? null : ezert$$1.body;
			return ezert$$2;
		}
		,
		"eze$$getAnnotations": function() {
			if(this.annotations === undefined){
				this.annotations = {};
				this.annotations["XMLRootElement"] = new egl.eglx.xml.binding.annotation.XMLRootElement("Response", null, false);
			}
			return this.annotations;
		}
		,
		"eze$$getFieldInfos": function() {
			if(this.fieldInfos === undefined){
				var annotations;
				this.fieldInfos = new Array();
				annotations = {};
				annotations["XMLStyle"] = new egl.eglx.xml.binding.annotation.XMLElement("status", null, false, false);
				annotations["JsonName"] = new egl.eglx.json.JsonName("status");
				this.fieldInfos[0] =new egl.eglx.services.FieldInfo("status", "status", "I;", Number, annotations);
				annotations = {};
				annotations["XMLStyle"] = new egl.eglx.xml.binding.annotation.XMLElement("statusMessage", null, false, false);
				annotations["JsonName"] = new egl.eglx.json.JsonName("statusMessage");
				this.fieldInfos[1] =new egl.eglx.services.FieldInfo("statusMessage", "statusMessage", "S;", String, annotations);
				annotations = {};
				annotations["XMLStyle"] = new egl.eglx.xml.binding.annotation.XMLElement("encoding", null, false, false);
				annotations["JsonName"] = new egl.eglx.json.JsonName("encoding");
				this.fieldInfos[2] =new egl.eglx.services.FieldInfo("encoding", "encoding", "eglx.services.Encoding", egl.eglx.services.Encoding, annotations);
				annotations = {};
				annotations["XMLStyle"] = new egl.eglx.xml.binding.annotation.XMLElement("charset", null, false, false);
				annotations["JsonName"] = new egl.eglx.json.JsonName("charset");
				this.fieldInfos[3] =new egl.eglx.services.FieldInfo("charset", "charset", "S;", String, annotations);
				annotations = {};
				annotations["XMLStyle"] = new egl.eglx.xml.binding.annotation.XMLElement("contentType", null, false, false);
				annotations["JsonName"] = new egl.eglx.json.JsonName("contentType");
				this.fieldInfos[4] =new egl.eglx.services.FieldInfo("contentType", "contentType", "S;", String, annotations);
				annotations = {};
				annotations["XMLStyle"] = new egl.eglx.xml.binding.annotation.XMLElement("headers", null, false, false);
				annotations["JsonName"] = new egl.eglx.json.JsonName("headers");
				this.fieldInfos[5] =new egl.eglx.services.FieldInfo("headers", "headers", "y;", egl.eglx.lang.EDictionary, annotations);
				annotations = {};
				annotations["XMLStyle"] = new egl.eglx.xml.binding.annotation.XMLElement("body", null, false, false);
				annotations["JsonName"] = new egl.eglx.json.JsonName("body");
				this.fieldInfos[6] =new egl.eglx.services.FieldInfo("body", "body", "S;", String, annotations);
			}
			return this.fieldInfos;
		}
		,
		"toString": function() {
			return "[Response]";
		}
		,
		"eze$$getName": function() {
			return "Response";
		}
		,
		"eze$$getChildVariables": function() {
			var eze$$parent = this;
			return [
			{name: "status", value : eze$$parent.status, type : "eglx.lang.EInt", jsName : "status"},
			{name: "statusMessage", value : eze$$parent.statusMessage, type : "eglx.lang.EString", jsName : "statusMessage"},
			{name: "encoding", value : eze$$parent.encoding, type : "eglx.services.Encoding", jsName : "encoding"},
			{name: "charset", value : eze$$parent.charset, type : "eglx.lang.EString", jsName : "charset"},
			{name: "contentType", value : eze$$parent.contentType, type : "eglx.lang.EString", jsName : "contentType"},
			{name: "headers", value : eze$$parent.headers, type : "eglx.lang.EDictionary", jsName : "headers"},
			{name: "body", value : eze$$parent.body, type : "eglx.lang.EString", jsName : "body"}
			];
		}
	}
);
