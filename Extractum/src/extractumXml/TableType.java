//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.07.19 um 06:31:19 PM CEST 
//

/*
 * Copyright 2017 Christoph.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package extractumXml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für tableType complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="tableType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="columns" type="{}columnsType"/>
 *         &lt;element name="sql" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="createStatement" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="primaryKeys" type="{}primaryKeysType"/>
 *         &lt;element name="foreignKeys" type="{}foreignKeysType"/>
 *         &lt;element name="path" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tableType", propOrder = {
    "columns",
    "sql",
    "createStatement",
    "primaryKeys",
    "foreignKeys",
    "path"
})
public class TableType {

    @XmlElement(required = true)
    protected ColumnsType columns;
    @XmlElement(required = true)
    protected String sql;
    @XmlElement(required = true)
    protected String createStatement;
    @XmlElement(required = true)
    protected PrimaryKeysType primaryKeys;
    @XmlElement(required = true)
    protected ForeignKeysType foreignKeys;
    @XmlElement(required = true)
    protected String path;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Ruft den Wert der columns-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ColumnsType }
     *     
     */
    public ColumnsType getColumns() {
        return columns;
    }

    /**
     * Legt den Wert der columns-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ColumnsType }
     *     
     */
    public void setColumns(ColumnsType value) {
        this.columns = value;
    }

    /**
     * Ruft den Wert der sql-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSql() {
        return sql;
    }

    /**
     * Legt den Wert der sql-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSql(String value) {
        this.sql = value;
    }

    /**
     * Ruft den Wert der createStatement-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateStatement() {
        return createStatement;
    }

    /**
     * Legt den Wert der createStatement-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateStatement(String value) {
        this.createStatement = value;
    }

    /**
     * Ruft den Wert der primaryKeys-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PrimaryKeysType }
     *     
     */
    public PrimaryKeysType getPrimaryKeys() {
        return primaryKeys;
    }

    /**
     * Legt den Wert der primaryKeys-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaryKeysType }
     *     
     */
    public void setPrimaryKeys(PrimaryKeysType value) {
        this.primaryKeys = value;
    }

    /**
     * Ruft den Wert der foreignKeys-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ForeignKeysType }
     *     
     */
    public ForeignKeysType getForeignKeys() {
        return foreignKeys;
    }

    /**
     * Legt den Wert der foreignKeys-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ForeignKeysType }
     *     
     */
    public void setForeignKeys(ForeignKeysType value) {
        this.foreignKeys = value;
    }

    /**
     * Ruft den Wert der path-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Legt den Wert der path-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
