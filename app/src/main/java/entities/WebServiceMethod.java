package entities;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebServiceMethod {
    public SoapObject request = null, objMain, objChild, objChild2;
    public SoapPrimitive response = null;
    public SoapSerializationEnvelope envelope = null;
    public HttpTransportSE androidHttpTransport = null;
    public int intPropertyCount;

    public String methodName;
    public String returnType;

    public WebServiceMethod(String MethodName, String ReturnType) {
        this.methodName = MethodName;
        this.returnType = ReturnType;
        request = new SoapObject(WebServiceConstants.NAMESPACE, methodName);
    }

    public void Method() {

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        androidHttpTransport = new HttpTransportSE(WebServiceConstants.URL, 5000);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.call(WebServiceConstants.SOAP_ACTION
                    + methodName, envelope);

            if (returnType.equals("Object")) {
                objMain = (SoapObject) envelope.bodyIn;
                intPropertyCount = 0;
                intPropertyCount = objMain.getPropertyCount();
            } else {
                response = (SoapPrimitive) envelope.getResponse();
            }


        } catch (Exception e) {

        }
    }
}
