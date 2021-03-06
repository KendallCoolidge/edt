/*******************************************************************************
 * Copyright © 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.gen.java;

import java.util.List;

import org.eclipse.edt.compiler.core.IEGLConstants;
import org.eclipse.edt.gen.GenerationException;
import org.eclipse.edt.javart.util.JavaAliaser;
import org.eclipse.edt.mof.egl.*;

public class CommonUtilities {

	public static String packageName(Part part) {
		return packageName(part.getCaseSensitivePackageName());
	}

	public static String packageName(String pkg) {
		if (pkg != null && pkg.length() > 0) {
			return JavaAliaser.packageNameAlias(pkg);
		}

		return pkg;
	}

	/**
	 * Returns the fully-qualified name for the Part's runtime class.
	 * @param part
	 * @return
	 */
	public static String fullClassAlias(Part part) {
		String alias = JavaAliaser.getAlias(part.getCaseSensitiveName());
		String pkg = part.getCaseSensitivePackageName();
		if (pkg.length() > 0) {
			pkg = packageName(pkg);
			return pkg + '.' + alias;
		}
		return alias;
	}

	/**
	 * Returns the unqualified name for the Part's runtime class.
	 * @param part
	 * @return
	 */
	public static String classAlias(Part part) {
		return JavaAliaser.getAlias(part.getCaseSensitiveName());
	}

	/**
	 * Returns true if the type is an ExternalType with subtype JavaObject or NativeType.
	 */
	public static boolean isJavaExternalType(Type type) {
		return getJavaExternalType(type) != null;
	}

	/**
	 * Returns the Type if it's an ExternalType, with subtype JavaObject or NativeType. If it's something else, returns null.
	 */
	public static ExternalType getJavaExternalType(Type type) {
		if (type instanceof ExternalType) {
			if (type.getAnnotation("eglx.java.JavaObject") != null ||
				type.getAnnotation("eglx.java.RootJavaObject") != null ||
				type.getAnnotation("eglx.lang.NativeType") != null) {
				return (ExternalType) type;
			} else {
				return null;
			}
		} else if (type instanceof Part) {
			Part member = (Part) type;
			if (member instanceof ExternalType) {
				if (member.getAnnotation("eglx.java.JavaObject") != null ||
					member.getAnnotation("eglx.java.RootJavaObject") != null ||
					member.getAnnotation("eglx.lang.NativeType") != null) {
					return (ExternalType) member;
				} else {
					return null;
				}
			}
		} else if (type instanceof ArrayType) {
			while (type instanceof ArrayType) {
				type = ((ArrayType) type).getElementType();
			}
			return getJavaExternalType(type);
		}
		return null;
	}

	/**
	 * @return true if the external type is serializable. To be serializable, it must extend the java.io.Serializable
	 * external type.
	 */
	public static boolean isSerializable(ExternalType et) {
		if (et == null) {
			return false;
		}

		// First see if we're looking at java.io.Serializable.
		Annotation annot = et.getAnnotation("eglx.java.JavaObject");
		if (annot != null) {
			String name = et.getCaseSensitiveName();
			if (annot.getValue("externalName") != null && ((String) annot.getValue("externalName")).length() > 0) {
				name = (String) annot.getValue("externalName");
			}
			if ("Serializable".equals(name)) {
				String pkg = et.getCaseSensitivePackageName();
				if (((String) annot.getValue(IEGLConstants.PROPERTY_PACKAGENAME)).length() > 0) {
					pkg = (String) annot.getValue(IEGLConstants.PROPERTY_PACKAGENAME);
				}
				if ("java.io".equals(pkg)) {
					return true;
				}
			}
		} else {
			annot = et.getAnnotation("eglx.java.RootJavaObject");
			if (annot != null) {
				if ("Serializable".equals(et.getCaseSensitiveName())) {
					String pkg = et.getCaseSensitivePackageName();
					if (((String) annot.getValue(IEGLConstants.PROPERTY_PACKAGENAME)).length() > 0) {
						pkg = (String) annot.getValue(IEGLConstants.PROPERTY_PACKAGENAME);
					}
					if ("java.io".equals(pkg)) {
						return true;
					}
				}
			}
		}

		// Check the super types.
		List<StructPart> extndsAry = et.getSuperTypes();
		if (extndsAry != null) {
			for (StructPart part : extndsAry) {
				if (part instanceof ExternalType && isSerializable((ExternalType) part)) {
					return true;
				}
			}
		}

		return false;
	}

	public static String getNativeRuntimeOperationName(UnaryExpression expr) throws GenerationException {
		// safety check to make sure the operation has been defined properly
		if (expr.getOperation() == null || expr.getOperation().getCaseSensitiveName() == null)
			throw new GenerationException();
		// process the operator
		String op = expr.getOperator();
		if (op.equals(UnaryExpression.Op_BITWISENOT))
			return "bitnot";
		if (op.equals(UnaryExpression.Op_NEGATE))
			return "negate";
		if (op.equals(UnaryExpression.Op_NOT))
			return "not";
		return "UnknownOp";
	}

	public static String getNativeRuntimeOperationName(BinaryExpression expr) throws GenerationException {
		// safety check to make sure the operation has been defined properly
		if (expr.getOperation() == null || expr.getOperation().getCaseSensitiveName() == null)
			throw new GenerationException();
		// process the operator
		String op = expr.getOperator();
		if (op.equals(BinaryExpression.Op_PLUS))
			return "plus";
		if (op.equals(BinaryExpression.Op_MINUS))
			return "minus";
		if (op.equals(BinaryExpression.Op_DIVIDE))
			return "divide";
		if (op.equals(BinaryExpression.Op_MULTIPLY))
			return "multiply";
		if (op.equals(BinaryExpression.Op_MODULO))
			return "remainder";
		if (op.equals(BinaryExpression.Op_EQ))
			return "equals";
		if (op.equals(BinaryExpression.Op_NE))
			return "notEquals";
		if (op.equals(BinaryExpression.Op_LT))
			return "compareTo";
		if (op.equals(BinaryExpression.Op_GT))
			return "compareTo";
		if (op.equals(BinaryExpression.Op_LE))
			return "compareTo";
		if (op.equals(BinaryExpression.Op_GE))
			return "compareTo";
		if (op.equals(BinaryExpression.Op_AND))
			return "and";
		if (op.equals(BinaryExpression.Op_OR))
			return "or";
		if (op.equals(BinaryExpression.Op_XOR))
			return "xor";
		if (op.equals(BinaryExpression.Op_CONCAT))
			return "concat";
		if (op.equals(BinaryExpression.Op_NULLCONCAT))
			return "concatNull";
		if (op.equals(BinaryExpression.Op_BITAND))
			return "bitand";
		if (op.equals(BinaryExpression.Op_BITOR))
			return "bitor";
		if (op.equals(BinaryExpression.Op_LEFTSHIFT))
			return "leftShift";
		if (op.equals(BinaryExpression.Op_RIGHTSHIFTARITHMETIC))
			return "rightShiftArithmetic";
		if (op.equals(BinaryExpression.Op_RIGHTSHIFTLOGICAL))
			return "rightShiftLogical";
		if (op.equals(BinaryExpression.Op_POWER))
			return "power";
		if (op.equals(BinaryExpression.Op_IN))
			return "in";
		if (op.equals(BinaryExpression.Op_MATCHES))
			return "matches";
		if (op.equals(BinaryExpression.Op_LIKE))
			return "like";
		return "UnknownOp";
	}

	public static String getNativeRuntimeComparisionOperation(BinaryExpression expr) {
		String op = expr.getOperator();
		if (op.equals(BinaryExpression.Op_LT))
			return " < 0";
		if (op.equals(BinaryExpression.Op_GT))
			return " > 0";
		if (op.equals(BinaryExpression.Op_LE))
			return " <= 0";
		if (op.equals(BinaryExpression.Op_GE))
			return " >= 0";
		return "";
	}

	public static String getNativeJavaOperation(BinaryExpression expr, Context ctx) {
		String op = expr.getOperator();
		// we must not use the equals or notequals from java, as the Object versions of these simply compare the 
		// object and not the values. we need to pass equals and notequals to the edt runtime instead 
		// if we are to use egl overflow checking, then don't pass back that we can do the mathematical operations in java
		if (expr.isNullable() || (Boolean) ctx.getParameter(Constants.parameter_checkOverflow)) {
			if (op.equals(BinaryExpression.Op_LT))
				return " < ";
			if (op.equals(BinaryExpression.Op_GT))
				return " > ";
			if (op.equals(BinaryExpression.Op_LE))
				return " <= ";
			if (op.equals(BinaryExpression.Op_GE))
				return " >= ";
			if (op.equals(BinaryExpression.Op_AND))
				return " && ";
			if (op.equals(BinaryExpression.Op_OR))
				return " || ";
			if (op.equals(BinaryExpression.Op_XOR))
				return " ^ ";
			if (op.equals(BinaryExpression.Op_CONCAT))
				return " + ";
			if (op.equals(BinaryExpression.Op_BITAND))
				return " & ";
			if (op.equals(BinaryExpression.Op_BITOR))
				return " | ";
			if (op.equals(BinaryExpression.Op_LEFTSHIFT))
				return " << ";
			if (op.equals(BinaryExpression.Op_RIGHTSHIFTARITHMETIC))
				return " >> ";
			if (op.equals(BinaryExpression.Op_RIGHTSHIFTLOGICAL))
				return " >>> ";
			return "";
		}
		// these are the defaults for all other types
		// division is intentionally left off as all division must be done through the egl runtime
		if (op.equals(BinaryExpression.Op_PLUS))
			return " + ";
		if (op.equals(BinaryExpression.Op_MINUS))
			return " - ";
		if (op.equals(BinaryExpression.Op_MULTIPLY))
			return " * ";
		if (op.equals(BinaryExpression.Op_MODULO))
			return " % ";
		if (op.equals(BinaryExpression.Op_LT))
			return " < ";
		if (op.equals(BinaryExpression.Op_GT))
			return " > ";
		if (op.equals(BinaryExpression.Op_LE))
			return " <= ";
		if (op.equals(BinaryExpression.Op_GE))
			return " >= ";
		if (op.equals(BinaryExpression.Op_AND))
			return " && ";
		if (op.equals(BinaryExpression.Op_OR))
			return " || ";
		if (op.equals(BinaryExpression.Op_XOR))
			return " ^ ";
		if (op.equals(BinaryExpression.Op_CONCAT))
			return " + ";
		if (op.equals(BinaryExpression.Op_BITAND))
			return " & ";
		if (op.equals(BinaryExpression.Op_BITOR))
			return " | ";
		if (op.equals(BinaryExpression.Op_LEFTSHIFT))
			return " << ";
		if (op.equals(BinaryExpression.Op_RIGHTSHIFTARITHMETIC))
			return " >> ";
		if (op.equals(BinaryExpression.Op_RIGHTSHIFTLOGICAL))
			return " >>> ";
		return "";
	}

	public static String getNativeNullTypeJavaOperation(BinaryExpression expr, Context ctx) {
		String op = expr.getOperator();
		if (op.equals(BinaryExpression.Op_EQ))
			return " == ";
		if (op.equals(BinaryExpression.Op_NE))
			return " != ";
		return "";
	}

	public static String getNativeJavaAssignment(String op) {
		if (op.equals(BinaryExpression.Op_XOR + "="))
			return "^=";
		if (op.equals(BinaryExpression.Op_CONCAT + "="))
			return "+=";
		return op;
	}

	public static boolean isHandledByJavaWithoutCast(Expression src, AsExpression tgt, Context ctx) {
		// nullables will never be handled by java natives
		if (src.isNullable() || tgt.isNullable())
			return false;
		if (!ctx.mapsToPrimitiveType(src.getType()) || !ctx.mapsToPrimitiveType(tgt.getType()))
			return false;
		String srcString = ctx.getPrimitiveMapping(src.getType());
		String tgtString = ctx.getPrimitiveMapping(tgt.getType());
		// check see to see it is safe to allow java to handle this conversion
		int srcIndex = getJavaAllowedType(srcString);
		int tgtIndex = getJavaAllowedType(tgtString);
		if (srcIndex >= 0 && tgtIndex >= 0 && isAcceptibleImplicitCast(srcIndex, tgtIndex))
			return true;
		else
			return false;
	}

	public static boolean isHandledByJavaWithCast(Expression src, AsExpression tgt, Context ctx) {
		// nullables will never be handled by java natives
		if (src.isNullable() || tgt.isNullable())
			return false;
		if (!ctx.mapsToPrimitiveType(src.getType()) || !ctx.mapsToPrimitiveType(tgt.getType()))
			return false;
		String srcString = ctx.getPrimitiveMapping(src.getType());
		String tgtString = ctx.getPrimitiveMapping(tgt.getType());
		// check see to see it is safe to allow java to handle this conversion
		int srcIndex = getJavaAllowedType(srcString);
		int tgtIndex = getJavaAllowedType(tgtString);
		if (srcIndex >= 0 && tgtIndex >= 0 && srcIndex != tgtIndex)
			return !isBoxedOutputTemp(src, ctx);
		else
			return false;
	}

	private static boolean isAcceptibleImplicitCast(int src, int tgt) {
		if (src == tgt)
			return true;
		else if (src == 1 && (tgt == 2 || tgt == 3))
			return true;
		else if (src == 2 && tgt == 3)
			return true;
		else
			return false;
	}

	private static int getJavaAllowedType(String value) {
		if (value.equals("boolean"))
			return 0;
		else if (value.equals("short"))
			return 1;
		else if (value.equals("int"))
			return 2;
		else if (value.equals("long"))
			return 3;
		else if (value.equals("float"))
			return 4;
		else if (value.equals("double"))
			return 5;
		else
			return -1;
	}

	public static boolean isBoxedOutputTemp(Expression expr, Context ctx) {
		return expr instanceof MemberName && isBoxedOutputTemp(((MemberName) expr).getMember(), ctx);
	}

	public static boolean isBoxedOutputTemp(Member member, Context ctx) {
		return ctx.getAttribute(member, org.eclipse.edt.gen.Constants.SubKey_functionArgumentTemporaryVariable) != null
			&& ctx.getAttribute(member, org.eclipse.edt.gen.Constants.SubKey_functionArgumentTemporaryVariable) != ParameterKind.PARM_IN;
	}
	
	public static Member getMember( Expression expr )
	{
		if ( expr instanceof MemberName )
		{
			return ((MemberName)expr).getMember();
		}
		else if ( expr instanceof MemberAccess )
		{
			return ((MemberAccess)expr).getMember();
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static void processImport(String qualifiedName, Context ctx) {
		// if we didn't get a name, or it doesn't have periods in it, then we don't want to consider it for importing
		if (qualifiedName == null || qualifiedName.indexOf('.') < 0)
			return;
		// check the types list we have already
		List<String> typesImported = (List<String>) ctx.getAttribute(ctx.getClass(), Constants.SubKey_partTypesImported);
		for (String imported : typesImported) {
			if (qualifiedName.equalsIgnoreCase(imported)) {
				// it was already found, so we have done this logic before. Simply return
				return;
			}
		}
		// ignore adding this entry to the list, if it is the part we are currently generating
		Part partBeingGenerated = (Part) ctx.getAttribute(ctx.getClass(), Constants.SubKey_partBeingGenerated);
		if (partBeingGenerated.getFullyQualifiedName().equalsIgnoreCase(qualifiedName))
			return;
		// don't import a type whose unqualified name matches the name of the part currently being generated
		String unqualifiedName = qualifiedName;
		if (unqualifiedName.indexOf('.') >= 0)
			unqualifiedName = unqualifiedName.substring(unqualifiedName.lastIndexOf('.') + 1);
		if (unqualifiedName.equalsIgnoreCase(partBeingGenerated.getCaseSensitiveName()))
			return;
		// if we get here, then we haven't processed this type before
		for (String imported : typesImported) {
			int dotIndex = imported.lastIndexOf('.');
			if (dotIndex >= 0)
				imported = imported.substring(dotIndex + 1);
			if (unqualifiedName.equalsIgnoreCase(imported)) {
				// we have an unqualified name that we are importing that matches the last node, Simply return
				return;
			}
		}
		typesImported.add(qualifiedName);
	}

	public static void generateSmapExtension(Member field, Context ctx) {
		// if this isn't an internal variable, then add the data to the debug extension buffer
		if (!field.getCaseSensitiveName().startsWith("eze")) {
			// get the line number. If it is not found, then we can't write the debug extension
			Annotation annotation = field.getAnnotation(IEGLConstants.EGL_LOCATION);
			if (annotation != null && annotation.getValue(IEGLConstants.EGL_PARTLINE) != null) {
				ctx.getSmapExtension().append("" + ((Integer) annotation.getValue(IEGLConstants.EGL_PARTLINE)).intValue());
				if (ctx.getCurrentFile() != null) {
					if (ctx.getSmapFiles().indexOf(ctx.getCurrentFile()) < 0)
						ctx.getSmapFiles().add(ctx.getCurrentFile());
					ctx.getSmapExtension().append("#" + (ctx.getSmapFiles().indexOf(ctx.getCurrentFile()) + 1) + ";");
				} else
					ctx.getSmapExtension().append("#1" + ";");
				ctx.getSmapExtension().append(field.getCaseSensitiveName() + ";");
				ctx.getSmapExtension().append(JavaAliaser.getAlias(field.getCaseSensitiveName()) + ";");
				ctx.getSmapExtension().append(field.getType().getTypeSignature() + "\n");
			}
		}
	}

	public static void generateSmapExtension(Function function, Context ctx) {
		// get the line number. If it is not found, then we can't write the debug extension
		Annotation annotation = function.getAnnotation(IEGLConstants.EGL_LOCATION);
		if (annotation != null && annotation.getValue(IEGLConstants.EGL_PARTLINE) != null) {
			ctx.getSmapExtension().append("" + ((Integer) annotation.getValue(IEGLConstants.EGL_PARTLINE)).intValue());
			if (ctx.getCurrentFile() != null) {
				if (ctx.getSmapFiles().indexOf(ctx.getCurrentFile()) < 0)
					ctx.getSmapFiles().add(ctx.getCurrentFile());
				ctx.getSmapExtension().append("#" + (ctx.getSmapFiles().indexOf(ctx.getCurrentFile()) + 1) + ";");
			} else
				ctx.getSmapExtension().append("#1" + ";");
			ctx.getSmapExtension().append("F:" + function.getCaseSensitiveName() + ";" + JavaAliaser.getAlias(function.getCaseSensitiveName()) + ";(");
			for (int i = 0; i < function.getParameters().size(); i++) {
				FunctionParameter decl = function.getParameters().get(i);
				if (org.eclipse.edt.gen.CommonUtilities.isBoxedParameterType(decl, ctx) && !decl.isConst())
					ctx.getSmapExtension().append("Lorg/eclipse/edt/javart/AnyBoxedObject;");
				else if (decl.getType() instanceof Delegate)
					ctx.getSmapExtension().append("Lorg/eclipse/edt/javart/Delegate;");
				else
					ctx.getSmapExtension().append(generateJavaTypeSignature(function.getParameters().get(i).getType(), ctx,
						function.getParameters().get(i).isNullable()));
			}
			ctx.getSmapExtension().append(")" + generateJavaTypeSignature(function.getReturnType(), ctx, function.isNullable()) + "\n");
		}
	}

	public static void generateSmapExtension(DataTable dataTable, Context ctx) {
		// TODO uncomment when tables are supported. this might also need to be updated
		// ctx.getSmapExtension().append(Constants.smap_extensionDataTable + ";" + dataTable.getFullyQualifiedName()
		// + ";" + dataTable.getFullyQualifiedName().replace('.', '_')+ "\n");
	}

	public static void generateSmapExtension(Form form, Context ctx) {
		// TODO uncomment when forms are supported. this might also need to be updated
		// ctx.getSmapExtension().append(Constants.smap_extensionForm + ";" + form.getFullyQualifiedName()
		// + ";" + form.getFullyQualifiedName().replace('.', '_')+ "\n");
	}

	public static void generateSmapExtension(Library library, Context ctx) {
		if (ctx.mapsToNativeType(library))
			ctx.getSmapExtension().append(
				Constants.smap_extensionSystemLibrary + ";" + library.getFullyQualifiedName() + ";" + Constants.LIBRARY_PREFIX
					+ library.getFullyQualifiedName().replace('.', '_') + "\n");
		else
			ctx.getSmapExtension().append(
				Constants.smap_extensionUserLibrary + ";" + library.getFullyQualifiedName() + ";" + Constants.LIBRARY_PREFIX
					+ library.getFullyQualifiedName().replace('.', '_') + "\n");
	}

	public static void generateSmapExtension(ProgramParameter programParameter, Context ctx) {
		ctx.getSmapExtension().append(
			Constants.smap_extensionProgramParameter + ";" + programParameter.getCaseSensitiveName() + ";" + JavaAliaser.getAlias(programParameter.getCaseSensitiveName()) + ";");
		ctx.getSmapExtension().append(programParameter.getType().getTypeSignature() + "\n");
	}

	private static String generateJavaTypeSignature(Type type, Context ctx, boolean isNullable) {
		String signature = "";
		// if this is an array, we need to handle it specially
		if (type instanceof ArrayType)
			signature += "Ljava/util/List;";
		else if (type instanceof ExternalType && ctx.mapsToNativeType(type.getClassifier()))
			signature += "L" + ctx.getRawNativeImplementationMapping(type.getClassifier()) + ";";
		else {
			// get the java primitive, if it exists
			if (type == null)
				signature += "V";
			else if (ctx.mapsToPrimitiveType(type.getClassifier())) {
				// do we want the java primitive or the object
				// now try to match up against the known primitives
				String value = ctx.getRawPrimitiveMapping(type.getClassifier());
				// if this is a nullable parm, we need the object versions of the primitives
				if (isNullable) {
					if (value.equals("boolean"))
						signature += "Ljava/lang/Boolean;";
					else if (value.equals("byte"))
						signature += "Ljava/lang/Byte;";
					else if (value.equals("double"))
						signature += "Ljava/lang/Double;";
					else if (value.equals("float"))
						signature += "Ljava/lang/Float;";
					else if (value.equals("int"))
						signature += "Ljava/lang/Integer;";
					else if (value.equals("long"))
						signature += "Ljava/lang/Long;";
					else if (value.equals("short"))
						signature += "Ljava/lang/Short;";
					else
						signature += "L" + value.replaceAll("\\.", "/") + ";";
				} else {
					if (value.equals("boolean"))
						signature += "Z";
					else if (value.equals("byte"))
						signature += "B";
					else if (value.equals("double"))
						signature += "D";
					else if (value.equals("float"))
						signature += "F";
					else if (value.equals("int"))
						signature += "I";
					else if (value.equals("long"))
						signature += "J";
					else if (value.equals("short"))
						signature += "S";
					else
						signature += "L" + value.replaceAll("\\.", "/") + ";";
				}
			} else
				signature += "L" + type.getClassifier().getTypeSignature().replaceAll("\\.", "/") + ";";
		}
		return signature;
	}

	/**
	 * Returns null if the desired propertyFunction isn't specified or shouldn't be used; otherwise, returns either the
	 * explicit name of the function (if specified) or implicit name if it should be inferred. According to the docs for both
	 * EGLProperty and Property, function names should be inferred if and only if the annotation is present but BOTH
	 * properties are missing.
	 */
	public static String getPropertyFunction(NamedElement field, boolean setter, Context context) {
		String result = null;

		boolean isEGLProperty = true;
		Annotation annotation = field.getAnnotation(Constants.Annotation_EGLProperty);
		if (annotation == null) {
			annotation = field.getAnnotation(Constants.Annotation_Property);
			isEGLProperty = false;
		}

		if (annotation != null) {
			String propertyFunction = setter ? Constants.Annotation_PropertySetter : Constants.Annotation_PropertyGetter;
			String otherPropertyFunction = setter ? Constants.Annotation_PropertyGetter : Constants.Annotation_PropertySetter;

			Object propFn = annotation.getValue(propertyFunction);
			Object otherPropFn = annotation.getValue(otherPropertyFunction);

			// If neither function is specified then we are supposed to infer the function
			// names for @Property and look up the functions for @EGLProperty.
			boolean bothUnspecified = (propFn == null || (propFn instanceof String && ((String) propFn).length() == 0))
				&& (otherPropFn == null || (otherPropFn instanceof String && ((String) otherPropFn).length() == 0));
			if (bothUnspecified) {
				String fieldName = field.getCaseSensitiveName();
				result = (setter ? Constants.SetterPrefix : Constants.GetterPrefix) + fieldName.substring(0, 1).toUpperCase();
				if (fieldName.length() > 1) {
					result += fieldName.substring(1);
				}

				if (isEGLProperty) {
					// For @EGLProperty we have to take EGL's case-insensitivity into account.
					// We can't simply assume the function for getting field XYZ is named getXYZ.
					// It might be named getxyz. We'll get the function by making a
					// QualifiedFunctionInvocation and using its ability to resolve the function being called.
					QualifiedFunctionInvocation qfi = context.getFactory().createQualifiedFunctionInvocation();
					qfi.setId((String) result);
					qfi.setQualifier(expressionForContainer(((Field) field).getContainer(), context));
					if (setter) {
						MemberName argName = context.getFactory().createMemberName();
						argName.setId(field.getCaseSensitiveName());
						argName.setMember((Member) field);
						qfi.getArguments().add(argName);
					}

					result = qfi.getTarget().getCaseSensitiveName();
				}
			} else {
				if (propFn instanceof Name) {
					result = ((Name) propFn).getId();
				} else {
					result = (String) propFn;
				}
			}
		}

		return result;
	}

	private static Expression expressionForContainer(Container container, Context ctx) {
		Expression result = null;
		Object pbg = ctx.getAttribute(ctx.getClass(), Constants.SubKey_partBeingGenerated);
		if (container instanceof Function
			|| (container instanceof Part && pbg instanceof Part && ((Part) container).getFullyQualifiedName().equalsIgnoreCase(
				((Part) pbg).getFullyQualifiedName()))) {
			ThisExpression thisExpr = ctx.getFactory().createThisExpression();
			thisExpr.setThisObject(container);
			result = thisExpr;
		} else {
			TypeName typeExpr = ctx.getFactory().createTypeName();
			typeExpr.setType((Type) container);
			result = typeExpr;
		}

		return result;
	}
	public static String getEnumerationName(Object enm){
		if(enm instanceof EnumerationEntry){
			return ((EnumerationEntry)enm).getCaseSensitiveName();
		}
		else{
			return "";
		}
	}

}
