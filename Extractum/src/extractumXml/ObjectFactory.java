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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Database_QNAME = new QName("", "database");
    private final static QName _ForeignKeysTypeForeignKey_QNAME = new QName("", "foreignKey");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DatabaseType }
     * 
     */
    public DatabaseType createDatabaseType() {
        return new DatabaseType();
    }

    /**
     * Create an instance of {@link PrimaryKeyType }
     * 
     */
    public PrimaryKeyType createPrimaryKeyType() {
        return new PrimaryKeyType();
    }

    /**
     * Create an instance of {@link TableType }
     * 
     */
    public TableType createTableType() {
        return new TableType();
    }

    /**
     * Create an instance of {@link ColType }
     * 
     */
    public ColType createColType() {
        return new ColType();
    }

    /**
     * Create an instance of {@link ForeignKeysType }
     * 
     */
    public ForeignKeysType createForeignKeysType() {
        return new ForeignKeysType();
    }

    /**
     * Create an instance of {@link PrimaryKeysType }
     * 
     */
    public PrimaryKeysType createPrimaryKeysType() {
        return new PrimaryKeysType();
    }

    /**
     * Create an instance of {@link ColumnsType }
     * 
     */
    public ColumnsType createColumnsType() {
        return new ColumnsType();
    }

    /**
     * Create an instance of {@link ForeignKeyType }
     * 
     */
    public ForeignKeyType createForeignKeyType() {
        return new ForeignKeyType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatabaseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "database")
    public JAXBElement<DatabaseType> createDatabase(DatabaseType value) {
        return new JAXBElement<DatabaseType>(_Database_QNAME, DatabaseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ForeignKeyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "foreignKey", scope = ForeignKeysType.class)
    public JAXBElement<ForeignKeyType> createForeignKeysTypeForeignKey(ForeignKeyType value) {
        return new JAXBElement<ForeignKeyType>(_ForeignKeysTypeForeignKey_QNAME, ForeignKeyType.class, ForeignKeysType.class, value);
    }

}
