package org.eclipse.edt.java.jtopen.access;

import java.util.Calendar;


public class AS400Time extends com.ibm.as400.access.AS400Time {
	private static final long serialVersionUID = 1L;

	public AS400Time(int ibmiFormat) {
		super(ibmiFormat);
	}

	public AS400Time(int ibmiFormat, Character seperator) {
		super(ibmiFormat, seperator);
	}

	@Override
	public byte[] toBytes(Object object) {
		if(object instanceof Calendar){
			object = new java.sql.Time(((Calendar)object).getTimeInMillis());
		}
		return super.toBytes(object);
	}
	@Override
	public int toBytes(Object object, byte[] bytes) {
		if(object instanceof Calendar){
			object = new java.sql.Time(((Calendar)object).getTimeInMillis());
		}
		return super.toBytes(object, bytes);
	}
	@Override
	public int toBytes(Object object, byte[] bytes, int offset) {
		if(object instanceof Calendar){
			object = new java.sql.Time(((Calendar)object).getTimeInMillis());
		}
		return super.toBytes(object, bytes, offset);
	}
	@Override
	public Object toObject(byte[] arg0) {
		Object obj = super.toObject(arg0);
		return convert(obj);
	}

	@Override
	public Object toObject(byte[] arg0, int arg1) {
		Object obj = super.toObject(arg0, arg1);
		return convert(obj);
	}
	private Object convert(Object obj) {
		if(obj instanceof Calendar){
//FIXME			return ETime.asDate((Calendar)obj);
		}
		else if(obj instanceof java.sql.Time){
//FIXME				return ETime.asDate(DateTimeUtil.getNewCalendar((java.sql.Time)obj));
		}
		else if(obj instanceof java.util.Date){
//FIXME				return ETime.asDate(DateTimeUtil.getNewCalendar((java.util.Date)obj));
		}
		return obj;
	}
}