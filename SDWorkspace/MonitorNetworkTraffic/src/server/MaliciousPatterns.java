/**
 * MaliciousPatterns.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package server;

public class MaliciousPatterns  implements java.io.Serializable {
    private java.lang.String[] mal_ips;

    private java.lang.String[] mal_patterns;

    private boolean isDeleted;

    public MaliciousPatterns() {
    }

    public MaliciousPatterns(
           java.lang.String[] mal_ips,
           java.lang.String[] mal_patterns,
           boolean isDeleted) {
           this.mal_ips = mal_ips;
           this.mal_patterns = mal_patterns;
           this.isDeleted = isDeleted;
    }


    /**
     * Gets the mal_ips value for this MaliciousPatterns.
     * 
     * @return mal_ips
     */
    public java.lang.String[] getMal_ips() {
        return mal_ips;
    }


    /**
     * Sets the mal_ips value for this MaliciousPatterns.
     * 
     * @param mal_ips
     */
    public void setMal_ips(java.lang.String[] mal_ips) {
        this.mal_ips = mal_ips;
    }

    public java.lang.String getMal_ips(int i) {
        return this.mal_ips[i];
    }

    public void setMal_ips(int i, java.lang.String _value) {
        this.mal_ips[i] = _value;
    }


    /**
     * Gets the mal_patterns value for this MaliciousPatterns.
     * 
     * @return mal_patterns
     */
    public java.lang.String[] getMal_patterns() {
        return mal_patterns;
    }


    /**
     * Sets the mal_patterns value for this MaliciousPatterns.
     * 
     * @param mal_patterns
     */
    public void setMal_patterns(java.lang.String[] mal_patterns) {
        this.mal_patterns = mal_patterns;
    }

    public java.lang.String getMal_patterns(int i) {
        return this.mal_patterns[i];
    }

    public void setMal_patterns(int i, java.lang.String _value) {
        this.mal_patterns[i] = _value;
    }


    /**
     * Gets the isDeleted value for this MaliciousPatterns.
     * 
     * @return isDeleted
     */
    public boolean isIsDeleted() {
        return isDeleted;
    }


    /**
     * Sets the isDeleted value for this MaliciousPatterns.
     * 
     * @param isDeleted
     */
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MaliciousPatterns)) return false;
        MaliciousPatterns other = (MaliciousPatterns) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.mal_ips==null && other.getMal_ips()==null) || 
             (this.mal_ips!=null &&
              java.util.Arrays.equals(this.mal_ips, other.getMal_ips()))) &&
            ((this.mal_patterns==null && other.getMal_patterns()==null) || 
             (this.mal_patterns!=null &&
              java.util.Arrays.equals(this.mal_patterns, other.getMal_patterns()))) &&
            this.isDeleted == other.isIsDeleted();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getMal_ips() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMal_ips());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMal_ips(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMal_patterns() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMal_patterns());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMal_patterns(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isIsDeleted() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MaliciousPatterns.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server/", "maliciousPatterns"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mal_ips");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mal_ips"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mal_patterns");
        elemField.setXmlName(new javax.xml.namespace.QName("", "mal_patterns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isDeleted");
        elemField.setXmlName(new javax.xml.namespace.QName("", "isDeleted"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
