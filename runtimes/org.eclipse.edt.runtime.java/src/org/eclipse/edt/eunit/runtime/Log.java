package org.eclipse.edt.eunit.runtime;
import org.eclipse.edt.javart.resources.*;
import org.eclipse.edt.javart.*;
import org.eclipse.edt.runtime.java.eglx.lang.AnyValue;
import org.eclipse.edt.runtime.java.eglx.lang.EString;
import java.lang.String;
@SuppressWarnings("unused")
@javax.xml.bind.annotation.XmlRootElement()
public class Log extends org.eclipse.edt.runtime.java.eglx.lang.AnyValue {
	private static final long serialVersionUID = 10L;
	@javax.xml.bind.annotation.XmlTransient
	public String msg;
	public Log() {
		super();
		ezeInitialize();
	}
	public void ezeCopy(Object source) {
		ezeCopy((Log) source);
	}
	public void ezeCopy(eglx.lang.AnyValue source) {
		this.msg = ((Log) source).msg;
	}
	public Log ezeNewValue(Object... args) {
		return new Log();
	}
	public void ezeSetEmpty() {
		msg = "";
	}
	public boolean isVariableDataLength() {
		return false;
	}
	public void loadFromBuffer(ByteStorage buffer, Program program) {
	}
	public int sizeInBytes() {
		return 0;
	}
	public void storeInBuffer(ByteStorage buffer) {
	}
	public void ezeInitialize() {
		msg = "";
	}
	@org.eclipse.edt.javart.json.Json(name="msg", clazz=EString.class, asOptions={})
	public String getMsg() {
		return msg;
	}
	public void setMsg(String ezeValue) {
		msg = ezeValue;
	}
}
