/*******************************************************************************
 * Copyright © 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.edt.ide.ui.internal.deployment;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IMSJ2C Protocol</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.edt.ide.ui.internal.deployment.IMSJ2CProtocol#getAttribute1 <em>Attribute1</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.edt.ide.ui.internal.deployment.DeploymentPackage#getIMSJ2CProtocol()
 * @model extendedMetaData="name='IMSJ2CProtocol' kind='empty'"
 * @generated
 */
public interface IMSJ2CProtocol extends Protocol {
	/**
	 * Returns the value of the '<em><b>Attribute1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute1</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute1</em>' attribute.
	 * @see #setAttribute1(String)
	 * @see org.eclipse.edt.ide.ui.internal.deployment.DeploymentPackage#getIMSJ2CProtocol_Attribute1()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='attribute1'"
	 * @generated
	 */
	String getAttribute1();

	/**
	 * Sets the value of the '{@link org.eclipse.edt.ide.ui.internal.deployment.IMSJ2CProtocol#getAttribute1 <em>Attribute1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute1</em>' attribute.
	 * @see #getAttribute1()
	 * @generated
	 */
	void setAttribute1(String value);

} // IMSJ2CProtocol
