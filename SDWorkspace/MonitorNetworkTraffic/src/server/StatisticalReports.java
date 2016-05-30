/**
 * StatisticalReports.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package server;

public class StatisticalReports  implements java.io.Serializable {
    private java.lang.String ips_statistics;

    private java.lang.String pattern_statistics;

    public StatisticalReports() {
    }

    public StatisticalReports(
           java.lang.String ips_statistics,
           java.lang.String pattern_statistics) {
           this.ips_statistics = ips_statistics;
           this.pattern_statistics = pattern_statistics;
    }


    /**
     * Gets the ips_statistics value for this StatisticalReports.
     * 
     * @return ips_statistics
     */
    public java.lang.String getIps_statistics() {
        return ips_statistics;
    }


    /**
     * Sets the ips_statistics value for this StatisticalReports.
     * 
     * @param ips_statistics
     */
    public void setIps_statistics(java.lang.String ips_statistics) {
        this.ips_statistics = ips_statistics;
    }


    /**
     * Gets the pattern_statistics value for this StatisticalReports.
     * 
     * @return pattern_statistics
     */
    public java.lang.String getPattern_statistics() {
        return pattern_statistics;
    }


    /**
     * Sets the pattern_statistics value for this StatisticalReports.
     * 
     * @param pattern_statistics
     */
    public void setPattern_statistics(java.lang.String pattern_statistics) {
        this.pattern_statistics = pattern_statistics;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StatisticalReports)) return false;
        StatisticalReports other = (StatisticalReports) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ips_statistics==null && other.getIps_statistics()==null) || 
             (this.ips_statistics!=null &&
              this.ips_statistics.equals(other.getIps_statistics()))) &&
            ((this.pattern_statistics==null && other.getPattern_statistics()==null) || 
             (this.pattern_statistics!=null &&
              this.pattern_statistics.equals(other.getPattern_statistics())));
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
        if (getIps_statistics() != null) {
            _hashCode += getIps_statistics().hashCode();
        }
        if (getPattern_statistics() != null) {
            _hashCode += getPattern_statistics().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StatisticalReports.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server/", "statisticalReports"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ips_statistics");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ips_statistics"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pattern_statistics");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pattern_statistics"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
